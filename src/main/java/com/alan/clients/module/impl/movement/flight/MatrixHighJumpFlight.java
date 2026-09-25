package com.alan.clients.module.impl.movement.flight;

import com.alan.clients.module.impl.movement.Flight;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.newevent.impl.motion.PreUpdateEvent;
import com.alan.clients.newevent.impl.packet.PacketReceiveEvent;
import com.alan.clients.newevent.impl.packet.PacketSendEvent;
import com.alan.clients.util.player.MoveUtil;
import com.alan.clients.value.Mode;
import com.alan.clients.value.impl.BooleanValue;
import com.alan.clients.value.impl.NumberValue;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer.C06PacketPlayerPosLook;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;

public class MatrixHighJumpFlight extends Mode<Flight> {
    private final BooleanValue selfDamage = new BooleanValue("Self Damage", this, false);
    private final NumberValue height = new NumberValue("Height", this, 1.0, 0.42, 7.0, 0.1);
    private int damageJumps;
    private boolean restoreMotion;
    private boolean boosting;
    private double savedMotionX;
    private double savedMotionY;
    private double savedMotionZ;
    @EventLink
    public final Listener<PreUpdateEvent> onPreUpdate = var1x -> {
        if (this.boosting) {
            aEg.thePlayer.motionY = this.height.wo().doubleValue();
        }

        if (aEg.thePlayer.hurtTime >= 1 && aEg.thePlayer.hurtTime <= 8) {
            if (aEg.thePlayer.onGround) {
                aEg.thePlayer.jump();
            } else if (aEg.thePlayer.motionY < 0.2) {
                this.boosting = true;
            }
        }

        if (this.damageJumps < 4 && this.selfDamage.wo() && aEg.thePlayer.onGround) {
            aEg.thePlayer.jump();
            this.damageJumps++;
        }
    };
    @EventLink
    public final Listener<PreMotionEvent> onPreMotion = var1x -> {
        if (this.damageJumps < 4 && this.selfDamage.wo()) {
            var1x.setOnGround(false);
        }
    };
    @EventLink
    public final Listener<PacketSendEvent> onPacketSend = var1x -> {
        if (var1x.dq() instanceof C06PacketPlayerPosLook && this.restoreMotion) {
            this.restoreMotion = false;
            aEg.thePlayer.motionX = this.savedMotionX;
            aEg.thePlayer.motionY = this.savedMotionY;
            aEg.thePlayer.motionZ = this.savedMotionZ;
        }
    };
    @EventLink
    public final Listener<PacketReceiveEvent> onPacketReceive = var1x -> {
        Packet packet = var1x.getPacket();
        if (this.boosting && packet instanceof S08PacketPlayerPosLook) {
            this.restoreMotion = true;
            this.savedMotionX = aEg.thePlayer.motionX;
            this.savedMotionY = aEg.thePlayer.motionY;
            this.savedMotionZ = aEg.thePlayer.motionZ;
            this.boosting = false;
        }
    };

    public MatrixHighJumpFlight(String var1, Flight flight) {
        super(var1, flight);
    }

    @Override
    public void onEnable() {
        this.damageJumps = 0;
        this.boosting = false;
        this.restoreMotion = false;
    }

    @Override
    public void onDisable() {
        MoveUtil.stop();
    }
}
