package com.alan.clients.ui.menu.impl.intro;

import com.alan.clients.util.animation.Animation;
import com.alan.clients.util.animation.Easing;
import com.alan.clients.util.render.RenderUtil;
import com.alan.clients.ui.menu.impl.main.LoginMenu;
import com.alan.clients.util.render.ColorUtil;
import com.alan.clients.util.font.FontManager;
import com.alan.clients.util.font.FontWeight;
import java.awt.Color;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import rip.vantage.commons.util.time.StopWatch;

public class PrereleaseDisclaimer extends GuiScreen {
    private final Animation fadeAnimation = new Animation(Easing.EASE_IN_OUT_CUBIC, 1000L);
    private final StopWatch aCt = new StopWatch();

    public PrereleaseDisclaimer() {
    }

    @Override
    public void initGui() {
        this.fadeAnimation.reset();
        this.aCt.aX();
    }

    @Override
    public void drawScreen(int var1, int var2, float var3) {
        RenderUtil.d(0.0, 0.0, aEg.displayWidth, aEg.displayHeight, Color.BLACK);
        this.fadeAnimation.Q(this.aCt.getElapsedTime() > 4000L ? 0.0 : 255.0);
        ScaledResolution scaledresolution = new ScaledResolution(aEg);
        FontManager.MAIN
            .a(24, FontWeight.REGULAR)
            .drawString(
                "Note: This is private prerelease software",
                scaledresolution.getScaledWidth() / 2.0,
                scaledresolution.getScaledHeight() / 2.0 - 70.0,
                ColorUtil.withAlpha(Color.WHITE, (int)this.fadeAnimation.getValue()).getRGB()
            );
        FontManager.MAIN
            .a(24, FontWeight.REGULAR)
            .drawString(
                "Features, interfaces and sequences are not final and can be expected to change at any time",
                scaledresolution.getScaledWidth() / 2.0,
                scaledresolution.getScaledHeight() / 2.0 - 50.0,
                ColorUtil.withAlpha(Color.WHITE, (int)this.fadeAnimation.getValue()).getRGB()
            );
        FontManager.MAIN
            .a(16, FontWeight.REGULAR)
            .drawString(
                "© Rise Client 2022. All Rights Reserved",
                scaledresolution.getScaledWidth() / 2.0,
                scaledresolution.getScaledHeight() / 2.0 + 70.0,
                ColorUtil.withAlpha(Color.WHITE, (int)this.fadeAnimation.getValue() / 2).getRGB()
            );
        if (this.aCt.T(5000L)) {
            aEg.displayGuiScreen(new LoginMenu());
        }
    }
}
