package com.alan.clients.util.account.auth;

import com.alan.clients.util.account.auth.AuthTokenResponse;
import com.alan.clients.util.account.auth.LoginData;
import com.alan.clients.util.account.auth.McResponse;
import com.alan.clients.util.account.auth.ProfileResponse;
import com.alan.clients.util.account.auth.XblXstsResponse;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.alan.clients.util.web.Browser;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import javax.net.ssl.HttpsURLConnection;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;

public class MicrosoftLogin {
    private static final ExecutorService executor = Executors.newSingleThreadExecutor();
    private static final String CLIENT_ID = "ba89e6e0-8490-4a26-8746-f389a0d3ccc7";
    private static final String CLIENT_SECRET = "hlQ8Q~33jTRilP4yE-UtuOt9wG.ZFLqq6pErIa2B";
    private static final int PORT = 8247;
    private static HttpServer server;
    static Consumer<String> callback;
    static final Gson gson;

    private static void browse(String string) {
        for (String string2 : new String[]{"C:\\Program Files (x86)\\Microsoft\\Edge\\Application\\msedge.exe " + string + " -incognito", "C:\\Program Files\\BraveSoftware\\Brave-Browser\\Application\\brave.exe " + string + " -incognito", "C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe " + string + " -incognito", "open -na \"Google Chrome\" --args --incognito \"" + string + "\""}) {
            try {
                Runtime.getRuntime().exec(string2);
                return;
            }
            catch (Exception exception) {
            }
        }
    }

    private static void copy(String string) {
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(string), null);
    }

    public static void getRefreshToken(Consumer<String> consumer) {
        callback = consumer;
        MicrosoftLogin.startServer();
        MicrosoftLogin.copy("https://login.live.com/oauth20_authorize.srf?client_id=ba89e6e0-8490-4a26-8746-f389a0d3ccc7&client_secret=hlQ8Q~33jTRilP4yE-UtuOt9wG.ZFLqq6pErIa2B&response_type=code&redirect_uri=http://localhost:8247&scope=XboxLive.signin%20offline_access&prompt=select_account");
    }

    public static void getRefreshTokenAndOpenBrowser(Consumer<String> consumer) {
        callback = consumer;
        MicrosoftLogin.startServer();
        MicrosoftLogin.browse("https://login.live.com/oauth20_authorize.srf?client_id=ba89e6e0-8490-4a26-8746-f389a0d3ccc7&client_secret=hlQ8Q~33jTRilP4yE-UtuOt9wG.ZFLqq6pErIa2B&response_type=code&redirect_uri=http://localhost:8247&scope=XboxLive.signin%20offline_access&prompt=select_account");
    }

    public static String extractRefreshTokenFromCookies(String string) {
        if (string == null || string.trim().isEmpty()) {
            return "";
        }
        try {
            String string2;
            HttpsURLConnection httpsURLConnection = (HttpsURLConnection)new URL("https://login.live.com/oauth20_authorize.srf?client_id=ba89e6e0-8490-4a26-8746-f389a0d3ccc7&client_secret=hlQ8Q~33jTRilP4yE-UtuOt9wG.ZFLqq6pErIa2B&response_type=code&redirect_uri=http://localhost:8247&scope=XboxLive.signin%20offline_access&prompt=none").openConnection();
            httpsURLConnection.setRequestMethod("GET");
            httpsURLConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/112.0.0.0 Safari/537.36");
            httpsURLConnection.setRequestProperty("Cookie", string);
            httpsURLConnection.setInstanceFollowRedirects(false);
            httpsURLConnection.connect();
            String string4 = httpsURLConnection.getHeaderField("Location");
            string2 = string4 == null ? "" : MicrosoftLogin.queryParam(string4, "code");
            if (string2.isEmpty()) {
                System.out.println("[DEBUG] Cookie->refresh got no code (HTTP " + httpsURLConnection.getResponseCode() + ", location=" + (string4 == null ? "null" : string4.substring(0, Math.min(80, string4.length()))) + ")");
                return "";
            }
            AuthTokenResponse authTokenResponse = gson.fromJson(Browser.postExternal("https://login.live.com/oauth20_token.srf", "client_id=ba89e6e0-8490-4a26-8746-f389a0d3ccc7&code=" + string2 + "&client_secret=hlQ8Q~33jTRilP4yE-UtuOt9wG.ZFLqq6pErIa2B&grant_type=authorization_code&redirect_uri=http://localhost:8247", false), AuthTokenResponse.class);
            if (authTokenResponse == null || authTokenResponse.refresh_token == null || authTokenResponse.refresh_token.isEmpty()) {
                System.out.println("[DEBUG] Cookie->refresh code exchange returned no refresh_token");
                return "";
            }
            return authTokenResponse.refresh_token;
        }
        catch (Exception exception) {
            System.out.println("[DEBUG] Cookie->refresh threw: " + String.valueOf(exception));
            return "";
        }
    }

    private static String queryParam(String string, String string2) {
        for (String string3 : new String[]{"?" + string2 + "=", "&" + string2 + "="}) {
            int n2 = string.indexOf(string3);
            if (n2 < 0) {
                continue;
            }
            String string4 = string.substring(n2 + string3.length());
            int n3 = string4.indexOf(38);
            if (n3 >= 0) {
                string4 = string4.substring(0, n3);
            }
            try {
                return URLDecoder.decode(string4, "UTF-8");
            }
            catch (Exception exception) {
                return string4;
            }
        }
        return "";
    }

    public static LoginData login(String string) {
        return MicrosoftLogin.loginWithMicrosoftAccessToken(gson.fromJson(Browser.postExternal("https://login.live.com/oauth20_token.srf", "client_id=" + MicrosoftLogin.formValue(CLIENT_ID) + "&client_secret=" + MicrosoftLogin.formValue(CLIENT_SECRET) + "&refresh_token=" + MicrosoftLogin.formValue(string) + "&grant_type=refresh_token&redirect_uri=" + MicrosoftLogin.formValue("http://localhost:8247") + "&prompt=select_account", false), AuthTokenResponse.class), string);
    }

    public static LoginData loginMarketplaceRefreshToken(String string) {
        return MicrosoftLogin.loginWithMicrosoftAccessToken(gson.fromJson(Browser.postExternal("https://login.live.com/oauth20_token.srf", "client_id=00000000402B5328&grant_type=refresh_token&refresh_token=" + MicrosoftLogin.formValue(string) + "&scope=" + MicrosoftLogin.formValue("service::user.auth.xboxlive.com::MBI_SSL"), false), AuthTokenResponse.class), string);
    }

    private static LoginData loginWithMicrosoftAccessToken(AuthTokenResponse authTokenResponse, String string) {
        String string2;
        if (authTokenResponse == null || authTokenResponse.access_token == null || authTokenResponse.access_token.isEmpty()) {
            return new LoginData();
        }
        String string3 = authTokenResponse.access_token;
        string2 = authTokenResponse.refresh_token == null || authTokenResponse.refresh_token.isEmpty() ? string : authTokenResponse.refresh_token;
        XblXstsResponse g2 = MicrosoftLogin.authenticateXboxLive("d=" + string3);
        if (!MicrosoftLogin.isUsable(g2)) {
            g2 = MicrosoftLogin.authenticateXboxLive(string3);
        }
        if (!MicrosoftLogin.isUsable(g2)) {
            return new LoginData();
        }
        XblXstsResponse g3 = gson.fromJson(Browser.postExternal("https://xsts.auth.xboxlive.com/xsts/authorize", "{\"Properties\":{\"SandboxId\":\"RETAIL\",\"UserTokens\":[\"" + g2.Token + "\"]},\"RelyingParty\":\"rp://api.minecraftservices.com/\",\"TokenType\":\"JWT\"}", true), XblXstsResponse.class);
        if (g3 == null) {
            return new LoginData();
        }
        McResponse mcResponse = gson.fromJson(Browser.postExternal("https://api.minecraftservices.com/authentication/login_with_xbox", "{\"identityToken\":\"XBL3.0 x=" + g2.DisplayClaims.xui[0].uhs + ";" + g3.Token + "\"}", true), McResponse.class);
        if (mcResponse == null) {
            return new LoginData();
        }
        ProfileResponse profileResponse = gson.fromJson(Browser.getBearerResponse("https://api.minecraftservices.com/minecraft/profile", mcResponse.access_token), ProfileResponse.class);
        if (profileResponse == null) {
            return new LoginData();
        }
        return new LoginData(mcResponse.access_token, string2, profileResponse.aEZ, profileResponse.name);
    }

    private static String formValue(String string) {
        try {
            return URLEncoder.encode(string, "UTF-8");
        }
        catch (Exception exception) {
            return string;
        }
    }

    private static XblXstsResponse authenticateXboxLive(String string) {
        return gson.fromJson(Browser.postExternal("https://user.auth.xboxlive.com/user/authenticate", "{\"Properties\":{\"AuthMethod\":\"RPS\",\"SiteName\":\"user.auth.xboxlive.com\",\"RpsTicket\":\"" + string + "\"},\"RelyingParty\":\"http://auth.xboxlive.com\",\"TokenType\":\"JWT\"}", true), XblXstsResponse.class);
    }

    private static boolean isUsable(XblXstsResponse xblXstsResponse) {
        if (xblXstsResponse == null) return false;
        if (xblXstsResponse.Token == null) return false;
        if (xblXstsResponse.DisplayClaims == null) return false;
        if (xblXstsResponse.DisplayClaims.xui == null) return false;
        if (xblXstsResponse.DisplayClaims.xui.length <= 0) return false;
        if (xblXstsResponse.DisplayClaims.xui[0] == null) return false;
        if (xblXstsResponse.DisplayClaims.xui[0].uhs == null) return false;
        return true;
    }

    private static void startServer() {
        if (server != null) {
            return;
        }
        try {
            server = HttpServer.create(new InetSocketAddress("localhost", 8247), 0);
            server.createContext("/", new Handler());
            server.setExecutor(executor);
            server.start();
            return;
        }
        catch (IOException iOException) {
            iOException.printStackTrace();
            return;
        }
    }

    static void stopServer() {
        if (server == null) {
            return;
        }
        server.stop(0);
        server = null;
        callback = null;
    }

    static {
        gson = new Gson();
    }

    public static class Handler
    implements HttpHandler {
        Handler() {
        }

        @Override
        public void handle(HttpExchange httpExchange) throws java.io.IOException {
            if (httpExchange.getRequestMethod().equals("GET")) {
                List<NameValuePair> list = URLEncodedUtils.parse(httpExchange.getRequestURI(), StandardCharsets.UTF_8.name());
                boolean bl = false;
                for (NameValuePair nameValuePair : list) {
                    if (!nameValuePair.getName().equals("code")) continue;
                    this.bk(nameValuePair.getValue());
                    bl = true;
                    break;
                }
                if (!bl) {
                    this.a(httpExchange, "Cannot authenticate.");
                } else {
                    this.a(httpExchange, "<html>You may now close this page, if you didn't specify an account then clear your cookies to select an individual one.</html>");
                }
            }
            MicrosoftLogin.stopServer();
        }

        private void bk(String string) {
            String string2 = Browser.postExternal("https://login.live.com/oauth20_token.srf", "client_id=ba89e6e0-8490-4a26-8746-f389a0d3ccc7&code=" + string + "&client_secret=hlQ8Q~33jTRilP4yE-UtuOt9wG.ZFLqq6pErIa2B&grant_type=authorization_code&redirect_uri=http://localhost:8247", false);
            AuthTokenResponse authTokenResponse = gson.fromJson(string2, AuthTokenResponse.class);
            if (authTokenResponse == null) {
                callback.accept(null);
                return;
            }
            callback.accept(authTokenResponse.refresh_token);
        }

        private void a(HttpExchange httpExchange, String string) throws java.io.IOException {
            OutputStream outputStream = httpExchange.getResponseBody();
            httpExchange.getResponseHeaders().add("Content-Type", "text/html; charset=utf-8");
            httpExchange.sendResponseHeaders(200, string.length());
            outputStream.write(string.getBytes(StandardCharsets.UTF_8));
            outputStream.flush();
            outputStream.close();
        }
    }
}
