package com.alan.clients.ui.menu.impl.main;

import com.alan.clients.Client;
import com.alan.clients.compat.NetworkToggles;
import com.alan.clients.compat.OfflineMode;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.util.animation.Animation;
import com.alan.clients.util.animation.Easing;
import com.alan.clients.util.render.RenderUtil;
import com.alan.clients.util.vector.Vector2d;
import com.alan.clients.ui.menu.Menu;
import com.alan.clients.ui.menu.component.button.MenuButton;
import com.alan.clients.ui.menu.component.button.impl.MenuTextButton;
import com.alan.clients.util.MouseUtil;
import com.alan.clients.ui.menu.impl.main.MainMenu;
import com.alan.clients.util.NetworkUtil;
import com.alan.clients.util.font.Font;
import com.alan.clients.util.gui.textbox.TextAlign;
import com.alan.clients.util.gui.textbox.TextBox;
import com.alan.clients.util.render.ColorUtil;
import com.alan.clients.util.shader.RiseShaders;
import com.alan.clients.util.shader.base.ShaderRenderType;
import com.alan.clients.util.vantage.MachineFingerprint;
import com.alan.clients.newevent.impl.other.BackendS2CEvent;
import com.alan.clients.util.font.FontManager;
import com.alan.clients.util.font.FontWeight;
import com.alan.clients.util.shader.ShaderQueueType;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import net.minecraft.client.gui.ScaledResolution;
import rip.vantage.commons.util.time.StopWatch;

public class LoginMenu
extends Menu {
    public String aCA;
    public StopWatch bN;
    public MenuTextButton emailButton;
    @EventLink
    public Listener<BackendS2CEvent> aCE;
    public MenuButton[] menuButtons;
    public MenuTextButton loginButton;
    public boolean aCz;
    public String jc;
    public Animation fadeAnimation;
    public String aCB = null;
    public TextBox emailBox;
    public Font aCu = FontManager.MAIN.a(64, FontWeight.LIGHT);
    public boolean aCC;
    public boolean aCD;
    public Animation animation = new Animation(Easing.EASE_OUT_QUINT, 600L);

    static {
    }


    @Override
    public void mouseClicked(final int n, final int n2, final int n3) {
        if (this.menuButtons == null) {
            return;
        }
        if (n3 == 0) {
            final MenuButton[] menuButtons = this.menuButtons;
            int count = menuButtons.length;
            for (int i = 0; i < count; i++) {
                final MenuButton menuButton = menuButtons[i];
                if (MouseUtil.isHovered(menuButton.getX(), menuButton.getY(), menuButton.oM(), menuButton.da(), n, n2)) {
                    menuButton.runAction();
                    break;
                }
            }
            this.emailBox.click(n, n2, n3);
        }
    }

    @Override
    public void keyTyped(char c2, int n) {
        this.emailBox.key(c2, n);
        if (n != 15) {
            if (n != 28) return;
            if (this.emailBox.getText().isEmpty()) return;
            this.emailButton.runAction();
            return;
        }
        this.emailBox.setSelected(!this.emailBox.isSelected());
    }

    public void aW(String string) {
        if (this.aCz) {
            return;
        }
        try {
            String string2 = MachineFingerprint.vW();
            if (this.aCB != null) {
                this.aCB.equals(string2);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        //add code
        if (NetworkToggles.versionCheck()) {
            try {
                if (this.aCA == null) {
                    this.rp();
                }

                String[] stringArray = "6.9.5".split("\\.");
                String[] stringArray2 = this.aCA.split("\\.");

                for (int i = 0; i < 2; i++) {
                    if (Float.parseFloat(stringArray[i]) < Float.parseFloat(stringArray2[i])) {
                        System.out.println("A newer version is available please update your client on https://Vantage.Rip");
                        this.aX("A newer version is available please update your client on https://Vantage.Rip");
                        return;
                    }
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        rip.vantage.network.core.VantageNetwork.aKB().kj(string);
        rip.vantage.network.core.VantageNetwork.aKB().aKI();
        this.aCz = true;
        this.bN.aX();
    }

    public void aX(String string) {
        this.jc = string;
        this.bN.aX();
        this.aCz = false;
    }

    public LoginMenu() {
        this.fadeAnimation = new Animation(Easing.EASE_IN_OUT_CUBIC, 3000L);
        this.bN = new StopWatch();
        this.aCE = er2 -> {
            String string;
            String string2;
            rip.vantage.commons.packet.impl.server.protection.S2CPacketAuthentication b2 = null;
            if (!(er2.dd() instanceof rip.vantage.commons.packet.impl.server.protection.S2CPacketAuthentication)) return;
            b2 = (rip.vantage.commons.packet.impl.server.protection.S2CPacketAuthentication)er2.dd();
            System.out.println("Auth");
            rip.vantage.network.handler.BackendWebSocket.eRC.aX();
            int aKi2 = (int)(b2.isSuccess() ? 1L : 0L);
            this.aCC = false;
            if (aKi2 != 0 && (string2 = b2.getExpectedHwid()) != null && !string2.isEmpty() && !rip.vantage.security.IntegrityGuard.aL(string = MachineFingerprint.vW(), string2)) {
                System.out.println("EC61");
                aKi2 = 0;
                this.aCC = true;
                StringSelection stringSelection = new StringSelection(string);
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, new StringSelection("Rise"));
            }
            if (aKi2 != 0 && !rip.vantage.security.IntegrityGuard.cV(aKi2 != 0)) {
                System.out.println("EC92");
                System.exit(1);
                Runtime.getRuntime().halt(1);
                throw new SecurityException("EC92");
            }
            if (aKi2 != 0) {
                this.aCB = null;
                this.aCD = false;
                aEg.displayGuiScreen(new MainMenu());
                Client.a.getConfigManager().tn();
                return;
            }
            this.aX(b2.getE());
            String string3 = null;
            string3 = MachineFingerprint.vW();
            StringSelection stringSelection = new StringSelection(string3);
            java.awt.datatransfer.Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(stringSelection, new StringSelection("Rise"));
            int equalsIgnoreCase2 = this.aCC || b2.getE() != null && b2.getE().equalsIgnoreCase("HWID_MISMATCH") ? 1 : 0;
            if (equalsIgnoreCase2 == 0) return;
            if (this.aCD) return;
            this.aCD = true;
            this.aCB = string3;
            try {
                java.awt.Desktop.getDesktop().browse(new java.net.URI("https://youtu.be/jeYDms69hBo"));
            } catch (java.io.IOException | java.net.URISyntaxException e) {
                e.printStackTrace();
            }
        };
        Client.a.e().b(this);
    }

    @Override
    public void onGuiClosed() {
        Client.a.e().c(this);
    }

    //add code
    public void rp() {
        this.aCA = OfflineMode.offline()
            ? null
            : NetworkUtil.aY("https://raw.githubusercontent.com/risellc/LatestRiseVersion/main/Version");
    }

    @Override
    public void initGui() {
        int width2 = this.width / 2;
        int height2 = this.height / 2;
        int l17_lo = 180;
        int l19_hi = 24;
        int l21_hi = 6;
        int dL19 = width2 - l17_lo / 2;
        int l25_hi = height2 - l19_hi / 2 - l21_hi / 2 - l19_hi / 2;
        this.loginButton = new MenuTextButton(dL19, l25_hi, l17_lo, l19_hi, () -> {}, "");
        this.emailButton = new MenuTextButton(dL19, l25_hi + l19_hi + l21_hi, l17_lo, l19_hi, () -> this.aW(this.emailBox.getText()), "Login");
        this.emailBox = new TextBox(new Vector2d(width2, l25_hi + 9), FontManager.MAIN.a(24, FontWeight.BOLD), Color.WHITE, TextAlign.CENTER, "Username", l17_lo * 5);
        this.animation = new Animation(Easing.EASE_OUT_QUINT, 600L);
        this.menuButtons = new MenuButton[]{this.loginButton, this.emailButton};
        this.fadeAnimation.setValue(255.0);
        this.fadeAnimation.reset();
        this.aCz = false;
    }

    @Override
    public void drawScreen(int n, int n2, float f) {
        if (this.fadeAnimation.getValue() < 255.0) {
            RiseShaders.aPL.a(ShaderRenderType.OVERLAY, f, null);
        }
        ScaledResolution scaledResolution = LoginMenu.aEg.jY;
        this.b(ShaderQueueType.BLUR).c(() -> RenderUtil.d(0.0, 0.0, scaledResolution.getScaledWidth(), scaledResolution.getScaledHeight(), Color.BLACK));
        this.loginButton.draw(n, n2, f);
        this.emailButton.draw(n, n2, f);
        this.b(ShaderQueueType.REGULAR).c(() -> {
            double d = 0.0;
            this.emailBox.draw();
            double d3 = this.loginButton.getY() - (double)this.aCu.height();
            this.animation.Q(d3);
            double d4 = this.animation.getValue();
            Color color = ColorUtil.withAlpha(Color.WHITE, (int)(d4 / d3 * 200.0));
            this.aCu.drawString("Welcome", (float)this.width / 2.0f, d4 - 10.0, color.getRGB());
            if (this.bN.T(3000L)) {
                if (this.aCz) {
                    try {
                        String string = MachineFingerprint.vW();
                        StringSelection stringSelection = new StringSelection(string);
                        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, new StringSelection("Rise"));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    this.aX("Login is taking longer than expected. HWID copied to clipboard.");
                }
                this.aCz = false;
            } else if (this.jc != null) {
                FontManager.MAIN.a(18, FontWeight.LIGHT).drawString(this.jc, (float)this.width / 2.0f, d4 + 26.0, Color.RED.getRGB());
            }
            FontManager.MAIN.a(18, FontWeight.REGULAR).drawCenteredString("Made with <3 by Alan and The_Bi11iona1re", scaledResolution.getScaledWidth() - 5, scaledResolution.getScaledHeight() - 20, ColorUtil.withAlpha(aBS, 100).getRGB());
            FontManager.MAIN.a(12, FontWeight.REGULAR).drawCenteredString("\u00a9 Rise Client 2026. All Rights Reserved", scaledResolution.getScaledWidth() - 5, scaledResolution.getScaledHeight() - 10, ColorUtil.withAlpha(aBS, 100).getRGB());
            this.fadeAnimation.Q(0.0);
            RenderUtil.d(0.0, 0.0, LoginMenu.aEg.displayWidth, LoginMenu.aEg.displayHeight, new Color(0, 0, 0, (int)this.fadeAnimation.getValue()));
        });
    }
}
