package com.alan.clients.security;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.file.Files;

public class NativeDecryptor {
    private static boolean loaded = false;
    private static String axa = null;

    public NativeDecryptor() {
    }

    public static synchronized void ok() {
        if (!loaded) {
            String s = System.getProperty("os.name", "").toLowerCase();
            String s1;
            if (s.contains("win")) {
                s1 = "rise_native.dll";
            } else if (!s.contains("mac") && !s.contains("darwin")) {
                s1 = "librise_native.so";
            } else {
                s1 = "librise_native.dylib";
            }

            try {
                InputStream inputstream = NativeDecryptor.class.getResourceAsStream("/native/" + s1);
                if (inputstream == null) {
                    axa = "Native library not found in resources: /native/" + s1;
                    System.err.println("[NativeDecryptor] " + axa);
                    return;
                }

                File file1 = Files.createTempDirectory("rise_native_").toFile();
                file1.deleteOnExit();
                if (s1.endsWith(".dll")) {
                    a("/native/libcrypto-3-x64.dll", new File(file1, "libcrypto-3-x64.dll"));
                    a("/native/libssl-3-x64.dll", new File(file1, "libssl-3-x64.dll"));
                }

                File file2 = new File(file1, s1);
                file2.deleteOnExit();

                try (FileOutputStream fileoutputstream = new FileOutputStream(file2)) {
                    byte[] abyte = new byte[8192];

                    int i;
                    while ((i = inputstream.read(abyte)) != -1) {
                        fileoutputstream.write(abyte, 0, i);
                    }
                }

                inputstream.close();
                System.setProperty("java.library.path", file1.getAbsolutePath() + File.pathSeparator + System.getProperty("java.library.path", ""));
                System.load(file2.getAbsolutePath());
                loaded = true;
            } catch (Throwable throwable1) {
                axa = throwable1.getMessage();
                System.err.println("[NativeDecryptor] Failed to load native library: " + throwable1.getMessage());
            }
        }
    }

    public static boolean isLoaded() {
        return loaded;
    }

    public static String ol() {
        return axa;
    }

    private static void a(String var0, File file) {
        try {
            InputStream inputstream = NativeDecryptor.class.getResourceAsStream(var0);
            if (inputstream == null) {
                return;
            }

            file.deleteOnExit();

            try (FileOutputStream fileoutputstream = new FileOutputStream(file)) {
                byte[] abyte = new byte[8192];

                int i;
                while ((i = inputstream.read(abyte)) != -1) {
                    fileoutputstream.write(abyte, 0, i);
                }
            }

            inputstream.close();
        } catch (Exception exception) {
        }
    }

    public static byte[] nativeDecryptAndDefine(byte[] var0, byte[] var1, String var2, ClassLoader classLoader) {
        return null;
    }
}
