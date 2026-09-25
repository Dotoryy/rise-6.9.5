package com.alan.clients.module.impl.render;

import com.alan.clients.module.Module;
import com.alan.clients.module.api.Category;
import com.alan.clients.module.api.ModuleInfo;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.other.TickEvent;
import com.alan.clients.newevent.impl.render.RenderHungerEvent;
import com.alan.clients.value.impl.NumberValue;
import com.alan.clients.module.impl.render.appleskin.FoodBarOffset;
import com.alan.clients.module.impl.render.appleskin.FoodHelper;
import com.alan.clients.module.impl.render.appleskin.FoodValues;
import java.util.Random;
import java.util.Vector;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.FoodStats;

@ModuleInfo(aliases = "module.render.appleskin.name", description = "module.render.appleskin.description", category = Category.RENDER)
public class AppleSkin extends Module {
    private final NumberValue maxFlashAlpha = new NumberValue("Max Flash Alpha", this, 0.65, 0, 1, 0.05);
    public final Vector<FoodBarOffset> foodBarOffsets = new Vector<>();
    private final Random random = new Random();
    private float flashProgress;
    private float flashAlpha;
    private byte alphaDir = 1;
    @EventLink
    public final Listener<RenderHungerEvent> onHunger = var1 -> {
        ScaledResolution scaledresolution = var1.getScaledResolution();
        FoodStats foodstats = aEg.thePlayer.getFoodStats();
        int i = scaledresolution.getScaledWidth() / 2 + 91;
        int j = scaledresolution.getScaledHeight() - 39;
        this.generateHungerBarOffsets(i, 0, aEg.ingameGUI.updateCounter);
        this.drawSaturationOverlay(0.0F, foodstats.getSaturationLevel(), 0, foodstats.getFoodLevel(), i, j, 1.0F);
        ItemStack itemstack = aEg.thePlayer.getHeldItem();
        boolean flag = itemstack != null && itemstack.getItem() instanceof ItemFood;
        if (!flag) {
            this.resetFlash();
            return;
        }

        FoodValues yc = FoodHelper.getFoodValues(itemstack);
        int k = yc.hunger;
        float f = yc.getSaturationIncrement();
        int l = foodstats.getFoodLevel() + k;
        float f1 = foodstats.getSaturationLevel() + f > l ? l - foodstats.getSaturationLevel() : f;
        this.drawHungerOverlay(k, foodstats.getFoodLevel(), i, j, this.flashAlpha, FoodHelper.isRotten(itemstack));
        this.drawSaturationOverlay(f1, foodstats.getSaturationLevel(), k, foodstats.getFoodLevel(), i, j, this.flashAlpha);
    };
    @EventLink
    public final Listener<TickEvent> onTick = var1 -> {
        this.flashProgress = this.flashProgress + this.alphaDir * 0.125F;
        if (this.flashProgress >= 1.5F) {
            this.alphaDir = -1;
        } else if (this.flashProgress <= -0.5F) {
            this.alphaDir = 1;
        }

        this.flashAlpha = Math.max(0.0F, Math.min(1.0F, this.flashProgress)) * Math.min(1.0F, this.maxFlashAlpha.wo().floatValue());
    };

    public AppleSkin() {
    }

    private void generateHungerBarOffsets(int var1, int var2, int var3) {
        this.random.setSeed(var3 * 312871L);
        FoodStats foodstats = aEg.thePlayer.getFoodStats();
        float f = foodstats.getSaturationLevel();
        int i = foodstats.getFoodLevel();
        boolean flag = f <= 0.0F && aEg.ingameGUI.updateCounter % (i * 3 + 1) == 0;
        if (this.foodBarOffsets.size() != 10) {
            this.foodBarOffsets.setSize(10);
        }

        for (int j = 0; j < 10; j++) {
            int k = var1 - j * 8 - 9;
            int l = var2;
            if (flag) {
                l += this.random.nextInt(3) - 1;
            }

            FoodBarOffset wf = this.foodBarOffsets.get(j);
            if (wf == null) {
                wf = new FoodBarOffset();
                this.foodBarOffsets.set(j, wf);
            }

            wf.offsetX = k - var1;
            wf.offsetY = l;
        }
    }

    private void drawSaturationOverlay(float var1, float var2, int var3, int var4, int var5, int var6, float var7) {
        if (!(var2 + var1 < 0.0F)) {
            GlStateManager.enableBlend();
            GlStateManager.color(1.0F, 1.0F, 1.0F, var7);
            GlStateManager.blendFunc(770, 771);
            float f = Math.max(0.0F, Math.min(20.0F, var2 + var1));
            int i = Math.max(0, Math.min(20, var4 + var3));
            int j = 0;
            int k = (int)Math.ceil(f / 2.0F);
            if (var1 != 0.0F) {
                j = (int)Math.max(var2 / 2.0F, 0.0F);
            }

            for (int l = j; l < k; l++) {
                FoodBarOffset wf = this.foodBarOffsets.get(l);
                if (wf != null) {
                    int i1 = var5 + wf.offsetX;
                    int j1 = var6 + wf.offsetY;
                    byte b0 = 52;
                    byte b1 = 25;

                    for (PotionEffect potioneffect : aEg.thePlayer.getActivePotionEffects()) {
                        if (potioneffect.getPotionID() == Potion.hunger.getId()) {
                            b0 += 36;
                            break;
                        }
                    }

                    int k1 = i1;
                    byte b2 = 9;
                    if (l * 2 + 1 == (int)f) {
                        k1 += 4;
                        b1 += 4;
                        b2 -= 4;
                    }

                    if (l * 2 + 1 == i) {
                        b0 += 9;
                    }

                    GlStateManager.color(0.75F, 0.65F, 0.0F, var7);
                    aEg.ingameGUI.drawTexturedModalRect(k1, j1, b1, 27, b2, 9);
                    if (!(f > i)) {
                        GlStateManager.color(1.0F, 1.0F, 1.0F, var7);
                        aEg.ingameGUI.drawTexturedModalRect(i1, j1, b0, 27, 9, 9);
                    }
                }
            }

            GlStateManager.disableBlend();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        }
    }

    private void drawHungerOverlay(int var1, int var2, int var3, int var4, float var5, boolean var6) {
        if (var1 > 0) {
            GlStateManager.enableBlend();
            GlStateManager.color(1.0F, 1.0F, 1.0F, var5);
            GlStateManager.blendFunc(770, 771);
            int i = Math.max(0, Math.min(20, var2 + var1));
            int j = Math.max(0, var2 / 2);
            int k = (int)Math.ceil(i / 2.0F);

            for (int l = j; l < k; l++) {
                FoodBarOffset wf = this.foodBarOffsets.get(l);
                if (wf != null) {
                    int i1 = var3 + wf.offsetX;
                    int j1 = var4 + wf.offsetY;
                    byte b0 = 52;
                    byte b1 = 25;
                    if (var6) {
                        b0 += 36;
                        b1 += 108;
                    }

                    if (l * 2 + 1 == i) {
                        b0 += 9;
                    }

                    GlStateManager.color(1.0F, 1.0F, 1.0F, var5 * 0.25F);
                    aEg.ingameGUI.drawTexturedModalRect(i1, j1, b1, 27, 9, 9);
                    GlStateManager.color(1.0F, 1.0F, 1.0F, var5);
                    aEg.ingameGUI.drawTexturedModalRect(i1, j1, b0, 27, 9, 9);
                }
            }

            GlStateManager.disableBlend();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        }
    }

    private void resetFlash() {
        this.flashProgress = 0.0F;
        this.flashAlpha = 0.0F;
        this.alphaDir = 1;
    }
}
