package com.alan.clients.module.impl.render.nametags;

import com.alan.clients.component.impl.render.ProjectionComponent;
import com.alan.clients.module.impl.player.HealthBypass;
import com.alan.clients.module.impl.render.NameTags;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.render.Render2DEvent;
import com.alan.clients.util.render.RenderUtil;
import com.alan.clients.value.Mode;
import com.alan.clients.value.impl.BooleanValue;
import com.alan.clients.ui.theme.Themes;
import com.alan.clients.util.font.Font;
import com.alan.clients.component.impl.combat.TargetComponent;
import com.alan.clients.util.font.FontManager;
import com.alan.clients.util.font.FontWeight;
import com.alan.clients.util.shader.ShaderQueueType;
import java.awt.Color;
import java.util.Iterator;
import java.util.List;
import javax.vecmath.Vector4d;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;

public class ModernNameTags
extends Mode<NameTags> {
    private final BooleanValue health = new BooleanValue("Show Health", (Mode<?>)this, (Boolean)true);
    private final BooleanValue overlaysBloomBlur = new BooleanValue("Overlays (Bloom/Blur)", (Mode<?>)this, (Boolean)true);
    private final Font healthFont = FontManager.MAIN.a(14, FontWeight.LIGHT);
    @EventLink
    public final Listener<Render2DEvent> onRender2D = render2DEvent -> {
        List<EntityLivingBase> list = TargetComponent.b((Boolean)((NameTags)this.getParent()).player.wo(), (Boolean)((NameTags)this.getParent()).invisibles.wo(), (Boolean)((NameTags)this.getParent()).animals.wo(), (Boolean)((NameTags)this.getParent()).mobs.wo(), (Boolean)((NameTags)this.getParent()).playerTeammates.wo(), true);
        if (ModernNameTags.aEg.gameSettings.thirdPersonView != 0) {
            list.add((EntityLivingBase)ModernNameTags.aEg.thePlayer);
        }
        Iterator<EntityLivingBase> iterator = list.iterator();
        while (iterator.hasNext()) {
            Color color;
            EntityLivingBase entityLivingBase = iterator.next();
            if (!RenderUtil.isInViewFrustrum((Entity)entityLivingBase)) continue;
            entityLivingBase.Tc();
            Vector4d vector4d = ProjectionComponent.e((Entity)entityLivingBase);
            if (vector4d == null) continue;
            String string = entityLivingBase.getName();
            double d2 = ((NameTags)this.getParent()).a(string, FontManager.MAIN.a(17, FontWeight.LIGHT));
            HealthBypass healthBypass = this.e(HealthBypass.class);
            float f2 = healthBypass != null && healthBypass.isEnabled() ? HealthBypass.getScoreboardHealth(entityLivingBase) : entityLivingBase.getHealth();
            double d3 = vector4d.x + (vector4d.z - vector4d.x) / 2.0;
            double d4 = vector4d.y - 2.0;
            double d5 = (double)(FontManager.MAIN.a(17, FontWeight.LIGHT).height() - 2.0f + ((Boolean)this.health.wo() != false ? this.healthFont.height() : 0.0f)) + 4.0;
            double d6 = d4 - d5 + 1.0;
            boolean bl = entityLivingBase.isPotionActive(Potion.damageBoost);
            Color color2 = new Color(255, 30, 30, 235);
            Color color3 = bl ? color2 : Color.BLACK;
            this.rz();
            Color color4 = Themes.rK();
            Color color5 = new Color(255, 40, 40, color4.getAlpha());
            Color color6 = color = bl ? ModernNameTags.a(color4, color5, 0.35f) : color4;
            if (((Boolean)this.overlaysBloomBlur.wo()).booleanValue()) {
                this.b(ShaderQueueType.BLOOM).c(() -> RenderUtil.roundedRectangle(d3 - 2.0 - d2 / 2.0 + 0.5, d6 + 0.5, d2 + 4.0 - 1.0, d5 - 1.0, this.rz().getRound(), color3));
            }
            this.b(ShaderQueueType.REGULAR).c(() -> {
                RenderUtil.roundedRectangle(d3 - 2.0 - d2 / 2.0, d6, d2 + 4.0, d5, this.rz().getRound() - 1, color);
                FontManager.MAIN.a(17, FontWeight.LIGHT).drawString(string, d3 - 0.5, d6 - 0.5 + 4.0, this.rz().rA().getRGB());
                if (((Boolean)this.health.wo()).booleanValue()) {
                    this.healthFont.drawString(String.valueOf((int)f2), d3, d4 + 5.0 - 2.0 - (double)9, Color.WHITE.getRGB());
                }
            });
            if (!((Boolean)this.overlaysBloomBlur.wo()).booleanValue()) continue;
            this.b(ShaderQueueType.BLUR).c(() -> RenderUtil.roundedRectangle(d3 - 2.0 - d2 / 2.0, d6, d2 + 4.0, d5, this.rz().getRound(), color3));
        }
        return;
    };

    public ModernNameTags(String string, NameTags nameTags) {
        super(string, nameTags);
    }

    private static Color a(Color color, Color color2, float f2) {
        float f3 = Math.max(0.0f, Math.min(1.0f, f2));
        int n2 = (int)((float)color.getRed() + (float)(color2.getRed() - color.getRed()) * f3);
        int n3 = (int)((float)color.getGreen() + (float)(color2.getGreen() - color.getGreen()) * f3);
        int n4 = (int)((float)color.getBlue() + (float)(color2.getBlue() - color.getBlue()) * f3);
        int n5 = (int)((float)color.getAlpha() + (float)(color2.getAlpha() - color.getAlpha()) * f3);
        return new Color(n2, n3, n4, n5);
    }
}
