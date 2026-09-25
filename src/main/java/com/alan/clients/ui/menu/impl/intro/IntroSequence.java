package com.alan.clients.ui.menu.impl.intro;

import com.alan.clients.ui.menu.impl.main.LoginMenu;
import com.alan.clients.util.animation.Animation;
import com.alan.clients.util.animation.Easing;
import com.alan.clients.util.render.RenderUtil;
import java.awt.Color;
import java.util.ArrayList;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import rip.vantage.commons.util.time.StopWatch;

public class IntroSequence extends GuiScreen {
    private final String aCj = "";
    private final String aCk = "";
    private final ArrayList<IntroLine> aCl = new IntroSequenceLines(this);
    private final StopWatch aCm = new StopWatch();
    private final int aCn = 0;
    private boolean yr = false;
    private final Animation aCo = new Animation(Easing.EASE_IN_OUT_CUBIC, 3000L);

    public IntroSequence() {
    }

    @Override
    public void initGui() {
    }

    @Override
    public void drawScreen(int var1, int var2, float var3) {
        if (!this.yr) {
            this.yr = true;
            this.aCm.aX();
            this.aCo.setValue(255.0);
            this.aCo.reset();
        }

        this.aCo.Q(0.0);
        ScaledResolution scaledresolution = new ScaledResolution(aEg);
        RenderUtil.color(Color.WHITE);
        RenderUtil.d(0.0, 0.0, scaledresolution.getScaledWidth(), scaledresolution.getScaledHeight(), Color.BLACK);
        RenderUtil.image(
            new ResourceLocation("rise/images/splash.png"),
            scaledresolution.getScaledWidth() / 2.0 - 75.0,
            scaledresolution.getScaledHeight() / 2.0 - 25.0,
            150.0,
            50.0,
            new Color(255, 255, 255, (int)this.aCo.getValue())
        );
        if (this.aCm.T(4000L)) {
            aEg.displayGuiScreen(new LoginMenu());
        }
    }
}
