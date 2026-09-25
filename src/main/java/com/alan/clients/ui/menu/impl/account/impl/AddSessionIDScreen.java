package com.alan.clients.ui.menu.impl.account.impl;

import com.alan.clients.ui.menu.impl.account.AccountManagerScreen;
import com.alan.clients.util.animation.Animation;
import com.alan.clients.util.animation.Easing;
import com.alan.clients.util.interfaces.InstanceAccess;
import com.alan.clients.util.render.RenderUtil;
import com.alan.clients.util.vector.Vector2d;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.alan.clients.ui.menu.component.button.MenuButton;
import com.alan.clients.ui.menu.component.button.impl.MenuTextButton;
import com.alan.clients.util.MouseUtil;
import com.alan.clients.util.account.auth.SessionIdLogin;
import com.alan.clients.util.font.Font;
import com.alan.clients.util.gui.textbox.TextAlign;
import com.alan.clients.util.gui.textbox.TextBox;
import com.alan.clients.util.shader.RiseShaders;
import com.alan.clients.util.shader.base.ShaderRenderType;
import com.alan.clients.util.font.FontManager;
import com.alan.clients.util.font.FontWeight;
import com.alan.clients.util.shader.ShaderQueueType;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.Session;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class AddSessionIDScreen extends GuiScreen implements InstanceAccess {
    private static final Font FONT_RENDERER = FontManager.MAIN.a(36, FontWeight.BOLD);
    private final MenuButton[] menuButtons = new MenuButton[4];
    private static TextBox sessionBox;
    private static GuiScreen reference;
    private Animation animation;
    private static String statusMessage = "Enter Session- or Refresh-Token";
    private static final Runnable FOCUS_BOX = () -> sessionBox.setSelected(true);
    private static final Runnable CANCEL = () -> aEg.displayGuiScreen(new AccountManagerScreen(reference));
    private static final Runnable LOGIN = () -> new Thread(() -> {
        String s = sessionBox.text.trim();
        if (s.isEmpty()) {
            statusMessage = "Nothing to log in with";
        } else {
            try {
                Session session = SessionIdLogin.bj(s);
                statusMessage = "Logged in as " + session.getUsername();
            } catch (IOException ioexception1) {
                try {
                    statusMessage = "Refreshing Microsoft token …";
                    String s1 = refreshMicrosoftToken(s);
                    statusMessage = "Authenticating with Xbox Live …";
                    tryMinecraftToken(microsoftToMinecraft(s1));
                } catch (Exception exception) {
                    statusMessage = "Login failed: " + exception.getMessage();
                    exception.printStackTrace();
                    return;
                }
            } catch (Exception exception1) {
                statusMessage = "Login failed: " + exception1.getMessage();
                exception1.printStackTrace();
                return;
            }

            try {
                Thread.sleep(600L);
            } catch (InterruptedException interruptedexception) {
            }

            CANCEL.run();
        }
    }, "Session Login Thread").start();
    private static final Runnable PASTE_AND_LOGIN = () -> {
        try {
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            String s = (String)clipboard.getData(DataFlavor.stringFlavor);
            if (s != null && !s.trim().isEmpty()) {
                sessionBox.text = s.trim();
                LOGIN.run();
            } else {
                statusMessage = "Clipboard is empty";
            }
        } catch (Exception exception) {
            statusMessage = "Paste failed";
            exception.printStackTrace();
        }
    };

    public AddSessionIDScreen() {
        reference = this;
    }

    @Override
    public void initGui() {
        short short1 = 300;
        byte b0 = 24;
        byte b1 = 4;
        float f = 296 / 2.0F;
        Vector2d vector2d = new Vector2d(this.width / 2.0F - short1 / 2.0F, this.height / 2.0F - 24.0F);
        sessionBox = new TextBox(vector2d.offset(short1 / 2, 8.0), FontManager.MAIN.a(24, FontWeight.BOLD), Color.WHITE, TextAlign.CENTER, "Session- / Refresh-Token", short1);
        this.menuButtons[0] = new MenuTextButton(vector2d.x, vector2d.y, short1, b0, FOCUS_BOX, "");
        this.menuButtons[1] = new MenuTextButton(vector2d.x, vector2d.y + b0 + b1, f, b0, LOGIN, "Login");
        this.menuButtons[2] = new MenuTextButton(vector2d.x + f + b1, vector2d.y + b0 + b1, f, b0, CANCEL, "Cancel");
        this.menuButtons[3] = new MenuTextButton(vector2d.x, vector2d.y + 2 * (b0 + b1), short1, b0, PASTE_AND_LOGIN, "Paste and Log In");
        this.animation = new Animation(Easing.EASE_OUT_QUINT, 600L);
        this.animation.setStartValue(-200.0);
    }

    @Override
    public void drawScreen(int var1, int var2, float var3) {
        this.animation.Q(0.0);
        RiseShaders.aPL.a(ShaderRenderType.OVERLAY, var3, null);
        this.b(ShaderQueueType.BLUR).c(() -> {
            ScaledResolution scaledresolution = new ScaledResolution(aEg);
            RenderUtil.d(0.0, 0.0, scaledresolution.getScaledWidth(), scaledresolution.getScaledHeight(), Color.BLACK);
        });
        MenuButton[] aadh = this.menuButtons;
        int i = aadh.length;

        for (int j = 0; j < i; j++) {
            aadh[j].draw(var1, var2, var3);
        }

        this.b(ShaderQueueType.REGULAR).c(() -> {
            FONT_RENDERER.drawString(statusMessage, this.width / 2.0F, this.height / 2.0F - 64.0F + this.animation.getValue(), Color.WHITE.getRGB());
            sessionBox.draw();
        });
    }

    @Override
    public void mouseClicked(int var1, int var2, int var3) {
        sessionBox.click(var1, var2, var3);

        for (MenuButton menuButton : this.menuButtons) {
            if (MouseUtil.isHovered(menuButton.getX(), menuButton.getY(), menuButton.oM(), menuButton.da(), var1, var2)) {
                menuButton.runAction();
                break;
            }
        }
    }

    @Override
    protected void keyTyped(char var1, int var2) {
        if (sessionBox.isSelected()) {
            sessionBox.key(var1, var2);
        }
    }

    private static void tryMinecraftToken(String var0) throws IOException {
        String[] astring = getProfile(var0);
        aEg.session = new Session(astring[0], astring[1], var0, "mojang");
        statusMessage = "Logged in as " + astring[0];
        System.out.println("Logged in as " + astring[0] + " (" + astring[1] + ")");
    }

    private static String refreshMicrosoftToken(String var0) throws IOException {
        try (CloseableHttpClient closeablehttpclient = HttpClients.createDefault()) {
            HttpPost httppost = new HttpPost("https://login.live.com/oauth20_token.srf");
            ArrayList arraylist = new ArrayList();
            arraylist.add(new BasicNameValuePair("client_id", "00000000402B5328"));
            arraylist.add(new BasicNameValuePair("grant_type", "refresh_token"));
            arraylist.add(new BasicNameValuePair("refresh_token", var0));
            arraylist.add(new BasicNameValuePair("scope", "service::user.auth.xboxlive.com::MBI_SSL"));
            httppost.setEntity(new UrlEncodedFormEntity(arraylist, StandardCharsets.UTF_8));

            try (CloseableHttpResponse closeablehttpresponse = closeablehttpclient.execute(httppost)) {
                String s = EntityUtils.toString(closeablehttpresponse.getEntity(), StandardCharsets.UTF_8);
                if (closeablehttpresponse.getStatusLine().getStatusCode() != 200) {
                    throw new IOException("Microsoft refresh failed (HTTP " + closeablehttpresponse.getStatusLine().getStatusCode() + ")");
                }

                JsonObject jsonobject = JsonParser.parseString(s).getAsJsonObject();
                return jsonobject.get("access_token").getAsString();
            }
        }
    }

    private static String microsoftToMinecraft(String var0) throws IOException {
        try (CloseableHttpClient closeablehttpclient = HttpClients.createDefault()) {
            String s = null;
            IOException ioexception = null;

            for (String s1 : new String[]{"d=" + var0, var0}) {
                HttpPost httppost = new HttpPost("https://user.auth.xboxlive.com/user/authenticate");
                httppost.setHeader("Content-Type", "application/json");
                httppost.setHeader("Accept", "application/json");
                httppost.setEntity(
                    json(
                        "{\"Properties\":{\"AuthMethod\":\"RPS\",\"SiteName\":\"user.auth.xboxlive.com\",\"RpsTicket\":\""
                            + s1
                            + "\"},\"RelyingParty\":\"http://auth.xboxlive.com\",\"TokenType\":\"JWT\"}"
                    )
                );

                try (CloseableHttpResponse closeablehttpresponse = closeablehttpclient.execute(httppost)) {
                    String s2 = EntityUtils.toString(closeablehttpresponse.getEntity(), StandardCharsets.UTF_8);
                    if (closeablehttpresponse.getStatusLine().getStatusCode() == 200) {
                        JsonObject jsonobject = JsonParser.parseString(s2).getAsJsonObject();
                        s = jsonobject.get("Token").getAsString();
                        break;
                    }

                    ioexception = new IOException("user/authenticate HTTP " + closeablehttpresponse.getStatusLine().getStatusCode());
                }
            }

            if (s == null) {
                throw ioexception;
            }

            HttpPost httppost1 = new HttpPost("https://xsts.auth.xboxlive.com/xsts/authorize");
            httppost1.setHeader("Content-Type", "application/json");
            httppost1.setHeader("Accept", "application/json");
            httppost1.setEntity(
                json(
                    "{\"Properties\":{\"SandboxId\":\"RETAIL\",\"UserTokens\":[\""
                        + s
                        + "\"]},\"RelyingParty\":\"rp://api.minecraftservices.com/\",\"TokenType\":\"JWT\"}"
                )
            );

            JsonObject jsonobject1;
            try (CloseableHttpResponse closeablehttpresponse1 = closeablehttpclient.execute(httppost1)) {
                String s3 = EntityUtils.toString(closeablehttpresponse1.getEntity(), StandardCharsets.UTF_8);
                if (closeablehttpresponse1.getStatusLine().getStatusCode() != 200) {
                    throw new IOException("xsts/authorize HTTP " + closeablehttpresponse1.getStatusLine().getStatusCode() + " : " + s3);
                }

                jsonobject1 = JsonParser.parseString(s3).getAsJsonObject();
            }

            String s4 = jsonobject1.getAsJsonObject("DisplayClaims").getAsJsonArray("xui").get(0).getAsJsonObject().get("uhs").getAsString();
            String s5 = jsonobject1.get("Token").getAsString();
            HttpPost httppost2 = new HttpPost("https://api.minecraftservices.com/authentication/login_with_xbox");
            httppost2.setHeader("Content-Type", "application/json");
            httppost2.setEntity(json("{\"identityToken\":\"XBL3.0 x=" + s4 + ";" + s5 + "\"}"));

            try (CloseableHttpResponse closeablehttpresponse2 = closeablehttpclient.execute(httppost2)) {
                String s6 = EntityUtils.toString(closeablehttpresponse2.getEntity(), StandardCharsets.UTF_8);
                if (closeablehttpresponse2.getStatusLine().getStatusCode() != 200) {
                    throw new IOException("login_with_xbox HTTP " + closeablehttpresponse2.getStatusLine().getStatusCode() + " : " + s6);
                }
                return JsonParser.parseString(s6).getAsJsonObject().get("access_token").getAsString();
            }
        }
    }

    private static StringEntity json(String var0) {
        return new StringEntity(var0, StandardCharsets.UTF_8);
    }

    private static String[] getProfile(String var0) throws IOException {
        try (CloseableHttpClient closeablehttpclient = HttpClients.createDefault()) {
            HttpGet httpget = new HttpGet("https://api.minecraftservices.com/minecraft/profile");
            httpget.setHeader("Authorization", "Bearer " + var0);

            try (CloseableHttpResponse closeablehttpresponse = closeablehttpclient.execute(httpget)) {
                String s = EntityUtils.toString(closeablehttpresponse.getEntity(), StandardCharsets.UTF_8);
                if (closeablehttpresponse.getStatusLine().getStatusCode() != 200) {
                    throw new IOException("Invalid/expired Minecraft token (HTTP " + closeablehttpresponse.getStatusLine().getStatusCode() + ")");
                }

                JsonObject jsonobject = JsonParser.parseString(s).getAsJsonObject();
                return new String[]{jsonobject.get("name").getAsString(), jsonobject.get("id").getAsString()};
            }
        }
    }
}
