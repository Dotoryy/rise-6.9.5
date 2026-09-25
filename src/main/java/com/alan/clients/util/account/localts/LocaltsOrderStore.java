package com.alan.clients.util.account.localts;

import com.alan.clients.util.file.FileManager;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.alan.clients.util.account.auth.MicrosoftCookieAuth;
import com.alan.clients.util.account.localts.LocaltsOrder;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public final class LocaltsOrderStore {
    public static final File aFY = new File(FileManager.DIRECTORY, "alts/localts_orders");
    private static final SimpleDateFormat aFZ = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");

    private LocaltsOrderStore() {
    }

    public static void init() {
        if (!aFY.exists()) {
            aFY.mkdirs();
        }
    }

    public static File g(long var0) {
        File[] afile = aFY.listFiles(var0x -> var0x.isDirectory() && var0x.getName().startsWith("order_"));
        if (afile != null && afile.length != 0) {
            File file1 = null;
            long i = 0L;

            for (File file2 : afile) {
                File file3 = new File(file2, "order.txt");
                long j = file3.isFile() ? file3.lastModified() : file2.lastModified();
                if (j > i) {
                    file1 = file2;
                    i = j;
                }
            }

            return file1 != null && System.currentTimeMillis() - i <= var0 ? file1 : null;
        }
        return null;
    }

    public static LocaltsDelivery a(LocaltsOrder var0) {
        return a(var0, null);
    }

    public static LocaltsDelivery a(LocaltsOrder var0, File file) {
        init();
        String s = var0.aFq.isEmpty() ? "pending" : var0.aFq;
        boolean flag = file != null;
        File file1 = flag ? file : new File(aFY, "order_" + s);
        if (!file1.exists() && !file1.mkdirs()) {
            return null;
        }

        File file2 = new File(file1, "order.txt");
        int i = 0;
        int j = 0;
        int k = 0;
        int l = flag ? b(file1) : 1;

        try (BufferedWriter bufferedwriter = new BufferedWriter(new FileWriter(file2, flag))) {
            if (flag) {
                bufferedwriter.write("\nReplacement order ID: " + s + "\n");
                bufferedwriter.write("Saved: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "\n");
            } else {
                bufferedwriter.write("===========================================\n");
                bufferedwriter.write("             LOCALTS ORDER\n");
                bufferedwriter.write("===========================================\n");
                bufferedwriter.write("Order ID: " + s + "\n");
                bufferedwriter.write("Status: " + var0.aFr + "\n");
                bufferedwriter.write("Product: " + var0.aFs + "\n");
                bufferedwriter.write("Saved: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "\n");
                bufferedwriter.write("===========================================\n\n");
            }

            for (JsonElement jsonelement : var0.aFt) {
                if (jsonelement.isJsonObject()) {
                    String s1 = f(jsonelement.getAsJsonObject(), "content");
                    if (!s1.trim().isEmpty()) {
                        String s2 = String.format("item-%02d", l++);
                        k++;
                        if (a(s1, file1, s2 + ".zip")) {
                            j++;
                            bufferedwriter.write("Item #" + k + ": extracted archive\n");
                        } else if (MicrosoftCookieAuth.bo(s1)) {
                            d(new File(file1, s2 + ".txt"), s1);
                            i++;
                            bufferedwriter.write("Item #" + k + ": MSAAUTH cookie saved as " + s2 + ".txt\n");
                        } else {
                            c(new File(file1, s2 + ".txt"), s1);
                            bufferedwriter.write("Item #" + k + ": delivery saved as " + s2 + ".txt\n");
                        }
                    }
                }
            }

            bufferedwriter.write("\nPurchased via Localts: https://localts.store/?campaign=rise\n");
            return new LocaltsDelivery(file1, k, i, j);
        } catch (Exception exception) {
            System.err.println("Failed to save Localts order: " + exception.getMessage());
            return null;
        }
    }

    private static int b(File file) {
        int i = 0;
        File[] afile = file.listFiles();
        if (afile == null) {
            return 1;
        }

        File[] afile1 = afile;
        int j = afile1.length;

        for (int k = 0; k < j; k++) {
            String s = afile1[k].getName();
            if (s.startsWith("item-")) {
                int l = s.indexOf(46, 5);
                String s1 = l < 0 ? s.substring(5) : s.substring(5, l);

                try {
                    i = Math.max(i, Integer.parseInt(s1));
                } catch (NumberFormatException numberformatexception) {
                }
            }
        }

        return i + 1;
    }

    private static void c(File file, String var1) throws java.io.IOException {
        try (BufferedWriter bufferedwriter = new BufferedWriter(new FileWriter(file))) {
            bufferedwriter.write(var1);
            bufferedwriter.newLine();
        }
    }

    private static void d(File file, String var1) throws java.io.IOException {
        String s = var1.trim();
        int i = s.indexOf(59);
        if (i >= 0) {
            s = s.substring(0, i);
        }

        int j = s.indexOf(61);
        if (j > 0 && j != s.length() - 1) {
            String s1 = s.substring(0, j);
            String s2 = s.substring(j + 1);

            try (BufferedWriter bufferedwriter = new BufferedWriter(new FileWriter(file))) {
                bufferedwriter.write("# Netscape HTTP Cookie File\n");
                bufferedwriter.write(".login.live.com\tTRUE\t/\tTRUE\t2147483647\t" + s1 + "\t" + s2 + "\n");
            }
        } else {
            throw new IOException("Invalid MSAAUTH cookie");
        }
    }

    private static boolean a(String var0, File file, String var2) {
        try {
            String s = var0.startsWith("data:") && var0.contains(",") ? var0.substring(var0.indexOf(44) + 1) : var0;
            byte[] abyte = Base64.getDecoder().decode(s.replaceAll("\\s", ""));
            if (abyte.length >= 4 && abyte[0] == 80 && abyte[1] == 75) {
                File file1 = new File(file, var2);

                try (FileOutputStream fileoutputstream = new FileOutputStream(file1)) {
                    fileoutputstream.write(abyte);
                }

                String s1 = file.getCanonicalPath() + File.separator;

                ZipEntry zipentry;
                try (ZipInputStream zipinputstream = new ZipInputStream(new ByteArrayInputStream(abyte))) {
                    while ((zipentry = zipinputstream.getNextEntry()) != null) {
                        File file2 = new File(file, zipentry.getName());
                        if (!file2.getCanonicalPath().startsWith(s1)) {
                            throw new IOException("Invalid archive path");
                        }

                        if (zipentry.isDirectory()) {
                            file2.mkdirs();
                        } else {
                            File file3 = file2.getParentFile();
                            if (!file3.exists() && !file3.mkdirs()) {
                                throw new IOException("Could not create delivery folder");
                            }

                            a(zipinputstream, file2);
                        }
                    }
                }

                return true;
            }
            return false;
        } catch (Exception exception) {
            return false;
        }
    }

    private static void a(InputStream in, File file) throws java.io.IOException {
        byte[] abyte = new byte[8192];

        int i;
        try (FileOutputStream fileoutputstream = new FileOutputStream(file)) {
            while ((i = in.read(abyte)) >= 0) {
                fileoutputstream.write(abyte, 0, i);
            }
        }
    }

    private static String f(JsonObject json, String var1) {
        return json.has(var1) && !json.get(var1).isJsonNull() ? json.get(var1).getAsString() : "";
    }
}
