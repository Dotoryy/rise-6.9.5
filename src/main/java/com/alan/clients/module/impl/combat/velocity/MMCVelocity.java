package com.alan.clients.module.impl.combat.velocity;

import com.alan.clients.module.impl.combat.Velocity;
import com.alan.clients.module.impl.movement.Speed;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.input.MoveInputEvent;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.newevent.impl.packet.PacketReceiveEvent;
import com.alan.clients.util.player.MoveUtil;
import com.alan.clients.value.Mode;
import com.alan.clients.component.impl.player.BadPacketsComponent;
import com.alan.clients.component.impl.combat.TargetComponent;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S12PacketEntityVelocity;

public final class MMCVelocity extends Mode<Velocity> {
    private boolean receivedVelocity;
    @EventLink
    public final Listener<PacketReceiveEvent> onPacketReceiveEvent = var1x -> {
        Packet packet = var1x.getPacket();
        if (packet instanceof S12PacketEntityVelocity && ((S12PacketEntityVelocity)packet).getEntityID() == aEg.thePlayer.getEntityId()) {
            this.receivedVelocity = true;
        }
    };
    @EventLink
    public final Listener<PreMotionEvent> onPreMotionEvent = var1x -> {
        if (!this.getParent().onSwing.wo() || aEg.thePlayer.isSwingInProgress) {
            TargetComponent.f(7.0);
            if (aEg.thePlayer.onGround && aEg.thePlayer.hurtTime > 0) {
                BadPacketsComponent.bad(false, true, false, false, false);
            }

            if (aEg.thePlayer.ae == 1) {
                aEg.thePlayer.motionX *= 0.0;
                aEg.thePlayer.motionZ *= 0.0;
            }

            if (aEg.thePlayer.ae == 1 && (aEg.thePlayer.isJumping || this.e(Speed.class).isEnabled())) {
                aEg.thePlayer.motionY -= 9.0;
            }

            if (aEg.thePlayer.cqL == 1 && aEg.thePlayer.ae < 4 && MoveUtil.speed() < 0.31) {
                MoveUtil.moveFlying(0.05);
            }
        }
    };
    @EventLink
    public final Listener<MoveInputEvent> onMoveInput = var1x -> {
        if (!this.getParent().onSwing.wo() || aEg.thePlayer.isSwingInProgress) {
            ;
        }
    };

    public MMCVelocity(String var1, Velocity velocity) {
        super(var1, velocity);
    }
}
