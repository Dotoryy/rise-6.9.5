package com.alan.clients.module.impl.player.scaffold.tower;

import com.alan.clients.module.impl.player.Scaffold;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.newevent.impl.packet.PacketSendEvent;
import com.alan.clients.util.player.MoveUtil;
import com.alan.clients.value.Mode;
import com.alan.clients.util.player.PlayerUtil;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;

public class SnapTower extends Mode<Scaffold> {
    private int ajX = 5;
    @EventLink
    public final Listener<PreMotionEvent> onPreMotion = var0 -> {
        if (aEg.gameSettings.keyBindJump.isKeyDown() && PlayerUtil.ay(2) && !MoveUtil.isMoving()) {
            if (aEg.thePlayer.posY % 1.0 <= 0.00153598) {
                aEg.thePlayer.setPosition(aEg.thePlayer.posX, Math.floor(aEg.thePlayer.posY), aEg.thePlayer.posZ);
                aEg.thePlayer.motionY = 0.42F;
            } else if (aEg.thePlayer.posY % 1.0 < 0.1 && aEg.thePlayer.tR != 0) {
                aEg.thePlayer.motionY = 0.0;
                aEg.thePlayer.setPosition(aEg.thePlayer.posX, Math.floor(aEg.thePlayer.posY), aEg.thePlayer.posZ);
            }
        }

        if (MoveUtil.isMoving() && aEg.gameSettings.keyBindJump.isKeyDown() && aEg.thePlayer.onGround) {
            aEg.thePlayer.jump();
        }
    };
    @EventLink
    public final Listener<PacketSendEvent> onPacketSend = var0 -> {
        Packet packet = var0.dq();
        if (MoveUtil.isMoving()
            && aEg.thePlayer.motionY > -0.09800000190734864
            && packet instanceof C08PacketPlayerBlockPlacement
            && ((C08PacketPlayerBlockPlacement)packet).getPosition().equals(new BlockPos(aEg.thePlayer.posX, aEg.thePlayer.posY - 1.4, aEg.thePlayer.posZ))) {
            aEg.thePlayer.motionY = -0.09800000190734864;
        }
    };

    public SnapTower(String var1, Scaffold scaffold) {
        super(var1, scaffold);
    }
}
