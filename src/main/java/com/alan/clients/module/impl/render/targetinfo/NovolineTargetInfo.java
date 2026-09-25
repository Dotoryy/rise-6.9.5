package com.alan.clients.module.impl.render.targetinfo;

import com.alan.clients.module.impl.player.HealthBypass;
import com.alan.clients.module.impl.render.TargetInfo;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.render.Render2DEvent;
import com.alan.clients.util.animation.Animation;
import com.alan.clients.util.animation.Easing;
import com.alan.clients.util.render.RenderUtil;
import com.alan.clients.value.Mode;
import com.alan.clients.util.math.MathUtil;
import com.alan.clients.util.render.ColorUtil;
import com.alan.clients.component.impl.community.UserLookupComponent;
import com.alan.clients.util.shader.ShaderQueueType;
import java.awt.Color;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderSkeleton;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public final class NovolineTargetInfo extends Mode<TargetInfo> {
    private TargetInfo targetInfo;
    private final Animation healthBarAnimation = new Animation(Easing.EASE_OUT_SINE, 500L);
    private final Animation widthAnimation = new Animation(Easing.EASE_IN_OUT_SINE, 300L);
    private Entity lastTarget = null;
    private double lastWidth = 0.0;
    @EventLink
    private final Listener<Render2DEvent> onRender2D = var1x -> {
        if (this.targetInfo == null) {
            this.targetInfo = this.e(TargetInfo.class);
        }

        Entity entity = this.targetInfo.target;
        boolean flag = !this.targetInfo.inWorld || this.targetInfo.stopwatch.T(1000L);
        if (entity == null || flag) {
            this.healthBarAnimation.reset();
            this.widthAnimation.reset();
            this.lastTarget = null;
            return;
        }

        double d0 = this.targetInfo.position.x;
        double d1 = this.targetInfo.position.y;
        AbstractClientPlayer abstractclientplayer = (AbstractClientPlayer)entity;
        HealthBypass healthbypass = this.e(HealthBypass.class);
        float f = healthbypass != null && healthbypass.isEnabled()
            ? HealthBypass.getScoreboardHealth(abstractclientplayer)
            : abstractclientplayer.getHealth();
        double d2 = Math.min(!this.targetInfo.inWorld ? 0.0 : MathUtil.round(f, 1), abstractclientplayer.getMaxHealth());
        double d3 = abstractclientplayer.getMaxHealth();
        double d4 = d2 / d3 * 100.0;
        String s = entity.getName();
        String s1 = UserLookupComponent.c(s, s);
        double d5 = aEg.fontRendererObj.getStringWidth(s1);
        double d6 = 74.0;
        double d7 = d6 + d5;
        if (this.lastTarget != entity) {
            this.widthAnimation.reset();
            this.widthAnimation.setValue(this.lastWidth);
            this.widthAnimation.Q(d7);
            this.lastTarget = entity;
        } else {
            this.widthAnimation.Q(d7);
        }

        double d8 = this.widthAnimation.getValue();
        double d9 = 42.0;
        RenderUtil.d(d0, d1, d8, d9, new Color(40, 40, 40, 255));
        aEg.fontRendererObj.b(s1, d0 + 44.0, d1 + 10.0, Color.WHITE.getRGB());
        double d10 = 26.0 + d5;
        RenderUtil.d(d0 + 44.0, d1 + 22.0, d10, 11.0, new Color(21, 21, 21, 150));
        double d11 = d10 * (d2 / d3);
        this.healthBarAnimation.Q(d11);
        double d12 = this.healthBarAnimation.getValue();
        RenderUtil.d(d0 + 44.0, d1 + 22.0, d12, 11.0, ColorUtil.brighter(this.rz().rB(), 0.5F));
        double d13 = d11;
        RenderUtil.d(d0 + 44.0, d1 + 22.0, d13, 11.0, this.rz().rA());
        String s2 = String.format("%.1f%%", d4);
        double d14 = aEg.fontRendererObj.getStringWidth(s2);
        aEg.fontRendererObj.b(s2, d0 + 44.0 + d10 / 2.0 - d14 / 2.0, d1 + 24.5, Color.WHITE.getRGB());
        if (this.isBloomEnabled()) {
            this.b(ShaderQueueType.BLOOM).c(() -> {
                RenderUtil.d(d0 + 44.0, d1 + 22.0, d13, 11.0, this.rz().rA());
                RenderUtil.d(d0 + 44.0, d1 + 22.0, d12, 11.0, ColorUtil.brighter(this.rz().rB(), 0.5F));
            });
        }

        if (entity instanceof AbstractClientPlayer) {
            this.drawHead(abstractclientplayer, d0 + 1.0, d1 + 1.0, 40.0);
        }

        this.lastWidth = d8;
    };

    public NovolineTargetInfo(String var1, TargetInfo targetInfo) {
        super(var1, targetInfo);
    }

    private void drawHead(AbstractClientPlayer abstractClientPlayer, double var2, double var4, double var6) {
        if (this.targetInfo == null) {
            this.targetInfo = this.e(TargetInfo.class);
        }

        Entity entity = this.targetInfo.target;
        boolean flag = !this.targetInfo.inWorld || this.targetInfo.stopwatch.T(1000L);
        if (entity == null || flag) {
            return;
        }

        GlStateManager.enableBlend();
        GlStateManager.blendFunc(770, 771);
        GlStateManager.alphaFunc(516, 0.0F);
        GlStateManager.enableTexture2D();
        HealthBypass healthbypass = this.e(HealthBypass.class);
        float f = healthbypass != null && healthbypass.isEnabled() ? HealthBypass.getScoreboardHealth(abstractClientPlayer) : abstractClientPlayer.getHealth();
        ResourceLocation resourcelocation = this.targetInfo.inWorld && f > 0.0F ? abstractClientPlayer.getLocationSkin() : RenderSkeleton.getEntityTexture();
        aEg.getTextureManager().bindTexture(resourcelocation);
        Gui.drawScaledCustomSizeModalRect((int)var2, (int)var4, 8.0F, 8.0F, 8.0F, 8.0F, (int)var6, (int)var6, 64.0F, 64.0F);
        Gui.drawScaledCustomSizeModalRect((int)var2, (int)var4, 40.0F, 8.0F, 8.0F, 8.0F, (int)var6, (int)var6, 64.0F, 64.0F);
        GlStateManager.disableBlend();
    }

    private Color getAccentColor() {
        return this.rz().rA();
    }

    private boolean isBloomEnabled() {
        return true;
    }
}
