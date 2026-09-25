package com.alan.clients.module.impl.render;

import com.alan.clients.Client;
import com.alan.clients.module.Module;
import com.alan.clients.module.api.Category;
import com.alan.clients.module.api.ModuleInfo;
import com.alan.clients.module.impl.combat.KillAura;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.render.Render2DEvent;
import com.alan.clients.util.render.RenderUtil;
import com.alan.clients.util.vector.Vector2d;
import com.alan.clients.value.impl.BooleanValue;
import com.alan.clients.value.impl.DragValue;
import com.alan.clients.ui.theme.Themes;
import com.alan.clients.util.font.Font;
import com.alan.clients.util.math.MathUtil;
import com.alan.clients.util.font.FontManager;
import com.alan.clients.util.font.FontWeight;
import com.alan.clients.util.shader.ShaderQueueType;
import java.awt.Color;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import rip.vantage.commons.util.time.StopWatch;

@ModuleInfo(
    aliases = "module.render.hotbar.name",
    description = "module.render.hotbar.description",
    category = Category.RENDER,
    autoEnabled = true,
    allowDisable = false
)
public class HotBar extends Module {
    private final DragValue structure = new DragValue("", this, new Vector2d(200.0, 200.0), false, true);
    private static final ResourceLocation WIDGETS_TEXTURE = new ResourceLocation("textures/gui/widgets.png");
    private final StopWatch stopwatch = new StopWatch();
    private float rPosX;
    private float xpProgress = 0.0F;
    private Interface interfaceModule;
    private KillAura killAura;
    public final BooleanValue showXPBar = new BooleanValue("Show XP Bar", this, true);
    private final Font levelFont = FontManager.MAIN.a(18, FontWeight.MEDIUM);
    @EventLink
    public final Listener<Render2DEvent> onPreMotionEvent = var1 -> {
        if (aEg.getRenderViewEntity() instanceof EntityPlayer) {
            ScaledResolution scaledresolution = var1.getScaledResolution();
            EntityPlayer entityplayer = (EntityPlayer)aEg.getRenderViewEntity();
            this.structure.apP = new Vector2d(
                (int)(scaledresolution.getScaledWidth() / 2.0F - 92.0F), scaledresolution.getScaledHeight() - 21 - Client.a.getThemeManager().getTheme().qd() - 17.5
            );
            this.structure.aHe = new Vector2d(182.0, 40.0);
            if (this.interfaceModule == null) {
                this.interfaceModule = this.e(Interface.class);
            }

            String s = this.interfaceModule.getInformationType().wo().getName();
            int i = scaledresolution.getScaledWidth() / 2;
            this.rPosX = MathUtil.lerp(this.rPosX, i - 92 + entityplayer.inventory.currentItem * 20, 0.03F * (float)this.stopwatch.getElapsedTime());
            this.stopwatch.aX();
            if ("Rise".equals(s)) {
                double d0 = this.interfaceModule != null ? this.interfaceModule.getRoundingRadius() : 9.0;
                this.b(ShaderQueueType.BLUR)
                    .c(
                        () -> RenderUtil.roundedRectangle(
                            this.structure.apP.x + 1.0, this.structure.apP.y + 18.0, this.structure.aHe.x - 0.0, this.structure.aHe.y - 18.0, d0, Color.BLACK
                        )
                    );
            }

            if (this.showXPBar.wo() && entityplayer.experience > 0.0F && "Rise".equals(s)) {
                double d1 = this.interfaceModule != null ? Math.min(this.interfaceModule.getRoundingRadius(), 2.0) : 2.0;
                this.b(ShaderQueueType.BLUR)
                    .c(
                        () -> RenderUtil.roundedRectangle(
                            this.structure.apP.x + 1.0, this.structure.apP.y + 14.0, this.structure.aHe.x - 0.0, 3.0, d1, Color.BLACK
                        )
                    );
            }

            this.b(ShaderQueueType.REGULAR, 1).c(() -> {
                GlStateManager.enableRescaleNormal();
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                RenderHelper.enableGUIStandardItemLighting();
                if (this.showXPBar.wo() && entityplayer.experience > 0.0F) {
                    this.renderXPBar(scaledresolution, entityplayer);
                }

                Client.a.getThemeManager().getTheme();
                this.renderHotBar(Themes.rK());
                if ("Rise".equals(s)) {
                    double d2 = this.rPosX + 1.0F;
                    double d3 = scaledresolution.getScaledHeight() - 26;
                    this.rz();
                    RenderUtil.roundedRectangle(d2, d3, 22.0, 22.0, 8.0, Themes.rK());
                } else if ("Traditional".equals(s)) {
                    aEg.getTextureManager().bindTexture(WIDGETS_TEXTURE);
                    GlStateManager.enableAlpha();
                    RenderUtil.color(Color.WHITE);
                    aEg.ingameGUI.drawTexturedModalRect(this.rPosX + 1.0F, (int)(this.structure.apP.y + 17.0), 0, 22, 24, 24);
                }

                for (int j = 0; j < 9; j++) {
                    int k = scaledresolution.getScaledWidth() / 2 - 90 + j * 20 + 2;
                    int l = scaledresolution.getScaledHeight() - 16 - 3 - 4;
                    this.renderHotBarItem(j, k, l, var1.getPartialTicks(), entityplayer);
                }

                RenderHelper.disableStandardItemLighting();
                GlStateManager.disableRescaleNormal();
                GlStateManager.disableBlend();
            });
            if ("Rise".equals(s)) {
                this.b(ShaderQueueType.BLOOM)
                    .c(
                        () -> {
                            double d0x = this.interfaceModule != null ? this.interfaceModule.getRoundingRadius() + 1.0 : 10.0;
                            RenderUtil.roundedRectangle(
                                this.structure.apP.x + 1.0,
                                this.structure.apP.y + 18.0,
                                this.structure.aHe.x - 0.0,
                                this.structure.aHe.y - 18.0,
                                d0x,
                                this.rz().rE()
                            );
                        }
                    );
            }

            if ("Rise".equals(s) && this.showXPBar.wo() && entityplayer.experienceLevel > 0) {
                this.b(ShaderQueueType.REGULAR, 1).c(() -> {
                    String s1 = String.valueOf(entityplayer.experienceLevel);
                    float f = this.levelFont.getStringWidth(s1);
                    float f1 = (scaledresolution.getScaledWidth() - f) / 2.0F;
                    float f2 = (float)(this.structure.apP.y - this.levelFont.height() + 17.0);
                    GlStateManager.enableBlend();
                    this.levelFont.b(s1, f1, f2, Color.WHITE.getRGB());
                    GlStateManager.disableBlend();
                });
                this.b(ShaderQueueType.BLOOM).c(() -> {
                    String s1 = String.valueOf(entityplayer.experienceLevel);
                    float f = this.levelFont.getStringWidth(s1);
                    float f1 = (scaledresolution.getScaledWidth() - f) / 2.0F;
                    float f2 = (float)(this.structure.apP.y - this.levelFont.height() + 17.0);
                    GlStateManager.enableBlend();
                    this.levelFont.b(s1, f1, f2, this.rz().rD().getRGB());
                    GlStateManager.disableBlend();
                });
            }
        }
    };

    public HotBar() {
    }

    private void renderXPBar(ScaledResolution resolution, EntityPlayer player) {
        {
            String s = this.interfaceModule.getInformationType().wo().getName();
            switch (s) {
                case "Rise":
                    float f = player.experience;
                    if (f <= 0.0F) {
                        return;
                    }

                    this.xpProgress = MathUtil.lerp(this.xpProgress, f, 0.1F);
                    double d0 = this.structure.apP.x + 1.0;
                    double d1 = this.structure.apP.y + 14.4;
                    double d2 = this.structure.aHe.x - 1.0;
                    double d3 = d2 * this.xpProgress;
                    double d4 = 3.0;
                    double d5 = this.interfaceModule != null ? Math.min(this.interfaceModule.getRoundingRadius(), 2.0) : 2.0;
                    this.rz();
                    RenderUtil.roundedRectangle(d0, d1, d2, d4, d5, Themes.rK());
                    RenderUtil.roundedRectangle(d0, d1, d3, d4, d5, this.rz().rD());
                    return;
                case "Traditional":
                    break;
                default:
                    return;
            }
        }

        if (this.showXPBar.wo()) {
            float experience = player.experience;
            if (!(experience <= 0.0F)) {
                int i = (int)(experience * 182.0F);
                int j = resolution.getScaledWidth() / 2 - 91;
                float f2 = resolution.getScaledHeight() - 28.5F;
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                aEg.getTextureManager().bindTexture(Gui.icons);
                aEg.ingameGUI.drawTexturedModalRect(j, f2, 0, 64, 182, 5);
                aEg.ingameGUI.drawTexturedModalRect(j, f2, 0, 69, i, 5);
                String s1 = String.valueOf(player.experienceLevel);
                int k = aEg.fontRendererObj.getStringWidth(s1);
                int l = resolution.getScaledWidth() / 2 - k / 2;
                float f3 = f2 - 5.0F;
                aEg.fontRendererObj.b(s1, l, f3, 8388383);
            }
        }
    }

    private void renderHotBar(Color color) {
        {
            String s = this.interfaceModule.getInformationType().wo().getName();
            switch (s) {
                case "Rise":
                    double d0 = this.interfaceModule != null ? this.interfaceModule.getRoundingRadius() : 9.0;
                    RenderUtil.roundedRectangle(
                        this.structure.apP.x + 1.0, this.structure.apP.y + 18.0, this.structure.aHe.x - 0.0, this.structure.aHe.y - 18.0, d0, color
                    );
                    return;
                case "Traditional":
                    break;
                default:
                    return;
            }
        }

        RenderUtil.color(Color.WHITE);
        aEg.getTextureManager().bindTexture(WIDGETS_TEXTURE);
        GlStateManager.enableAlpha();
        aEg.ingameGUI.drawTexturedModalRect((float)(this.structure.apP.x + 1.0), (int)(this.structure.apP.y + 18.0), 0, 0, 182, 22);
    }

    private void renderHotBarItem(int var1, int var2, int var3, float var4, EntityPlayer player) {
        if (this.killAura == null) {
            this.killAura = this.e(KillAura.class);
        }

        if (this.killAura == null || !this.killAura.shouldHideSwordSlot(var1)) {
            ItemStack itemstack = player.inventory.mainInventory[var1];
            if (itemstack != null) {
                RenderItem renderitem = aEg.getRenderItem();
                float f = itemstack.animationsToGo - var4;
                if (f > 0.0F) {
                    GlStateManager.pushMatrix();
                    float f1 = 1.0F + f / 5.0F;
                    GlStateManager.translate(var2 + 8, var3 + 12, 0.0F);
                    GlStateManager.scale(1.0F / f1, (f1 + 1.0F) / 2.0F, 1.0F);
                    GlStateManager.translate(-(var2 + 8), -(var3 + 12), 0.0F);
                }

                renderitem.b(itemstack, var2, var3);
                if (f > 0.0F) {
                    GlStateManager.popMatrix();
                }

                renderitem.renderItemOverlays(aEg.fontRendererObj, itemstack, var2, var3);
            }
        }
    }
}
