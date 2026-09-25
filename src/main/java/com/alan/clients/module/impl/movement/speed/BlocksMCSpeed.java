package com.alan.clients.module.impl.movement.speed;

import com.alan.clients.module.impl.movement.Speed;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.newevent.impl.motion.StrafeEvent;
import com.alan.clients.newevent.impl.other.TeleportEvent;
import com.alan.clients.newevent.impl.packet.PacketSendEvent;
import com.alan.clients.util.player.MoveUtil;
import com.alan.clients.value.Mode;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.potion.Potion;

public class BlocksMCSpeed extends Mode<Speed> {
    private boolean reset;
    private double speed;
    @EventLink
    public final Listener<StrafeEvent> onStrafe = var1x -> {
        double d0 = MoveUtil.getAllowedHorizontalDistance();
        boolean flag = aEg.thePlayer.isPotionActive(Potion.moveSpeed);
        if (MoveUtil.isMoving()) {
            switch (aEg.thePlayer.tR) {
                case 0:
                    aEg.thePlayer.motionY = MoveUtil.jumpBoostMotion(0.42F);
                    this.speed = d0 * (flag ? 1.4 : 2.15);
                    break;
                case 1:
                    this.speed = this.speed - 0.8 * (this.speed - d0);
                    break;
                default:
                    this.speed = this.speed - this.speed / 159.9F;
            }

            this.reset = false;
        } else if (!this.reset) {
            this.speed = 0.0;
            this.reset = true;
            this.speed = MoveUtil.getAllowedHorizontalDistance();
        }

        if (aEg.thePlayer.isCollidedHorizontally) {
            this.speed = MoveUtil.getAllowedHorizontalDistance();
        }

        var1x.setSpeed(Math.max(this.speed, d0), Math.random() / 2000.0);
    };
    @EventLink
    public final Listener<TeleportEvent> onTeleport = var1x -> this.speed = 0.0;
    @EventLink
    public final Listener<PreMotionEvent> onPreMotionEvent = var0 -> {
        if (!MoveUtil.isMoving()) {
            var0.setPosX(var0.getPosX() + (Math.random() - 0.5) / 3.0);
            var0.setPosZ(var0.getPosZ() + (Math.random() - 0.5) / 3.0);
        }
    };
    @EventLink
    public final Listener<PacketSendEvent> onPacketSend = var0 -> {
        boolean flag = var0.dq() instanceof C0BPacketEntityAction;
    };

    public BlocksMCSpeed(String var1, Speed var2) {
        super(var1, var2);
    }

    @Override
    public void onDisable() {
        this.speed = 0.0;
    }
}
