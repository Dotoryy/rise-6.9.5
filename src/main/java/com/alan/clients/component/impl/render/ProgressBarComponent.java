package com.alan.clients.component.impl.render;

import com.alan.clients.Client;
import com.alan.clients.component.Component;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreUpdateEvent;
import com.alan.clients.newevent.impl.render.Render2DEvent;
import com.alan.clients.util.animation.Animation;
import com.alan.clients.util.animation.Easing;
import com.alan.clients.util.render.RenderUtil;
import com.alan.clients.ui.theme.Themes;
import com.alan.clients.util.math.MathUtil;
import com.alan.clients.util.render.ColorUtil;
import com.alan.clients.util.font.FontManager;
import com.alan.clients.util.font.FontWeight;
import com.alan.clients.util.shader.ShaderQueueType;
import java.awt.Color;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.MathHelper;

public class ProgressBarComponent extends Component {
    private static final float hy = 150.0F;
    private static final float hz = 10.0F;
    private static final Animation hA = new Animation(Easing.LINEAR, 50L);
    private static final Animation hB = new Animation(Easing.EASE_OUT_EXPO, 900L);
    public static boolean dj;
    private static boolean fY = true;
    private static boolean hC;
    private static float hD;
    private static float hE = 1.0F;
    private static boolean hF = true;
    private static boolean hG = true;
    private static int hH;
    @EventLink(value = 4)
    public final Listener<PreUpdateEvent> onPreUpdate = var0 -> {
        hC = dj;
        dj = false;
        hH = 0;
    };
    @EventLink
    public final Listener<Render2DEvent> onRender2D = var1 -> {
        if (!fY) {
            ScaledResolution scaledresolution = var1.getScaledResolution();
            float f = 150.0F * hE;
            float f1 = 10.0F * hE;
            float f2 = scaledresolution.getScaledWidth() * 0.5F - f * 0.5F;
            float f3 = scaledresolution.getScaledHeight() * 0.5F + 15.0F;
            hB.Q(!dj && hG ? 1.1 : 1.0);
            hB.setDuration(900L);
            hB.setEasing(Easing.EASE_OUT_EXPO);
            double d0 = hB.getValue();
            double d1 = 1.0 - 10.0 * Math.abs(1.0 - hB.getValue());
            hA.Q(hD);
            Themes adv = Client.a.getThemeManager().getTheme();
            this.b(ShaderQueueType.REGULAR, 1)
                .c(
                    () -> {
                        GlStateManager.pushMatrix();
                        GlStateManager.translate((f2 + f * 0.5F) * (1.0 - d0), (f3 + f1 * 0.5F) * (1.0 - d0), 0.0);
                        GlStateManager.scale(d0, d0, 0.0);
                        RenderUtil.roundedRectangle(f2, f3, f, f1, adv.getRound(), ColorUtil.withAlpha(adv.rK(), (int)(adv.rK().getAlpha() * d1)));
                        RenderUtil.a(f2, f3, f * hA.getValue(), f1, adv.getRound(), ColorUtil.withAlpha(adv.rA(), (int)(255.0 * d1)), ColorUtil.withAlpha(adv.rB(), (int)(255.0 * d1)), false);
                        if (hF) {
                            String s = MathUtil.round(hA.getValue() * 100.0, 1) + "%";
                            int i = Math.max(12, Math.round(16.0F * hE));
                            FontManager.MAIN
                                .a(i, FontWeight.REGULAR)
                                .b(
                                    s,
                                    f2 + f - FontManager.MAIN.a(i, FontWeight.REGULAR).getStringWidth(s) - 2.0F * hE,
                                    f3 + 3.0F * hE,
                                    ColorUtil.withAlpha(Color.WHITE, (int)(255.0 * d1)).getRGB()
                                );
                        }

                        GlStateManager.popMatrix();
                    }
                );
            this.b(ShaderQueueType.BLOOM)
                .c(
                    () -> {
                        GlStateManager.pushMatrix();
                        GlStateManager.translate((f2 + f * 0.5F) * (1.0 - d0), (f3 + f1 * 0.5F) * (1.0 - d0), 0.0);
                        GlStateManager.scale(d0, d0, 0.0);
                        RenderUtil.roundedRectangle(
                            f2 + 0.5F, f3 + 0.5F, f - 1.0F, f1 - 1.0F, adv.getRound() + 1, ColorUtil.withAlpha(this.rz().rE(), (int)(this.rz().rE().getAlpha() * d1))
                        );
                        GlStateManager.popMatrix();
                    }
                );
            this.b(ShaderQueueType.BLUR).c(() -> {
                GlStateManager.pushMatrix();
                GlStateManager.translate((f2 + f * 0.5F) * (1.0 - d0), (f3 + f1 * 0.5F) * (1.0 - d0), 0.0);
                GlStateManager.scale(d0, d0, 0.0);
                RenderUtil.roundedRectangle(f2, f3, f, f1, adv.getRound(), ColorUtil.withAlpha(Color.BLACK, (int)(255.0 * d1)));
                GlStateManager.popMatrix();
            });
            if (!dj && hB.isFinished()) {
                hA.setValue(0.0);
                fY = true;
            }
        }
    };

    public ProgressBarComponent() {
    }

    public static void a(float var0) {
        a(var0, 1.0F, true, true);
    }

    public static void a(float var0, float var1) {
        a(var0, var1, true, true);
    }

    public static void a(float var0, float var1, boolean var2, boolean var3) {
        a(var0, var1, var2, var3, 0);
    }

    public static void cl() {
        hA.setValue(0.0);
        hA.setStartValue(0.0);
        hA.setDestinationValue(0.0);
        hA.setStartTime(System.currentTimeMillis());
    }

    public static void stop() {
        dj = false;
        hC = false;
        fY = true;
        hD = 0.0F;
        cl();
    }

    public static void a(float var0, float var1, boolean var2, boolean var3, int var4) {
        if (var4 >= hH) {
            float f = MathHelper.clamp_float(var0, 0.0F, 1.0F);
            float f1 = MathHelper.clamp_float(var1, 0.5F, 1.0F);
            boolean flag = !(hC && !fY && hE == f1 && hF == var2 && hG == var3);
            hD = f;
            hE = f1;
            hF = var2;
            hG = var3;
            hH = var4;
            fY = false;
            dj = true;
            if (flag) {
                hB.setValue(0.95);
                hB.setStartValue(0.95);
                hB.setDestinationValue(0.95);
                hA.setValue(0.0);
                hA.setStartValue(0.0);
                hA.setDestinationValue(0.0);
                hA.setStartTime(System.currentTimeMillis());
            }
        }
    }
}
