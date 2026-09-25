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
import com.alan.clients.value.impl.BooleanValue;
import com.alan.clients.value.impl.ModeValue;
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
import com.alan.clients.module.impl.render.targetinfo.CreidaBackgroundModeValue;
import java.awt.Color;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderSkeleton;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class CreidaModernTargetInfo extends Mode<TargetInfo> {
    private final BooleanValue particles = new BooleanValue("Particles", this, true);
    private final Font lightFont = FontManager.MAIN.a(22, FontWeight.LIGHT);
    private final Font mediumFont = FontManager.MAIN.a(22, FontWeight.MEDIUM);
    private final ModeValue backgroundMode = new CreidaBackgroundModeValue(this, "Background Mode", this);
    private TargetInfo targetInfo;
    private final int padding = 10;
    private final int spacing = 6;
    private final int textOffset = 4;
    private final Animation showAnimation = new Animation(Easing.EASE_OUT_ELASTIC, 500L);
    private final Animation healthAnimation = new Animation(Easing.EASE_OUT_SINE, 500L);
    private final Animation hurtAnimation = new Animation(Easing.EASE_IN_OUT_CUBIC, 300L);
    @EventLink
    public final Listener<Render2DEvent> onRender2D = var1x -> {
        if (this.targetInfo == null) {
            this.targetInfo = this.e(TargetInfo.class);
        }

        this.b(ShaderQueueType.BLOOM).c(ParticleComponent::ci);
        this.b(ShaderQueueType.REGULAR, 1).c(ParticleComponent::cj);
        Entity entity = this.targetInfo.target;
        if (entity != null) {
            boolean flag = !this.targetInfo.inWorld || this.targetInfo.stopwatch.T(1000L);
            this.showAnimation.setDuration(flag ? 400L : 850L);
            this.showAnimation.setEasing(flag ? Easing.EASE_IN_BACK : Easing.EASE_OUT_ELASTIC);
            this.showAnimation.Q(flag ? 0.0 : 1.0);
            if (!(this.showAnimation.getValue() <= 0.0)) {
                String s = entity.getName();
                String s1 = UserLookupComponent.c(s, s);
                double d0 = this.targetInfo.position.x;
                double d1 = this.targetInfo.position.y;
                AbstractClientPlayer abstractclientplayer = (AbstractClientPlayer)entity;
                HealthBypass healthbypass = this.e(HealthBypass.class);
                float f = healthbypass != null && healthbypass.isEnabled() ? HealthBypass.getScoreboardHealth(abstractclientplayer) : abstractclientplayer.getHealth();
                double d2 = this.mediumFont.getStringWidth(s1);
                double d3 = Math.min(!this.targetInfo.inWorld ? 0.0 : MathUtil.round(f, 1), abstractclientplayer.getMaxHealth());
                double d4 = this.mediumFont.getStringWidth(String.valueOf(d3));
                double d5 = Math.max(d2 + 35.0 - d4, 75.0);
                this.healthAnimation.Q(d3 / abstractclientplayer.getMaxHealth() * d5);
                this.healthAnimation.setEasing(Easing.EASE_OUT_QUINT);
                this.healthAnimation.setDuration(250L);
                double d6 = this.healthAnimation.getValue();
                double d7 = (abstractclientplayer.hurtTime == 0 ? 0.0F : abstractclientplayer.hurtTime - aEg.timer.bWm) * 0.5;
                byte b0 = 32;
                this.hurtAnimation.Q(d7 / 2.0);
                double d8 = this.hurtAnimation.getValue();
                double d9 = 52 + d5 + 4.0 + d4 + 10.0;
                double d10 = 42;
                this.targetInfo.positionValue.n(new Vector2d(d9, d10));
                double d11 = this.showAnimation.getValue();
                this.b(ShaderQueueType.REGULAR).c(() -> {
                    GlStateManager.pushMatrix();
                    GlStateManager.translate((d0 + d9 / 2.0) * (1.0 - d11), (d1 + d10 / 2.0) * (1.0 - d11), 0.0);
                    GlStateManager.scale(d11, d11, 0.0);
                    this.rz();
                    Color color = Themes.rK();
                    this.rz();
                    Color color1 = Themes.rK();
                    Color color2 = this.rz().rA();
                    Color color3 = this.rz().rB();
                    if (this.backgroundMode.wo().getName().equals("Tint")) {
                        Color color4 = this.rz().getAccentColor(new Vector2d(d0, d1));
                        Color color5 = this.rz().getAccentColor(new Vector2d(d0, d1 + d10));
                        color = new Color(color4.getRed() / 5, color4.getGreen() / 5, color4.getBlue() / 5, 128);
                        color1 = new Color(color5.getRed() / 5, color5.getGreen() / 5, color5.getBlue() / 5, 128);
                    } else if (this.backgroundMode.wo().getName().equals("Solid")) {
                        Color color6 = this.rz().rA();
                        Color color7 = this.rz().rB();
                        color = new Color(color6.getRed(), color6.getGreen(), color6.getBlue(), 128);
                        color1 = new Color(color7.getRed(), color7.getGreen(), color7.getBlue(), 128);
                        color2 = new Color(255, 255, 255);
                        color3 = new Color(164, 164, 164);
                    }

                    RenderUtil.a(d0 + 3.0, d1 + 5.0, d9 - 1.0, d10, 11.0, color, color1, true);
                    this.lightFont.b(Localization.ce("ui.targethud.name"), d0 + 10.0 + b0 + 6.0, d1 + 10.0 + 4.0 + 2.0, Color.WHITE.hashCode());
                    this.mediumFont.b(s1, d0 + 10.0 + b0 + 6.0 + this.lightFont.getStringWidth(Localization.ce("ui.targethud.name")) + 3.0, d1 + 10.0 + 4.0 + 2.5, color2.hashCode());
                    GlStateManager.popMatrix();
                    GlStateManager.pushMatrix();
                    GlStateManager.translate((d0 + d9 / 2.0) * (1.0 - d11), (d1 + d10 / 2.0) * (1.0 - d11), 0.0);
                    GlStateManager.scale(d11, d11, 0.0);
                    double d12 = d0 + 10.0 + b0 + 6.0;
                    double d13 = d1 + 10.0 + b0 - 4.0 - 7.0;
                    this.rz();
                    Color color8 = Themes.rK();
                    this.rz();
                    color8 = ColorUtil.withAlpha(color8, (int)(Themes.rK().getAlpha() / 1.7F));
                    this.rz();
                    RenderUtil.a(d12, d13, d5, 6.5, 3.5, color8, Themes.rK(), true);
                    RenderUtil.b(d0 + 10.0 + b0 + 6.0, d1 + 10.0 + b0 - 4.0 - 7.0, d6, 6.5, 3.5, color3, color2, false);
                    this.mediumFont.b(String.valueOf(d3), d0 + 10.0 + b0 + 6.0 + d5 + 4.0, d1 + 10.0 + b0 - 4.0 - 8.0, color2.hashCode());
                    GlStateManager.popMatrix();
                });
                this.b(ShaderQueueType.REGULAR, 1).c(() -> {
                    GlStateManager.pushMatrix();
                    GlStateManager.translate((d0 + d9 / 2.0) * (1.0 - d11), (d1 + d10 / 2.0) * (1.0 - d11), 0.0);
                    GlStateManager.scale(d11, d11, 0.0);
                    RenderUtil.color(ColorUtil.a(Color.RED, Color.WHITE, d7 / 9.0));
                    RenderUtil.dropShadow(3, d0 + 10.0 + d8, d1 + 10.0 + d8, b0 - d7, b0 - d7, 20.0, this.rz().getRound() * 2);
                    this.hurtAnimation.Q(d7 / 2.0);
                    double d12 = this.hurtAnimation.getValue() == 0.0 ? 1.0 : this.hurtAnimation.getValue();
                    System.out.println(this.hurtAnimation.getValue());
                    this.drawHead(abstractclientplayer, d0 + 10.0 + d12 / 2.0, d1 + 10.0 + d12 / 2.0, b0 - d7 / 2.0);
                    GlStateManager.popMatrix();
                });
                this.b(ShaderQueueType.BLUR).c(() -> {
                    GlStateManager.pushMatrix();
                    GlStateManager.translate((d0 + d9 / 2.0) * (1.0 - d11), (d1 + d10 / 2.0) * (1.0 - d11), 0.0);
                    GlStateManager.scale(d11, d11, 0.0);
                    RenderUtil.roundedRectangle(d0 + 3.0, d1 + 5.0, d9 - 1.0, d10, 11.0, Color.BLACK);
                    GlStateManager.popMatrix();
                });
                this.b(ShaderQueueType.BLOOM).c(() -> {
                    GlStateManager.pushMatrix();
                    GlStateManager.translate((d0 + d9 / 2.0) * (1.0 - d11), (d1 + d10 / 2.0) * (1.0 - d11), 0.0);
                    GlStateManager.scale(d11, d11, 0.0);
                    RenderUtil.roundedRectangle(d0 + 3.5, d1 + 5.5, d9 - 2.0, d10 - 1.0, 12.0, this.rz().rE());
                    Color color = this.rz().rA();
                    Color color1 = this.rz().rB();
                    RenderUtil.b(d0 + 10.0 + b0 + 6.0, d1 + 10.0 + b0 - 4.0 - 7.0, d6, 6.0, 3.0, ColorUtil.withAlpha(color1, 255), ColorUtil.withAlpha(color, 255), false);
                    GlStateManager.popMatrix();
                });
            }
        }
    };
    @EventLink
    public final Listener<TickEvent> onTick = var1x -> {
        if (this.targetInfo != null) {
            Entity entity = this.targetInfo.target;
            if (entity != null && !(this.showAnimation.getValue() <= 0.0) && this.particles.wo()) {
                double d0 = (((AbstractClientPlayer)entity).hurtTime == 0 ? 0.0F : ((AbstractClientPlayer)entity).hurtTime - aEg.timer.bWm) * 0.5;
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

    public CreidaModernTargetInfo(String var1, TargetInfo targetInfo) {
        super(var1, targetInfo);
    }

    private void drawHead(AbstractClientPlayer abstractClientPlayer, double var2, double var4, double var6) {
        StencilUtil.initStencil();
        StencilUtil.bindWriteStencilBuffer();
        this.rz();
        RenderUtil.roundedRectangle(var2, var4, var6, var6, 7.0, Themes.rK());
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
        float f1 = 0.5F;
        RenderUtil.roundedOutlineRectangle(var2 - f1, var4 - f1, var6 + f1 * 2.0F, var6 + f1 * 2.0F, this.rz().getRound() * 2, 0.5, ColorUtil.withAlpha(Color.BLACK, 40));
    }
}
