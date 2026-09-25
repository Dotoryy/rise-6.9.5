package com.alan.clients.module.impl.render;

import com.alan.clients.component.impl.render.ProjectionComponent;
import com.alan.clients.module.Module;
import com.alan.clients.module.api.Category;
import com.alan.clients.module.api.ModuleInfo;
import com.alan.clients.module.impl.player.HealthBypass;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.render.Render2DEvent;
import com.alan.clients.util.render.RenderUtil;
import com.alan.clients.util.vector.Vector2d;
import com.alan.clients.value.Mode;
import com.alan.clients.value.impl.BooleanValue;
import com.alan.clients.value.impl.ModeValue;
import com.alan.clients.value.impl.SubMode;
import com.alan.clients.util.render.ColorUtil;
import com.alan.clients.component.impl.combat.TargetComponent;
import com.alan.clients.util.shader.ShaderQueueType;
import java.awt.Color;
import java.util.List;
import javax.vecmath.Vector4d;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;

@ModuleInfo(aliases={"module.render.2desp.name"}, description="module.render.projectionesp.description", category=Category.RENDER)
public class M2DESP
extends Module {
    private final ModeValue box = new ModeValue("Box", this).add(new Mode[]{new SubMode("Normal")}).add(new Mode[]{new SubMode("CS")}).add(new Mode[]{new SubMode("None")}).setDefault("Normal");
    private final ModeValue healthBarMode = new ModeValue("Health Bar Mode", this).add(new Mode[]{new SubMode("Health")}).add(new Mode[]{new SubMode("Standard")}).add(new Mode[]{new SubMode("Gradient")}).add(new Mode[]{new SubMode("None")}).setDefault("Health");
    private final BooleanValue armorBar = new BooleanValue("Armor Bar", (Module)this, (Boolean)false);
    private final BooleanValue showTargets = new BooleanValue("Targets", (Module)this, (Boolean)false);
    private final BooleanValue player = new BooleanValue("Player", (Module)this, (Boolean)true, () -> {
        if ((Boolean)this.showTargets.wo() != false) return false;
        return true;
    });
    private final BooleanValue invisibles = new BooleanValue("Invisibles", (Module)this, (Boolean)false, () -> {
        if ((Boolean)this.showTargets.wo() != false) return false;
        return true;
    });
    private final BooleanValue animals = new BooleanValue("Animals", (Module)this, (Boolean)false, () -> {
        if ((Boolean)this.showTargets.wo() != false) return false;
        return true;
    });
    private final BooleanValue mobs = new BooleanValue("Mobs", (Module)this, (Boolean)false, () -> {
        if ((Boolean)this.showTargets.wo() != false) return false;
        return true;
    });
    private final BooleanValue playerTeammates = new BooleanValue("Player Teammates", (Module)this, (Boolean)true, () -> {
        if ((Boolean)this.showTargets.wo() != false) return false;
        return true;
    });
    private final BooleanValue glow = new BooleanValue("Glow", (Module)this, (Boolean)false);
    @EventLink
    public final Listener<Render2DEvent> onRender2D = render2DEvent -> {
        List<EntityLivingBase> list = TargetComponent.b(((Boolean)this.player.wo()).booleanValue(), ((Boolean)this.invisibles.wo()).booleanValue(), ((Boolean)this.animals.wo()).booleanValue(), ((Boolean)this.mobs.wo()).booleanValue(), ((Boolean)this.playerTeammates.wo()).booleanValue(), true);
        if (M2DESP.aEg.gameSettings.thirdPersonView != 0) {
            list.add(M2DESP.aEg.thePlayer);
        }
        for (EntityLivingBase entityLivingBase : list) {
            Vector4d vector4d;
            if (M2DESP.aEg.getRenderManager() == null || !RenderUtil.isInViewFrustrum(entityLivingBase) || entityLivingBase.isDead || entityLivingBase.isInvisible()) continue;
            if ((vector4d = ProjectionComponent.e(entityLivingBase)) == null) continue;
            double d2 = vector4d.x;
            double d3 = vector4d.y;
            double d4 = vector4d.z;
            double d5 = vector4d.w;
            double d6 = d4 - d2;
            double d7 = d5 - d3;
            Vector2d vector2d = new Vector2d(0.0, 0.0);
            Vector2d vector2d2 = new Vector2d(0.0, 500.0);
            Color color = this.rz().getAccentColor(vector2d);
            Color color2 = this.rz().getAccentColor(vector2d2);
            Color color3 = Color.BLACK;
            block90: {
                {
                    String string = ((Mode)this.box.wo()).getName();
                    switch (string) {
                        case "Normal": {
                            RenderUtil.d(d2 - 0.5, d3 + 1.0, 1.5, d7 - 1.5, color3);
                            RenderUtil.d(d2 - 0.5, d3 - 0.5, d6 + 1.5, 1.5, color3);
                            RenderUtil.d(d4 - 0.5, d3 + 1.0, 1.5, d7, color3);
                            RenderUtil.d(d2 - 0.5, d5 - 0.5, d6, 1.5, color3);
                            RenderUtil.horizontalCenteredGradient(d2, d3 + 0.5, 0.5, d7 - 0.5, color, color2);
                            RenderUtil.horizontalCenteredGradient(d2, d3, d6, 0.5, color, color2);
                            RenderUtil.horizontalCenteredGradient(d4, d3, 0.5, d7, color, color2);
                            RenderUtil.horizontalCenteredGradient(d2, d5, d6 + 0.5, 0.5, color, color2);
                            this.b(ShaderQueueType.BLOOM).c(() -> this.glowBox(d2, d3, d7, color, color2, d6, d4, d5));
                            break block90;
                        }
                        case "CS": {
                            break;
                        }
                        case "None":
                        default: {
                            break block90;
                        }
                    }
                }
                float f5 = (float)(d6 / 4.0);
                float f6 = (float)(d7 / 4.0);
                RenderUtil.d(d2 - 0.5, d3 + 1.0, 1.5, f6, color3);
                RenderUtil.d(d2 - 0.5, d5 - (double)f6 - 0.5, 1.5, f6, color3);
                RenderUtil.d(d2 - 0.5, d3 - 0.5, f5 + 1.0f, 1.5, color3);
                RenderUtil.d(d4 - (double)f5 - 0.5, d3 - 0.5, f5 + 1.5f, 1.5, color3);
                RenderUtil.d(d4 - 0.5, d3 + 1.0, 1.5, f6, color3);
                RenderUtil.d(d4 - 0.5, d5 - (double)f6 - 0.5, 1.5, f6, color3);
                RenderUtil.d(d2 - 0.5, d5 - 0.5, f5 + 1.0f, 1.5, color3);
                RenderUtil.d(d4 - (double)f5 - 0.5, d5 - 0.5, f5 + 1.5f, 1.5, color3);
                RenderUtil.d(d2, d3 + 0.5, 0.5, f6, color2);
                RenderUtil.d(d2, d5 - (double)f6, 0.5, f6, color2);
                RenderUtil.d(d2, d3, f5, 0.5, color2);
                RenderUtil.d(d4 - (double)f5, d3, f5, 0.5, color2);
                RenderUtil.d(d4, d3, 0.5, f6 + 0.5f, color2);
                RenderUtil.d(d4, d5 - (double)f6, 0.5, f6 + 0.5f, color2);
                RenderUtil.d(d2, d5, f5, 0.5, color2);
                RenderUtil.d(d4 - (double)f5, d5, f5, 0.5, color2);
                this.b(ShaderQueueType.BLOOM).c(() -> this.glowCornerBox(d2, d3, f6, color2, d5, f5, d4));
            }
            if (!(entityLivingBase instanceof EntityLivingBase)) continue;
            HealthBypass healthBypass = (HealthBypass)this.e(HealthBypass.class);
            float f2 = healthBypass != null && healthBypass.isEnabled() ? HealthBypass.getScoreboardHealth(entityLivingBase) : entityLivingBase.getHealth();
            block72: {
                if (((Mode)this.healthBarMode.wo()).getName().equals("None")) break block72;
                double d8;
                block69: {
                    float f3;
                    {
                        Color color4 = new Color(0, 0, 0, 180);
                        RenderUtil.d(d2 - 2.5, d3 - 0.5, 1.5, d7 + 1.5, color4);
                        f3 = MathHelper.clamp_float((float)(f2 / entityLivingBase.getMaxHealth()), (float)0.0f, (float)1.0f);
                        d8 = (d5 - d3 - 2.0) * (double)(1.0f - f3);
                        String string2 = ((Mode)this.healthBarMode.wo()).getName();
                        switch (string2) {
                            case "Health": {
                                int n6 = Color.HSBtoRGB(f2 / entityLivingBase.getMaxHealth() / 3.0f, 1.0f, 1.0f);
                                RenderUtil.d(d2 - 2.0, d3 + d8, 0.5, d5 - d3 - d8 + 0.5, new Color(n6));
                                this.b(ShaderQueueType.BLOOM).c(() -> this.glowHealthColorBar(d2, d3, d8, d5, n6));
                                break block72;
                            }
                            case "Standard": {
                                break block69;
                            }
                            case "Gradient": {
                                break;
                            }
                            default: {
                                break block72;
                            }
                        }
                    }
                    Color color5 = new Color(ColorUtil.a(this.rz().rA(), this.rz().rB(), f3).getRGB());
                    RenderUtil.horizontalGradient(d2 - 2.0, d3 + d8, 0.5, d5 - d3 - d8 + 0.5, color5, this.rz().rB());
                    this.b(ShaderQueueType.BLOOM).c(() -> this.glowGradientBar(d2, d3, d8, d5, color5));
                    break block72;
                }
                RenderUtil.d(d2 - 2.0, d3 + d8, 0.5, d5 - d3 - d8 + 0.5, this.rz().getAccentColor(vector2d));
                this.b(ShaderQueueType.BLOOM).c(() -> this.glowStandardBar(d2, d3, d8, d5, vector2d));
            }
            if (!((Boolean)this.armorBar.wo()).booleanValue()) continue;
            float f4 = (float)entityLivingBase.getTotalArmorValue() / 20.0f;
            if (!(f4 > 0.0f)) continue;
            RenderUtil.d(d2 - 0.5, d5 + 1.5, d4 - d2 + 1.5, 1.5, new Color(0, 0, 0, 180));
            RenderUtil.horizontalCenteredGradient(d2, d5 + 2.0, (d6 + 0.5) * (double)f4, 0.5, this.rz().rA(), ColorUtil.a(this.rz().rA(), this.rz().rB(), f4));
            this.b(ShaderQueueType.BLOOM).c(() -> this.glowArmorBar(d2, d5, d4, d6, f4));
        }
    };

    private  void glowArmorBar(double d2, double d3, double d4, double d5, float f2) {
        if (((Boolean)this.glow.wo()).booleanValue()) {
            GlStateManager.pushMatrix();
            RenderUtil.d(d2 - 0.5, d3 + 1.5, d4 - d2 + 1.5, 1.5, new Color(0, 0, 0, 180));
            RenderUtil.horizontalCenteredGradient(d2, d3 + 2.0, (d5 + 2.0) * (double)f2, 0.5, this.rz().rA(), ColorUtil.a(this.rz().rA(), this.rz().rB(), f2));
            GlStateManager.popMatrix();
        }
    }

    private  void glowGradientBar(double d2, double d3, double d4, double d5, Color color) {
        if (((Boolean)this.glow.wo()).booleanValue()) {
            GlStateManager.pushMatrix();
            RenderUtil.horizontalGradient(d2 - 2.0, d3 + d4, 2.0, d5 - d3 - d4 + 0.5, color, this.rz().rB());
            GlStateManager.popMatrix();
        }
    }

    private  void glowStandardBar(double d2, double d3, double d4, double d5, Vector2d vector2d) {
        if (((Boolean)this.glow.wo()).booleanValue()) {
            GlStateManager.pushMatrix();
            RenderUtil.d(d2 - 2.0, d3 + d4, 2.0, d5 - d3 - d4 + 0.5, this.rz().getAccentColor(vector2d));
            GlStateManager.popMatrix();
        }
    }

    private  void glowHealthColorBar(double d2, double d3, double d4, double d5, int n2) {
        if (((Boolean)this.glow.wo()).booleanValue()) {
            GlStateManager.pushMatrix();
            RenderUtil.d(d2 - 2.0, d3 + d4, 2.0, d5 - d3 - d4 + 0.5, new Color(n2));
            GlStateManager.popMatrix();
        }
    }

    private  void glowCornerBox(double d2, double d3, float f2, Color color, double d4, float f3, double d5) {
        GlStateManager.pushMatrix();
        if (((Boolean)this.glow.wo()).booleanValue()) {
            RenderUtil.d(d2, d3 + 0.5, 0.5, f2, color);
            RenderUtil.d(d2, d4 - (double)f2, 0.5, f2, color);
            RenderUtil.d(d2, d3, f3, 0.5, color);
            RenderUtil.d(d5 - (double)f3, d3, f3, 0.5, color);
            RenderUtil.d(d5, d3, 0.5, f2 + 0.5f, color);
            RenderUtil.d(d5, d4 - (double)f2, 0.5, f2 + 0.5f, color);
            RenderUtil.d(d2, d4, f3, 0.5, color);
            RenderUtil.d(d5 - (double)f3, d4, f3, 0.5, color);
        }
        GlStateManager.popMatrix();
    }

    private  void glowBox(double d2, double d3, double d4, Color color, Color color2, double d5, double d6, double d7) {
        GlStateManager.pushMatrix();
        if (((Boolean)this.glow.wo()).booleanValue()) {
            RenderUtil.horizontalCenteredGradient(d2, d3 + 0.5, 0.5, d4 - 0.5, color, color2);
            RenderUtil.horizontalCenteredGradient(d2, d3, d5, 0.5, color, color2);
            RenderUtil.horizontalCenteredGradient(d6, d3, 0.5, d4, color, color2);
            RenderUtil.horizontalCenteredGradient(d2, d7, d5 + 0.5, 0.5, color, color2);
        }
        GlStateManager.popMatrix();
    }
}
