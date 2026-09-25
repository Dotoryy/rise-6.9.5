package com.alan.clients.ui.palette;

import com.alan.clients.Client;
import com.alan.clients.util.file.insult.InsultManager;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

final class InsultSuggestionProvider implements SuggestionProvider {
    private static final String[] aBs = new String[]{"create", "delete"};

    InsultSuggestionProvider() {
    }

    @Override
    public List<Suggestion> b(SuggestionContext suggestionContext) {
        String[] astring = suggestionContext.rk();
        if (astring.length == 0) {
            return Collections.emptyList();
        }

        String s = CommandPalette.aQ(suggestionContext.rj());
        if (astring.length != 1) {
            String s4 = astring[0] == null ? "" : astring[0].toLowerCase(Locale.ROOT);
            if (!s4.equals("delete")) {
                return Collections.emptyList();
            }

            String s5 = astring[1] == null ? "" : astring[1].toLowerCase(Locale.ROOT);
            InsultManager insultManager = Client.a.getInsultManager();
            insultManager.update();
            ArrayList arraylist1 = new ArrayList();
            insultManager.forEach(var2 -> {
                String s6 = var2.getFile().getName().replace(".txt", "");
                String s7 = s6.toLowerCase(Locale.ROOT);
                if (s5.isEmpty() || s7.startsWith(s5)) {
                    arraylist1.add(new Suggestion(s6, "Insult file", ".insults delete <name>", s6, 1, false));
                }
            });
            arraylist1.sort((var2, var3) -> {
                String s6 = ((Suggestion)var2).aBC.toLowerCase(Locale.ROOT);
                String s7 = ((Suggestion)var3).aBC.toLowerCase(Locale.ROOT);
                boolean flag = !s5.isEmpty() && s6.equals(s5);
                boolean flag1 = !s5.isEmpty() && s7.equals(s5);
                if (flag != flag1) {
                    return flag ? -1 : 1;
                }

                long i = CommandPalette.b(s, 1, s6);
                long j = CommandPalette.b(s, 1, s7);
                return i != j ? Long.compare(j, i) : ((Suggestion)var2).aBC.compareToIgnoreCase(((Suggestion)var3).aBC);
            });
            return arraylist1;
        }
        String s1 = astring[0] == null ? "" : astring[0].toLowerCase(Locale.ROOT);
        ArrayList arraylist = new ArrayList();

        for (String s2 : aBs) {
            String s3 = s2.toLowerCase(Locale.ROOT);
            if (s1.isEmpty() || s3.startsWith(s1)) {
                arraylist.add(new Suggestion(s2, "Insults action", ".insults <create/delete> <name>", s2, 0, true));
            }
        }

        arraylist.sort((var2, var3) -> {
            String s6 = ((Suggestion)var2).aBC.toLowerCase(Locale.ROOT);
            String s7 = ((Suggestion)var3).aBC.toLowerCase(Locale.ROOT);
            int i = s6.equals(s1) ? 2 : (s6.startsWith(s1) ? 1 : 0);
            int j = s7.equals(s1) ? 2 : (s7.startsWith(s1) ? 1 : 0);
            if (i != j) {
                return Integer.compare(j, i);
            }

            long k = CommandPalette.b(s, 0, s6);
            long l = CommandPalette.b(s, 0, s7);
            return k != l ? Long.compare(l, k) : ((Suggestion)var2).aBC.compareToIgnoreCase(((Suggestion)var3).aBC);
        });
        return arraylist;
    }
}
