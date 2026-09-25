package com.alan.clients.module.impl.movement.flight;

import com.alan.clients.module.impl.movement.Flight;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.input.MoveInputEvent;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.newevent.impl.motion.StrafeEvent;
import com.alan.clients.newevent.impl.packet.PacketSendEvent;
import com.alan.clients.util.player.MoveUtil;
import com.alan.clients.value.Mode;
import com.alan.clients.value.impl.BooleanValue;
import com.alan.clients.value.impl.NumberValue;
import com.alan.clients.component.impl.player.BadPacketsComponent;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;

public class BufferAbuseFlight extends Mode<Flight> {
    @EventLink
    public Listener<PacketSendEvent> onPacketSend;
    public NumberValue speed;
    public BooleanValue sendFlying;
    @EventLink
    public Listener<MoveInputEvent> onMoveInput;
    @EventLink
    public Listener<StrafeEvent> onStrafe;
    @EventLink
    public Listener<PreMotionEvent> onPreMotion;

    public BufferAbuseFlight(String var1, Flight flight) {
        super(var1, flight);
        this.speed = new NumberValue("Speed", this, 1, 0.1, 9.5, 0.1);
        this.sendFlying = new BooleanValue("Send Flying", this, false);
        this.onStrafe = var1x -> {
            float f = 0.0F;
            float f1 = this.speed.wo().floatValue();
            var1x.setSpeed(f1);
        };
        this.onPreMotion = var1x -> {
            float f = 0.0F;
            float f1 = this.speed.wo().floatValue();
            aEg.thePlayer.motionY = -1.0E-10 + (aEg.gameSettings.keyBindJump.isKeyDown() ? f1 : 0.0) - (aEg.gameSettings.keyBindSneak.isKeyDown() ? f1 : 0.0);
            if (aEg.thePlayer.getDistance(aEg.thePlayer.lastReportedPosX, aEg.thePlayer.lastReportedPosY, aEg.thePlayer.lastReportedPosZ) <= 10.0F - f1 - 0.15) {
                var1x.setCancelled();
            }
        };
        this.onMoveInput = var0 -> var0.setSneak(false);
        this.onPacketSend = var1x -> {
            if (!this.sendFlying.wo()) {
                Packet packet = var1x.dq();
                if (packet instanceof C03PacketPlayer && !((C03PacketPlayer)packet).isMoving() && !BadPacketsComponent.aW()) {
                    var1x.setCancelled();
                }
            }
        };
    }


    @Override
    public void onDisable() {
        MoveUtil.stop();
    }

    static {
    }
}
