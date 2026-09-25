package com.alan.clients.util.account.localts;

import com.alan.clients.util.file.FileManager;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public final class LocaltsConfig {
    private static final File aFW = new File(FileManager.DIRECTORY, "alts/localts_config.json");
    private static String cs = "";
    private static String aFX = "";

    private LocaltsConfig() {
    }

    public static void init() {
        load();
    }

    public static void load() {
        if (aFW.exists()) {
            try (BufferedReader bufferedreader = new BufferedReader(new FileReader(aFW))) {
                JsonObject jsonobject = JsonParser.parseReader(bufferedreader).getAsJsonObject();
                if (jsonobject.has("apiKey")) {
                    cs = jsonobject.get("apiKey").getAsString();
                }

                if (jsonobject.has("lastProductId")) {
                    aFX = jsonobject.get("lastProductId").getAsString();
                }
            } catch (Exception exception) {
                System.err.println("Failed to load Localts config: " + exception.getMessage());
            }
        }
    }

    public static void su() {
        try {
            File file1 = aFW.getParentFile();
            if (!file1.exists()) {
                file1.mkdirs();
            }

            JsonObject jsonobject = new JsonObject();
            jsonobject.addProperty("apiKey", cs);
            jsonobject.addProperty("lastProductId", aFX);

            try (BufferedWriter bufferedwriter = new BufferedWriter(new FileWriter(aFW))) {
                bufferedwriter.write(jsonobject.toString());
            }
        } catch (Exception exception) {
            System.err.println("Failed to save Localts config: " + exception.getMessage());
        }
    }

    public static String sv() {
        return cs;
    }

    public static void bv(String var0) {
        cs = var0 == null ? "" : var0;
        su();
    }

    public static String sw() {
        return aFX;
    }

    public static void bw(String var0) {
        aFX = var0 == null ? "" : var0;
        su();
    }
}
