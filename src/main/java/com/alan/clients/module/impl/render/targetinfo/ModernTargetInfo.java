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
import com.alan.clients.module.impl.render.targetinfo.TargetInfoBackgroundModeValue;
import com.alan.clients.ui.theme.Themes;
import com.alan.clients.util.font.Font;
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
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;

public class ModernTargetInfo extends Mode<TargetInfo> {
    private final BooleanValue particles = new BooleanValue("Particles", this, true);
    private final Font lightFont = FontManager.MAIN.a(22, FontWeight.LIGHT);
    private final Font mediumFont = FontManager.MAIN.a(22, FontWeight.MEDIUM);
    private final ModeValue backgroundMode = new TargetInfoBackgroundModeValue(this, "Background Mode", this);
    private static final float MIN_DAMAGE = 0.01F;
    private static final float MAX_REDUCTION = 0.8F;
    private static final int MAX_ITERATIONS = 256;
    private long lastUpdateTime = 0L;
    private float cachedAdvantage = 0.0F;
    private TargetInfo targetInfoModule;
    private final int padding = 8;
    private final int auN = 7;
    private final int auO = 4;
    private final Animation scaleAnimation = new Animation(Easing.EASE_OUT_ELASTIC, 500L);
    private final Animation healthAnimation = new Animation(Easing.EASE_OUT_SINE, 500L);
    @EventLink
    public final Listener<Render2DEvent> onRender2D = var1x -> {
        if (this.targetInfoModule == null) {
            this.targetInfoModule = this.e(TargetInfo.class);
        }

        this.b(ShaderQueueType.BLOOM).c(ParticleComponent::ci);
        this.b(ShaderQueueType.REGULAR, 1).c(ParticleComponent::cj);
        Entity entity = this.targetInfoModule.target;
        if (entity != null) {
            boolean flag = !this.targetInfoModule.inWorld || this.targetInfoModule.stopwatch.T(1000L);
            this.scaleAnimation.setDuration(flag ? 400L : 850L);
            this.scaleAnimation.setEasing(flag ? Easing.EASE_IN_BACK : Easing.EASE_OUT_ELASTIC);
            this.scaleAnimation.Q(flag ? 0.0 : 1.0);
            if (!(this.scaleAnimation.getValue() <= 0.0)) {
                String s = entity.getName();
                String s1 = UserLookupComponent.c(s, s);
                String s2 = this.getAdvantage() > 0.0F ? "Winning:" : "Losing:";
                double d0 = this.targetInfoModule.position.x;
                double d1 = this.targetInfoModule.position.y;
                double d2 = this.mediumFont.getStringWidth(s1);
                double d3 = this.lightFont.getStringWidth(s2);
                AbstractClientPlayer abstractclientplayer = (AbstractClientPlayer)entity;
                HealthBypass healthbypass = this.e(HealthBypass.class);
                float f = healthbypass != null && healthbypass.isEnabled() ? HealthBypass.getScoreboardHealth(abstractclientplayer) : abstractclientplayer.getHealth();
                double d4 = Math.min(!this.targetInfoModule.inWorld ? 0.0 : MathUtil.round(f, 1), abstractclientplayer.getMaxHealth());
                double d5 = this.mediumFont.getStringWidth(String.valueOf(d4));
                double d6 = Math.max(d3 + d2 + 35.0 - d5, 65.0);
                this.healthAnimation.Q(d4 / abstractclientplayer.getMaxHealth() * d6);
                this.healthAnimation.setEasing(Easing.EASE_OUT_QUINT);
                this.healthAnimation.setDuration(250L);
                double d7 = this.healthAnimation.getValue();
                double d8 = (abstractclientplayer.hurtTime == 0 ? 0.0F : abstractclientplayer.hurtTime - aEg.timer.bWm) * 0.5;
                byte b0 = 32;
                double d9 = d8 / 2.0;
                double d10 = 48 + d6 + 4.0 + d5 + 8.0;
                double d11 = 48;
                this.targetInfoModule.positionValue.n(new Vector2d(d10, d11));
                double d12 = this.scaleAnimation.getValue();
                this.b(ShaderQueueType.REGULAR).c(() -> {
                    GlStateManager.pushMatrix();
                    GlStateManager.translate((d0 + d10 / 2.0) * (1.0 - d12), (d1 + d11 / 2.0) * (1.0 - d12), 0.0);
                    GlStateManager.scale(d12, d12, 0.0);
                    Color color = Themes.rK();
                    Color color1 = Themes.rK();
                    Color color2 = this.rz().rA();
                    Color color3 = this.rz().rB();
                    if (this.backgroundMode.wo().getName().equals("Tint")) {
                        Color color4 = this.rz().getAccentColor(new Vector2d(d0, d1));
                        Color color5 = this.rz().getAccentColor(new Vector2d(d0, d1 + d11));
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

                    RenderUtil.a(d0, d1, d10 - 1.0, d11, 16.0, color, color1, true);
                    this.lightFont.b(s2, d0 + 8.0 + b0 + 7.0, d1 + 8.0 + 4.0 + 2.0, Color.WHITE.hashCode());
                    this.mediumFont.b(s1, d0 + 8.0 + b0 + 7.0 + d3 + 3.0, d1 + 8.0 + 4.0 + 2.5, color2.hashCode());
                    GlStateManager.popMatrix();
                    GlStateManager.pushMatrix();
                    GlStateManager.translate((d0 + d10 / 2.0) * (1.0 - d12), (d1 + d11 / 2.0) * (1.0 - d12), 0.0);
                    GlStateManager.scale(d12, d12, 0.0);
                    double d13 = d0 + 8.0 + b0 + 7.0;
                    double d14 = d1 + 8.0 + b0 - 4.0 - 7.0;
                    Color color8 = ColorUtil.withAlpha(Themes.rK(), (int)(Themes.rK().getAlpha() / 1.7F));
                    this.rz();
                    RenderUtil.a(d13, d14, d6, 6.0, 3.0, color8, Themes.rK(), true);
                    RenderUtil.a(d0 + 8.0 + b0 + 7.0, d1 + 8.0 + b0 - 4.0 - 7.0, d7, 6.0, 3.0, color3, color2, true);
                    this.mediumFont.b(String.valueOf(d4), d0 + 8.0 + b0 + 7.0 + d6 + 4.0, d1 + 8.0 + b0 - 4.0 - 8.0, color2.hashCode());
                    GlStateManager.popMatrix();
                });
                this.b(ShaderQueueType.REGULAR, 1).c(() -> {
                    GlStateManager.pushMatrix();
                    GlStateManager.translate((d0 + d10 / 2.0) * (1.0 - d12), (d1 + d11 / 2.0) * (1.0 - d12), 0.0);
                    GlStateManager.scale(d12, d12, 0.0);
                    RenderUtil.color(ColorUtil.a(Color.RED, Color.WHITE, d8 / 9.0));
                    RenderUtil.dropShadow(3, d0 + 8.0 + d9, d1 + 8.0 + d9, b0 - d8, b0 - d8, 20.0, this.rz().getRound() * 2);
                    this.renderTargetHead((AbstractClientPlayer)entity, d0 + 8.0 + d9, d1 + 8.0 + d9, b0 - d8);
                    GlStateManager.popMatrix();
                });
                this.b(ShaderQueueType.BLUR).c(() -> {
                    GlStateManager.pushMatrix();
                    GlStateManager.translate((d0 + d10 / 2.0) * (1.0 - d12), (d1 + d11 / 2.0) * (1.0 - d12), 0.0);
                    GlStateManager.scale(d12, d12, 0.0);
                    RenderUtil.roundedRectangle(d0, d1, d10 - 1.0, d11, 16.0, Color.BLACK);
                    GlStateManager.popMatrix();
                });
                this.b(ShaderQueueType.BLOOM).c(() -> {
                    GlStateManager.pushMatrix();
                    GlStateManager.translate((d0 + d10 / 2.0) * (1.0 - d12), (d1 + d11 / 2.0) * (1.0 - d12), 0.0);
                    GlStateManager.scale(d12, d12, 0.0);
                    RenderUtil.roundedRectangle(d0 + 0.5, d1 + 0.5, d10 - 2.0, d11 - 1.0, 17.0, this.rz().rE());
                    GlStateManager.popMatrix();
                });
            }
        }
    };
    @EventLink
    public final Listener<TickEvent> onTick = var1x -> {
        if (this.targetInfoModule != null) {
            Entity entity = this.targetInfoModule.target;
            if (entity != null && !(this.scaleAnimation.getValue() <= 0.0) && this.particles.wo()) {
                double d0 = (((AbstractClientPlayer)entity).hurtTime == 0 ? 0.0F : ((AbstractClientPlayer)entity).hurtTime - aEg.timer.bWm) * 0.5;
                if (d0 > 0.0) {
                    for (int i = 0; i < d0 * Math.random() / 2.0; i++) {
                        ParticleComponent.a(
                            new Particle(
                                new Vector2f((float)(this.targetInfoModule.position.x + 20.0), (float)(this.targetInfoModule.position.y + 20.0)),
                                new Vector2f((float)(Math.random() - 0.5) * 1.7F, (float)(Math.random() - 0.5) * 1.7F)
                            )
                        );
                    }
                }
            }
        }
    };

    public ModernTargetInfo(String var1, TargetInfo targetInfo) {
        super(var1, targetInfo);
    }

    private float calculateDamage(ItemStack stack, Entity entity, Entity var3) {
        float f = 1.0F;
        if (stack != null) {
            Item item = stack.getItem();
            if (item instanceof ItemSword) {
                f = ((ItemSword)item).attackDamage;
            } else if (item instanceof ItemTool) {
                f = ((ItemTool)item).damageVsEntity;
            }

            int i = EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, stack);
            if (i > 0) {
                f += i * 1.25F;
            }
        }

        if (entity instanceof EntityLivingBase entitylivingbase) {
            int j = entitylivingbase.getActivePotionEffect(Potion.damageBoost) != null
                ? entitylivingbase.getActivePotionEffect(Potion.damageBoost).getAmplifier() + 1
                : 0;
            if (j > 0) {
                f += j * 3.0F;
            }
        }

        if (var3 instanceof EntityLivingBase entitylivingbase1) {
            float totalArmorValue = entitylivingbase1.getTotalArmorValue();
            float f2 = Math.min(totalArmorValue * 0.04F, 0.8F);
            f *= 1.0F - f2;
            int k = this.getProtectionPoints(entitylivingbase1);
            int l = Math.min(20, (int)Math.ceil(Math.min(25, k) * 0.75F));
            if (l > 0) {
                f *= 1.0F - l * 0.04F;
            }

            int i1 = entitylivingbase1.getActivePotionEffect(Potion.resistance) != null
                ? entitylivingbase1.getActivePotionEffect(Potion.resistance).getAmplifier() + 1
                : 0;
            if (i1 > 0) {
                float f3 = Math.min(i1 * 0.2F, 0.8F);
                f *= 1.0F - f3;
            }
        }

        if (!Float.isFinite(f)) {
            f = 1.0F;
        }

        return Math.max(0.01F, f);
    }

    private int getProtectionPoints(EntityLivingBase living) {
        int i = 0;

        for (int j = 0; j < 4; j++) {
            ItemStack itemstack = living.getCurrentArmor(j);
            int k = itemstack != null ? EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, itemstack) : 0;
            if (k > 0) {
                i += this.getProtectionModifier(k);
            }
        }

        return i;
    }

    private int getProtectionModifier(int var1) {
        return (int)Math.floor((6 + var1 * var1) * 0.75F / 3.0F);
    }

    private float getAdvantage() {
        long now = System.currentTimeMillis();
        if (now - this.lastUpdateTime > 200L) {
            this.cachedAdvantage = this.calculateAdvantage();
            this.lastUpdateTime = now;
        }

        return this.cachedAdvantage;
    }

    private float calculateAdvantage() {
        Entity entity = this.targetInfoModule.target;
        if (!(entity instanceof AbstractClientPlayer)) {
            return 0.0F;
        }

        float f = Math.max(0.1F, aEg.thePlayer.getHealth());
        AbstractClientPlayer abstractclientplayer = (AbstractClientPlayer)entity;
        HealthBypass healthbypass = this.e(HealthBypass.class);
        float f1 = healthbypass != null && healthbypass.isEnabled() ? HealthBypass.getScoreboardHealth(abstractclientplayer) : abstractclientplayer.getHealth();
        float f2 = Math.max(0.1F, f1);
        float f3 = this.calculateDamage(aEg.thePlayer.getHeldItem(), aEg.thePlayer, entity);
        float f4 = this.calculateDamage(((AbstractClientPlayer)entity).getHeldItem(), entity, aEg.thePlayer);
        if (f3 <= 0.01F && f4 <= 0.01F) {
            return 0.0F;
        }

        int i;
        for (i = 0; f > 0.0F && f2 > 0.0F && i < 256; i++) {
            f2 -= f3;
            f -= f4;
        }

        return i >= 256 ? f3 - f4 : f - f2;
    }

    private void renderTargetHead(AbstractClientPlayer abstractClientPlayer, double var2, double var4, double var6) {
        StencilUtil.initStencil();
        StencilUtil.bindWriteStencilBuffer();
        RenderUtil.roundedRectangle(var2, var4, var6, var6, this.rz().getRound() * 2, Themes.rK());
        StencilUtil.bindReadStencilBuffer(1);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(770, 771);
        GlStateManager.alphaFunc(516, 0.0F);
        GlStateManager.enableTexture2D();
        HealthBypass healthbypass = this.e(HealthBypass.class);
        float f = healthbypass != null && healthbypass.isEnabled() ? HealthBypass.getScoreboardHealth(abstractClientPlayer) : abstractClientPlayer.getHealth();
        ResourceLocation resourcelocation = this.targetInfoModule.inWorld && f > 0.0F ? abstractClientPlayer.getLocationSkin() : RenderSkeleton.getEntityTexture();
        aEg.getTextureManager().bindTexture(resourcelocation);
        Gui.drawScaledCustomSizeModalRect(var2, var4, 4.0F, 4.0F, 4.0F, 4.0F, var6, var6, 32.0F, 32.0F);
        GlStateManager.disableBlend();
        StencilUtil.uninitStencilBuffer();
        float f1 = 0.5F;
        RenderUtil.roundedOutlineRectangle(var2 - f1, var4 - f1, var6 + f1 * 2.0F, var6 + f1 * 2.0F, this.rz().getRound() * 2, 0.5, ColorUtil.withAlpha(Color.BLACK, 40));
    }
}
