package com.alan.clients.module.impl.render.targetinfo;

import com.alan.clients.component.impl.render.ParticleComponent;
import com.alan.clients.module.impl.player.HealthBypass;
import com.alan.clients.module.impl.render.TargetInfo;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.other.TickEvent;
import com.alan.clients.newevent.impl.render.Render2DEvent;
import com.alan.clients.util.animation.Animation;
import com.alan.clients.util.animation.Easing;
import com.alan.clients.util.render.RenderUtil;
import com.alan.clients.util.vector.Vector2d;
import com.alan.clients.util.vector.Vector2f;
import com.alan.clients.value.Mode;
import com.alan.clients.ui.theme.Themes;
import com.alan.clients.util.font.Font;
import com.alan.clients.util.localization.Localization;
import com.alan.clients.util.math.MathUtil;
import com.alan.clients.util.render.ColorUtil;
import com.alan.clients.util.render.StencilUtil;
import com.alan.clients.util.render.particle.Particle;
import com.alan.clients.component.impl.community.UserLookupComponent;
import com.alan.clients.util.font.FontManager;
import com.alan.clients.util.font.FontWeight;
import com.alan.clients.util.shader.ShaderQueueType;
import java.awt.Color;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderSkeleton;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class BMSTargetInfo extends Mode<TargetInfo> {
    private final Font healthFont = FontManager.MAIN.a(22, FontWeight.LIGHT);
    private final Font nameFont = FontManager.MAIN.a(22, FontWeight.LIGHT);
    private TargetInfo targetInfo;
    private int padding = 4;
    private int barOffsetX = 4;
    private int barOffsetY = 4;
    private Animation scaleAnimation = new Animation(Easing.EASE_OUT_ELASTIC, 500L);
    private Animation healthAnimation = new Animation(Easing.EASE_OUT_SINE, 500L);
    @EventLink
    public final Listener<Render2DEvent> onRender2D = var1x -> {
        if (this.targetInfo == null) {
            this.targetInfo = this.e(TargetInfo.class);
        }

        Entity entity = this.targetInfo.target;
        if (entity != null) {
            boolean flag = !this.targetInfo.inWorld || this.targetInfo.stopwatch.T(1000L);
            this.scaleAnimation.setDuration(flag ? 400L : 850L);
            this.scaleAnimation.setEasing(flag ? Easing.EASE_IN_BACK : Easing.EASE_OUT_ELASTIC);
            this.scaleAnimation.Q(flag ? 0.0 : 1.0);
            if (!(this.scaleAnimation.getValue() <= 0.0)) {
                String s = entity.getName();
                String s1 = UserLookupComponent.c(s, s);
                double d0 = this.targetInfo.position.x;
                double d1 = this.targetInfo.position.y;
                AbstractClientPlayer abstractclientplayer = (AbstractClientPlayer)entity;
                HealthBypass healthbypass = this.e(HealthBypass.class);
                float f = healthbypass != null && healthbypass.isEnabled() ? HealthBypass.getScoreboardHealth(abstractclientplayer) : abstractclientplayer.getHealth();
                double d2 = Math.min(!this.targetInfo.inWorld ? 0.0 : MathUtil.round(f, 1), abstractclientplayer.getMaxHealth());
                double d3 = Math.max(0, 100);
                this.healthAnimation.Q(d2 / abstractclientplayer.getMaxHealth() * d3);
                this.healthAnimation.setEasing(Easing.EASE_OUT_QUINT);
                this.healthAnimation.setDuration(250L);
                double d4 = this.healthAnimation.getValue();
                double d5 = (abstractclientplayer.hurtTime == 0 ? 0.0F : abstractclientplayer.hurtTime - aEg.timer.bWm) * 0.0F;
                byte b0 = 32;
                double d6 = Math.round(d2 / abstractclientplayer.getMaxHealth() * 100.0);
                double d7 = d5 / 2.0;
                double d8 = this.padding + b0 + this.padding + d3 + this.barOffsetY + this.padding;
                double d9 = b0 + this.padding * 2;
                this.targetInfo.positionValue.n(new Vector2d(d8, d9));
                double d10 = this.scaleAnimation.getValue();
                this.b(ShaderQueueType.REGULAR, 1)
                    .c(
                        () -> {
                            GlStateManager.pushMatrix();
                            GlStateManager.translate((d0 + d8 / 2.0) * (1.0 - d10), (d1 + d9 / 2.0) * (1.0 - d10), 0.0);
                            GlStateManager.scale(d10, d10, 0.0);
                            this.rz();
                            Color color = Themes.rK();
                            Color color1 = this.rz().rA();
                            RenderUtil.a(d0, d1, d8 - 4.0, d9, 6.0, color, color, true);
                            this.nameFont
                                .b(
                                    s1,
                                    d0 - 28.0 + b0 + this.barOffsetX + this.healthFont.getStringWidth(Localization.ce("ui.targethud.name")) + 3.0,
                                    d1 + this.padding + this.barOffsetY,
                                    Color.white.getRGB()
                                );
                            GlStateManager.popMatrix();
                            GlStateManager.pushMatrix();
                            GlStateManager.translate((d0 + d8 / 2.0) * (1.0 - d10), (d1 + d9 / 2.0) * (1.0 - d10), 0.0);
                            GlStateManager.scale(d10, d10, 0.0);
                            RenderUtil.color(ColorUtil.a(Color.RED, Color.WHITE, d5 / 9.0));
                            RenderUtil.dropShadow(3, d0 + this.padding + d7, d1 + this.padding + d7, b0 - d5, b0 - d5, 20.0, 5.0);
                            this.drawHead(abstractclientplayer, d0 + this.padding + d7, d1 + this.padding + d7, b0 - d5);
                            RenderUtil.roundedRectangle(
                                d0 + this.padding + b0 + this.barOffsetX, d1 + this.padding + b0 - this.barOffsetY - 10.0, d3, 12.0, 2.0, Color.darkGray.darker()
                            );
                            RenderUtil.a(
                                d0 + this.padding + b0 + this.barOffsetX,
                                d1 + this.padding + b0 - this.barOffsetY - 10.0,
                                d4,
                                12.0,
                                2.0,
                                ColorUtil.withAlpha(color1, 100),
                                ColorUtil.withAlpha(color1, 100),
                                false
                            );
                            this.healthFont
                                .drawString(d6 + "%", d0 + this.padding + b0 + this.barOffsetX + d3 + this.barOffsetY - 50.0, d1 + this.padding + b0 - this.barOffsetY - 8.0, Color.WHITE.getRGB());
                            GlStateManager.popMatrix();
                        }
                    );
                this.b(ShaderQueueType.BLUR).c(() -> {
                    GlStateManager.pushMatrix();
                    GlStateManager.translate((d0 + d8 / 2.0) * (1.0 - d10), (d1 + d9 / 2.0) * (1.0 - d10), 0.0);
                    GlStateManager.scale(d10, d10, 0.0);
                    RenderUtil.roundedRectangle(d0, d1, d8 - 4.5, d9, 6.0, Color.BLACK);
                    GlStateManager.popMatrix();
                });
                this.b(ShaderQueueType.BLOOM).c(() -> {
                    GlStateManager.pushMatrix();
                    GlStateManager.translate((d0 + d8 / 2.0) * (1.0 - d10), (d1 + d9 / 2.0) * (1.0 - d10), 0.0);
                    GlStateManager.scale(d10, d10, 0.0);
                    Color color = this.rz().rE();
                    Color color1 = this.rz().rE();
                    RenderUtil.a(d0 + 0.5, d1, d8 - 4.5, d9, 7.0, color, color1, true);
                    GlStateManager.popMatrix();
                });
            }
        }
    };
    @EventLink
    public final Listener<TickEvent> onTick = var1x -> {
        if (this.targetInfo != null) {
            Entity entity = this.targetInfo.target;
            if (entity != null && !(this.scaleAnimation.getValue() <= 0.0)) {
                double d0 = (((AbstractClientPlayer)entity).hurtTime == 0 ? 0.0F : ((AbstractClientPlayer)entity).hurtTime - aEg.timer.bWm) * 0.0F;
                if (d0 > 0.0) {
                    for (int i = 0; i < d0 * Math.random() / 2.0; i++) {
                        ParticleComponent.a(
                            new Particle(
                                new Vector2f((float)(this.targetInfo.position.x + 20.0), (float)(this.targetInfo.position.y + 20.0)),
                                new Vector2f((float)(Math.random() - 0.5) * 1.7F, (float)(Math.random() - 0.5) * 1.7F)
                            )
                        );
                    }
                }
            }
        }
    };

    public BMSTargetInfo(String var1, TargetInfo targetInfo) {
        super(var1, targetInfo);
    }

    private void drawHead(AbstractClientPlayer abstractClientPlayer, double var2, double var4, double var6) {
        StencilUtil.initStencil();
        StencilUtil.bindWriteStencilBuffer();
        this.rz();
        RenderUtil.roundedRectangle(var2, var4, var6, var6, 3.0, Themes.rK());
        StencilUtil.bindReadStencilBuffer(1);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(770, 771);
        GlStateManager.alphaFunc(516, 0.0F);
        GlStateManager.enableTexture2D();
        HealthBypass healthbypass = this.e(HealthBypass.class);
        float f = healthbypass != null && healthbypass.isEnabled() ? HealthBypass.getScoreboardHealth(abstractClientPlayer) : abstractClientPlayer.getHealth();
        ResourceLocation resourcelocation = this.targetInfo.inWorld && f > 0.0F ? abstractClientPlayer.getLocationSkin() : RenderSkeleton.getEntityTexture();
        aEg.getTextureManager().bindTexture(resourcelocation);
        Gui.drawScaledCustomSizeModalRect(var2, var4, 4.0F, 4.0F, 4.0F, 4.0F, var6, var6, 32.0F, 32.0F);
        GlStateManager.disableBlend();
        StencilUtil.uninitStencilBuffer();
    }
}
