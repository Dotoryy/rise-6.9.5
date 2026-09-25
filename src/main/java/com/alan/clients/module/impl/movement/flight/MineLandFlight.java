package com.alan.clients.module.impl.movement.flight;

import com.alan.clients.module.impl.movement.Flight;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.newevent.impl.packet.PacketReceiveEvent;
import com.alan.clients.util.player.MoveUtil;
import com.alan.clients.value.Mode;
import com.alan.clients.util.packet.PacketUtil;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.util.MathHelper;

public class MineLandFlight extends Mode<Flight> {
    private double serverPosX;
    private double serverPosY;
    private double serverPosZ;
    private boolean teleported;
    @EventLink
    public final Listener<PreMotionEvent> onPreMotionEvent = var1x -> {
        if (!this.teleported) {
            double d0 = MoveUtil.direction();
            if (aEg.thePlayer.ticksExisted % 3 == 0) {
                PacketUtil.send(new C03PacketPlayer(aEg.thePlayer.onGround));
                aEg.thePlayer.setPosition(this.serverPosX, this.serverPosY, this.serverPosZ);
            }

            var1x.setPosY(var1x.getPosY() - 1.1 + (aEg.thePlayer.ticksExisted % 3 == 0 ? 0.42F : 0.0F));
            var1x.setPosX(var1x.getPosX() + MathHelper.sin((float)d0) * 6.0);
            var1x.setPosZ(var1x.getPosZ() - MathHelper.cos((float)d0) * 6.0);
        } else {
            aEg.timer.dzD = 0.3F;
        }
    };
    @EventLink
    public final Listener<PacketReceiveEvent> onPacketReceiveEvent = var1x -> {
        Packet packet = var1x.getPacket();
        if (packet instanceof S08PacketPlayerPosLook && !this.teleported) {
            var1x.setCancelled();
        } else if (packet instanceof S12PacketEntityVelocity s12packetentityvelocity
            && s12packetentityvelocity.getEntityID() == aEg.thePlayer.getEntityId()
            && s12packetentityvelocity.motionY / 8000.0 > 0.5) {
            this.teleported = true;
        }
    };

    public MineLandFlight(String var1, Flight flight) {
        super(var1, flight);
    }

    @Override
    public void onEnable() {
        this.serverPosX = aEg.thePlayer.posX;
        this.serverPosY = aEg.thePlayer.posY;
        this.serverPosZ = aEg.thePlayer.posZ;
        this.teleported = false;
    }
}
