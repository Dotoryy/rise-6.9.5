package com.alan.clients.ui.palette;

import com.alan.clients.script.ScriptManager;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

final class ScriptSuggestionProvider implements SuggestionProvider {
    private static final String[] aBx = new String[]{"load", "reload", "unload", "disablesecurity", "enablesecurity", "download", "install", "folder"};

    ScriptSuggestionProvider() {
    }

    @Override
    public List<Suggestion> b(SuggestionContext suggestionContext) {
        String[] astring = suggestionContext.rk();
        if (astring.length == 0) {
            return Collections.emptyList();
        }

        String s = CommandPalette.aQ(suggestionContext.rj());
        if (astring.length == 1) {
            String s1 = astring[0] == null ? "" : astring[0].toLowerCase(Locale.ROOT);
            ArrayList arraylist = new ArrayList();

            for (String s2 : aBx) {
                String s3 = s2.toLowerCase(Locale.ROOT);
                if (s1.isEmpty() || s3.startsWith(s1)) {
                    arraylist.add(new Suggestion(s2, "Script action", "load/reload/unload/disablesecurity/enablesecurity/download/install/folder", s2, 0, true));
                }
            }

            arraylist.sort((var2, var3) -> {
                String s8 = ((Suggestion)var2).aBC.toLowerCase(Locale.ROOT);
                String s9 = ((Suggestion)var3).aBC.toLowerCase(Locale.ROOT);
                int k = s8.equals(s1) ? 2 : (s8.startsWith(s1) ? 1 : 0);
                int l = s9.equals(s1) ? 2 : (s9.startsWith(s1) ? 1 : 0);
                if (k != l) {
                    return Integer.compare(l, k);
                }

                long i1 = CommandPalette.b(s, 0, s8);
                long j1 = CommandPalette.b(s, 0, s9);
                return i1 != j1 ? Long.compare(j1, i1) : ((Suggestion)var2).aBC.compareToIgnoreCase(((Suggestion)var3).aBC);
            });
            return arraylist;
        }
        String s4 = astring[0] == null ? "" : astring[0].toLowerCase(Locale.ROOT);
        if (!s4.equals("load") && !s4.equals("unload") && !s4.equals("download") && !s4.equals("install")) {
            return Collections.emptyList();
        }

        String s5 = astring[1] == null ? "" : astring[1].toLowerCase(Locale.ROOT);
        ArrayList arraylist1 = new ArrayList();

        try {
            File file1 = ScriptManager.SCRIPT_DIRECTORY;
            if (file1.exists()) {
                File[] afile = file1.listFiles((var0, var1x) -> var1x != null && var1x.toLowerCase(Locale.ENGLISH).endsWith(".js"));
                if (afile != null) {
                    File[] afile1 = afile;
                    int i = afile1.length;

                    for (int j = 0; j < i; j++) {
                        String s6 = afile1[j].getName().replace(".js", "");
                        String s7 = s6.toLowerCase(Locale.ROOT);
                        if (s5.isEmpty() || s7.startsWith(s5)) {
                            arraylist1.add(new Suggestion(s6, "Script", ".script " + s4 + " " + s6, s6, 1, false));
                        }
                    }
                }
            }
        } catch (Throwable throwable) {
        }

        arraylist1.sort((var2, var3) -> {
            String s8 = ((Suggestion)var2).aBC.toLowerCase(Locale.ROOT);
            String s9 = ((Suggestion)var3).aBC.toLowerCase(Locale.ROOT);
            boolean flag = !s5.isEmpty() && s8.equals(s5);
            boolean flag1 = !s5.isEmpty() && s9.equals(s5);
            if (flag != flag1) {
                return flag ? -1 : 1;
            }

            long k = CommandPalette.b(s, 1, s8);
            long l = CommandPalette.b(s, 1, s9);
            return k != l ? Long.compare(l, k) : ((Suggestion)var2).aBC.compareToIgnoreCase(((Suggestion)var3).aBC);
        });
        return arraylist1;
    }
}
