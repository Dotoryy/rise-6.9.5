package com.alan.clients.security.impl;

import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.other.WorldChangeEvent;
import com.alan.clients.newevent.impl.packet.PacketReceiveEvent;
import com.alan.clients.security.SecurityFeature;
import com.alan.clients.util.player.ServerUtil;
import net.minecraft.network.play.server.c;

public class HypixelIpNoScoreboardCheck extends SecurityFeature {
    public volatile int avJ;
    public volatile Object avF;
    @EventLink
    public Listener<WorldChangeEvent> onWorldChange;
    public volatile boolean avA;
    public volatile Object avI;
    public static String avB;
    public static String avC;
    public static int avE;
    @EventLink
    public Listener<PacketReceiveEvent> onPacketReceive;
    public volatile boolean avH;
    public static int avD;
    public volatile int avK = Integer.MIN_VALUE;
    public volatile Object avG;


    public HypixelIpNoScoreboardCheck() {
        this.onPacketReceive = var1 -> {
            if (var1.getPacket() instanceof c && aEg != null && aEg.theWorld != null) {
                c c = (c)var1.getPacket();
                String s = c.getChatComponent() != null ? c.getChatComponent().getUnformattedText() : "";
                if (s != null && (s.contains("You were spawned in Limbo.") || s.contains("You are AFK, Move around to return from AFK."))) {
                    this.avG = aEg.theWorld;
                    this.avH = true;
                    this.nR();
                }
            }
        };
        this.onWorldChange = var1 -> {
            if (this.avH) {
                this.avG = aEg != null ? aEg.theWorld : null;
                this.avH = false;
                this.nR();
            } else {
                this.avG = null;
                this.avH = false;
                this.nR();
            }
        };
    }

    @Override
    public String getReason() {
        return "hypixelipnoscoreboard";
    }

    @Override
    public boolean run() {
        if (aEg == null || aEg.thePlayer == null || aEg.theWorld == null || aEg.isIntegratedServerRunning()) {
            this.avG = null;
            this.avH = false;
            this.nR();
            return false;
        }

        if (this.avG == aEg.theWorld) {
            return false;
        }

        if (this.avA && this.avF == aEg.theWorld) {
            return true;
        }

        if (aEg.thePlayer.ticksExisted < 150) {
            return false;
        }

        if (!ServerUtil.vr()) {
            return false;
        }

        if (this.avI != aEg.theWorld) {
            this.avI = aEg.theWorld;
            this.avJ = 0;
            this.avK = Integer.MIN_VALUE;
        }

        if (ServerUtil.vm()) {
            this.avJ = 0;
            this.avK = Integer.MIN_VALUE;
            return false;
        }

        int ticksExisted2 = aEg.thePlayer.ticksExisted;
        if (this.avK == Integer.MIN_VALUE || ticksExisted2 - this.avK >= 100) {
            this.avJ++;
            this.avK = ticksExisted2;
        }

        if (this.avJ >= 5) {
            this.avA = true;
            this.avF = aEg.theWorld;
        }

        return this.avA;
    }

    public void nR() {
        this.avA = false;
        this.avF = null;
        this.avI = null;
        this.avJ = 0;
        this.avK = Integer.MIN_VALUE;
    }

    static {
    }
}
