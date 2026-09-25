package com.alan.clients.module.impl.combat.velocity;

import com.alan.clients.module.impl.combat.Velocity;
import com.alan.clients.module.impl.movement.Speed;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.newevent.impl.other.TeleportEvent;
import com.alan.clients.newevent.impl.packet.PacketReceiveEvent;
import com.alan.clients.value.Mode;
import com.alan.clients.value.impl.BooleanValue;
import com.alan.clients.value.impl.NumberValue;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S27PacketExplosion;

public final class StandardVelocity extends Mode<Velocity> {
    public Boolean flag = false;
    private Integer counter = 0;
    private Speed speed = null;
    private String fH = null;
    private int sG;
    private final NumberValue horizontal = new NumberValue("Horizontal", this, 0, 0, 100, 1);
    private final NumberValue vertical = new NumberValue("Vertical", this, 0, 0, 100, 1);
    private final BooleanValue explosionIgnore = new BooleanValue("Explosion Ignore", this, false);
    @EventLink(value = 0)
    public final Listener<PacketReceiveEvent> onPacketReceiveEvent = var1x -> {
        if ((!this.getParent().onSwing.wo() || aEg.thePlayer.isSwingInProgress) && !var1x.isCancelled()) {
            Packet packet = var1x.getPacket();
            double d0 = this.horizontal.wo().doubleValue();
            double d1 = this.vertical.wo().doubleValue();
            boolean flag = this.explosionIgnore.wo();
            if (packet instanceof S12PacketEntityVelocity s12packetentityvelocity) {
                if (s12packetentityvelocity.getEntityID() == aEg.thePlayer.getEntityId()) {
                    if (d0 == 0.0) {
                        if (d1 != 0.0 && !var1x.isCancelled()) {
                            aEg.thePlayer.motionY = s12packetentityvelocity.getMotionY() / 8000.0;
                        }

                        var1x.setCancelled();
                        return;
                    }

                    s12packetentityvelocity.motionX = (int)(s12packetentityvelocity.motionX * (d0 / 100.0));
                    s12packetentityvelocity.motionY = (int)(s12packetentityvelocity.motionY * (d1 / 100.0));
                    s12packetentityvelocity.motionZ = (int)(s12packetentityvelocity.motionZ * (d0 / 100.0));
                    var1x.setPacket(s12packetentityvelocity);
                }
            } else if (packet instanceof S27PacketExplosion s27packetexplosion) {
                if (flag) {
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
    @EventLink
    public final Listener<PreMotionEvent> onPreMotion = var1x -> {
        if (this.speed == null) {
            this.speed = this.e(Speed.class);
        }
    };
    @EventLink
    public final Listener<TeleportEvent> onTeleport = var0 -> {
        double d0;
        int i = (d0 = var0.getPosY() - (aEg.thePlayer.posY - 2.0)) == 0.0 ? 0 : (d0 < 0.0 ? -1 : 1);
    };

    @Override
    public void onEnable() {
        this.counter = 0;
        this.setFlag(false);
    }

    public StandardVelocity(String var1, Velocity velocity) {
        super(var1, velocity);
    }

    public Boolean getFlag() {
        return this.flag;
    }

    public void setFlag(Boolean var1) {
        this.flag = var1;
    }
}
