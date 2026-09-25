package com.alan.clients.ui.menu.component.button.impl;

import com.alan.clients.util.render.RenderUtil;
import com.alan.clients.ui.menu.component.button.impl.MenuTextButton;
import com.alan.clients.util.font.Font;
import com.alan.clients.util.render.ColorUtil;
import com.alan.clients.util.font.FontManager;
import com.alan.clients.util.font.FontWeight;
import java.awt.Color;

public class MenuLabelButton extends MenuTextButton {
    private static final Font aCe = FontManager.MAIN.a(14, FontWeight.LIGHT);
    private static final Color aCf = ColorUtil.withAlpha(Color.WHITE, 150);
    private final Color aCg;

    public MenuLabelButton(double var1, double var3, double var5, double var7, Runnable var9, String var10, Color color) {
        super(var1, var3, var5, var7, var9, var10);
        this.aCg = ColorUtil.withAlpha(color, 150);
    }

    @Override
    public void draw(int var1, int var2, float var3) {
        RenderUtil.roundedRectangle(this.getX(), this.getY(), this.oM(), this.da(), 2.0, this.aCg);
        aCe.a(this.name, this.getX() + this.oM() / 2.0 - aCe.getStringWidth(this.name) / 2.0F, this.getY() + this.da() / 2.0 - aCe.height() / 4.0F + 0.5, aCf.getRGB());
    }
}
