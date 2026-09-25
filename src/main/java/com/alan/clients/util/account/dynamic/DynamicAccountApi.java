package com.alan.clients.util.account.dynamic;

import com.alan.clients.compat.NetworkToggles;
import com.alan.clients.util.account.dynamic.DynamicAccountResult;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DynamicAccountApi {
    private static final Gson aHf = new Gson();
    private static final String aHg = "http://alts.russia-games.eu/api/v1/accounts";
    private static final String aHh = "http://alts.russia-games.eu/api/v1/login";
    public static String aHi = "api-token";

    public DynamicAccountApi() {
    }

    public static DynamicAccountResult[] sX() {
        //add code
        if (!NetworkToggles.altService()) {
            return null;
        }

        try {
            HttpURLConnection httpurlconnection = (HttpURLConnection)new URL("http://alts.russia-games.eu/api/v1/accounts").openConnection();
            httpurlconnection.setRequestProperty("Content-Type", "application/json");
            httpurlconnection.setRequestProperty("Accept", "application/json");
            httpurlconnection.setRequestProperty("Accept-Charset", "UTF-8");
            httpurlconnection.setRequestMethod("POST");
            JsonObject jsonobject = new JsonObject();
            jsonobject.addProperty("api-token", aHi);
            httpurlconnection.setDoOutput(true);
            httpurlconnection.getOutputStream().write(aHf.toJson(jsonobject).getBytes());
            httpurlconnection.connect();
            BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(httpurlconnection.getInputStream()));
            StringBuilder stringbuilder = new StringBuilder();

            String s;
            while ((s = bufferedreader.readLine()) != null) {
                stringbuilder.append(s);
            }

            JsonObject jsonobject1 = JsonParser.parseString(stringbuilder.toString()).getAsJsonObject();
            JsonArray jsonarray = jsonobject1.get("accounts").getAsJsonArray();
            DynamicAccountResult[] aafo = new DynamicAccountResult[jsonarray.size()];
            if (aafo.length == 0) {
                return null;
            }

            for (int i = 0; i < jsonarray.size(); i++) {
                JsonObject jsonobject2 = jsonarray.get(i).getAsJsonObject();
                DynamicAccountResult afo = aHf.fromJson(jsonobject2, DynamicAccountResult.class);
                aafo[i] = afo;
            }

            return aafo;
        } catch (IOException ioexception) {
            ioexception.printStackTrace();
            return null;
        }
    }

    public static String am(int var0) {
        //add code
        if (!NetworkToggles.altService()) {
            return null;
        }

        try {
            HttpURLConnection httpurlconnection = (HttpURLConnection)new URL("http://alts.russia-games.eu/api/v1/login").openConnection();
            httpurlconnection.setRequestProperty("Content-Type", "application/json");
            httpurlconnection.setRequestProperty("Accept", "application/json");
            httpurlconnection.setRequestProperty("Accept-Charset", "UTF-8");
            httpurlconnection.setRequestMethod("POST");
            JsonObject jsonobject = new JsonObject();
            jsonobject.addProperty("api-token", aHi);
            jsonobject.addProperty("id", var0);
            httpurlconnection.setDoOutput(true);
            httpurlconnection.getOutputStream().write(aHf.toJson(jsonobject).getBytes());
            httpurlconnection.connect();
            BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(httpurlconnection.getInputStream()));
            StringBuilder stringbuilder = new StringBuilder();

            String s;
            while ((s = bufferedreader.readLine()) != null) {
                stringbuilder.append(s);
            }

            JsonObject jsonobject1 = JsonParser.parseString(stringbuilder.toString()).getAsJsonObject();
            if (jsonobject1.has("access_token")) {
                return jsonobject1.get("access_token").getAsString();
            }
        } catch (IOException ioexception) {
            ioexception.printStackTrace();
        }

        return null;
    }
}
