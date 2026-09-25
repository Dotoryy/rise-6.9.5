package com.alan.clients.ui.menu.impl.account.impl;

import com.alan.clients.ui.menu.impl.account.AccountManagerScreen;
import com.alan.clients.util.animation.Animation;
import com.alan.clients.util.animation.Easing;
import com.alan.clients.util.interfaces.InstanceAccess;
import com.alan.clients.util.render.RenderUtil;
import com.alan.clients.ui.menu.MenuColors;
import com.alan.clients.ui.menu.component.button.impl.MenuIconTextButton;
import com.alan.clients.util.MouseUtil;
import com.alan.clients.util.web.CommunityChat;
import com.alan.clients.util.font.Font;
import com.alan.clients.util.render.ColorUtil;
import com.alan.clients.util.shader.RiseShaders;
import com.alan.clients.util.shader.base.ShaderRenderType;
import com.alan.clients.util.font.FontManager;
import com.alan.clients.util.font.FontWeight;
import com.alan.clients.util.shader.ShaderQueueType;
import java.awt.Color;
import java.awt.Desktop;
import java.net.URI;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;

public class AddAccountScreen extends GuiScreen implements InstanceAccess {
    private static final Font FONT_RENDERER = FontManager.MAIN.a(36, FontWeight.BOLD);
    private static final Font AD_FONT_RENDERER = FontManager.MAIN.a(16, FontWeight.REGULAR);
    private static final Font DISCOUNT_FONT_RENDERER = FontManager.MAIN.a(14, FontWeight.BOLD);
    private static final ResourceLocation XBOX_RESOURCE = new ResourceLocation("rise/images/xbox.png");
    private static final ResourceLocation MICROSOFT_RESOURCE = new ResourceLocation("rise/images/microsoft.png");
    private static final ResourceLocation CRACKED_RESOURCE = new ResourceLocation("rise/images/minecraft.png");
    private static final ResourceLocation COOKIE_RESOURCE = new ResourceLocation("rise/images/Cookie.png");
    private static final ResourceLocation LOCALTS_RESOURCE = new ResourceLocation("rise/images/localts.png");
    private static final String AD_TEXT = "Need alts? Localts purchases in Rise are 5% off — Click here!";
    private static final String AD_URL = "https://localts.store/?campaign=rise";
    private static final int AD_LOGO_SIZE = 24;
    private static final int AD_GAP = 8;
    private static final int AD_SIDE_PADDING = 14;
    private static final float CENTER_REF_HEIGHT = FontManager.MAIN.a(24, FontWeight.BOLD).height();
    private static GuiScreen reference;
    private Animation animation;
    private Animation adHoverAnimation;
    private int adX;
    private int adY;
    private int adWidth;
    private int adHeight;
    private static final Runnable BACKGROUND_RUNNABLE = () -> {
        ScaledResolution scaledresolution = new ScaledResolution(aEg);
        RenderUtil.d(0.0, 0.0, scaledresolution.getScaledWidth(), scaledresolution.getScaledHeight(), Color.BLACK);
    };
    private static final Runnable CANCEL_RUNNABLE = () -> aEg.displayGuiScreen(new AccountManagerScreen(reference));
    private static final Runnable CRACKED_RUNNABLE = () -> aEg.displayGuiScreen(new AddCrackedScreen());
    private static final Runnable MICROSOFT_RUNNABLE = () -> aEg.displayGuiScreen(new AddMicrosoftScreen());
    private static final Runnable GAMEPASS_RUNNABLE = () -> aEg.displayGuiScreen(new AddSessionIDScreen());
    private static final Runnable COOKIE_RUNNABLE = () -> aEg.displayGuiScreen(new AddCookieScreen());
    private static final Runnable LOCALTS_RUNNABLE = () -> aEg.displayGuiScreen(new AddLocaltsScreen());
    private final MenuIconTextButton[] menuButtons = new MenuIconTextButton[5];

    private static float centeredTextY(double var0, double var2, Font var4) {
        return (float)(var0 + var2 / 2.0 - 4.0 * var4.height() / CENTER_REF_HEIGHT);
    }

    public AddAccountScreen() {
        reference = this;
    }

    @Override
    public void drawScreen(int var1, int var2, float var3) {
        this.animation.Q(0.0);
        RiseShaders.aPL.a(ShaderRenderType.OVERLAY, var3, null);
        this.b(ShaderQueueType.BLUR).c(BACKGROUND_RUNNABLE);
        this.adHoverAnimation.Q(MouseUtil.isHovered(this.adX, this.adY, this.adWidth, this.adHeight, var1, var2) ? 100.0 : 45.0);
        this.b(ShaderQueueType.REGULAR)
            .c(() -> FONT_RENDERER.drawString("Select your login method", this.width / 2, this.height / 2 - 76 + this.animation.getValue(), Color.WHITE.getRGB()));
        Color color = ColorUtil.withAlpha(Color.BLACK, 150);
        Color color1 = ColorUtil.withAlpha(Color.WHITE, (int)(150.0 + this.adHoverAnimation.getValue()));
        this.b(ShaderQueueType.BLUR).c(() -> RenderUtil.roundedRectangle(this.adX, this.adY, this.adWidth, this.adHeight, 5.0, Color.WHITE));
        this.b(ShaderQueueType.BLOOM).c(() -> RenderUtil.roundedRectangle(this.adX + 0.5F, this.adY + 0.5F, this.adWidth - 1, this.adHeight - 1, 6.0, color));
        this.b(ShaderQueueType.REGULAR).c(() -> {
            RenderUtil.roundedRectangle(this.adX, this.adY, this.adWidth, this.adHeight, 5.0, ColorUtil.withAlpha(MenuColors.aBV, (int)this.adHoverAnimation.getValue() - 15));
            RenderUtil.roundedOutlineGradientRectangle(this.adX, this.adY, this.adWidth, this.adHeight, 5.0, 1.0, ColorUtil.withAlpha(MenuColors.aBP, 32), ColorUtil.withAlpha(MenuColors.aBO, 32));
            int k = this.adX + 14;
            int l = this.adY + (this.adHeight - 24) / 2;
            RenderUtil.image(LOCALTS_RESOURCE, k, l, 24.0F, 24.0F, color1);
            float f = k + 24 + 8;
            float f1 = centeredTextY(this.adY, this.adHeight, AD_FONT_RENDERER);
            AD_FONT_RENDERER.a("Need alts? Localts purchases in Rise are 5% off — Click here!", f, f1, color1.getRGB());
        });
        MenuIconTextButton[] aadi = this.menuButtons;
        int i = aadi.length;

        for (int j = 0; j < i; j++) {
            aadi[j].draw(var1, var2, var3);
        }

        this.b(ShaderQueueType.REGULAR).c(() -> {
            MenuIconTextButton adi = this.menuButtons[4];
            if (adi != null) {
                String s = "5% OFF";
                float f = 16.0F;
                float f1 = DISCOUNT_FONT_RENDERER.getStringWidth(s) + 16;
                float f2 = (float)(adi.getX() + adi.oM() / 2.0 - f1 / 2.0F);
                float f3 = (float)(adi.getY() + adi.da() - f - 10.0);
                RenderUtil.roundedRectangle(f2, f3, f1, f, 7.0, new Color(38, 132, 93, 225));
                DISCOUNT_FONT_RENDERER.drawString(s, f2 + f1 / 2.0F, centeredTextY(f3, f, DISCOUNT_FONT_RENDERER), Color.WHITE.getRGB());
            }
        });
    }

    @Override
    public void mouseClicked(int var1, int var2, int var3) {
        if (MouseUtil.isHovered(this.adX, this.adY, this.adWidth, this.adHeight, var1, var2)) {
            CommunityChat.A("login_ad", "https://localts.store/?campaign=rise");

            try {
                Desktop.getDesktop().browse(new URI("https://localts.store/?campaign=rise"));
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        } else {
            for (MenuIconTextButton adi : this.menuButtons) {
                if (MouseUtil.isHovered(adi.getX(), adi.getY(), adi.oM(), adi.da(), var1, var2)) {
                    adi.runAction();
                    break;
                }
            }
        }
    }

    @Override
    public void initGui() {
        int i = this.menuButtons.length;
        byte b0 = 100;
        short short1 = 140;
        byte b1 = 5;
        int j = this.width / 2 - ((i & 1) == 0 ? 105 * (i / 2) + 2 : (b0 + b1) * (i / 2) + b0 / 2);
        this.menuButtons[0] = new MenuIconTextButton(0.0, 0.0, 0.0, 0.0, GAMEPASS_RUNNABLE, "SessionID/refID", XBOX_RESOURCE);
        this.menuButtons[1] = new MenuIconTextButton(0.0, 0.0, 0.0, 0.0, MICROSOFT_RUNNABLE, "Microsoft", MICROSOFT_RESOURCE);
        this.menuButtons[2] = new MenuIconTextButton(0.0, 0.0, 0.0, 0.0, CRACKED_RUNNABLE, "Cracked", CRACKED_RESOURCE);
        this.menuButtons[3] = new MenuIconTextButton(0.0, 0.0, 0.0, 0.0, COOKIE_RUNNABLE, "Cookie", COOKIE_RESOURCE);
        this.menuButtons[4] = new MenuIconTextButton(0.0, 0.0, 0.0, 0.0, LOCALTS_RUNNABLE, "Localts", LOCALTS_RESOURCE);

        for (MenuIconTextButton adi : this.menuButtons) {
            adi.setX(j);
            adi.setY(this.height / 2 - short1 / 2 + 24);
            adi.P(b0);
            adi.h(short1);
            j += b0 + b1;
        }

        this.animation = new Animation(Easing.EASE_OUT_QUINT, 600L);
        this.animation.setStartValue(-200.0);
        this.adHoverAnimation = new Animation(Easing.LINEAR, 200L);
        this.adWidth = 60 + AD_FONT_RENDERER.getStringWidth("Need alts? Localts purchases in Rise are 5% off — Click here!");
        this.adHeight = 40;
        this.adX = this.width / 2 - this.adWidth / 2;
        this.adY = this.height - 50;
    }
}
