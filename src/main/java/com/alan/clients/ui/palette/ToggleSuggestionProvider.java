package com.alan.clients.ui.palette;

import com.alan.clients.Client;
import com.alan.clients.module.Module;
import com.alan.clients.ui.palette.CommandPalette;
import com.alan.clients.ui.palette.Suggestion;
import com.alan.clients.ui.palette.SuggestionContext;
import com.alan.clients.ui.palette.SuggestionProvider;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

final class ToggleSuggestionProvider
implements SuggestionProvider {
    ToggleSuggestionProvider() {
    }

    @Override
    public List<Suggestion> b(SuggestionContext suggestionContext) {
        String[] stringArray = suggestionContext.rk();
        if (stringArray.length == 0) {
            return Collections.emptyList();
        }
        String string = CommandPalette.aQ(suggestionContext.rj());
        String string2 = stringArray[0] == null ? "" : stringArray[0].toLowerCase(Locale.ROOT);
        String string3 = string2 == null ? "" : string2.trim().toLowerCase(Locale.ROOT).replace(" ", "");
        ArrayList<Module> arrayList = new ArrayList<Module>();
        for (Module module3 : Client.a.g().getAll()) {
            if (module3 == null || !string3.isEmpty() && this.b(module3, string3) <= 1) continue;
            arrayList.add(module3);
        }
        arrayList.sort((module, module2) -> {
            long l2;
            int n2;
            int n3 = this.b((Module)module, string3);
            if (n3 != (n2 = this.b((Module)module2, string3))) {
                return Integer.compare(n2, n3);
            }
            long l3 = this.a(string, 0, (Module)module);
            if (l3 != (l2 = this.a(string, 0, (Module)module2))) {
                return Long.compare(l2, l3);
            }
            return module.getName().compareToIgnoreCase(module2.getName());
        });
        ArrayList<Suggestion> arrayList2 = new ArrayList<Suggestion>();
        Iterator iterator = arrayList.iterator();
        while (iterator.hasNext()) {
            Module module4 = (Module)iterator.next();
            String string4 = module4.getName().replace(" ", "");
            arrayList2.add(new Suggestion(module4.getName(), "Module", ".toggle <module>", string4, 0, false));
        }
        return arrayList2;
    }

    private int b(Module module, String string) {
        if (string == null || string.isEmpty()) {
            return 0;
        }
        int n2 = 1;
        int n3 = Math.max(n2, this.s(module.getName(), string));
        String[] stringArray = module.getAliases();
        if (stringArray != null) {
            for (String string2 : stringArray) {
                n3 = Math.max(n3, this.s(string2, string));
            }
        }
        return n3;
    }

    private int s(String string, String string2) {
        if (string == null) {
            return 1;
        }
        String string3 = string.toLowerCase(Locale.ROOT).replace(" ", "");
        if (string3.equals(string2)) {
            return 4;
        }
        if (string3.startsWith(string2)) {
            return 3;
        }
        if (string3.contains(string2)) {
            return 2;
        }
        return 1;
    }

    private long a(String string, int n2, Module module) {
        String string2;
        long l2 = 0L;
        String[] stringArray = module.getAliases();
        if (stringArray != null) {
            for (String string3 : stringArray) {
                String string4;
                if (string3 == null || (string4 = string3.replace(" ", "")).isEmpty()) continue;
                l2 = Math.max(l2, CommandPalette.b(string, n2, string4));
            }
        }
        if ((string2 = module.getName()) == null) return l2;
        String string5 = string2.replace(" ", "");
        if (string5.isEmpty()) return l2;
        return Math.max(l2, CommandPalette.b(string, n2, string5));
    }
}
