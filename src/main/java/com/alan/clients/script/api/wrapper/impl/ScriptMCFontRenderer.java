package com.alan.clients.script.api.wrapper.impl;

import com.alan.clients.script.api.RenderAPI;
import com.alan.clients.util.font.impl.minecraft.FontRenderer;

public class ScriptMCFontRenderer extends ScriptFontRenderer<FontRenderer> {
    public ScriptMCFontRenderer(FontRenderer var1) {
        super(var1);
    }

    @Override
    public double width(String var1) {
        return this.wrapped.getStringWidth(var1);
    }

    @Override
    public double height() {
        return 9;
    }

    @Override
    public void draw(String var1, double var2, double var4, int[] var6) {
        this.wrapped.a(var1, var2, var4, RenderAPI.intArrayToColor(var6).getRGB());
    }

    @Override
    public void drawCentered(String var1, double var2, double var4, int[] var6) {
        this.wrapped.b(var1, (float)var2 - ((int)this.width(var1) >> 1), (float)var4, RenderAPI.intArrayToColor(var6).getRGB(), false);
    }

    @Override
    public void drawWithShadow(String var1, double var2, double var4, int[] var6) {
        this.wrapped.b(var1, var2, var4, RenderAPI.intArrayToColor(var6).getRGB(), true);
    }

    @Override
    public void drawCenteredWithShadow(String var1, float var2, float var3, int[] var4) {
        this.wrapped.b(var1, var2 - ((int)this.width(var1) >> 1), var3, RenderAPI.intArrayToColor(var4).getRGB(), true);
    }
}
