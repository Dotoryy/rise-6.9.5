package com.alan.clients.ui.menu.impl.account.impl;

import com.alan.clients.ui.menu.impl.account.AccountManagerScreen;
import com.alan.clients.util.account.auth.MicrosoftLogin;
import com.alan.clients.util.account.auth.McResponse;
import com.alan.clients.util.account.auth.ProfileResponse;
import com.alan.clients.util.animation.Animation;
import com.alan.clients.util.animation.Easing;
import com.alan.clients.util.interfaces.InstanceAccess;
import com.alan.clients.util.render.RenderUtil;
import com.alan.clients.util.vector.Vector2d;
import com.google.gson.Gson;
import com.alan.clients.ui.menu.component.button.MenuButton;
import com.alan.clients.ui.menu.component.button.impl.MenuTextButton;
import com.alan.clients.util.MouseUtil;
import com.alan.clients.util.account.impl.MicrosoftAccount;
import com.alan.clients.util.account.localts.LocaltsOrderStore;
import com.alan.clients.util.font.Font;
import com.alan.clients.util.gui.textbox.TextAlign;
import com.alan.clients.util.gui.textbox.TextBox;
import com.alan.clients.util.shader.RiseShaders;
import com.alan.clients.util.shader.base.ShaderRenderType;
import com.alan.clients.util.web.Browser;
import com.alan.clients.util.font.FontManager;
import com.alan.clients.util.font.FontWeight;
import com.alan.clients.util.shader.ShaderQueueType;
import java.awt.Color;
import java.awt.FileDialog;
import java.awt.Frame;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Scanner;
import java.util.regex.Pattern;
import javax.net.ssl.HttpsURLConnection;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;

public class AddCookieScreen extends GuiScreen implements InstanceAccess {
    private static final long RECENT_LOCALTS_ORDER_WINDOW = 600000L;
    private static final Font FONT_RENDERER = FontManager.MAIN.a(36, FontWeight.BOLD);
    private final MenuButton[] menuButtons = new MenuButton[4];
    private static TextBox usernameBox;
    private static GuiScreen reference;
    private static String text_to_render = "Select file";
    private static String[] cookie_string;
    private Animation animation;
    private static final Gson gson = new Gson();
    private static final Runnable TEXT_BOX_RUNNABLE = () -> usernameBox.setSelected(true);
    private static final Runnable CANCEL_RUNNABLE = () -> aEg.displayGuiScreen(new AccountManagerScreen(reference));
    private static final Runnable SELECT_FILE_RUNNABLE = () -> new Thread(() -> {
        FileDialog filedialog = new FileDialog((Frame)null, "Select Cookie File");
        filedialog.setMode(0);
        File file1 = LocaltsOrderStore.g(600000L);
        if (file1 != null) {
            filedialog.setDirectory(file1.getAbsolutePath());
        }

        filedialog.setVisible(true);
        if (filedialog.getFile() != null && filedialog.getDirectory() != null) {
            filedialog.dispose();
            String s = new File(filedialog.getDirectory() + filedialog.getFile()).getAbsolutePath();

            try {
                StringBuilder stringbuilder = new StringBuilder();
                Scanner scanner = new Scanner(new FileReader(s));

                while (scanner.hasNextLine()) {
                    stringbuilder.append(scanner.nextLine()).append("\n");
                }

                scanner.close();
                usernameBox.bW(filedialog.getFile());
                text_to_render = "Selected file!";
                cookie_string = stringbuilder.toString().split("\n");
            } catch (IOException ioexception) {
                text_to_render = "Error (read)";
            }
        } else {
            filedialog.dispose();
        }
    }, "Select Cookie File").start();
    private static final Runnable LOGIN_RUNNABLE = () -> new Thread(
            () -> {
                try {
                    if (cookie_string.length != 0) {
                        StringBuilder stringbuilder = new StringBuilder();
                        ArrayList arraylist = new ArrayList();

                        for (String s : cookie_string) {
                            String[] astring = s.split("\t");
                            if (astring.length < 7) {
                                System.out.println("[DEBUG] Skipping malformed cookie line: " + s);
                            } else {
                                String s1 = astring[0].startsWith(".") ? astring[0].substring(1) : astring[0];
                                if ((s1.equals("live.com") || s1.endsWith(".live.com")) && !arraylist.contains(astring[5])) {
                                    stringbuilder.append(astring[5]).append("=").append(astring[6]).append("; ");
                                    arraylist.add(astring[5]);
                                }
                            }
                        }

                        StringBuilder stringbuilder1 = new StringBuilder(stringbuilder.substring(0, stringbuilder.length() - 2));
                        System.out.println(stringbuilder1);
                        HttpsURLConnection httpsurlconnection = (HttpsURLConnection)new URL(
                                "https://sisu.xboxlive.com/connect/XboxLive/?state=login&cobrandId=8058f65d-ce06-4c30-9559-473c9275a65d&tid=896928775&ru=https%3A%2F%2Fwww.minecraft.net%2Fen-us%2Flogin&aid=1142970254"
                            )
                            .openConnection();
                        httpsurlconnection.setRequestMethod("GET");
                        httpsurlconnection.setRequestProperty(
                            "Accept",
                            "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7"
                        );
                        httpsurlconnection.setRequestProperty("Accept-Encoding", "niggas");
                        httpsurlconnection.setRequestProperty("Accept-Language", "en-US;q=0.8");
                        httpsurlconnection.setRequestProperty(
                            "User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/112.0.0.0 Safari/537.36"
                        );
                        httpsurlconnection.setInstanceFollowRedirects(false);
                        httpsurlconnection.connect();
                        String s2 = httpsurlconnection.getHeaderField("Location").replaceAll(" ", "%20");
                        HttpsURLConnection httpsurlconnection1 = (HttpsURLConnection)new URL(s2).openConnection();
                        httpsurlconnection1.setRequestMethod("GET");
                        httpsurlconnection1.setRequestProperty(
                            "Accept",
                            "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7"
                        );
                        httpsurlconnection1.setRequestProperty("Accept-Encoding", "niggas");
                        httpsurlconnection1.setRequestProperty("Accept-Language", "fr-FR,fr;q=0.9,en-US;q=0.8,en;q=0.7");
                        httpsurlconnection1.setRequestProperty("Cookie", stringbuilder1.toString());
                        httpsurlconnection1.setRequestProperty(
                            "User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/112.0.0.0 Safari/537.36"
                        );
                        httpsurlconnection1.setInstanceFollowRedirects(false);
                        httpsurlconnection1.connect();
                        String s3 = httpsurlconnection1.getHeaderField("Location");
                        HttpsURLConnection httpsurlconnection2 = (HttpsURLConnection)new URL(s3).openConnection();
                        httpsurlconnection2.setRequestMethod("GET");
                        httpsurlconnection2.setRequestProperty(
                            "Accept",
                            "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7"
                        );
                        httpsurlconnection2.setRequestProperty("Accept-Encoding", "niggas");
                        httpsurlconnection2.setRequestProperty("Accept-Language", "fr-FR,fr;q=0.9,en-US;q=0.8,en;q=0.7");
                        httpsurlconnection2.setRequestProperty("Cookie", stringbuilder1.toString());
                        httpsurlconnection2.setRequestProperty(
                            "User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/112.0.0.0 Safari/537.36"
                        );
                        httpsurlconnection2.setInstanceFollowRedirects(false);
                        httpsurlconnection2.connect();
                        String s4 = httpsurlconnection2.getHeaderField("Location");
                        String s5 = s4.split("accessToken=")[1];
                        String s6 = new String(Base64.getDecoder().decode(s5), StandardCharsets.UTF_8).split("\"rp://api.minecraftservices.com/\",")[1];
                        String s7 = s6.split("\"Token\":\"")[1].split("\"")[0];
                        String s8 = "XBL3.0 x=" + s6.split(Pattern.quote("{\"DisplayClaims\":{\"xui\":[{\"uhs\":\""))[1].split("\"")[0] + ";" + s7;
                        McResponse e = gson.fromJson(
                            Browser.postExternal(
                                "https://api.minecraftservices.com/authentication/login_with_xbox",
                                "{\"identityToken\":\"" + s8 + "\",\"ensureLegacyEnabled\":true}",
                                true
                            ),
                            McResponse.class
                        );
                        if (e == null) {
                            text_to_render = "Error (Invalid Acc)";
                            return;
                        }

                        ProfileResponse f = gson.fromJson(Browser.getBearerResponse("https://api.minecraftservices.com/minecraft/profile", e.access_token), ProfileResponse.class);
                        if (f == null) {
                            text_to_render = "Error (Invalid Acc)";
                            return;
                        }

                        String s9 = MicrosoftLogin.extractRefreshTokenFromCookies(stringbuilder1.toString());
                        MicrosoftAccount aep = new MicrosoftAccount(f.name, f.aEZ, e.access_token, s9);
                        System.out.println("Name: " + f.name + ", UUID: " + f.aEZ + ", AccessToken: " + e.access_token);
                        aep.se();
                        System.out.println("[DEBUG] Proceeding to CANCEL_RUNNABLE");
                        AccountManagerScreen.addAccount(aep);
                        CANCEL_RUNNABLE.run();
                    }
                } catch (Exception exception) {
                    System.out.println("[DEBUG] Exception during login: " + exception.getMessage());
                    text_to_render = "Invalid Account";
                }
            },
            "Login To Cookie"
        )
        .start();
    private static final Runnable BACKGROUND_RUNNABLE = () -> {
        ScaledResolution scaledresolution = new ScaledResolution(aEg);
        RenderUtil.d(0.0, 0.0, scaledresolution.getScaledWidth(), scaledresolution.getScaledHeight(), Color.BLACK);
    };

    public AddCookieScreen() {
        reference = this;
    }

    @Override
    public void drawScreen(int var1, int var2, float var3) {
        this.animation.Q(0.0);
        RiseShaders.aPL.a(ShaderRenderType.OVERLAY, var3, null);
        this.b(ShaderQueueType.BLUR).c(BACKGROUND_RUNNABLE);
        this.b(ShaderQueueType.REGULAR).c(() -> {
            FONT_RENDERER.drawString(text_to_render, this.width / 2, this.height / 2 - 64 + this.animation.getValue(), Color.WHITE.getRGB());
            usernameBox.draw();
        });
        MenuButton[] aadh = this.menuButtons;
        int i = aadh.length;

        for (int j = 0; j < i; j++) {
            aadh[j].draw(var1, var2, var3);
        }
    }

    @Override
    public void mouseClicked(int var1, int var2, int var3) {
        usernameBox.click(var1, var2, var3);

        for (MenuButton menuButton : this.menuButtons) {
            if (MouseUtil.isHovered(menuButton.getX(), menuButton.getY(), menuButton.oM(), menuButton.da(), var1, var2)) {
                menuButton.runAction();
                break;
            }
        }
    }

    @Override
    protected void keyTyped(char var1, int var2) {
        if (usernameBox.isSelected()) {
            usernameBox.key(var1, var2);
        }
    }

    public static String parseNetscapeCookie(String var0) {
        StringBuilder stringbuilder = new StringBuilder();
        Arrays.stream(var0.split("\n"))
            .filter(var0x -> var0x.contains("live.com"))
            .map(var0x -> var0x.split("\t"))
            .filter(var0x -> var0x.length > 6)
            .forEach(var1 -> stringbuilder.append(String.format("%s=%s; ", var1[5], var1[6])));
        return stringbuilder.toString().isEmpty() ? null : stringbuilder.substring(0, stringbuilder.length() - 1);
    }

    @Override
    public void initGui() {
        short short1 = 200;
        byte b0 = 24;
        byte b1 = 4;
        Vector2d vector2d = new Vector2d(this.width / 2 - 100, this.height / 2 - 24);
        usernameBox = new TextBox(vector2d.offset(100, 8.0), FontManager.MAIN.a(24, FontWeight.BOLD), Color.WHITE, TextAlign.CENTER, "Username", short1);
        this.menuButtons[0] = new MenuTextButton(vector2d.x, vector2d.y, short1, b0, TEXT_BOX_RUNNABLE, "");
        this.menuButtons[1] = new MenuTextButton(vector2d.x, vector2d.y + b0 + b1, short1, b0, SELECT_FILE_RUNNABLE, "Select File");
        this.menuButtons[2] = new MenuTextButton(vector2d.x, vector2d.y + (b0 + b1) * 2, short1 / 2 * 1.5, b0, LOGIN_RUNNABLE, "Login & Add");
        this.menuButtons[3] = new MenuTextButton(vector2d.x + short1 / 2 * 1.5 + b1, vector2d.y + (b0 + b1) * 2, short1 / 2 * 0.5 - b1, b0, CANCEL_RUNNABLE, "Cancel");
        this.animation = new Animation(Easing.EASE_OUT_QUINT, 600L);
        this.animation.setStartValue(-200.0);
    }
}
