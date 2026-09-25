package com.alan.clients.ui.palette;

import com.alan.clients.command.Command;
import com.alan.clients.util.localization.Localization;
import java.util.Locale;

final class CommandEntry {
    final String aBk;
    private final String[] aBl;
    final String aBm;
    private final String aBn;
    final String aBo;
    final String aBp;
    double aBq;

    CommandEntry(Command command) {
        this.aBl = command.getExpressions();
        this.aBk = this.aBl.length == 0 ? "" : this.aBl[0];
        this.aBm = Localization.ce(command.getDescription());
        this.aBn = this.aBm.toLowerCase(Locale.ROOT);
        this.aBo = aV(command.getDescription());
        this.aBp = c(this.aBl);
    }

    double aU(String var1) {
        if (var1.isEmpty()) {
            return 1.0;
        }

        double d0 = 0.0;
        String s = this.aBk.toLowerCase(Locale.ROOT);
        if (s.startsWith(var1)) {
            d0 += 6.0;
        } else if (s.contains(var1)) {
            d0 += Math.max(0.0, 4.0 - s.indexOf(var1) * 0.1);
        }

        for (int i = 1; i < this.aBl.length; i++) {
            String s1 = this.aBl[i].toLowerCase(Locale.ROOT);
            if (s1.startsWith(var1)) {
                d0 += 3.0;
            } else if (s1.contains(var1)) {
                d0 += Math.max(0.0, 2.0 - s1.indexOf(var1) * 0.05);
            }
        }

        if (this.aBn.contains(var1)) {
            d0++;
        }

        return d0;
    }

    private static String aV(String var0) {
        if (var0 == null) {
            return null;
        }

        String s = var0.endsWith(".description") ? var0.replace(".description", ".usage") : var0 + ".usage";
        String s1 = Localization.ce(s);
        return s.equals(s1) ? null : s1;
    }

    private static String c(String[] var0) {
        if (var0.length <= 1) {
            return "";
        }

        StringBuilder stringbuilder = new StringBuilder();

        for (int i = 1; i < Math.min(var0.length, 4); i++) {
            if (stringbuilder.length() > 0) {
                stringbuilder.append(", ");
            }

            stringbuilder.append(var0[i]);
        }

        return stringbuilder.toString();
    }
}
