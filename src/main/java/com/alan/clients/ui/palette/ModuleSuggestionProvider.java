package com.alan.clients.ui.palette;

import com.alan.clients.Client;
import com.alan.clients.module.Module;
import com.alan.clients.value.Mode;
import com.alan.clients.value.Value;
import com.alan.clients.value.impl.BooleanValue;
import com.alan.clients.value.impl.ModeValue;
import com.alan.clients.value.impl.NumberValue;
import com.alan.clients.value.impl.StringValue;
import com.alan.clients.ui.palette.CommandPalette;
import com.alan.clients.ui.palette.Suggestion;
import com.alan.clients.ui.palette.SuggestionContext;
import com.alan.clients.ui.palette.SuggestionProvider;
import com.alan.clients.util.localization.Localization;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

final class ModuleSuggestionProvider
implements SuggestionProvider {
    ModuleSuggestionProvider() {
    }

    @Override
    public List<Suggestion> b(SuggestionContext suggestionContext) {
        ArrayList<Suggestion> arrayList;
        String string;
        String string2;
        block26: {
            int n2;
            String[] stringArray;
            Object t2;
            block27: {
                Iterator<Mode<?>> iterator;
                block25: {
                    Iterator<Value<?>> iterator2;
                    ArrayList<Suggestion> arrayList2;
                    String string3;
                    block23: {
                        Iterator<Module> iterator3;
                        ArrayList<Module> arrayList3;
                        String string4;
                        block21: {
                            Value<?> value;
                            block24: {
                                String[] stringArray2;
                                block22: {
                                    block20: {
                                        stringArray2 = suggestionContext.rk();
                                        if (stringArray2.length == 0) {
                                            return Collections.emptyList();
                                        }
                                        string2 = CommandPalette.aQ(suggestionContext.rj());
                                        if (stringArray2.length != 1) break block20;
                                        String string5 = stringArray2[0] == null ? "" : stringArray2[0].toLowerCase(Locale.ROOT);
                                        string4 = string5.trim().toLowerCase(Locale.ROOT).replace(" ", "");
                                        arrayList3 = new ArrayList<Module>();
                                        iterator3 = Client.a.g().getAll().iterator();
                                        break block21;
                                    }
                                    String string6 = stringArray2[0];
                                    t2 = Client.a.g().get(string6);
                                    if (t2 == null) {
                                        return Collections.emptyList();
                                    }
                                    if (stringArray2.length != 2) break block22;
                                    string3 = stringArray2[1] == null ? "" : stringArray2[1].toLowerCase(Locale.ROOT);
                                    arrayList2 = new ArrayList<Suggestion>();
                                    iterator2 = ((Module)t2).getAllValues().iterator();
                                    break block23;
                                }
                                if (stringArray2.length < 3) {
                                    return Collections.emptyList();
                                }
                                String string7 = stringArray2[1];
                                string = stringArray2[2] == null ? "" : stringArray2[2].toLowerCase(Locale.ROOT);
                                value = ModuleSuggestionProvider.c((Module)t2, string7);
                                if (value == null) {
                                    return Collections.emptyList();
                                }
                                arrayList = new ArrayList<Suggestion>();
                                if (!(value instanceof ModeValue)) break block24;
                                ModeValue modeValue = (ModeValue)value;
                                iterator = modeValue.getModes().iterator();
                                break block25;
                            }
                            if (!(value instanceof BooleanValue)) break block26;
                            stringArray = new String[]{"true", "false"};
                            n2 = stringArray.length;
                            break block27;
                        }
                        while (iterator3.hasNext()) {
                            Module module3 = iterator3.next();
                            if (module3 == null || !string4.isEmpty() && ModuleSuggestionProvider.b(module3, string4) <= 1) continue;
                            arrayList3.add(module3);
                        }
                        arrayList3.sort((module, module2) -> {
                            long l2;
                            int n222;
                            int n3 = ModuleSuggestionProvider.b(module, string4);
                            if (n3 != (n222 = ModuleSuggestionProvider.b(module2, string4))) {
                                return Integer.compare(n222, n3);
                            }
                            long l3 = ModuleSuggestionProvider.a(string2, 0, module);
                            if (l3 != (l2 = ModuleSuggestionProvider.a(string2, 0, module2))) {
                                return Long.compare(l2, l3);
                            }
                            return module.getName().compareToIgnoreCase(module2.getName());
                        });
                        ArrayList<Suggestion> arrayList4 = new ArrayList<Suggestion>();
                        Iterator iterator4 = arrayList3.iterator();
                        while (true) {
                            if (!iterator4.hasNext()) {
                                return arrayList4;
                            }
                            Module module4 = (Module)iterator4.next();
                            String string9 = module4.getName();
                            String string10 = string9.replace(" ", "");
                            arrayList4.add(new Suggestion(string9, Localization.ce(module4.getModuleInfo().description()), module4.getModuleInfo().category().getName(), string10, 0, true));
                        }
                    }
                    while (iterator2.hasNext()) {
                        boolean bl;
                        String string11;
                        Value<?> value = iterator2.next();
                        if (value.getBooleanSupplier() != null && value.getBooleanSupplier().getAsBoolean()) continue;
                        String string12 = value.getName();
                        String string13 = string12.replace(" ", "");
                        String string14 = string13.toLowerCase(Locale.ROOT);
                        if (!string3.isEmpty() && !string14.startsWith(string3)) continue;
                        if (value instanceof NumberValue) {
                            string11 = "Number";
                            bl = true;
                        } else if (value instanceof BooleanValue) {
                            string11 = "Boolean";
                            bl = true;
                        } else if (value instanceof ModeValue) {
                            string11 = "Mode";
                            bl = true;
                        } else if (value instanceof StringValue) {
                            string11 = "String";
                            bl = true;
                        } else {
                            string11 = "Value";
                            bl = true;
                        }
                        arrayList2.add(new Suggestion(string12, ((Module)t2).getName() + " setting", string11, string13, 1, bl));
                    }
                    ModeValue modeValue = ModuleSuggestionProvider.a((Module)t2);
                    if (modeValue != null) {
                        String string15 = string3;
                        for (Mode<?> mode : modeValue.getModes()) {
                            String string16 = mode.getName();
                            String string17 = string16.toLowerCase(Locale.ROOT);
                            if (!string15.isEmpty() && !string17.startsWith(string15)) continue;
                            String string18 = string16.replace(" ", "");
                            arrayList2.add(new Suggestion(string16, "Mode", "Mode for " + ((Module)t2).getName(), string18, 1, false));
                        }
                    }
                    arrayList2.sort((acy2, acy3) -> {
                        long l2;
                        int n22;
                        String string32 = acy2.aBC.toLowerCase(Locale.ROOT);
                        String string4 = acy3.aBC.toLowerCase(Locale.ROOT);
                        int n3 = string32.equals(string3) ? 2 : (!string3.isEmpty() && string32.startsWith(string3) ? 1 : 0);
                        int n4 = string4.equals(string3) ? 2 : (!string3.isEmpty() && string4.startsWith(string3) ? 1 : 0);
                        if (n3 != n4) {
                            return Integer.compare(n4, n3);
                        }
                        long l3 = CommandPalette.b(string2, 1, string32);
                        if (l3 != (l2 = CommandPalette.b(string2, 1, string4))) {
                            return Long.compare(l2, l3);
                        }
                        return acy2.aBz.compareToIgnoreCase(acy3.aBz);
                    });
                    return arrayList2;
                }
                while (iterator.hasNext()) {
                    Mode<?> mode = iterator.next();
                    String string19 = mode.getName();
                    String string20 = string19.toLowerCase(Locale.ROOT);
                    if (!string.isEmpty() && !string20.startsWith(string)) continue;
                    String string21 = string19.replace(" ", "");
                    arrayList.add(new Suggestion(string19, ((Module)t2).getName() + " mode", "Modes", string21, 2, false));
                }
                break block26;
            }
            for (int i2 = 0; i2 < n2; ++i2) {
                String string22 = stringArray[i2];
                if (!string.isEmpty() && !string22.startsWith(string)) continue;
                arrayList.add(new Suggestion(string22, ((Module)t2).getName() + " flag", "Boolean", string22, 2, false));
            }
        }
        arrayList.sort((acy2, acy3) -> {
            long l2;
            int n2;
            String string3 = acy2.aBC.toLowerCase(Locale.ROOT);
            String string4 = acy3.aBC.toLowerCase(Locale.ROOT);
            int n3 = string3.equals(string) ? 2 : (!string.isEmpty() && string3.startsWith(string) ? 1 : 0);
            int n4 = string4.equals(string) ? 2 : (!string.isEmpty() && string4.startsWith(string) ? 1 : 0);
            if (n3 != n4) {
                return Integer.compare(n4, n3);
            }
            long l3 = CommandPalette.b(string2, 2, string3);
            if (l3 != (l2 = CommandPalette.b(string2, 2, string4))) {
                return Long.compare(l2, l3);
            }
            return acy2.aBz.compareToIgnoreCase(acy3.aBz);
        });
        return arrayList;
    }

    private static int b(Module module, String string) {
        if (string == null || string.isEmpty()) {
            return 0;
        }
        int n2 = 1;
        int n3 = Math.max(n2, ModuleSuggestionProvider.s(module.getName(), string));
        String[] stringArray = module.getAliases();
        if (stringArray != null) {
            for (String string2 : stringArray) {
                n3 = Math.max(n3, ModuleSuggestionProvider.s(string2, string));
            }
        }
        return n3;
    }

    private static int s(String string, String string2) {
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

    private static long a(String string, int n2, Module module) {
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

    private static ModeValue a(Module module) {
        ModeValue modeValue = null;
        for (Value<?> value : module.getAllValues()) {
            if (!(value instanceof ModeValue)) continue;
            ModeValue modeValue2 = (ModeValue)value;
            if ("mode".equalsIgnoreCase(value.getName())) {
                return modeValue2;
            }
            if (modeValue != null) continue;
            modeValue = modeValue2;
        }
        return modeValue;
    }

    private static Value<?> c(Module module, String string) {
        String string2 = string.toLowerCase(Locale.ROOT);
        for (Value<?> value : module.getAllValues()) {
            if (value.getBooleanSupplier() != null && value.getBooleanSupplier().getAsBoolean()) continue;
            String string3 = value.getName();
            if (string3.equalsIgnoreCase(string)) {
                return value;
            }
            if (!string3.replace(" ", "").toLowerCase(Locale.ROOT).equals(string2)) continue;
            return value;
        }
        return null;
    }
}
