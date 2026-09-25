package com.alan.clients.module.impl.player.scaffold.tower;

import com.alan.clients.module.impl.player.Scaffold;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.packet.PacketSendEvent;
import com.alan.clients.value.Mode;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;

public class MMCTower extends Mode<Scaffold> {
    @EventLink
    public final Listener<PacketSendEvent> onPacketSend = var0 -> {
        Packet packet = var0.dq();
        if (aEg.gameSettings.keyBindJump.isKeyDown()
            && packet instanceof C08PacketPlayerBlockPlacement
            && ((C08PacketPlayerBlockPlacement)packet).getPosition().equals(new BlockPos(aEg.thePlayer.posX, aEg.thePlayer.posY - 1.4, aEg.thePlayer.posZ))) {
            aEg.gameSettings.cgG.setPressed(false);
            aEg.thePlayer.setSprinting(false);
            aEg.thePlayer.motionY = 0.42F;
        }
    };

    public MMCTower(String var1, Scaffold scaffold) {
        super(var1, scaffold);
    }
}
