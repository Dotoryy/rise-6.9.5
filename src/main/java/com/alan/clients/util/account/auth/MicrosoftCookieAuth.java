package com.alan.clients.util.account.auth;

import com.alan.clients.Client;
import com.alan.clients.ui.menu.impl.account.AccountManagerScreen;
import com.alan.clients.util.account.auth.MicrosoftLogin;
import com.alan.clients.util.account.auth.LoginData;
import com.alan.clients.util.account.auth.McResponse;
import com.alan.clients.util.account.auth.ProfileResponse;
import com.alan.clients.util.account.impl.MicrosoftAccount;
import com.alan.clients.util.web.Browser;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.alan.clients.util.account.AltAccount;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashSet;
import javax.net.ssl.HttpsURLConnection;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;

public final class MicrosoftCookieAuth {
    private static final Gson gson = new Gson();
    private static final String CLIENT_ID = "https://sisu.xboxlive.com/connect/XboxLive/?state=login&cobrandId=8058f65d-ce06-4c30-9559-473c9275a65d&tid=896928775&ru=https%3A%2F%2Fwww.minecraft.net%2Fen-us%2Flogin&aid=1142970254";

    private MicrosoftCookieAuth() {
    }

    public static MSAAuthResult a(JsonArray var0) {
        HashSet hashset = new HashSet();

        for (AltAccount altAccount : Client.a.getAltManager().getAccounts()) {
            if (altAccount.sh() != null) {
                hashset.add(altAccount.sh().toLowerCase());
            }
        }

        String s = "";
        int i = 0;

        for (JsonElement jsonelement : var0) {
            if (jsonelement.isJsonObject()) {
                JsonObject jsonobject = jsonelement.getAsJsonObject();
                if (jsonobject.has("content") && !jsonobject.get("content").isJsonNull()) {
                    String s1 = jsonobject.get("content").getAsString().trim();
                    boolean flag = bo(s1);
                    String s2 = bp(s1);
                    if (flag || !s2.isEmpty()) {
                        LoginData d = flag ? bs(s1) : MicrosoftLogin.loginMarketplaceRefreshToken(s2);
                        if (d != null && d.sm()) {
                            if (s.isEmpty()) {
                                Minecraft.getMinecraft().setSession(new Session(d.aCj, d.aEL, d.aEX, "mojang"));
                                s = d.aCj;
                            }

                            String s3 = d.aEL == null ? "" : d.aEL.toLowerCase();
                            if (s3.isEmpty() || hashset.add(s3)) {
                                String s4 = flag ? d.aCj : v(bq(s1), d.aCj);
                                String s5 = flag ? (d.aEY == null ? "" : d.aEY) : s2;
                                AccountManagerScreen.addAccount(new MicrosoftAccount(s4, d.aEL, d.aEX, s5));
                                i++;
                            }
                        }
                    }
                }
            }
        }

        return new MSAAuthResult(s, i);
    }

    public static boolean bo(String var0) {
        return var0.contains("MSAAUTH=") || var0.contains("MSAAUTHP=") || var0.contains("__Host-MSAAUTH=") || var0.contains("__Host-MSAAUTHP=");
    }

    private static String bp(String var0) {
        String s = var0 == null ? "" : var0.trim();
        int i = s.indexOf(58);
        if (i > 0) {
            s = s.substring(i + 1).trim();
        }

        return br(s) ? s : "";
    }

    private static String bq(String var0) {
        String s = var0 == null ? "" : var0.trim();
        int i = s.indexOf(58);
        return i > 0 ? s.substring(0, i).trim() : "";
    }

    private static String v(String var0, String var1) {
        return var0 != null && !var0.isEmpty() ? var0 : (var1 == null ? "" : var1);
    }

    private static boolean br(String var0) {
        return var0.length() > 100 && var0.startsWith("M.") && var0.matches("[A-Za-z0-9.!*_-]+");
    }

    private static LoginData bs(String var0) {
        try {
            String s = var0.trim();
            int i = s.indexOf(59);
            if (i >= 0) {
                s = s.substring(0, i);
            }

            if (!s.contains("=")) {
                return new LoginData();
            }

            String s1 = w(
                    "https://sisu.xboxlive.com/connect/XboxLive/?state=login&cobrandId=8058f65d-ce06-4c30-9559-473c9275a65d&tid=896928775&ru=https%3A%2F%2Fwww.minecraft.net%2Fen-us%2Flogin&aid=1142970254",
                    null
                )
                .getHeaderField("Location");
            if (s1 == null) {
                return new LoginData();
            }

            String s2 = w(s1.replace(" ", "%20"), s).getHeaderField("Location");
            if (s2 == null) {
                return new LoginData();
            }

            String s3 = w(s2, s).getHeaderField("Location");
            if (s3 == null) {
                return new LoginData();
            }

            String s4 = x(s3, "accessToken");
            if (s4.isEmpty()) {
                return new LoginData();
            }

            String s5 = new String(bt(s4), StandardCharsets.UTF_8);
            String s6 = "\"rp://api.minecraftservices.com/\",";
            int j = s5.indexOf(s6);
            if (j < 0) {
                return new LoginData();
            }

            String s7 = s5.substring(j + s6.length());
            String s8 = y(s7, "Token");
            String s9 = y(s7, "uhs");
            if (!s8.isEmpty() && !s9.isEmpty()) {
                McResponse e = gson.fromJson(
                    Browser.postExternal(
                        "https://api.minecraftservices.com/authentication/login_with_xbox",
                        "{\"identityToken\":\"XBL3.0 x=" + s9 + ";" + s8 + "\",\"ensureLegacyEnabled\":true}",
                        true
                    ),
                    McResponse.class
                );
                if (e != null && e.access_token != null) {
                    ProfileResponse f = gson.fromJson(Browser.getBearerResponse("https://api.minecraftservices.com/minecraft/profile", e.access_token), ProfileResponse.class);
                    if (f != null && f.aEZ != null && f.name != null) {
                        String s10 = MicrosoftLogin.extractRefreshTokenFromCookies(s);
                        return new LoginData(e.access_token, s10, f.aEZ, f.name);
                    }
                    return new LoginData();
                } else {
                    return new LoginData();
                }
            } else {
                return new LoginData();
            }
        } catch (Exception exception) {
            return new LoginData();
        }
    }

    private static HttpsURLConnection w(String var0, String var1) throws java.io.IOException {
        HttpsURLConnection httpsurlconnection = (HttpsURLConnection)new URL(var0).openConnection();
        httpsurlconnection.setRequestMethod("GET");
        httpsurlconnection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        httpsurlconnection.setRequestProperty("User-Agent", "Mozilla/5.0");
        if (var1 != null) {
            httpsurlconnection.setRequestProperty("Cookie", var1);
        }

        httpsurlconnection.setInstanceFollowRedirects(false);
        httpsurlconnection.connect();
        return httpsurlconnection;
    }

    private static String x(String var0, String var1) throws java.io.UnsupportedEncodingException {
        int i = var0.indexOf(var1 + "=");
        if (i < 0) {
            return "";
        }

        String s = var0.substring(i + var1.length() + 1);
        int j = s.indexOf(38);
        return URLDecoder.decode(j >= 0 ? s.substring(0, j) : s, "UTF-8");
    }

    private static byte[] bt(String var0) {
        try {
            return Base64.getDecoder().decode(var0);
        } catch (IllegalArgumentException illegalargumentexception) {
            return Base64.getUrlDecoder().decode(var0);
        }
    }

    private static String y(String var0, String var1) {
        String s = "\"" + var1 + "\":\"";
        int i = var0.indexOf(s);
        if (i < 0) {
            return "";
        }

        int j = i + s.length();
        int k = var0.indexOf(34, j);
        return k < 0 ? "" : var0.substring(j, k);
    }
}
