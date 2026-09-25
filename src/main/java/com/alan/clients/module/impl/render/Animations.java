package com.alan.clients.module.impl.render;

import com.alan.clients.module.Module;
import com.alan.clients.module.api.Category;
import com.alan.clients.module.api.ModuleInfo;
import com.alan.clients.module.impl.movement.TerrainSpeed;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.render.RenderItemEvent;
import com.alan.clients.newevent.impl.render.SwingAnimationEvent;
import com.alan.clients.value.impl.BooleanValue;
import com.alan.clients.value.impl.ModeValue;
import com.alan.clients.value.impl.NumberValue;
import com.alan.clients.value.impl.SubMode;
import com.alan.clients.util.value.ConstantManager;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemMap;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;

@ModuleInfo(aliases = "module.render.animations.name", description = "module.render.animations.description", category = Category.RENDER)
public final class Animations extends Module {
    private final ModeValue blockAnimation = new ModeValue("Block Animation", this)
        .add(new SubMode("None"))
        .add(new SubMode("1.7"))
        .add(new SubMode("1.7 Accurate"))
        .add(new SubMode("Sunny"))
        .add(new SubMode("Lucid"))
        .add(new SubMode("Astro"))
        .add(new SubMode("Smooth"))
        .add(new SubMode("Spin"))
        .add(new SubMode("Leaked"))
        .add(new SubMode("Old"))
        .add(new SubMode("Exhibition"))
        .add(new SubMode("Exhibition Old"))
        .add(new SubMode("Exhibition New"))
        .add(new SubMode("Swong"))
        .add(new SubMode("Stella"))
        .add(new SubMode("Flup"))
        .add(new SubMode("Noov"))
        .add(new SubMode("Komorebi"))
        .add(new SubMode("Rhys"))
        .add(new SubMode("Swing"))
        .add(new SubMode("?"))
        .add(new SubMode("Stab"))
        .add(new SubMode("Beta"))
        .add(new SubMode("Dortware"))
        .add(new SubMode("Avatar"))
        .add(new SubMode("Tap"))
        .add(new SubMode("XIV"))
        .setDefault("None");
    public final ModeValue swingAnimation = new ModeValue("Swing Animation", this)
        .add(new SubMode("None"))
        .add(new SubMode("Punch"))
        .add(new SubMode("Shove"))
        .add(new SubMode("Smooth"))
        .add(new SubMode("1.9+"))
        .setDefault("None");
    private final BooleanValue updatePositionOnlyWhenBlocking = new BooleanValue("Update Position Only When Blocking", this, true);
    public final NumberValue swingSpeed = new NumberValue("Swing Speed", this, 1, -200, 50, 1);
    private final NumberValue x = new NumberValue("X", this, 0.0F, -2.0F, 2.0F, 0.05F);
    private final NumberValue y = new NumberValue("Y", this, 0.0F, -2.0F, 2.0F, 0.05F);
    private final NumberValue z = new NumberValue("Z", this, 0.0F, -2.0F, 2.0F, 0.05F);
    private final NumberValue scale = new NumberValue("Scale", this, 1, 0.1, 2, 0.1);
    private final BooleanValue alwaysShow = new BooleanValue("Always Show", this, false);
    @EventLink
    public final Listener<RenderItemEvent> onRenderItem = var1 -> {
        if (!(var1.getItemToRender().getItem() instanceof ItemMap)) {
            if (!this.updatePositionOnlyWhenBlocking.wo()) {
                GlStateManager.translate(this.x.wo().floatValue(), this.y.wo().floatValue(), this.z.wo().floatValue());
            }

            double d0 = 0.0;
            double d1 = this.scale.wo().doubleValue();
            EnumAction enumaction = var1.dD();
            ItemRenderer itemrenderer = aEg.getItemRenderer();
            float f = this.alwaysShow.wo() && var1.db() ? 0.0F : var1.dE();
            float f1 = var1.dF();
            float f2 = MathHelper.sin(MathHelper.sqrt_float(f1) * (float)ConstantManager.aHb);
            if (var1.db() && enumaction == EnumAction.BLOCK) {
                if (this.updatePositionOnlyWhenBlocking.wo()) {
                    GlStateManager.translate(this.x.wo().floatValue(), this.y.wo().floatValue(), this.z.wo().floatValue());
                }

                label274: {
                    label273: {
                        label272: {
                            label271: {
                                label270: {
                                    label269: {
                                        label268: {
                                            label267: {
                                                label266: {
                                                    label265: {
                                                        label264: {
                                                            label263: {
                                                                label262: {
                                                                    label261: {
                                                                        label260: {
                                                                            label259: {
                                                                                label258: {
                                                                                    label257: {
                                                                                        label256: {
                                                                                            label255: {
                                                                                                label254: {
                                                                                                    label253: {
                                                                                                        label252: {
                                                                                                            label251: {
                                                                                                                label250: {
                                                                                                                    label293: {
                                                                                                                        {
                                                                                                                            String s = this.blockAnimation.wo().getName();
                                                                                                                            switch (s) {
                                                                                                                                case "None":
                                                                                                                                    itemrenderer.transformFirstPersonItem(
                                                                                                                                        f, 0.0F
                                                                                                                                    );
                                                                                                                                    GlStateManager.scale(
                                                                                                                                        d1, d1, d1
                                                                                                                                    );
                                                                                                                                    itemrenderer.doBlockTransformations();
                                                                                                                                    break label274;
                                                                                                                                case "1.7":
                                                                                                                                    break label273;
                                                                                                                                case "1.7 Accurate":
                                                                                                                                    break label272;
                                                                                                                                case "Sunny":
                                                                                                                                    break label271;
                                                                                                                                case "Lucid":
                                                                                                                                    break label270;
                                                                                                                                case "Astro":
                                                                                                                                    break label269;
                                                                                                                                case "Tap":
                                                                                                                                    break label268;
                                                                                                                                case "Beta":
                                                                                                                                    break;
                                                                                                                                case "XIV":
                                                                                                                                    break label267;
                                                                                                                                case "Avatar":
                                                                                                                                    break label266;
                                                                                                                                case "Smooth":
                                                                                                                                    break label265;
                                                                                                                                case "Stab":
                                                                                                                                    break label264;
                                                                                                                                case "Spin":
                                                                                                                                    break label263;
                                                                                                                                case "Leaked":
                                                                                                                                    break label262;
                                                                                                                                case "Old":
                                                                                                                                    break label261;
                                                                                                                                case "Exhibition":
                                                                                                                                    break label260;
                                                                                                                                case "Exhibition Old":
                                                                                                                                    break label259;
                                                                                                                                case "Exhibition New":
                                                                                                                                    break label258;
                                                                                                                                case "Swong":
                                                                                                                                    break label257;
                                                                                                                                case "Stella":
                                                                                                                                    break label256;
                                                                                                                                case "Flup":
                                                                                                                                    break label255;
                                                                                                                                case "Noov":
                                                                                                                                    break label254;
                                                                                                                                case "Komorebi":
                                                                                                                                    break label253;
                                                                                                                                case "Rhys":
                                                                                                                                    break label252;
                                                                                                                                case "Swing":
                                                                                                                                    break label251;
                                                                                                                                case "?":
                                                                                                                                    break label250;
                                                                                                                                case "Dortware":
                                                                                                                                    break label293;
                                                                                                                                default:
                                                                                                                                    break label274;
                                                                                                                            }
                                                                                                                        }

                                                                                                                        GL11.glTranslatef(0.0F, 0.3F, 0.0F);
                                                                                                                        float f4 = MathHelper.sin(
                                                                                                                            f1 * f1 * (float) Math.PI
                                                                                                                        );
                                                                                                                        itemrenderer.transformFirstPersonItem(
                                                                                                                            itemrenderer.equippedProgress
                                                                                                                                * 0.5F,
                                                                                                                            0.0F
                                                                                                                        );
                                                                                                                        GlStateManager.scale(d1, d1, d1);
                                                                                                                        GlStateManager.rotate(
                                                                                                                            -f4 * 55.0F / 2.0F,
                                                                                                                            -8.0F,
                                                                                                                            -0.0F,
                                                                                                                            9.0F
                                                                                                                        );
                                                                                                                        GlStateManager.rotate(
                                                                                                                            -f4 * 45.0F, 1.0F, f4 / 2.0F, -0.0F
                                                                                                                        );
                                                                                                                        itemrenderer.doBlockTransformations();
                                                                                                                        GL11.glTranslated(1.2, 0.3, 0.5);
                                                                                                                        GL11.glTranslatef(
                                                                                                                            -1.0F,
                                                                                                                            aEg.thePlayer.isSneaking()
                                                                                                                                ? -0.1F
                                                                                                                                : -0.2F,
                                                                                                                            0.2F
                                                                                                                        );
                                                                                                                        break label274;
                                                                                                                    }

                                                                                                                    float f12 = MathHelper.sin(
                                                                                                                        (float)(f1 * f1 * Math.PI - 3.0)
                                                                                                                    );
                                                                                                                    float f13 = MathHelper.sin(
                                                                                                                        (float)(
                                                                                                                            MathHelper.sqrt_float(f1) * Math.PI
                                                                                                                        )
                                                                                                                    );
                                                                                                                    itemrenderer.transformFirstPersonItem(
                                                                                                                        f, 1.0F
                                                                                                                    );
                                                                                                                    GlStateManager.rotate(
                                                                                                                        -f13 * 10.0F, 0.0F, 15.0F, 200.0F
                                                                                                                    );
                                                                                                                    GlStateManager.rotate(
                                                                                                                        -f13 * 10.0F, 300.0F, f13 / 2.0F, 1.0F
                                                                                                                    );
                                                                                                                    itemrenderer.doBlockTransformations();
                                                                                                                    GL11.glTranslated(2.4, 0.3, 0.5);
                                                                                                                    GL11.glTranslatef(-2.1F, -0.2F, 0.1F);
                                                                                                                    GlStateManager.rotate(
                                                                                                                        f12 * 13.0F, -10.0F, -1.4F, -10.0F
                                                                                                                    );
                                                                                                                    break label274;
                                                                                                                }

                                                                                                                itemrenderer.transformFirstPersonItem(f, f1);
                                                                                                                GlStateManager.scale(d1, d1, d1);
                                                                                                                GL11.glTranslatef(-0.35F, 0.1F, 0.0F);
                                                                                                                GL11.glTranslatef(-0.05F, -0.1F, 0.1F);
                                                                                                                itemrenderer.doBlockTransformations();
                                                                                                                break label274;
                                                                                                            }

                                                                                                            itemrenderer.transformFirstPersonItem(f, f1);
                                                                                                            GlStateManager.scale(d1, d1, d1);
                                                                                                            itemrenderer.doBlockTransformations();
                                                                                                            GlStateManager.translate(-0.3F, -0.1F, -0.0F);
                                                                                                            break label274;
                                                                                                        }

                                                                                                        GlStateManager.translate(0.41F, -0.25F, -0.5555557F);
                                                                                                        GlStateManager.translate(0.0F, 0.0F, 0.0F);
                                                                                                        GlStateManager.rotate(35.0F, 0.0F, 1.5F, 0.0F);
                                                                                                        float f11 = MathHelper.sin(
                                                                                                            f1 * f1 / 64.0F * (float)ConstantManager.aHb
                                                                                                        );
                                                                                                        GlStateManager.rotate(f11 * -5.0F, 0.0F, 0.0F, 0.0F);
                                                                                                        GlStateManager.rotate(f2 * -12.0F, 0.0F, 0.0F, 1.0F);
                                                                                                        GlStateManager.rotate(f2 * -65.0F, 1.0F, 0.0F, 0.0F);
                                                                                                        GlStateManager.scale(d1, d1, d1);
                                                                                                        itemrenderer.doBlockTransformations();
                                                                                                        break label274;
                                                                                                    }

                                                                                                    itemrenderer.transformFirstPersonItem(
                                                                                                        -0.25F, 1.0F + f2 / 10.0F
                                                                                                    );
                                                                                                    GlStateManager.scale(d1, d1, d1);
                                                                                                    GL11.glRotated(-f2 * 25.0F, 1.0, 0.0, 0.0);
                                                                                                    itemrenderer.doBlockTransformations();
                                                                                                    break label274;
                                                                                                }

                                                                                                itemrenderer.transformFirstPersonItem(f / 1.5F, 0.0F);
                                                                                                GlStateManager.scale(d1, d1, d1);
                                                                                                itemrenderer.doBlockTransformations();
                                                                                                GlStateManager.translate(-0.05F, 0.3F, 0.3F);
                                                                                                GlStateManager.rotate(-f2 * 140.0F, 8.0F, 0.0F, 8.0F);
                                                                                                GlStateManager.rotate(f2 * ConstantManager.J, 8.0F, 0.0F, 8.0F);
                                                                                                break label274;
                                                                                            }

                                                                                            GlStateManager.translate(0.0F, 0.1F, -0.05F);
                                                                                            itemrenderer.transformFirstPersonItem(f, 0.0F);
                                                                                            GlStateManager.scale(d1, d1, d1);
                                                                                            itemrenderer.doBlockTransformations();
                                                                                            GlStateManager.translate(-0.05F, 0.2F, 0.0F);
                                                                                            GlStateManager.rotate(-f2 * 70.0F / 2.0F, -8.0F, -0.0F, 9.0F);
                                                                                            GlStateManager.rotate(-f2 * 70.0F, 1.0F, -0.4F, -0.0F);
                                                                                            break label274;
                                                                                        }

                                                                                        itemrenderer.transformFirstPersonItem(f, f1);
                                                                                        GlStateManager.scale(d1, d1, d1);
                                                                                        GlStateManager.translate(-0.5F, 0.4F, -0.2F);
                                                                                        GlStateManager.rotate(30.0F, 0.0F, 1.0F, 0.0F);
                                                                                        GlStateManager.rotate(-70.0F, 1.0F, 0.0F, 0.0F);
                                                                                        GlStateManager.rotate(40.0F, 0.0F, 1.0F, 0.0F);
                                                                                        break label274;
                                                                                    }

                                                                                    GlStateManager.translate(0.0F, 0.1F, -0.05F);
                                                                                    itemrenderer.transformFirstPersonItem(f / 2.0F, f1);
                                                                                    GlStateManager.scale(d1, d1, d1);
                                                                                    GlStateManager.rotate(f2 * 30.0F, -f2, -0.0F, 9.0F);
                                                                                    GlStateManager.rotate(f2 * 40.0F, 1.0F, -f2, -0.0F);
                                                                                    itemrenderer.doBlockTransformations();
                                                                                    break label274;
                                                                                }

                                                                                GlStateManager.translate(0.0F, -0.04F, -0.01F);
                                                                                itemrenderer.transformFirstPersonItem(f / 2.0F, 0.0F);
                                                                                GlStateManager.scale(d1, d1, d1);
                                                                                GlStateManager.translate(0.0F, 0.3F, -0.0F);
                                                                                GlStateManager.rotate(-f2 * 30.0F, 1.0F, 0.0F, 2.0F);
                                                                                GlStateManager.rotate(-f2 * 44.0F, 1.5F, f2 / 1.2F, 0.0F);
                                                                                itemrenderer.doBlockTransformations();
                                                                                break label274;
                                                                            }

                                                                            GlStateManager.translate(0.0F, -0.05F, 0.0F);
                                                                            GlStateManager.translate(-0.04F, 0.13F, 0.0F);
                                                                            itemrenderer.transformFirstPersonItem(f / 2.5F, 0.0F);
                                                                            GlStateManager.scale(d1, d1, d1);
                                                                            GlStateManager.rotate(-f2 * 40.0F / 2.0F, f2 / 2.0F, 1.0F, 4.0F);
                                                                            GlStateManager.rotate(-f2 * 30.0F, 1.0F, f2 / 3.0F, -0.0F);
                                                                            itemrenderer.doBlockTransformations();
                                                                            break label274;
                                                                        }

                                                                        GlStateManager.translate(0.0F, -0.05F, -0.0F);
                                                                        itemrenderer.transformFirstPersonItem(f / 2.0F, 0.0F);
                                                                        GlStateManager.scale(d1, d1, d1);
                                                                        GlStateManager.translate(0.0F, 0.3F, -0.0F);
                                                                        GlStateManager.rotate(-f2 * 31.0F, 1.0F, 0.0F, 2.0F);
                                                                        GlStateManager.rotate(-f2 * 33.0F, 1.5F, f2 / 1.1F, 0.0F);
                                                                        itemrenderer.doBlockTransformations();
                                                                        break label274;
                                                                    }

                                                                    GlStateManager.translate(0.0F, 0.1F, 0.0F);
                                                                    itemrenderer.transformFirstPersonItem(f / 2.0F - 0.2F, f1);
                                                                    GlStateManager.scale(d1, d1, d1);
                                                                    itemrenderer.doBlockTransformations();
                                                                    break label274;
                                                                }

                                                                GlStateManager.translate(0.0F, -0.03F, -0.13F);
                                                                itemrenderer.transformFirstPersonItem(f / 3.0F, 0.0F);
                                                                GlStateManager.scale(d1, d1, d1);
                                                                GlStateManager.translate(0.0F, 0.1F, 0.0F);
                                                                itemrenderer.doBlockTransformations();
                                                                GlStateManager.rotate(f2 * 20.0F / 2.0F, 0.0F, 1.0F, 1.5F);
                                                                GlStateManager.rotate(-f2 * 200.0F / 4.0F, 1.0F, 0.9F, 0.0F);
                                                                break label274;
                                                            }

                                                            itemrenderer.transformFirstPersonItem(f, 0.0F);
                                                            GlStateManager.scale(d1, d1, d1);
                                                            GlStateManager.translate(0.0F, 0.2F, -1.0F);
                                                            GlStateManager.rotate(-59.0F, -1.0F, 0.0F, 3.0F);
                                                            GlStateManager.rotate((float)(-(System.currentTimeMillis() / 2L % 360L)), 1.0F, 0.0F, 0.0F);
                                                            GlStateManager.rotate(60.0F, 0.0F, 1.0F, 0.0F);
                                                            break label274;
                                                        }

                                                        float f10 = MathHelper.sin(MathHelper.sqrt_float(f1) * (float)ConstantManager.aHb);
                                                        GlStateManager.translate(0.6F, 0.3F, -0.6F + -f10 * 0.7);
                                                        GlStateManager.rotate(6090.0F, 0.0F, 0.0F, 0.1F);
                                                        GlStateManager.rotate(6085.0F, 0.0F, 0.1F, 0.0F);
                                                        GlStateManager.rotate(6110.0F, 0.1F, 0.0F, 0.0F);
                                                        itemrenderer.transformFirstPersonItem(0.0F, 0.0F);
                                                        GlStateManager.scale(d1, d1, d1);
                                                        itemrenderer.doBlockTransformations();
                                                        break label274;
                                                    }

                                                    itemrenderer.transformFirstPersonItem(f, 0.0F);
                                                    GlStateManager.scale(d1, d1, d1);
                                                    float f9 = -f2 * 2.0F;
                                                    GlStateManager.translate(0.0F, f9 / 10.0F + 0.1F, 0.0F);
                                                    GlStateManager.rotate(f9 * 10.0F, 0.0F, 1.0F, 0.0F);
                                                    GlStateManager.rotate(250.0F, 0.2F, 1.0F, -0.6F);
                                                    GlStateManager.rotate(-10.0F, 1.0F, 0.5F, 1.0F);
                                                    GlStateManager.rotate(-f9 * 20.0F, 1.0F, 0.5F, 1.0F);
                                                    break label274;
                                                }

                                                GlStateManager.translate(0.56F, -0.52F, -0.71999997F);
                                                GlStateManager.rotate(45.0F, 0.0F, 1.0F, 0.0F);
                                                float f7 = MathHelper.sin(f1 * f1 * (float) Math.PI);
                                                float f8 = MathHelper.sin(MathHelper.sqrt_float(f1) * (float) Math.PI);
                                                GlStateManager.rotate(f7 * -20.0F, 0.0F, 1.0F, 0.0F);
                                                GlStateManager.rotate(f8 * -20.0F, 0.0F, 0.0F, 1.0F);
                                                GlStateManager.rotate(f8 * -40.0F, 1.0F, 0.0F, 0.0F);
                                                itemrenderer.transformFirstPersonItem(-0.1F, -0.1F);
                                                GlStateManager.scale(0.4F, 0.4F, 0.4F);
                                                GlStateManager.scale(d1, d1, d1);
                                                itemrenderer.doBlockTransformations();
                                                break label274;
                                            }

                                            GL11.glTranslatef(0.0F, 0.0F, 0.0F);
                                            itemrenderer.transformFirstPersonItem(f, f1);
                                            GlStateManager.scale(d1, d1, d1);
                                            itemrenderer.doBlockTransformations();
                                            float f5 = MathHelper.sin(f1 * f1 * (float) Math.PI);
                                            float f6 = MathHelper.sin(MathHelper.sqrt_float(f1) * (float) Math.PI);
                                            GlStateManager.rotate(-f5 * 20.0F, 0.0F, 1.0F, 0.0F);
                                            GlStateManager.rotate(-f6 * 20.0F, 0.0F, 0.0F, 1.0F);
                                            GlStateManager.rotate(-f6 * 80.0F, 1.0F, 0.0F, 0.0F);
                                            break label274;
                                        }

                                        GL11.glTranslatef(0.0F, 0.3F, 0.0F);
                                        float f3 = f1 * 0.8F - f1 * f1 * 0.8F;
                                        GlStateManager.scale(d1, d1, d1);
                                        GlStateManager.translate(0.56F, -0.52F, -0.71999997F);
                                        GlStateManager.rotate(45.0F, 0.0F, 1.0F, 0.0F);
                                        GlStateManager.rotate(f3 * -90.0F, 0.0F, 1.0F, 0.0F);
                                        GlStateManager.scale(0.37F, 0.37F, 0.37F);
                                        itemrenderer.doBlockTransformations();
                                        break label274;
                                    }

                                    GlStateManager.translate(0.0F, 0.03F, -0.05F);
                                    itemrenderer.transformFirstPersonItem(f / 2.0F, f1);
                                    GlStateManager.scale(d1, d1, d1);
                                    GlStateManager.rotate(f2 * 30.0F / 2.0F, -f2, -0.0F, 9.0F);
                                    GlStateManager.rotate(f2 * 40.0F, 1.0F, -f2 / 2.0F, -0.0F);
                                    itemrenderer.doBlockTransformations();
                                    break label274;
                                }

                                itemrenderer.transformFirstPersonItem(f - 0.1F, f1);
                                GlStateManager.scale(d1, d1, d1);
                                itemrenderer.doBlockTransformations();
                                break label274;
                            }

                            double d2 = d1 * 0.99;
                            GlStateManager.translate(0.05F, -0.05F, -0.12F);
                            itemrenderer.transformFirstPersonItem(f + 0.15F, f1);
                            GlStateManager.scale(d2, d2, d2);
                            itemrenderer.doBlockTransformations();
                            GlStateManager.translate(-0.5F, 0.2F, 0.0F);
                            break label274;
                        }

                        itemrenderer.transformFirstPersonItem(f, f1);
                        GlStateManager.scale(d1, d1, d1);
                        itemrenderer.doBlockTransformations();
                        itemrenderer.JX();
                        break label274;
                    }

                    itemrenderer.transformFirstPersonItem(f, f1);
                    GlStateManager.scale(d1, d1, d1);
                    itemrenderer.doBlockTransformations();
                }

                var1.setCancelled();
            } else if (!var1.db()) {
                label160: {
                    label159: {
                        label158: {
                            label157: {
                                {
                                    String s1 = this.swingAnimation.wo().getName();
                                    switch (s1) {
                                        case "None":
                                            itemrenderer.doItemUsedTransformations(f1);
                                            itemrenderer.transformFirstPersonItem(f, f1);
                                            if (!this.updatePositionOnlyWhenBlocking.wo()) {
                                                GlStateManager.scale(d1, d1, d1);
                                            }
                                            break label160;
                                        case "Punch":
                                            break;
                                        case "1.9+":
                                            break label157;
                                        case "Shove":
                                            break label158;
                                        case "Smooth":
                                            break label159;
                                        default:
                                            break label160;
                                    }
                                }

                                itemrenderer.transformFirstPersonItem(f, f1);
                                itemrenderer.doItemUsedTransformations(f1);
                                if (!this.updatePositionOnlyWhenBlocking.wo()) {
                                    GlStateManager.scale(d1, d1, d1);
                                }
                                break label160;
                            }

                            itemrenderer.doItemUsedTransformations(f1);
                            itemrenderer.transformFirstPersonItem(f, f1);
                            if (!this.updatePositionOnlyWhenBlocking.wo()) {
                                GlStateManager.scale(d1, d1, d1);
                            }
                            break label160;
                        }

                        itemrenderer.transformFirstPersonItem(f, f);
                        itemrenderer.doItemUsedTransformations(f1);
                        if (!this.updatePositionOnlyWhenBlocking.wo()) {
                            GlStateManager.scale(d1, d1, d1);
                        }
                        break label160;
                    }

                    itemrenderer.transformFirstPersonItem(f, f1);
                    itemrenderer.doItemUsedTransformations(f);
                    if (!this.updatePositionOnlyWhenBlocking.wo()) {
                        GlStateManager.scale(d1, d1, d1);
                    }
                }

                var1.setCancelled();
            }
        }
    };
    @EventLink
    public final Listener<SwingAnimationEvent> onSwingAnimation = var1 -> {
        int i = var1.dK();
        TerrainSpeed terrainspeed = this.e(TerrainSpeed.class);
        if (terrainspeed.isEnabled() && "Bloxd".equals(terrainspeed.mode.wo().getName())) {
            i = (int)(i * 1.5F);
        }

        int j = (int)(i * (-this.swingSpeed.wo().floatValue() / 100.0F + 1.0F));
        var1.setAnimationEnd(j);
    };

    public Animations() {
    }
}
