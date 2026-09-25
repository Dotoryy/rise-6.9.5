package com.alan.clients.module.impl.combat.velocity;

import com.alan.clients.module.impl.combat.Velocity;
import com.alan.clients.module.impl.movement.Flight;
import com.alan.clients.module.impl.movement.LongJump;
import com.alan.clients.module.impl.movement.Speed;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.packet.PacketReceiveEvent;
import com.alan.clients.util.player.MoveUtil;
import com.alan.clients.value.Mode;
import com.alan.clients.value.impl.NumberValue;
import com.alan.clients.util.chat.ChatUtil;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S27PacketExplosion;

public final class WatchdogDamageBoostFlyDisablerVelocity extends Mode<Velocity> {
    private final NumberValue horizontal = new NumberValue("Horizontal", this, 0, 0, 100, 1);
    private final NumberValue vertical = new NumberValue("Vertical", this, 0, 0, 100, 1);
    @EventLink
    public final Listener<PacketReceiveEvent> onPacketReceive = var1x -> {
        if ((!this.getParent().onSwing.wo() || aEg.thePlayer.isSwingInProgress)
            && !var1x.isCancelled()
            && !this.e(Flight.class).isEnabled()
            && !this.e(LongJump.class).isEnabled()) {
            Packet packet = var1x.getPacket();
            double d0 = this.horizontal.wo().doubleValue();
            double d1 = this.vertical.wo().doubleValue();
            if (packet instanceof S12PacketEntityVelocity s12packetentityvelocity && s12packetentityvelocity.getEntityID() == aEg.thePlayer.getEntityId()) {
                if (!this.e(Speed.class).isEnabled()
                    || aEg.thePlayer.tR <= 0
                    || !(MoveUtil.speed() < Math.hypot(s12packetentityvelocity.getMotionX() / 8000.0, s12packetentityvelocity.getMotionZ() / 8000.0))) {
                    if (!aEg.thePlayer.onGround || this.e(Speed.class).isEnabled()) {
                        ChatUtil.b("no boost");
                        var1x.setCancelled();
                    } else if (!this.e(Speed.class).isEnabled()) {
                        aEg.thePlayer.motionY = s12packetentityvelocity.getMotionY() / 8000.0;
                        var1x.setCancelled();
                    }
                }

                var1x.setPacket(s12packetentityvelocity);
            }

            if (packet instanceof S27PacketExplosion s27packetexplosion) {
                if (d0 == 0.0 && d1 == 0.0) {
                    var1x.setCancelled();
                    return;
                }

                s27packetexplosion.posX *= d0 / 100.0;
                s27packetexplosion.posY *= d1 / 100.0;
                s27packetexplosion.posZ *= d0 / 100.0;
                var1x.setPacket(s27packetexplosion);
            }
        }
    };

    public WatchdogDamageBoostFlyDisablerVelocity(String var1, Velocity velocity) {
        super(var1, velocity);
    }
}
