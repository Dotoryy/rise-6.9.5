package com.alan.clients.module.impl.player.nofall;

import com.alan.clients.module.impl.player.NoFall;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreUpdateEvent;
import com.alan.clients.newevent.impl.packet.PacketSendEvent;
import com.alan.clients.value.Mode;
import com.alan.clients.util.packet.PacketUtil;
import net.minecraft.network.play.client.C03PacketPlayer;

public class MatrixNoFall extends Mode<NoFall> {
    private int fallStage = 0;
    private boolean fallTriggered = false;
    private boolean pendingGroundSpoof = false;
    @EventLink
    public final Listener<PreUpdateEvent> onPreUpdate = var1x -> {
        if (Math.round(aEg.thePlayer.fallDistance) - aEg.thePlayer.motionY > 3.0) {
            aEg.thePlayer.motionY = 0.0;
            aEg.thePlayer.fallDistance = 0.0F;
            aEg.thePlayer.motionX *= 0.1;
            aEg.thePlayer.motionZ *= 0.1;
            this.pendingGroundSpoof = true;
        }

        if (aEg.thePlayer.fallDistance / 3.0F > this.fallStage) {
            this.fallStage = Math.round(aEg.thePlayer.fallDistance) / 3;
            this.fallTriggered = true;
        }

        if (aEg.thePlayer.onGround) {
            this.fallStage = 0;
        }
    };
    @EventLink
    public final Listener<PacketSendEvent> onPacketSend = var1x -> {
        if (var1x.dq() instanceof C03PacketPlayer c03packetplayer && this.pendingGroundSpoof) {
            aEg.timer.dzD = 0.5F;
            c03packetplayer.aO = false;
            PacketUtil.sendNoEvent(new C03PacketPlayer(true));
            this.pendingGroundSpoof = false;
        }
    };

    public MatrixNoFall(String var1, NoFall noFall) {
        super(var1, noFall);
    }

    @Override
    public void onEnable() {
        this.pendingGroundSpoof = false;
        this.fallTriggered = false;
        this.fallStage = 0;
    }
}
