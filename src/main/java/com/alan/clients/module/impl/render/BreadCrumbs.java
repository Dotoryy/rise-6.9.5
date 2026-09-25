package com.alan.clients.module.impl.render;

import com.alan.clients.module.Module;
import com.alan.clients.module.api.Category;
import com.alan.clients.module.api.ModuleInfo;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.newevent.impl.render.Render3DEvent;
import com.alan.clients.value.impl.NumberValue;
import com.alan.clients.module.impl.render.breadcrumbs.BreadCrumb;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.GL11;

@ModuleInfo(aliases = "module.render.breadcrumbs.name", description = "module.render.breadcrumbs.description", category = Category.RENDER)
public class BreadCrumbs extends Module {
    private final List<BreadCrumb> crumbs = new ArrayList<>();
    private final NumberValue time = new NumberValue("Time", this, 15, 1, 150, 0.1);
    @EventLink
    public final Listener<PreMotionEvent> onPreMotion = var1 -> {
        if (aEg.thePlayer.lastTickPosX != aEg.thePlayer.posX
            || aEg.thePlayer.lastTickPosY != aEg.thePlayer.posY
            || aEg.thePlayer.lastTickPosZ != aEg.thePlayer.posZ) {
            this.crumbs.add(new BreadCrumb(new Vec3(aEg.thePlayer.posX, aEg.thePlayer.posY, aEg.thePlayer.posZ), System.currentTimeMillis()));
        }

        long i = System.currentTimeMillis();
        this.crumbs.removeIf(var3 -> i - var3.creationTime > this.time.wo().intValue() * 100L);
    };
    @EventLink
    public final Listener<Render3DEvent> onRender3D = var1 -> this.renderCrumbs(this.crumbs);

    public BreadCrumbs() {
    }

    @Override
    public void onEnable() {
        this.crumbs.clear();
    }

    public void renderCrumbs(List<BreadCrumb> var1) {
        GlStateManager.pushMatrix();
        GlStateManager.disableDepth();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.blendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glLineWidth(2.0F);
        long now = System.currentTimeMillis();
        GL11.glBegin(3);

        for (int j = 0; j < var1.size(); j++) {
            BreadCrumb ws = (BreadCrumb)var1.get(j);
            Vec3 vec3 = ws.position;
            double d0 = vec3.xCoord - RenderManager.bUO;
            double d1 = vec3.yCoord - RenderManager.bUP;
            double d2 = vec3.zCoord - RenderManager.bUQ;
            float f = 1.0F - (float)(now - ws.creationTime) / (this.time.wo().intValue() * 100);
            float f1 = Math.max(0.0F, Math.min(1.0F, f));
            Color color = this.rz().rD();
            GL11.glColor4f(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, f1);
            GL11.glVertex3d(d0, d1, d2);
        }

        GL11.glEnd();
        GL11.glDisable(2848);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.enableDepth();
        GlStateManager.popMatrix();
    }
}
