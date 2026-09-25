package com.alan.clients.util.ime;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Map;
import java.util.Properties;
import net.minecraft.client.Minecraft;

public final class PinyinUsageStore {
    private static final String aKc = "*";
    private static final String aKd = "|";
    private static final PinyinUsageStore aKe = new PinyinUsageStore();
    private final Map<String, Integer> aKf = new HashMap<>();
    private boolean loaded;

    public static PinyinUsageStore uC() {
        return aKe;
    }

    private PinyinUsageStore() {
    }

    public synchronized int C(String var1, String var2) {
        this.sL();
        if (var2 != null && !var2.isEmpty()) {
            String s = E(var1, var2);
            Integer integer = this.aKf.get(s);
            return integer == null ? 0 : integer;
        }
        return 0;
    }

    public synchronized int cb(String var1) {
        this.sL();
        if (var1 != null && !var1.isEmpty()) {
            String s = E("*", var1);
            Integer integer = this.aKf.get(s);
            return integer == null ? 0 : integer;
        }
        return 0;
    }

    public synchronized void D(String var1, String var2) {
        this.sL();
        if (var2 != null && !var2.isEmpty()) {
            this.cc(E(var1, var2));
            this.cc(E("*", var2));
            this.su();
        }
    }

    private void cc(String var1) {
        int i = this.aKf.getOrDefault(var1, 0);
        int j = i >= 2147483646 ? Integer.MAX_VALUE : i + 1;
        this.aKf.put(var1, j);
    }

    private synchronized void sL() {
        if (!this.loaded) {
            this.loaded = true;

            try {
                File file1 = uD();
                if (file1 == null || !file1.isFile()) {
                    return;
                }

                Properties properties = new Properties();

                try (FileInputStream fileinputstream = new FileInputStream(file1)) {
                    properties.load(fileinputstream);
                }

                for (String s : properties.stringPropertyNames()) {
                    String s1 = properties.getProperty(s);
                    if (s1 != null) {
                        try {
                            int i = Integer.parseInt(s1.trim());
                            if (i > 0) {
                                this.aKf.put(s, i);
                            }
                        } catch (NumberFormatException numberformatexception) {
                        }
                    }
                }
            } catch (Throwable throwable1) {
            }
        }
    }

    private synchronized void su() {
        try {
            File file1 = uD();
            if (file1 == null) {
                return;
            }

            File file2 = file1.getParentFile();
            if (file2 != null && !file2.isDirectory()) {
                file2.mkdirs();
            }

            Properties properties = new Properties();

            for (Entry entry : this.aKf.entrySet()) {
                if (entry.getKey() != null && !((String)entry.getKey()).isEmpty()) {
                    Integer integer = (Integer)entry.getValue();
                    if (integer != null && integer > 0) {
                        properties.setProperty((String)entry.getKey(), String.valueOf(integer));
                    }
                }
            }

            try (FileOutputStream fileoutputstream = new FileOutputStream(file1)) {
                fileoutputstream.write("# Rise Pinyin IME usage weights (auto-generated)\n".getBytes(StandardCharsets.UTF_8));
                fileoutputstream.write("# Format: pinyin|word = count, and *|word = global count\n".getBytes(StandardCharsets.UTF_8));
                properties.store(fileoutputstream, null);
            }
        } catch (Throwable throwable1) {
        }
    }

    private static String E(String var0, String var1) {
        return (var0 != null && !var0.isEmpty() ? var0 : "*") + "|" + var1;
    }

    private static File uD() {
        try {
            Minecraft minecraft = Minecraft.getMinecraft();
            if (minecraft != null && minecraft.mcDataDir != null) {
                File file1 = new File(minecraft.mcDataDir, "Rise");
                return new File(file1, "pinyin_usage.properties");
            }
            return null;
        } catch (Throwable throwable) {
            return null;
        }
    }
}
