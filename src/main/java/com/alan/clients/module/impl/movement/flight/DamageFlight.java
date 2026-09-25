package com.alan.clients.module.impl.movement.flight;

import com.alan.clients.module.impl.movement.Flight;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.newevent.impl.motion.PreUpdateEvent;
import com.alan.clients.newevent.impl.packet.PacketReceiveEvent;
import com.alan.clients.util.player.MoveUtil;
import com.alan.clients.value.Mode;
import com.alan.clients.value.impl.NumberValue;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S12PacketEntityVelocity;

public class DamageFlight extends Mode<Flight> {
    private final NumberValue duration = new NumberValue("Duration", this, 3000.0, 1000.0, 10000.0, 100.0);
    private final NumberValue timer = new NumberValue("Timer", this, 1.0, 0.1, 2.0, 0.1);
    private long damageTime;
    private boolean damaged;
    private int flying;
    @EventLink
    public final Listener<PacketReceiveEvent> onPacketReceive = var1x -> {
        Packet packet = var1x.getPacket();
        if (packet instanceof S12PacketEntityVelocity && ((S12PacketEntityVelocity)packet).getEntityID() == aEg.thePlayer.getEntityId()) {
            this.damageTime = System.currentTimeMillis();
            this.damaged = true;
        }
    };
    @EventLink
    public final Listener<PreUpdateEvent> onPreUpdate = var1x -> {
        if (this.damaged) {
            if (System.currentTimeMillis() - this.damageTime >= this.duration.wo().longValue()) {
                if (this.flying == 1) {
                    this.getParent().toggle();
                }
            } else {
                aEg.timer.dzD = this.timer.wo().floatValue();
                this.flying = 1;
                if (aEg.thePlayer.onGround && aEg.thePlayer.isCollidedVertically) {
                    aEg.thePlayer.motionY = 0.42;
                } else {
                    MoveUtil.strafe(0.002);
                    aEg.timer.dzD = 4.0F;
                    aEg.thePlayer.motionY = Math.max(-1.0E-8, aEg.thePlayer.motionY);
                }

                if (aEg.thePlayer.isCollidedHorizontally) {
                    aEg.timer.dzD = 1.0F;
                    this.getParent().toggle();
                }
            }
        }
    };
    @EventLink
    public final Listener<PreMotionEvent> onPreMotion = var1x -> {
        if (this.damaged) {
            if (System.currentTimeMillis() - this.damageTime < this.duration.wo().longValue()) {
                var1x.setOnGround(false);
            }
        }
    };

    public DamageFlight(String var1, Flight flight) {
        super(var1, flight);
    }

    @Override
    public void onEnable() {
        this.damageTime = 0L;
        this.damaged = false;
        this.flying = 0;
    }

    @Override
    public void onDisable() {
        aEg.timer.dzD = 1.0F;
        MoveUtil.stop();
        this.damaged = false;
        this.flying = 0;
    }
}
