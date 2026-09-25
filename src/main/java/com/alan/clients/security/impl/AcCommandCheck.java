package com.alan.clients.security.impl;

import com.alan.clients.security.SecurityFeature;
import com.alan.clients.security.ChatMessageObserver;
import java.util.Locale;
import java.util.regex.Pattern;

public class AcCommandCheck
extends SecurityFeature
implements ChatMessageObserver {
    public volatile boolean avA;
    public static Pattern avN;

    @Override
    public boolean run() {
        return this.avA;
    }

    @Override
    public void ar(String string) {
        if (string == null) return;
        if (this.avA) {
            return;
        }
        String string2 = string.trim().toLowerCase(Locale.ENGLISH);
        if (!string2.startsWith("/") || string2.contains(" ")) {
            return;
        }
        if (avN.matcher(string2).matches()) {
            this.avA = true;
        }
    }

    @Override
    public String getReason() {
        return "accommand";
    }


    static {
        avN = Pattern.compile("^/(?:ac[a-z]+|verus[a-z]*|grim[a-z]*|vulcan[a-z]*|alerts?[a-z]*|verbose[a-z]*|watchdog[a-z]*|anticheat[a-z]*|ncp[a-z]*|aac[a-z]*|karhu[a-z]*|matrix[a-z]*|spartan[a-z]*|intave[a-z]*|polar[a-z]*|hawk[a-z]*)$", 2);
    }
}
