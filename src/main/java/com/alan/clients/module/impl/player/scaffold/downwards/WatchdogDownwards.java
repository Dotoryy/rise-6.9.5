package com.alan.clients.module.impl.player.scaffold.downwards;

import com.alan.clients.module.impl.player.Scaffold;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreUpdateEvent;
import com.alan.clients.newevent.impl.motion.StrafeEvent;
import com.alan.clients.newevent.impl.packet.PacketSendEvent;
import com.alan.clients.value.Mode;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import org.lwjgl.input.Keyboard;

public class WatchdogDownwards extends Mode<Scaffold> {
    public static int fullBlockTicks;
    @EventLink(value = 0)
    public final Listener<StrafeEvent> onStrafe = var0 -> {
        if (Keyboard.isKeyDown(aEg.gameSettings.keyBindSneak.getKeyCode())) {
            ;
        }
    };
    @EventLink(value = 3)
    public final Listener<PreUpdateEvent> onPreUpdate = var0 -> {
        if (!Keyboard.isKeyDown(aEg.gameSettings.keyBindSneak.getKeyCode()) || aEg.thePlayer.crH > 20) {
            ;
        }
    };
    @EventLink
    public final Listener<PacketSendEvent> onPacketSend = var0 -> {
        Packet packet = var0.dq();
        if (packet instanceof C08PacketPlayerBlockPlacement) {
            ;
        }
    };

    public WatchdogDownwards(String var1, Scaffold scaffold) {
        super(var1, scaffold);
    }

    @Override
    public void onEnable() {
        fullBlockTicks = 0;
    }
}
