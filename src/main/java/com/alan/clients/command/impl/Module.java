package com.alan.clients.command.impl;

import com.alan.clients.Client;
import com.alan.clients.command.Command;
import com.alan.clients.value.Mode;
import com.alan.clients.value.Value;
import com.alan.clients.value.impl.BooleanValue;
import com.alan.clients.value.impl.ModeValue;
import com.alan.clients.value.impl.NumberValue;
import com.alan.clients.value.impl.StringValue;
import com.alan.clients.util.chat.ChatUtil;
import com.alan.clients.util.localization.Localization;
import java.util.Locale;

public final class Module extends Command {
    public Module() {
        super("command.module.description", "module", "modules");
    }

    @Override
    public void execute(String[] var1) {
        if (var1.length < 3) {
            this.error(".module <module> <setting> <value>  or  .module <module> <modeName>");
        } else {
            String s = var1[1];
            com.alan.clients.module.Module module = Client.a.g().get(s);
            if (module == null) {
                ChatUtil.b(Localization.ce("command.bind.invalidmodule"));
            } else {
                String s1 = var1[2];
                Value value = this.a(module, s1);
                if (value != null) {
                    if (var1.length < 4) {
                        this.error(".module " + s + " " + s1 + " <value>");
                    } else {
                        String s2 = a(var1, 3);

                        try {
                            this.a(value, s2);
                            ChatUtil.b("Set " + module.getName() + "." + value.getName() + " to " + s2);
                        } catch (IllegalArgumentException illegalargumentexception) {
                            ChatUtil.b(illegalargumentexception.getMessage());
                        }
                    }
                } else {
                    ModeValue modevalue = this.a(module);
                    if (modevalue == null) {
                        this.error(".module " + s + " <setting> <value>");
                    } else {
                        String s3 = a(var1, 2);

                        try {
                            this.a(modevalue, s3);
                            ChatUtil.b("Set " + module.getName() + " mode to " + s3);
                        } catch (IllegalArgumentException illegalargumentexception1) {
                            ChatUtil.b(illegalargumentexception1.getMessage());
                        }
                    }
                }
            }
        }
    }

    private ModeValue a(com.alan.clients.module.Module module) {
        ModeValue modevalue = null;

        for (Value value : module.getAllValues()) {
            if (value instanceof ModeValue modevalue1) {
                if ("mode".equalsIgnoreCase(value.getName())) {
                    return modevalue1;
                }

                if (modevalue == null) {
                    modevalue = modevalue1;
                }
            }
        }

        return modevalue;
    }

    private Value<?> a(com.alan.clients.module.Module module, String var2) {
        String s = var2.toLowerCase(Locale.ROOT);

        for (Value value : module.getAllValues()) {
            if (value.getBooleanSupplier() == null || !value.getBooleanSupplier().getAsBoolean()) {
                String s1 = value.getName();
                if (s1.equalsIgnoreCase(var2)) {
                    return value;
                }

                if (s1.replace(" ", "").toLowerCase(Locale.ROOT).equals(s)) {
                    return value;
                }
            }
        }

        return null;
    }

    private void a(Value<?> var1, String var2) {
        if (var1 instanceof NumberValue numbervalue) {
            try {
                double d0 = Double.parseDouble(var2);
                double d1 = numbervalue.getMin().doubleValue();
                double d2 = numbervalue.getMax().doubleValue();
                if (!(d0 < d1) && !(d0 > d2)) {
                    numbervalue.n(d0);
                } else {
                    throw new IllegalArgumentException("Value out of range [" + d1 + ", " + d2 + "]");
                }
            } catch (NumberFormatException numberformatexception) {
                throw new IllegalArgumentException("Invalid number \"" + var2 + "\"");
            }
        } else if (var1 instanceof BooleanValue booleanvalue) {
            String s = var2.toLowerCase(Locale.ROOT);
            if (!s.equals("true") && !s.equals("1") && !s.equals("on")) {
                if (!s.equals("false") && !s.equals("0") && !s.equals("off")) {
                    throw new IllegalArgumentException("Expected true/false for " + var1.getName());
                }

                booleanvalue.setValue(false);
            } else {
                booleanvalue.setValue(true);
            }
        } else if (!(var1 instanceof ModeValue modevalue)) {
            if (var1 instanceof StringValue stringvalue) {
                stringvalue.n(var2);
            } else {
                var1.setValueAsObject(var2);
            }
        } else {
            String s1 = var2.toLowerCase(Locale.ROOT);
            String s2 = s1.replace(" ", "");
            Mode mode = null;
            Mode mode1 = null;

            for (Mode mode2 : modevalue.getModes()) {
                String s3 = mode2.getName().toLowerCase(Locale.ROOT);
                String s4 = s3.replace(" ", "");
                if (s3.equals(s1) || s4.equals(s2)) {
                    mode = mode2;
                    break;
                }

                if (mode1 == null && (s3.startsWith(s1) || s4.startsWith(s2))) {
                    mode1 = mode2;
                }
            }

            Mode mode3 = mode != null ? mode : mode1;
            if (mode3 == null) {
                throw new IllegalArgumentException("Unknown mode \"" + var2 + "\"");
            }

            modevalue.update(mode3);
        }
    }

    private static String a(String[] var0, int var1) {
        if (var0 != null && var1 >= 0 && var1 < var0.length) {
            StringBuilder stringbuilder = new StringBuilder();

            for (int i = var1; i < var0.length; i++) {
                String s = var0[i];
                if (s != null) {
                    String s1 = s.trim();
                    if (!s1.isEmpty()) {
                        if (stringbuilder.length() > 0) {
                            stringbuilder.append(' ');
                        }

                        stringbuilder.append(s1);
                    }
                }
            }

            return stringbuilder.toString();
        }
        return "";
    }
}
