package com.alan.clients.ui.palette;

import com.alan.clients.Client;
import com.alan.clients.command.impl.Bind;
import com.alan.clients.module.Module;
import com.alan.clients.util.file.config.ConfigFile;
import com.alan.clients.util.localization.Localization;
import com.alan.clients.util.interfaces.Bindable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

final class BindSuggestionProvider implements SuggestionProvider {
    BindSuggestionProvider() {
    }

    @Override
    public void a(SuggestionContext suggestionContext) {
        try {
            Client.a.getConfigManager().update();
        } catch (Throwable throwable) {
        }
    }

    @Override
    public List<Suggestion> b(SuggestionContext suggestionContext) {
        if (suggestionContext.rk().length != 0 && suggestionContext.rk().length <= 1) {
            String s = CommandPalette.aQ(suggestionContext.rj());
            String s1 = suggestionContext.rk()[0].toLowerCase(Locale.ROOT);
            List list = Client.a.t().aP();
            ArrayList arraylist = new ArrayList();
            HashSet hashset = new HashSet();

            for (Bindable p : (Iterable<Bindable>)list) {
                if (p != null && this.b(p, s1)) {
                    String s2 = this.b(p);
                    if (s2 != null) {
                        s2 = s2.replace(" ", "");
                        String s3 = s2.toLowerCase(Locale.ROOT);
                        if (hashset.add(s3)) {
                            String s4 = this.c(p);
                            String s5 = this.d(p);
                            arraylist.add(new PaletteCommandRow(p, s2, s4, s5));
                        }
                    }
                }
            }

            arraylist.sort((var3x, var4x) -> {
                int j = this.a(((PaletteCommandRow)var3x).aBe(), s1);
                int k = this.a(((PaletteCommandRow)var4x).aBe(), s1);
                if (j != k) {
                    return Integer.compare(k, j);
                }

                long l = this.a(s, 0, ((PaletteCommandRow)var3x).aBe(), ((PaletteCommandRow)var3x).aBf());
                long i1 = this.a(s, 0, ((PaletteCommandRow)var4x).aBe(), ((PaletteCommandRow)var4x).aBf());
                if (l != i1) {
                    return Long.compare(i1, l);
                }

                String s7 = ((PaletteCommandRow)var3x).aBf().toLowerCase(Locale.ROOT);
                String s8 = ((PaletteCommandRow)var4x).aBf().toLowerCase(Locale.ROOT);
                if (s7.length() != s8.length()) {
                    return Integer.compare(s7.length(), s8.length());
                }

                String s9 = ((PaletteCommandRow)var3x).aBe().getName() == null ? ((PaletteCommandRow)var3x).aBf() : ((PaletteCommandRow)var3x).aBe().getName();
                String s10 = ((PaletteCommandRow)var4x).aBe().getName() == null ? ((PaletteCommandRow)var4x).aBf() : ((PaletteCommandRow)var4x).aBe().getName();
                return s9.compareToIgnoreCase(s10);
            });
            ArrayList arraylist1 = new ArrayList();

            for (int i = 0; i < Math.min(80, arraylist.size()); i++) {
                PaletteCommandRow aco = (PaletteCommandRow)arraylist.get(i);
                String s6 = aco.aBe().getName() == null ? aco.aBf() : aco.aBe().getName();
                arraylist1.add(new Suggestion(s6, aco.aBg(), aco.aBh(), aco.aBf(), 0, true));
            }

            return arraylist1;
        }
        return Collections.emptyList();
    }

    private int a(Bindable var1, String var2) {
        if (var2 == null) {
            var2 = "";
        }

        String s = var2.trim().toLowerCase(Locale.ROOT).replace(" ", "");
        if (s.isEmpty()) {
            return 0;
        }

        int i = 1;
        i = Math.max(i, this.s(var1.getName(), s));
        String[] astring = var1.getAliases();
        if (astring != null) {
            for (String s1 : astring) {
                i = Math.max(i, this.s(s1, s));
            }
        }

        return i;
    }

    private int s(String var1, String var2) {
        if (var1 == null) {
            return 1;
        }
        String s = var1.toLowerCase(Locale.ROOT).replace(" ", "");
        if (s.equals(var2)) {
            return 4;
        } else if (s.startsWith(var2)) {
            return 3;
        }
        return s.contains(var2) ? 2 : 1;
    }

    private long a(String var1, int var2, Bindable var3, String var4) {
        long i = 0L;
        if (var4 != null) {
            i = Math.max(i, CommandPalette.b(var1, var2, var4));
        }

        String[] astring = var3.getAliases();
        if (astring != null) {
            for (String s : astring) {
                if (s != null) {
                    String s1 = s.replace(" ", "");
                    if (!s1.isEmpty()) {
                        i = Math.max(i, CommandPalette.b(var1, var2, s1));
                    }
                }
            }
        }

        String s2 = var3.getName();
        if (s2 != null) {
            String s3 = s2.replace(" ", "");
            if (!s3.isEmpty()) {
                i = Math.max(i, CommandPalette.b(var1, var2, s3));
            }
        }

        return i;
    }

    private boolean b(Bindable var1, String var2) {
        if (var2.isEmpty()) {
            return true;
        }

        if ((var1.getName() == null ? "" : var1.getName().toLowerCase(Locale.ROOT)).contains(var2)) {
            return true;
        }

        String[] astring = var1.getAliases();
        if (astring != null) {
            for (String s : astring) {
                if (s != null && s.toLowerCase(Locale.ROOT).contains(var2)) {
                    return true;
                }
            }
        }

        return false;
    }

    private String b(Bindable var1) {
        String[] astring = var1.getAliases();
        if (astring != null && astring.length != 0) {
            String s = null;

            for (String s1 : astring) {
                if (s1 != null && !s1.trim().isEmpty()) {
                    if (!s1.contains(" ")) {
                        s = s1;
                        break;
                    }

                    if (s == null) {
                        s = s1;
                    }
                }
            }

            return s != null ? s : var1.getName();
        }
        return var1.getName();
    }

    private String c(Bindable var1) {
        if (var1 instanceof Module) {
            return Localization.ce(((Module)var1).getModuleInfo().category().getName()) + " • Module";
        }
        return var1 instanceof ConfigFile ? "Config" : "Bindable";
    }

    private String d(Bindable var1) {
        int i = var1.getKey();
        return i == 0 ? Localization.ce("ui.command.palette.bind.meta.unbound") : String.format(Localization.ce("ui.command.palette.bind.meta.bound"), Bind.b(i));
    }
}
