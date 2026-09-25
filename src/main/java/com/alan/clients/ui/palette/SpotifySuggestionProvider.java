package com.alan.clients.ui.palette;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

final class SpotifySuggestionProvider implements SuggestionProvider {
    private static final String[] aBy = new String[]{"clientid", "clientsecret"};

    SpotifySuggestionProvider() {
    }

    @Override
    public List<Suggestion> b(SuggestionContext suggestionContext) {
        String[] astring = suggestionContext.rk();
        if (astring.length == 0) {
            return Collections.emptyList();
        }

        String s = CommandPalette.aQ(suggestionContext.rj());
        if (astring.length != 1) {
            return Collections.emptyList();
        }

        String s1 = astring[0] == null ? "" : astring[0].toLowerCase(Locale.ROOT);
        ArrayList arraylist = new ArrayList();

        for (String s2 : aBy) {
            String s3 = s2.toLowerCase(Locale.ROOT);
            if (s1.isEmpty() || s3.startsWith(s1)) {
                arraylist.add(new Suggestion(s2, "Spotify setting", ".spotify clientid / .spotify clientsecret", s2, 0, true));
            }
        }

        arraylist.sort((var2, var3) -> {
            String s4 = ((Suggestion)var2).aBC.toLowerCase(Locale.ROOT);
            String s5 = ((Suggestion)var3).aBC.toLowerCase(Locale.ROOT);
            int i = s4.equals(s1) ? 2 : (s4.startsWith(s1) ? 1 : 0);
            int j = s5.equals(s1) ? 2 : (s5.startsWith(s1) ? 1 : 0);
            if (i != j) {
                return Integer.compare(j, i);
            }

            long k = CommandPalette.b(s, 0, s4);
            long l = CommandPalette.b(s, 0, s5);
            return k != l ? Long.compare(l, k) : ((Suggestion)var2).aBC.compareToIgnoreCase(((Suggestion)var3).aBC);
        });
        return arraylist;
    }
}
