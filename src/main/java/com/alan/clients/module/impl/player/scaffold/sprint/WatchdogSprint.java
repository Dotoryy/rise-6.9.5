package com.alan.clients.module.impl.player.scaffold.sprint;

import com.alan.clients.module.impl.player.Scaffold;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.StrafeEvent;
import com.alan.clients.newevent.impl.packet.PacketSendEvent;
import com.alan.clients.util.player.MoveUtil;
import com.alan.clients.value.Mode;
import net.minecraft.potion.Potion;

public class WatchdogSprint extends Mode<Scaffold> {
    private int blocks;
    @EventLink(value = 0)
    public final Listener<StrafeEvent> onStrafe = var1x -> {
        this.blocks++;
        aEg.gameSettings.cgG.setPressed(true);
        aEg.thePlayer.setSprinting(true);
        double d0 = aEg.thePlayer.isPotionActive(Potion.moveSpeed) ? 0.118 : 0.083;
        if (aEg.thePlayer.onGround) {
            MoveUtil.strafe(d0 - (aEg.thePlayer.Zl <= 40 ? 0.1 : 0.0) - Math.random() * 1.0E-4);
        }

        if (MoveUtil.speed() >= d0 && !aEg.gameSettings.keyBindJump.isKeyDown()) {
            MoveUtil.moveFlying((MoveUtil.speed() - d0) * -1.0);
        }
    };
    @EventLink
    public final Listener<PacketSendEvent> onPacketSend = var0 -> {};

    public WatchdogSprint(String var1, Scaffold scaffold) {
        super(var1, scaffold);
    }

    @Override
    public void onDisable() {
        aEg.thePlayer.capabilities.isFlying = false;
        aEg.timer.dzD = 1.0F;
    }
}
