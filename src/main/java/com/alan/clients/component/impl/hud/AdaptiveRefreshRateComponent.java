package com.alan.clients.component.impl.hud;

import com.alan.clients.component.Component;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.newevent.impl.packet.PacketReceiveEvent;
import com.alan.clients.newevent.impl.render.Render2DEvent;
import com.alan.clients.ui.ingame.GuiIngameCache;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S30PacketWindowItems;
import net.minecraft.network.play.server.c;
import rip.vantage.commons.util.time.StopWatch;

public class AdaptiveRefreshRateComponent extends Component {
    private boolean keyDown;
    private boolean debugInfoShown;
    private boolean forceRefresh;
    private int disabledFor;
    private float health;
    public final StopWatch inventoryPacketTimer = new StopWatch();
    @EventLink
    public final Listener<PreMotionEvent> onPreMotion = var1 -> {
        //add code
        if (aEg.thePlayer.ticksExisted % 100 == 0
            || this.forceRefresh
            || aEg.gameSettings.keyBindPlayerList.isKeyDown() != this.keyDown
            || aEg.gameSettings.bJf
            || aEg.gameSettings.bJf != this.debugInfoShown
            || aEg.thePlayer.ticksExisted <= 10
            || this.disabledFor > 0) {
            GuiIngameCache.dirty = true;
            this.keyDown = aEg.gameSettings.keyBindPlayerList.isKeyDown();
            this.debugInfoShown = aEg.gameSettings.bJf;
            this.forceRefresh = false;
        }

        if (this.health != aEg.thePlayer.getHealth()) {
            this.disabledFor = 3;
            this.health = aEg.thePlayer.getHealth();
        }

        this.disabledFor--;
    };
    @EventLink
    public final Listener<Render2DEvent> onRender2D = var1 -> {
        if (aEg.currentScreen != null) {
            this.forceRefresh = true;
            GuiIngameCache.dirty = true;
        }
    };
    @EventLink
    public final Listener<PacketReceiveEvent> onPacketReceive = var1 -> {
        Packet packet = var1.getPacket();
        if (packet instanceof c || packet instanceof S30PacketWindowItems || packet instanceof net.minecraft.network.play.server.az) {
            this.inventoryPacketTimer.aX();
            this.forceRefresh = true;
        }

        if (packet instanceof net.minecraft.network.play.server.o) {
            this.disabledFor = 5;
        }
    };

    public AdaptiveRefreshRateComponent() {
    }
}
