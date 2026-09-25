package com.alan.clients.module.impl.movement.flight;

import com.alan.clients.module.impl.movement.Flight;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.input.MoveInputEvent;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.newevent.impl.motion.StrafeEvent;
import com.alan.clients.newevent.impl.packet.PacketSendEvent;
import com.alan.clients.value.Mode;
import com.alan.clients.value.impl.BooleanValue;
import com.alan.clients.value.impl.NumberValue;
import com.alan.clients.util.player.DamageUtil;
import com.alan.clients.util.player.DamageType;
import com.alan.clients.component.impl.player.BadPacketsComponent;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;

public class CubeCraftFlight extends Mode<Flight> {
    private final NumberValue speed = new NumberValue("Speed", this, 1, 0.1, 9.5, 0.1);
    private final BooleanValue sendFlying = new BooleanValue("Send Flying", this, false);
    @EventLink
    public final Listener<StrafeEvent> onStrafe = var1x -> {
        float f = this.speed.wo().floatValue();
        var1x.setSpeed(f);
    };
    @EventLink
    public final Listener<PreMotionEvent> onPreMotionEvent = var1x -> {
        float f = this.speed.wo().floatValue();
        aEg.thePlayer.motionY = -1.0E-10 + (aEg.gameSettings.keyBindJump.isKeyDown() ? f : 0.0) - (aEg.gameSettings.keyBindSneak.isKeyDown() ? f : 0.0);
        if (aEg.thePlayer.getDistance(aEg.thePlayer.lastReportedPosX, aEg.thePlayer.lastReportedPosY, aEg.thePlayer.lastReportedPosZ) <= 10.0F - f - 0.15) {
            var1x.setCancelled();
        }
    };
    @EventLink
    public final Listener<MoveInputEvent> onMove = var0 -> var0.setSneak(false);
    @EventLink
    public final Listener<PacketSendEvent> onPacketSend = var1x -> {
        if (!this.sendFlying.wo()) {
            Packet packet = var1x.dq();
            if (packet instanceof C03PacketPlayer && !((C03PacketPlayer)packet).isMoving() && !BadPacketsComponent.aW()) {
                var1x.setCancelled();
            }
        }
    };

    public CubeCraftFlight(String var1, Flight flight) {
        super(var1, flight);
    }

    @Override
    public void onEnable() {
        DamageUtil.damagePlayer(DamageType.POSITION, 3.42F, 1, false, false);
    }
}
