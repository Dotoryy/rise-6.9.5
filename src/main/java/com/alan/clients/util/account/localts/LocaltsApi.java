package com.alan.clients.util.account.localts;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;

public final class LocaltsApi {
    private static final String aFl = "https://localts.store/v1";
    private static final String aFm = "campaign=rise";
    private static final int aFn = 10000;
    private static final int aFo = 15000;

    private LocaltsApi() {
    }

    public static LocaltsProducts ss() {
        LocaltsResponse aez = b("GET", "/products?campaign=rise", null);
        if (!aez.st()) {
            return new LocaltsProducts(false, Collections.emptyList(), aez.aFR);
        }

        JsonObject jsonobject = aez.json;
        if (!c(jsonobject)) {
            return new LocaltsProducts(false, Collections.emptyList(), a(jsonobject, "Localts did not return products"));
        }

        ArrayList arraylist = new ArrayList();

        for (JsonElement jsonelement : e(jsonobject, "products")) {
            if (jsonelement.isJsonObject()) {
                JsonObject jsonobject1 = jsonelement.getAsJsonObject();
                arraylist.add(
                    new LocaltsProduct(
                        b(jsonobject1, "id"),
                        b(jsonobject1, "name"),
                        b(jsonobject1, "description"),
                        b(jsonobject1, "category"),
                        d(jsonobject1, "priceInCredits"),
                        c(jsonobject1, "stock"),
                        b(jsonobject1, "type")
                    )
                );
            }
        }

        return new LocaltsProducts(true, arraylist, "");
    }

    public static LocaltsPurchase bu(String var0) {
        LocaltsResponse aez = b("GET", "/me?campaign=rise", var0);
        if (!aez.st()) {
            return new LocaltsPurchase(false, "", 0, aez.aFR);
        }

        JsonObject jsonobject = aez.json;
        return !c(jsonobject)
            ? new LocaltsPurchase(false, "", 0, a(jsonobject, "Invalid Localts API key"))
            : new LocaltsPurchase(true, b(jsonobject, "username"), d(jsonobject, "balance"), "");
    }

    public static LocaltsResult c(String var0, String var1, int var2) {
        if (var1 != null && !var1.isEmpty()) {
            try {
                String s = "/products/" + URLEncoder.encode(var1, "UTF-8") + "/purchase?amount=" + var2 + "&campaign=rise";
                LocaltsResponse aez = b("POST", s, var0);
                if (!aez.st()) {
                    return new LocaltsResult(false, "", aez.aFR);
                }

                JsonObject jsonobject = aez.json;
                return !c(jsonobject) ? new LocaltsResult(false, "", a(jsonobject, "Purchase failed")) : new LocaltsResult(true, b(jsonobject, "orderId"), "");
            } catch (Exception exception) {
                return new LocaltsResult(false, "", "Unable to create Localts purchase request");
            }
        } else {
            return new LocaltsResult(false, "", "Choose a Localts product first");
        }
    }

    public static LocaltsOrder z(String var0, String var1) {
        try {
            LocaltsResponse aez = b("GET", "/orders/get-order?id=" + URLEncoder.encode(var1, "UTF-8") + "&campaign=rise", var0);
            if (!aez.st()) {
                return new LocaltsOrder(false, var1, "", "", new JsonArray(), aez.aFR);
            }

            JsonObject jsonobject = aez.json;
            return !c(jsonobject)
                ? new LocaltsOrder(false, var1, "", "", new JsonArray(), a(jsonobject, "Could not retrieve order"))
                : new LocaltsOrder(true, b(jsonobject, "order-id"), b(jsonobject, "status"), b(jsonobject, "product-name"), e(jsonobject, "items"), "");
        } catch (Exception exception) {
            return new LocaltsOrder(false, var1, "", "", new JsonArray(), "Unable to retrieve Localts order");
        }
    }

    public static LocaltsOrderPage b(String var0, int var1, int var2) {
        if (var1 >= 0 && var2 >= 1 && var2 <= 100) {
            LocaltsResponse aez = b("GET", "/orders?page=" + var1 + "&size=" + var2 + "&campaign=rise", var0);
            if (!aez.st()) {
                return new LocaltsOrderPage(false, new JsonArray(), 0, 0, 0, 0, aez.aFR);
            }

            JsonObject jsonobject = aez.json;
            return !c(jsonobject)
                ? new LocaltsOrderPage(false, new JsonArray(), 0, 0, 0, 0, a(jsonobject, "Could not retrieve Localts orders"))
                : new LocaltsOrderPage(
                    true,
                    e(jsonobject, "orders"),
                    c(jsonobject, "page"),
                    c(jsonobject, "size"),
                    c(jsonobject, "totalPages"),
                    c(jsonobject, "totalElements"),
                    ""
                );
        }
        return new LocaltsOrderPage(false, new JsonArray(), 0, 0, 0, 0, "Invalid order page or size");
    }

    private static LocaltsResponse b(String var0, String var1, String var2) {
        HttpURLConnection httpurlconnection = null;

        try {
            httpurlconnection = (HttpURLConnection)new URL("https://localts.store/v1" + var1).openConnection();
            httpurlconnection.setRequestMethod(var0);
            httpurlconnection.setConnectTimeout(10000);
            httpurlconnection.setReadTimeout(15000);
            httpurlconnection.setRequestProperty("Accept", "application/json");
            httpurlconnection.setRequestProperty("User-Agent", "Rise-Client/6.7.10");
            if (var2 != null && !var2.trim().isEmpty()) {
                httpurlconnection.setRequestProperty("X-API-Key", var2.trim());
            }

            int i = httpurlconnection.getResponseCode();
            InputStream inputstream = i >= 200 && i < 300 ? httpurlconnection.getInputStream() : httpurlconnection.getErrorStream();
            String s = a(inputstream);
            JsonElement jsonelement = s.isEmpty() ? null : JsonParser.parseString(s);
            return jsonelement != null && jsonelement.isJsonObject()
                ? new LocaltsResponse(i, jsonelement.getAsJsonObject(), "")
                : new LocaltsResponse(i, null, i == 401 ? "Invalid Localts API key" : "Localts API returned HTTP " + i);
        } catch (Exception exception) {
            return new LocaltsResponse(0, null, "Unable to reach Localts API");
        } finally {
            if (httpurlconnection != null) {
                httpurlconnection.disconnect();
            }
        }
    }

    private static String a(InputStream in) throws java.io.IOException {
        if (in == null) {
            return "";
        }

        StringBuilder stringbuilder = new StringBuilder();

        String s;
        try (BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
            while ((s = bufferedreader.readLine()) != null) {
                stringbuilder.append(s);
            }
        }

        return stringbuilder.toString();
    }

    private static boolean c(JsonObject json) {
        return json.has("success") && !json.get("success").isJsonNull() && json.get("success").getAsBoolean();
    }

    private static String a(JsonObject json, String var1) {
        return json.has("error") && !json.get("error").isJsonNull() ? json.get("error").getAsString() : var1;
    }

    private static String b(JsonObject json, String var1) {
        return json.has(var1) && !json.get(var1).isJsonNull() ? json.get(var1).getAsString() : "";
    }

    private static int c(JsonObject json, String var1) {
        return json.has(var1) && !json.get(var1).isJsonNull() ? json.get(var1).getAsInt() : 0;
    }

    private static int d(JsonObject json, String var1) {
        return json.has(var1) && !json.get(var1).isJsonNull() ? json.get(var1).getAsInt() : 0;
    }

    private static JsonArray e(JsonObject json, String var1) {
        return json.has(var1) && json.get(var1).isJsonArray() ? json.getAsJsonArray(var1) : new JsonArray();
    }
}
