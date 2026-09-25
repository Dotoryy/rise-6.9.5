package com.alan.clients.module.impl.player.scaffold.tower;

import com.alan.clients.module.impl.player.Scaffold;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.newevent.impl.packet.PacketSendEvent;
import com.alan.clients.value.Mode;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;

public class NormalTower extends Mode<Scaffold> {
    @EventLink
    public final Listener<PreMotionEvent> onPreMotionEvent = var0 -> {
        if (aEg.gameSettings.keyBindJump.isKeyDown() && aEg.thePlayer.onGround) {
            aEg.thePlayer.motionY = 0.42F;
        }
    };
    @EventLink
    public final Listener<PacketSendEvent> onPacketSend = var0 -> {
        Packet packet = var0.dq();
        if (aEg.thePlayer.motionY > -0.0784000015258789
            && packet instanceof C08PacketPlayerBlockPlacement
            && ((C08PacketPlayerBlockPlacement)packet)
                .getPosition()
                .equals(new BlockPos(aEg.thePlayer.posX, Math.floor(aEg.thePlayer.posY) - 2.0, aEg.thePlayer.posZ))) {
            aEg.thePlayer.motionY = -0.0784000015258789;
        }
    };

    public NormalTower(String var1, Scaffold scaffold) {
        super(var1, scaffold);
    }
}
