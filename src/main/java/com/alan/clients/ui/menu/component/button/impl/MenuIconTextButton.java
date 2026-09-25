package com.alan.clients.ui.menu.component.button.impl;

import com.alan.clients.util.render.RenderUtil;
import com.alan.clients.ui.menu.component.button.impl.MenuTextButton;
import com.alan.clients.util.MouseUtil;
import com.alan.clients.util.font.Font;
import com.alan.clients.util.render.ColorUtil;
import com.alan.clients.util.font.FontManager;
import com.alan.clients.util.font.FontWeight;
import com.alan.clients.util.shader.ShaderQueueType;
import java.awt.Color;
import net.minecraft.util.ResourceLocation;

public class MenuIconTextButton extends MenuTextButton {
    private static final Font aBZ = FontManager.MAIN.a(24, FontWeight.BOLD);
    private final ResourceLocation aCa;

    public MenuIconTextButton(double var1, double var3, double var5, double var7, Runnable var9, String var10, ResourceLocation location) {
        super(var1, var3, var5, var7, var9, var10);
        this.aCa = location;
    }

    @Override
    public void draw(int var1, int var2, float var3) {
        this.getHoverAnimation().Q(MouseUtil.isHovered(this.getX(), this.getY(), this.oM(), this.da(), var1, var2) ? 100.0 : 45.0);
        double d0 = this.getY();
        Color color = ColorUtil.withAlpha(Color.BLACK, 150);
        Color color1 = ColorUtil.withAlpha(Color.WHITE, (int)(150.0 + this.getHoverAnimation().getValue()));
        this.b(ShaderQueueType.BLUR).c(() -> RenderUtil.roundedRectangle(this.getX(), this.getY(), this.oM(), this.da(), 5.0, Color.WHITE));
        this.b(ShaderQueueType.BLOOM).c(() -> RenderUtil.roundedRectangle(this.getX() + 0.5, d0 + 0.5, this.oM() - 1.0, this.da() - 1.0, 6.0, color));
        this.b(ShaderQueueType.REGULAR).c(() -> {
            RenderUtil.roundedRectangle(this.getX(), d0, this.oM(), this.da(), 5.0, ColorUtil.withAlpha(aBV, (int)this.getHoverAnimation().getValue() - 15));
            RenderUtil.roundedOutlineGradientRectangle(this.getX(), d0, this.oM(), this.da(), 5.0, 1.0, ColorUtil.withAlpha(aBP, 32), ColorUtil.withAlpha(aBO, 32));
            byte b0 = 64;
            RenderUtil.image(this.aCa, this.getX() + this.oM() / 2.0 - 32, d0 + this.da() / 2.0 - 32, b0, b0, color1);
            Font agc = aBZ;
            float f = (float)(this.oM() - 8.0);

            for (int i = 24; i > 11 && agc.getStringWidth(this.name) > f; i--) {
                agc = FontManager.MAIN.a(i, FontWeight.BOLD);
            }

            agc.drawString(this.name, (float)(this.getX() + this.oM() / 2.0), (float)(d0 + this.da() / 2.0 - b0 / 2 - 24.0), color1.getRGB());
        });
    }
}
