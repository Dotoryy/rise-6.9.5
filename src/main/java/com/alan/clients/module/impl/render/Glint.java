package com.alan.clients.module.impl.render;

import com.alan.clients.module.Module;
import com.alan.clients.module.api.Category;
import com.alan.clients.module.api.ModuleInfo;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.render.GlintEvent;
import com.alan.clients.newevent.impl.render.Render3DEvent;
import com.alan.clients.value.impl.BooleanValue;
import com.alan.clients.value.impl.BoundsNumberValue;
import com.alan.clients.value.impl.NumberValue;
import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemSword;
import net.minecraft.src.a;
import net.optifine.ae;
import net.optifine.shaders.ah;
import net.optifine.shaders.aj;

@ModuleInfo(aliases = "module.render.glint.name", description = "module.render.glint.description", category = Category.RENDER)
public final class Glint extends Module {
    private final BooleanValue glintWeapons = new BooleanValue("Glint Weapons", this, true);
    private final BoundsNumberValue hue = new BoundsNumberValue("Hue", this, 0, 360, 0, 360, 1);
    private final NumberValue layers = new NumberValue("Layers", this, 4, 1, 8, 1);
    @EventLink
    public final Listener<GlintEvent> onGlint = var1 -> {
        Item item = var1.getItemStack().getItem();
        if (this.glintWeapons.wo() && (item instanceof ItemSword || item instanceof ItemAxe)) {
            var1.l(true);
        }

        var1.setCancelled();
        if (var1.dt() && var1.du()) {
            this.renderEffect(var1.getModel());
        }
    };
    @EventLink
    public final Listener<Render3DEvent> onRender3D = var0 -> {};

    public Glint() {
    }

    public void renderEffect(IBakedModel iBakedModel) {
        if (RendererLivingEntity.bWq) {
            return;
        }

        if ((!a.aoE() || ae.aAA()) && (!a.aor() || !ah.exs)) {
            GlStateManager.depthMask(false);
            GlStateManager.depthFunc(514);
            GlStateManager.disableLighting();
            GlStateManager.blendFunc(768, 1);
            aEg.getRenderItem().textureManager.bindTexture(RenderItem.RES_ITEM_GLINT);
            if (a.aor() && !aEg.getRenderItem().bUz) {
                aj.aGT();
            }

            GlStateManager.matrixMode(5890);
            GlStateManager.pushMatrix();
            GlStateManager.scale(8.0F, 8.0F, 8.0F);
            GlStateManager.translate((float)(Minecraft.getSystemTime() % 3000L) / 3000.0F / 8.0F, 0.0F, 0.0F);

            for (int i = 1; i <= this.layers.wo().intValue(); i++) {
                GlStateManager.rotate(-50.0F, 0.0F, 0.0F, 1.0F);
                aEg.getRenderItem()
                    .renderModel(
                        iBakedModel,
                        new Color(
                                Color.HSBtoRGB(
                                    (
                                            this.hue.wo().intValue()
                                                + Math.abs(this.hue.wA().intValue() - this.hue.wo().intValue()) * (i / this.layers.wo().floatValue())
                                        )
                                        / 255.0F,
                                    1.0F,
                                    1.0F
                                )
                            )
                            .hashCode()
                    );
            }

            GlStateManager.popMatrix();
            GlStateManager.matrixMode(5888);
            GlStateManager.blendFunc(770, 771);
            GlStateManager.enableLighting();
            GlStateManager.depthFunc(515);
            GlStateManager.depthMask(true);
            aEg.getRenderItem().textureManager.bindTexture(TextureMap.locationBlocksTexture);
            if (a.aor() && !aEg.getRenderItem().bUz) {
                aj.aGU();
            }
        }
    }
}
