package com.alan.clients.security.impl;

import com.alan.clients.security.SecurityFeature;
import com.alan.clients.security.ChatMessageObserver;
import java.util.Locale;

public class DebugOrPacketCommandCheck extends SecurityFeature implements ChatMessageObserver {
    public volatile boolean avA;


    static {
    }

    @Override
    public boolean run() {
        return this.avA;
    }

    @Override
    public String getReason() {
        return "debugorpacketcommand";
    }

    public DebugOrPacketCommandCheck() {
    }

    @Override
    public void ar(String var1) {
        if (var1 != null && !this.avA) {
            String s = var1.trim().toLowerCase(Locale.ENGLISH);
            if (s.startsWith("/") || s.startsWith(".")) {
                if (s.contains("debug") || s.contains("packet")) {
                    this.avA = true;
                }
            }
        }
    }
}
