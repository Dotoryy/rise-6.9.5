package com.alan.clients.script.api.wrapper.impl;

import com.alan.clients.script.api.wrapper.ScriptWrapper;

public abstract class ScriptFontRenderer<T> extends ScriptWrapper<T> {
    public ScriptFontRenderer(T var1) {
        super(var1);
    }

    public abstract double width(String var1);

    public abstract double height();

    public abstract void draw(String var1, double var2, double var4, int[] var6);

    public abstract void drawCentered(String var1, double var2, double var4, int[] var6);

    public abstract void drawWithShadow(String var1, double var2, double var4, int[] var6);

    public abstract void drawCenteredWithShadow(String var1, float var2, float var3, int[] var4);
}
