package com.alan.clients.module.impl.movement.flight;

import com.alan.clients.module.impl.movement.Flight;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.newevent.impl.motion.PreUpdateEvent;
import com.alan.clients.newevent.impl.motion.StrafeEvent;
import com.alan.clients.newevent.impl.other.MoveEvent;
import com.alan.clients.newevent.impl.packet.PacketReceiveEvent;
import com.alan.clients.util.player.MoveUtil;
import com.alan.clients.value.Mode;
import net.minecraft.network.play.server.S12PacketEntityVelocity;

public class Watchdog2Flight extends Mode<Flight> {
    private boolean shouldDisableOnLand;
    @EventLink
    public final Listener<PreMotionEvent> onPreMotion = var0 -> {
        if (aEg.thePlayer.ae == 1) {
            aEg.thePlayer.motionZ *= 1.004;
            aEg.thePlayer.motionX *= 1.004;
        }
    };
    @EventLink
    public final Listener<StrafeEvent> onStrafe = var1x -> {
        if (aEg.thePlayer.tR == 20) {
            this.shouldDisableOnLand = true;
        }

        if (aEg.thePlayer.ae > 1 && aEg.thePlayer.ae < 5) {
            aEg.thePlayer.motionY += 0.0445;
        } else if (aEg.thePlayer.ae > 1 && aEg.thePlayer.tR > 11) {
            aEg.thePlayer.motionY += 0.028;
        }

        if (this.shouldDisableOnLand && aEg.thePlayer.onGround) {
            this.e(Flight.class).setEnabled(false);
        }

        var1x.setFriction((float)(var1x.getFriction() + 0.005));
        MoveUtil.useDiagonalSpeed();
        MoveUtil.strafe();
    };
    @EventLink
    public final Listener<PreUpdateEvent> onPreUpdate = var0 -> {};
    @EventLink(value = 0)
    public final Listener<PacketReceiveEvent> onPacketReceive = var0 -> {
        if (var0.getPacket() instanceof S12PacketEntityVelocity s12packetentityvelocity && s12packetentityvelocity.getEntityID() == aEg.thePlayer.getEntityId()) {
            aEg.thePlayer.motionY = s12packetentityvelocity.getMotionY() / 8000.0;
        }
    };
    @EventLink
    private final Listener<MoveEvent> onMove = var0 -> {};

    public Watchdog2Flight(String var1, Flight flight) {
        super(var1, flight);
    }

    @Override
    public void onDisable() {
        if (aEg.thePlayer.ae < 4 && !aEg.thePlayer.onGround) {
            MoveUtil.stop();
        }

        this.shouldDisableOnLand = false;
    }

    @Override
    public void onEnable() {
    }
}
