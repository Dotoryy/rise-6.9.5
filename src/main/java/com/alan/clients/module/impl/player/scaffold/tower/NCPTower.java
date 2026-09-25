package com.alan.clients.module.impl.player.scaffold.tower;

import com.alan.clients.module.impl.player.Scaffold;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.value.Mode;
import com.alan.clients.util.packet.PacketUtil;
import com.alan.clients.util.player.PlayerUtil;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;

public class NCPTower extends Mode<Scaffold> {
    @EventLink
    public final Listener<PreMotionEvent> onPreMotionEvent = var0 -> {
        if (aEg.gameSettings.keyBindJump.isKeyDown() && PlayerUtil.ay(2)) {
            PacketUtil.sendNoEvent(new C08PacketPlayerBlockPlacement(null));
            if (aEg.thePlayer.posY % 1.0 <= 0.00153598) {
                aEg.thePlayer.setPosition(aEg.thePlayer.posX, Math.floor(aEg.thePlayer.posY), aEg.thePlayer.posZ);
                aEg.thePlayer.motionY = 0.42F;
            } else if (aEg.thePlayer.posY % 1.0 < 0.1 && aEg.thePlayer.tR != 0) {
                aEg.thePlayer.motionY = 0.0;
                aEg.thePlayer.setPosition(aEg.thePlayer.posX, Math.floor(aEg.thePlayer.posY), aEg.thePlayer.posZ);
            }
        }
    };

    public NCPTower(String var1, Scaffold scaffold) {
        super(var1, scaffold);
    }
}
