package com.alan.clients.ui.menu.impl.main;

import com.alan.clients.Client;
import com.alan.clients.ui.menu.Menu;
import com.alan.clients.ui.menu.component.button.MenuButton;
import com.alan.clients.ui.menu.component.button.impl.MenuTextButton;
import com.alan.clients.ui.menu.impl.account.AccountManagerScreen;
import com.alan.clients.util.MouseUtil;
import com.alan.clients.util.animation.Animation;
import com.alan.clients.util.animation.Easing;
import com.alan.clients.util.font.FontManager;
import com.alan.clients.util.font.FontWeight;
import com.alan.clients.util.render.ColorUtil;
import com.alan.clients.util.render.RenderUtil;
import com.alan.clients.util.shader.ShaderQueueType;
import com.alan.clients.util.font.Font;
import com.alan.clients.util.shader.RiseShaders;
import com.alan.clients.util.shader.base.ShaderRenderType;
import java.awt.Color;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiSelectWorld;
import net.minecraft.client.gui.ScaledResolution;
import rip.vantage.network.core.VantageNetwork;

public final class MainMenu extends Menu {
    private Animation animation = new Animation(Easing.EASE_OUT_QUINT, 600L);
    private MenuTextButton aCF;
    private MenuTextButton aCG;
    private MenuTextButton aCH;
    private MenuTextButton aCI;
    private MenuButton[] menuButtons;
    private boolean rice;

    public MainMenu() {
    }

    @Override
    public void drawScreen(int var1, int var2, float var3) {
        if (this.aCF != null && this.aCG != null && this.aCH != null) {
            ScaledResolution scaledresolution = aEg.jY;
            RiseShaders.aPL.a(ShaderRenderType.OVERLAY, var3, null);
            this.b(ShaderQueueType.BLUR).c(() -> RenderUtil.d(0.0, 0.0, scaledresolution.getScaledWidth(), scaledresolution.getScaledHeight(), Color.BLACK));

            for (MenuButton menuButton : this.menuButtons) {
                menuButton.draw(var1, var2, var3);
            }

            Font agc = FontManager.MAIN.a(64, FontWeight.REGULAR);
            double d0 = this.aCF.getY() - agc.height();
            this.animation.Q(d0);
            String s = this.rice ? "Rice" : Client.b;
            double d1 = this.animation.getValue();
            Color color = ColorUtil.withAlpha(Color.WHITE, (int)(d1 / d0 * 200.0));
            this.b(ShaderQueueType.REGULAR)
                .c(
                    () -> {
                        agc.drawString(s, this.width / 2.0F, d1, color.getRGB());
                        FontManager.MAIN
                            .a(16, FontWeight.REGULAR)
                            .drawCenteredString(
                                "Made with <3 by Alan and The_Bi11iona1re",
                                scaledresolution.getScaledWidth() - 5,
                                scaledresolution.getScaledHeight() - 20,
                                ColorUtil.withAlpha(aBS, 100).getRGB()
                            );
                        FontManager.MAIN
                            .a(12, FontWeight.REGULAR)
                            .drawCenteredString(
                                "© Rise Client 2026. All Rights Reserved",
                                scaledresolution.getScaledWidth() - 5,
                                scaledresolution.getScaledHeight() - 10,
                                ColorUtil.withAlpha(aBS, 100).getRGB()
                            );
                        if (!System.getProperty("java.vm.vendor").toLowerCase().contains("oracle corporation")) {
                            FontManager.MAIN.a(32, FontWeight.BOLD);
                        }
                    }
                );
        }
    }

    @Override
    protected void keyTyped(char var1, int var2) {
        switch (var2) {
            case 203:
                System.out.println("Reconnecting");
                VantageNetwork.aKB().aKI();
        }
    }

    @Override
    public void mouseClicked(int var1, int var2, int var3) {
        if (this.menuButtons != null) {
            if (var3 == 0) {
                for (MenuButton menuButton : this.menuButtons) {
                    if (MouseUtil.isHovered(menuButton.getX(), menuButton.getY(), menuButton.oM(), menuButton.da(), var1, var2)) {
                        menuButton.runAction();
                        break;
                    }
                }
            }
        }
    }

    @Override
    public void initGui() {
        this.rice = Math.random() > 0.98;
        int i = this.width / 2;
        int j = this.height / 2;
        short short1 = 180;
        byte b0 = 24;
        byte b1 = 6;
        int k = i - 90;
        int l = j - 12 - 3 - 12;
        this.aCF = new MenuTextButton(k, l, short1, b0, () -> aEg.displayGuiScreen(new GuiSelectWorld(this)), "Singleplayer");
        this.aCG = new MenuTextButton(k, l + b0 + b1, short1, b0, () -> aEg.displayGuiScreen(new GuiMultiplayer(this)), "Multiplayer");
        this.aCH = new MenuTextButton(
            k + short1 / 2 + b1 / 2, l + b0 * 2 + b1 * 2, short1 / 2 - b1 / 2, b0, () -> aEg.displayGuiScreen(new AccountManagerScreen(this)), "Alts"
        );
        this.aCI = new MenuTextButton(k, l + b0 * 2 + b1 * 2, short1 / 2 - b1 / 2, b0, () -> aEg.displayGuiScreen(new GuiOptions(this, aEg.gameSettings)), "Options");
        this.animation = new Animation(Easing.EASE_OUT_QUINT, 600L);
        this.menuButtons = new MenuButton[]{this.aCF, this.aCG, this.aCH, this.aCI};
    }
}
