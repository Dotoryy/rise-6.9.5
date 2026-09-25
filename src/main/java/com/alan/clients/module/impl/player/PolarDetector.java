package com.alan.clients.module.impl.player;

import com.alan.clients.module.Module;
import com.alan.clients.module.api.Category;
import com.alan.clients.module.api.ModuleInfo;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.newevent.impl.other.WorldChangeEvent;
import com.alan.clients.newevent.impl.packet.PacketSendEvent;
import com.alan.clients.util.chat.ChatUtil;
import com.alan.clients.component.impl.render.NotificationComponent;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;

@ModuleInfo(aliases = "module.player.polardetector.name", description = "module.player.polardetector.description", category = Category.PLAYER)
public class PolarDetector extends Module {
    boolean polarEnabled = false;
    @EventLink
    public final Listener<PreMotionEvent> onPreMotion = var1 -> {
        if (aEg.thePlayer.ticksExisted == 30) {
            ChatUtil.b(this.polarEnabled ? "Polar is enabled" : "Polar is disabled");
        }
    };
    @EventLink
    public final Listener<PacketSendEvent> onPacketSend = var1 -> {
        if (var1.dq() instanceof C0FPacketConfirmTransaction) {
            this.polarEnabled = true;
        }
    };
    @EventLink
    public final Listener<WorldChangeEvent> onWorldChange = var1 -> this.polarEnabled = false;

    public PolarDetector() {
    }

    @Override
    public void onEnable() {
        NotificationComponent.e("Polar Detector", "Join a game and this module will notify you of polars status");
    }
}
