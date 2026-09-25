package com.alan.clients.component.impl.player;

import com.alan.clients.component.Component;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.inventory.SyncCurrentItemEvent;
import com.alan.clients.newevent.impl.motion.PreUpdateEvent;
import com.alan.clients.newevent.impl.render.Render2DEvent;
import com.alan.clients.util.animation.Animation;
import com.alan.clients.util.animation.Easing;
import com.alan.clients.util.render.RenderUtil;
import com.alan.clients.ui.theme.Themes;
import com.alan.clients.util.font.Font;
import com.alan.clients.util.render.ColorUtil;
import com.alan.clients.util.font.FontManager;
import com.alan.clients.util.font.FontWeight;
import com.alan.clients.util.shader.ShaderQueueType;
import java.awt.Color;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public final class SlotComponent extends Component {
    private static final Animation animation = new Animation(Easing.EASE_OUT_EXPO, 900L);
    private static boolean fX = true;
    public static boolean fY = true;
    public static boolean dj;
    private static boolean fZ = false;
    private static ItemStack itemStack = null;
    private static boolean gb = false;
    private static String stackSize = "0";
    private static double gd = 38.0;
    private final Font ge = com.alan.clients.util.font.FontManager.MAIN.a(18, com.alan.clients.util.font.FontWeight.LIGHT);
    private final Font gf = com.alan.clients.util.font.FontManager.MAIN.a(18, com.alan.clients.util.font.FontWeight.MEDIUM);
    @EventLink(value = 4)
    public final Listener<SyncCurrentItemEvent> onSyncItem = var0 -> {
        InventoryPlayer inventoryplayer = aEg.thePlayer.inventory;
        var0.setSlot(inventoryplayer.cIU ? inventoryplayer.cIT : inventoryplayer.currentItem);
    };
    @EventLink(value = 4)
    public final Listener<PreUpdateEvent> onPreUpdate = var0 -> {
        InventoryPlayer inventoryplayer = aEg.thePlayer.inventory;
        inventoryplayer.cIU = false;
        inventoryplayer.cIT = inventoryplayer.currentItem;
    };
    @EventLink(value = 4)
    public final Listener<Render2DEvent> onRender2D = var1 -> {
        if (!fY && aEg.thePlayer != null && fX) {
            ItemStack itemstack = getItemStack();
            ItemStack itemstack1 = itemstack != null && aEg.thePlayer.inventory.cIU ? itemstack : null;
            boolean flag = itemstack1 != null
                && itemstack1.stackSize > 0
                && aEg.thePlayer.inventory.cIU
                && (
                    aEg.thePlayer.inventory.cIT != aEg.thePlayer.inventory.currentItem
                        || getItemStack() == null
                        || getItemStack() != null && getItemStack().getItem() instanceof ItemBlock
                )
                && aEg.currentScreen == null;
            if (flag != fZ) {
                animation.setValue(flag ? 1.1 : 1.0);
            }

            fZ = flag;
            if (flag && itemstack1 != null) {
                itemStack = itemstack1;
                gb = itemstack1.getItem() instanceof ItemBlock;
                stackSize = gb ? String.valueOf(bN()) : String.valueOf(itemstack1.stackSize);
                gd = this.a(gb, stackSize);
            }

            animation.setDuration(900L);
            animation.Q(flag ? 1.0 : 1.1);
            double d0 = animation.getValue();
            double d1 = 1.0 - 10.0 * Math.abs(1.0 - d0);
            double d2 = Math.max(0.0, Math.min(1.0, d1));
            boolean flag1 = flag ? itemstack1.getItem() instanceof ItemBlock : gb;
            String s = flag ? (flag1 ? String.valueOf(bN()) : String.valueOf(itemstack1.stackSize)) : stackSize;
            ItemStack itemstack2 = flag ? itemstack1 : itemStack;
            double d3 = flag ? this.a(flag1, s) : gd;
            ScaledResolution scaledresolution = var1.getScaledResolution();
            double d4 = scaledresolution.getScaledWidth() / 2.0 - d3 / 2.0;
            double d5 = scaledresolution.getScaledHeight() - 90;
            double d6 = d5 + 11.0 - this.ge.height() / 2.0 + 3.0;
            if (d2 > 0.01 && itemstack2 != null) {
                if (d2 > 0.15) {
                    this.b(com.alan.clients.util.shader.ShaderQueueType.BLUR).c(() -> {
                        GlStateManager.pushMatrix();
                        GlStateManager.translate((d4 + d3 * 0.5) * (1.0 - d0), (d5 + 11.0) * (1.0 - d0), 0.0);
                        GlStateManager.scale(d0, d0, d0);
                        RenderUtil.roundedRectangle(d4, d5, d3, 22.0, this.rz().getRound(), Color.BLACK);
                        GlStateManager.popMatrix();
                    });
                }

                double d7 = d2;
                this.b(com.alan.clients.util.shader.ShaderQueueType.REGULAR, 1).c(() -> {
                    GlStateManager.pushMatrix();
                    GlStateManager.translate((d4 + d3 * 0.5) * (1.0 - d0), (d5 + 11.0) * (1.0 - d0), 0.0);
                    GlStateManager.scale(d0, d0, d0);
                    double d9 = this.rz().getRound();
                    this.rz();
                    Color color = Themes.rK();
                    this.rz();
                    RenderUtil.roundedRectangle(d4, d5, d3, 22.0, d9, ColorUtil.withAlpha(color, (int)(Themes.rK().getAlpha() * d7)));
                    if (flag1) {
                        float f = this.ge.getStringWidth("Amount:") + 2;
                        this.ge.b("Amount:", d4 + 6.0 + 16.0, d6, ColorUtil.withAlpha(Color.WHITE, (int)(255.0 * d7)).getRGB());
                        this.gf.b(s, d4 + 6.0 + 16.0 + f, d6, ColorUtil.withAlpha(this.rz().rA(), (int)(255.0 * d7)).getRGB());
                    }

                    RenderUtil.a(d4 + 3.0, d5 + 3.0, ColorUtil.withAlpha(Color.WHITE, (int)(255.0 * d7)).getRGB(), itemstack2);
                    GlStateManager.popMatrix();
                });
                double d8 = d2;
                this.b(com.alan.clients.util.shader.ShaderQueueType.BLOOM)
                    .c(
                        () -> {
                            GlStateManager.pushMatrix();
                            GlStateManager.translate((d4 + d3 * 0.5) * (1.0 - d0), (d5 + 11.0) * (1.0 - d0), 0.0);
                            GlStateManager.scale(d0, d0, 0.0);
                            RenderUtil.roundedRectangle(
                                d4 + 0.5, d5 + 0.5, d3 - 1.0, 21.0, this.rz().getRound() + 1, ColorUtil.withAlpha(this.rz().rE(), (int)(this.rz().rE().getAlpha() * d8))
                            );
                            GlStateManager.popMatrix();
                        }
                    );
            }

            if (!flag && animation.isFinished() && d0 >= 1.099) {
                fY = true;
            }
        }
    };

    public SlotComponent() {
    }

    public static void setSlot(int slot) {
        b(slot, true);
    }

    public static void b(int var0, boolean var1) {
        if (var0 >= 0 && var0 < 9) {
            dj = true;
            InventoryPlayer inventoryplayer = aEg.thePlayer.inventory;
            inventoryplayer.cIT = var0;
            inventoryplayer.cIU = true;
            fX = var1;
            fY = false;
            animation.setValue(1.1);
            aEg.playerController.syncCurrentPlayItem();
            dj = false;
        }
    }

    public static void c(int var0, boolean var1) {
        a(var0, var1, true);
    }

    public static void a(int var0, boolean var1, boolean var2) {
        if (!(Math.random() * Math.random() > 0.25) && !var1) {
            b(var0, var2);
        } else {
            b(aEg.playerController.bCP, var2);
        }
    }

    private double a(boolean var1, String var2) {
        float f = this.ge.getStringWidth("Amount:") + 2;
        return 22.0F + (var1 ? f + this.gf.getStringWidth(var2) + 2.0F + 3.0F : 0.0F);
    }

    private static int bN() {
        if (aEg.thePlayer == null) {
            return 0;
        }

        int i = 0;
        InventoryPlayer inventoryplayer = aEg.thePlayer.inventory;

        for (int j = 0; j < 9; j++) {
            ItemStack itemstack = inventoryplayer.mainInventory[j];
            if (itemstack != null && itemstack.getItem() instanceof ItemBlock) {
                i += itemstack.stackSize;
            }
        }

        return i;
    }

    public static ItemStack getItemStack() {
        return aEg.thePlayer != null && aEg.thePlayer.inventoryContainer != null ? aEg.thePlayer.inventoryContainer.getSlot(bQ() + 36).getStack() : null;
    }

    public static Item bP() {
        ItemStack itemstack = getItemStack();
        return itemstack == null ? null : itemstack.getItem();
    }

    public static int bQ() {
        InventoryPlayer inventoryplayer = aEg.thePlayer.inventory;
        return inventoryplayer.cIU ? inventoryplayer.cIT : inventoryplayer.currentItem;
    }
}
