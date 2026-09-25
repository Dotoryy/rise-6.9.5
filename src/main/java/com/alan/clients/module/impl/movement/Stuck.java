package com.alan.clients.module.impl.movement;

import com.alan.clients.module.Module;
import com.alan.clients.module.api.Category;
import com.alan.clients.module.api.ModuleInfo;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PostStrafeEvent;
import com.alan.clients.newevent.impl.other.TickEvent;
import com.alan.clients.newevent.impl.packet.PacketSendEvent;
import com.alan.clients.util.player.MoveUtil;
import com.alan.clients.value.impl.BooleanValue;
import com.alan.clients.value.impl.NumberValue;
import com.alan.clients.util.vector.Vector3d;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer.C05PacketPlayerLook;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;

@ModuleInfo(aliases = {"module.movement.stuck.name", "stasis"}, description = "module.movement.stuck.description", category = Category.MOVEMENT)
public class Stuck extends Module {
    private Vector3d savedMotion;
    private final BooleanValue rotations = new BooleanValue("Rotate", this, false);
    private final BooleanValue test = new BooleanValue("Test", this, false);
    private final NumberValue pulseTicks = new NumberValue("Pulse Ticks", this, 0, 0, 30, 1);
    private boolean stuck;
    private int stuckTicks;
    private int refreezeTicks;
    @EventLink
    public final Listener<PostStrafeEvent> onPostStrafe = var1 -> {
        if (this.stuck) {
            MoveUtil.stop();
            aEg.thePlayer.motionY = 0.0;
        }
    };
    @EventLink
    public final Listener<TickEvent> onTick = var1 -> {
        int i = this.pulseTicks.wo().intValue();
        if (i <= 0) {
            if (!this.stuck) {
                this.freeze();
            }

            this.stuckTicks = 0;
            this.refreezeTicks = 0;
        } else if (this.stuck) {
            if (++this.stuckTicks >= i) {
                this.release();
                this.stuckTicks = 0;
                this.refreezeTicks = 1;
            }
        } else {
            if (this.refreezeTicks > 0 && --this.refreezeTicks <= 0) {
                this.freeze();
            }
        }
    };
    @EventLink
    public final Listener<PacketSendEvent> onPacketSend = var1 -> {
        Packet packet = var1.dq();
        if (this.test.wo()
            && this.stuck
            && (packet instanceof C02PacketUseEntity || packet instanceof C07PacketPlayerDigging || packet instanceof C08PacketPlayerBlockPlacement)) {
            this.pulse();
        }

        if (this.stuck && packet instanceof C03PacketPlayer) {
            if (!this.rotations.wo()) {
                var1.setCancelled();
                return;
            }

            if (!(packet instanceof C05PacketPlayerLook)) {
                var1.setCancelled();
            }
        }
    };

    public Stuck() {
    }

    @Override
    public void onEnable() {
        this.stuck = false;
        this.stuckTicks = 0;
        this.refreezeTicks = 0;
        this.freeze();
    }

    @Override
    public void onDisable() {
        this.stuckTicks = 0;
        this.refreezeTicks = 0;
        this.release();
    }

    private void freeze() {
        if (!this.stuck) {
            this.savedMotion = new Vector3d(aEg.thePlayer.motionX, aEg.thePlayer.motionY, aEg.thePlayer.motionZ);
            this.stuck = true;
        }
    }

    private void release() {
        if (this.stuck) {
            if (this.savedMotion != null) {
                aEg.thePlayer.motionX = this.savedMotion.x;
                aEg.thePlayer.motionY = this.savedMotion.y;
                aEg.thePlayer.motionZ = this.savedMotion.z;
            }

            this.stuck = false;
        }
    }

    private void pulse() {
        this.release();
        this.stuckTicks = 0;
        this.refreezeTicks = 1;
    }
}
