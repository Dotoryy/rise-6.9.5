package com.alan.clients.util;

import com.alan.clients.util.interfaces.InstanceAccess;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.alan.clients.util.SkinType;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import javax.net.ssl.HttpsURLConnection;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.util.ResourceLocation;

public class SkinUtil implements InstanceAccess {
    private static final Map<String, ResourceLocation> SKIN_CACHE = new HashMap<>();
    private static final String NAME_TO_UUID = "https://api.mojang.com/users/profiles/minecraft/";
    private static final String[] aEs = new String[]{
        "https://mc-heads.net/avatar/%s/%d", "https://minotar.net/avatar/%s/%d", "https://cravatar.eu/helmavatar/%s/%d.png"
    };
    private static int aEt = 0;

    public SkinUtil() {
    }

    public static ResourceLocation getResourceLocation(SkinType var0, String var1, int var2) {
        String s = var1 + "_" + var0.name() + "_" + var2;
        if (SKIN_CACHE.containsKey(s)) {
            return SKIN_CACHE.get(s);
        }

        String s1 = b(var0, var1, var2);
        ResourceLocation resourcelocation = new ResourceLocation("skins/" + var1 + "_" + var0.name() + "_" + var2);
        ThreadDownloadImageData threaddownloadimagedata = new ThreadDownloadImageData(null, s1, null, null);
        aEg.getTextureManager().loadTexture(resourcelocation, threaddownloadimagedata);
        SKIN_CACHE.put(s, resourcelocation);
        AbstractClientPlayer.getDownloadImageSkin(resourcelocation, var1);
        return resourcelocation;
    }

    private static String b(SkinType var0, String var1, int var2) {
        String s = aEs[aEt];
        switch (var0) {
            case AVATAR:
            case HELM:
            case ARMOR_BUST:
            case SKIN:
            default:
                return String.format(s, var1, var2);
            case BUST:
                if (aEt == 0) {
                    return "https://mc-heads.net/bust/" + var1 + "/" + var2;
                }

                return String.format(s, var1, var2);
            case BODY:
            case ARMOR_BODY:
                if (aEt == 0) {
                    return "https://mc-heads.net/body/" + var1 + "/" + var2;
                }

                return String.format(s, var1, var2);
            case CUBE:
                return aEt == 0 ? "https://mc-heads.net/head/" + var1 + "/" + var2 : String.format(s, var1, var2);
        }
    }

    public static void rY() {
        aEt = (aEt + 1) % aEs.length;
        SKIN_CACHE.clear();
    }

    public static String rZ() {
        return aEs[aEt].split("/")[2];
    }

    public static String uuidOf(String var0) {
        JsonObject jsonobject = JsonParser.parseString(scrape("https://api.mojang.com/users/profiles/minecraft/" + var0)).getAsJsonObject();
        return jsonobject != null && jsonobject.has("id") ? jsonobject.get("id").getAsString() : null;
    }

    private static String scrape(String var0) {
        StringBuilder stringbuilder = new StringBuilder();

        try {
            HttpsURLConnection httpsurlconnection = (HttpsURLConnection)new URL(var0).openConnection();
            httpsurlconnection.setRequestProperty("User-Agent", "Chrome Version 88.0.4324.150");
            httpsurlconnection.connect();
            BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(httpsurlconnection.getInputStream()));

            String s;
            while ((s = bufferedreader.readLine()) != null) {
                stringbuilder.append(s).append(System.lineSeparator());
            }

            bufferedreader.close();
        } catch (IOException ioexception) {
        }

        return stringbuilder.toString();
    }

    public static String name(String var0) {
        return null;
    }
}
