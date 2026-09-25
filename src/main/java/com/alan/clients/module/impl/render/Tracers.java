package com.alan.clients.module.impl.render;

import com.alan.clients.Client;
import com.alan.clients.module.Module;
import com.alan.clients.module.api.Category;
import com.alan.clients.module.api.ModuleInfo;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.render.Render3DEvent;
import com.alan.clients.util.render.RenderUtil;
import com.alan.clients.util.render.ColorUtil;
import com.alan.clients.component.impl.combat.TargetComponent;
import java.awt.Color;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;

@ModuleInfo(aliases = "module.render.tracers.name", description = "module.render.tracers.description", category = Category.RENDER)
public final class Tracers extends Module {
    @EventLink
    public final Listener<Render3DEvent> onRender3D = var1 -> {
        if (!aEg.gameSettings.hideGUI) {
            GlStateManager.pushMatrix();
            GlStateManager.loadIdentity();
            aEg.entityRenderer.orientCamera(aEg.timer.bWm);

            for (Entity entity : TargetComponent.b(true, true, false, false, false)) {
                if (entity != aEg.thePlayer && !entity.isDead && !Client.a.getBotManager().a(entity)) {
                    double d0 = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * var1.getPartialTicks();
                    double d1 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * var1.getPartialTicks() + 1.62F;
                    double d2 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * var1.getPartialTicks();
                    Color color = ColorUtil.withAlpha(ColorUtil.a(this.rz().rB(), this.rz().rA(), Math.min(1.0F, aEg.thePlayer.getDistanceToEntity(entity) / 50.0F)), 128);
                    aEg.getRenderManager();
                    double d3 = RenderManager.bUO;
                    aEg.getRenderManager();
                    double d4 = RenderManager.bUP + aEg.thePlayer.getEyeHeight();
                    aEg.getRenderManager();
                    RenderUtil.drawLine(d3, d4, RenderManager.bUQ, d0, d1, d2, color, 1.5F);
                }
            }

            GlStateManager.resetColor();
            GlStateManager.popMatrix();
        }
    };

    public Tracers() {
    }
}
