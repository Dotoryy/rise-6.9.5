package com.alan.clients.util.web;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import javax.net.ssl.HttpsURLConnection;

public class Browser {
    public Browser() {
    }

    public static String postExternal(String var0, String var1, boolean var2) {
        try {
            HttpsURLConnection httpsurlconnection = (HttpsURLConnection)new URL(var0).openConnection();
            httpsurlconnection.addRequestProperty(
                "User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/102.0.0.0 Safari/537.36"
            );
            httpsurlconnection.setRequestMethod("POST");
            httpsurlconnection.setDoOutput(true);
            byte[] abyte = var1.getBytes(StandardCharsets.UTF_8);
            int i = abyte.length;
            httpsurlconnection.setFixedLengthStreamingMode(i);
            httpsurlconnection.addRequestProperty("Content-Type", var2 ? "application/json" : "application/x-www-form-urlencoded; charset=UTF-8");
            httpsurlconnection.addRequestProperty("Accept", "application/json");
            httpsurlconnection.connect();

            try (OutputStream outputstream = httpsurlconnection.getOutputStream()) {
                outputstream.write(abyte);
            }

            int j = httpsurlconnection.getResponseCode();
            InputStream inputstream = j / 100 != 2 && j / 100 != 3 ? httpsurlconnection.getErrorStream() : httpsurlconnection.getInputStream();
            if (inputstream == null) {
                System.err.println(j + ": " + var0);
                return null;
            }

            BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(inputstream));
            StringBuilder stringbuilder = new StringBuilder();

            String s;
            while ((s = bufferedreader.readLine()) != null) {
                stringbuilder.append(s);
            }

            bufferedreader.close();
            return stringbuilder.toString();
        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }

    public static String getBearerResponse(String var0, String var1) {
        try {
            HttpsURLConnection httpsurlconnection = (HttpsURLConnection)new URL(var0).openConnection();
            httpsurlconnection.addRequestProperty(
                "User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/102.0.0.0 Safari/537.36"
            );
            httpsurlconnection.addRequestProperty("Accept", "application/json");
            httpsurlconnection.addRequestProperty("Authorization", "Bearer " + var1);
            if (httpsurlconnection.getResponseCode() == 200) {
                BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(httpsurlconnection.getInputStream()));
                StringBuilder stringbuilder = new StringBuilder();

                String s;
                while ((s = bufferedreader.readLine()) != null) {
                    stringbuilder.append(s);
                }

                return stringbuilder.toString();
            }
            BufferedReader bufferedreader1 = new BufferedReader(new InputStreamReader(httpsurlconnection.getErrorStream()));
            StringBuilder stringbuilder1 = new StringBuilder();

            String s1;
            while ((s1 = bufferedreader1.readLine()) != null) {
                stringbuilder1.append(s1);
            }

            return stringbuilder1.toString();
        } catch (Exception exception) {
            return null;
        }
    }
}
