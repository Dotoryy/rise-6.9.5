package com.alan.clients.security.impl;

import com.alan.clients.security.SecurityFeature;
import com.alan.clients.security.ChatMessageObserver;
import java.util.Locale;
import java.util.regex.Pattern;

public class SusChatCheck
extends SecurityFeature
implements ChatMessageObserver {
    public volatile boolean avA;
    public static Pattern[] avR;

    @Override
    public void ar(String string) {
        if (string == null) return;
        if (this.avA) {
            return;
        }
        String string2 = string.trim().toLowerCase(Locale.ENGLISH);
        if (string2.isEmpty()) return;
        if (string2.startsWith("/")) return;
        if (string2.startsWith(".")) {
            return;
        }
        Pattern[] patternArray = avR;
        int count = patternArray.length;
        int i = 0;
        while (i < count) {
            if (patternArray[i].matcher(string2).find()) {
                this.avA = true;
                return;
            }
            i++;
        }
    }


    @Override
    public boolean run() {
        return this.avA;
    }

    static {
        avR = new Pattern[]{Pattern.compile("\\bpacket\\s*log(?:ger)?\\b", 2), Pattern.compile("\\bpacket\\s*debug(?:ger)?\\b", 2), Pattern.compile("\\bdebug(?:ger)?\\b", 2), Pattern.compile("\\bverbose\\b", 2), Pattern.compile("\\balerts?\\b", 2), Pattern.compile("\\bproxy\\b|\\bmitm\\b|\\bmitmproxy\\b|\\bsniff(?:er|ing)?\\b", 2), Pattern.compile("\\bwireshark\\b|\\bburp\\b|\\bcharles\\b|\\bproxyman\\b", 2), Pattern.compile("\\bfrida\\b|\\bjdwp\\b|\\bjfr\\b|\\battach\\b|\\bjavaagent\\b|\\bagentlib\\b", 2), Pattern.compile("\\brecaf\\b|\\bdecompil(?:e|er|ing)\\b|\\bbytecode\\b|\\bhook(?:ing)?\\b|\\binject(?:ion|ing)?\\b", 2)};
    }

    @Override
    public String getReason() {
        return "suschat";
    }
}
