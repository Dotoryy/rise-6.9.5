package com.alan.clients.module.impl.render.targetinfo;

import com.alan.clients.module.impl.player.HealthBypass;
import com.alan.clients.module.impl.render.TargetInfo;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.render.Render2DEvent;
import com.alan.clients.util.render.RenderUtil;
import com.alan.clients.util.vector.Vector2d;
import com.alan.clients.value.Mode;
import com.alan.clients.util.math.MathUtil;
import com.alan.clients.util.render.ColorUtil;
import com.alan.clients.component.impl.community.UserLookupComponent;
import java.awt.Color;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import org.lwjgl.opengl.GL11;

public class ExhibitionTargetInfo extends Mode<TargetInfo> {
    private TargetInfo targetInfo;
    @EventLink
    public final Listener<Render2DEvent> onRender2D = var1x -> {
        if (this.targetInfo == null) {
            this.targetInfo = this.e(TargetInfo.class);
        }

        Entity entity = this.targetInfo.target;
        boolean flag = !this.targetInfo.inWorld || this.targetInfo.stopwatch.T(1000L);
        if (entity == null || flag) {
            return;
        }

        String s = entity.getName();
        String s1 = UserLookupComponent.c(s, s);
        double d0 = this.targetInfo.position.x;
        double d1 = this.targetInfo.position.y;
        RenderUtil.d(d0, d1, 140.0, 50.0, new Color(0, 0, 0));
        RenderUtil.d(d0 + 0.5, d1 + 0.5, 139.0, 49.0, new Color(60, 60, 60));
        RenderUtil.d(d0 + 1.5, d1 + 1.5, 137.0, 47.0, new Color(0, 0, 0));
        RenderUtil.d(d0 + 2.0, d1 + 2.0, 136.0, 46.0, new Color(25, 25, 24));
        aEg.fontRendererObj.a(s1, d0 + 40.0, d1 + 6.0, Color.WHITE.getRGB());
        GlStateManager.pushMatrix();
        GlStateManager.scale(0.7, 0.7, 0.7);
        AbstractClientPlayer abstractclientplayer = (AbstractClientPlayer)entity;
        HealthBypass healthbypass = this.e(HealthBypass.class);
        float f = healthbypass != null && healthbypass.isEnabled()
            ? HealthBypass.getScoreboardHealth(abstractclientplayer)
            : abstractclientplayer.getHealth();
        aEg.fontRendererObj
            .a(
                "HP: " + Math.round(f) + " | Dist: " + Math.round(aEg.thePlayer.getDistanceToEntity(entity)),
                (d0 + 40.0) * 1.4285714285714286,
                (d1 + 17.0) * 1.4285714285714286,
                Color.WHITE.getRGB()
            );
        GlStateManager.popMatrix();
        this.targetInfo.positionValue.aHe = new Vector2d(140.0, 50.0);
        double d2 = Math.min(!this.targetInfo.inWorld ? 0.0 : MathUtil.round(f, 1), abstractclientplayer.getMaxHealth());
        Color color = getHealthColor(f, abstractclientplayer.getMaxHealth());
        double d3 = d0 + 40.0;
        RenderUtil.d(d3, d1 + 25.0, 91.0, 5.0, ColorUtil.withAlpha(color, 50));
        RenderUtil.d(d3, d1 + 25.0, 91.0 * (d2 / abstractclientplayer.getMaxHealth()), 6.0, color);
        RenderUtil.d(d3, d1 + 25.0, 91.0, 1.0, Color.BLACK);
        RenderUtil.d(d3, d1 + 30.0, 91.0, 1.0, Color.BLACK);

        for (int i = 0; i < 10; i++) {
            RenderUtil.d(d3 + 10 * i, d1 + 25.0, 1.0, 6.0, Color.BLACK);
        }

        RenderUtil.a(d3, d1 + 31.0, -1, abstractclientplayer.getHeldItem());
        RenderUtil.a(d3 + 15.0, d1 + 31.0, -1, abstractclientplayer.getEquipmentInSlot(4));
        RenderUtil.a(d3 + 30.0, d1 + 31.0, -1, abstractclientplayer.getEquipmentInSlot(3));
        RenderUtil.a(d3 + 45.0, d1 + 31.0, -1, abstractclientplayer.getEquipmentInSlot(2));
        RenderUtil.a(d3 + 60.0, d1 + 31.0, -1, abstractclientplayer.getEquipmentInSlot(1));
        GlStateManager.pushMatrix();
        GlStateManager.scale(0.4, 0.4, 0.4);
        GlStateManager.translate((d0 + 20.0) * 2.5, (d1 + 44.0) * 2.5, 100.0);
        drawEntity(entity.pl, entity.rotationPitch, (EntityLivingBase)entity);
        GlStateManager.popMatrix();
    };

    public ExhibitionTargetInfo(String var1, TargetInfo targetInfo) {
        super(var1, targetInfo);
    }

    private static Color getHealthColor(float var0, float var1) {
        Color color = new Color(0, 165, 0);
        if (var0 < var1 / 1.5F) {
            color = new Color(200, 200, 0);
        }

        if (var0 < var1 / 2.5F) {
            color = new Color(200, 155, 0);
        }

        if (var0 < var1 / 4.0F) {
            color = new Color(120, 0, 0);
        }

        return color;
    }

    public static void drawEntity(float var0, float var1, EntityLivingBase living) {
        GlStateManager.resetColor();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.enableColorMaterial();
        GlStateManager.pushMatrix();
        GlStateManager.translate(0.0F, 0.0F, 50.0F);
        GlStateManager.scale(-50.0F, 50.0F, 50.0F);
        GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
        float f = living.renderYawOffset;
        float f1 = living.pl;
        float rotationPitch = living.rotationPitch;
        float prevRotationYawHead = living.prevRotationYawHead;
        float rotationYawHead = living.rotationYawHead;
        GlStateManager.rotate(135.0F, 0.0F, 1.0F, 0.0F);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.rotate(-135.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate((float)(-Math.atan(var1 / 40.0F) * 20.0), 1.0F, 0.0F, 0.0F);
        living.renderYawOffset = var0 - 0.4F;
        living.pl = var0 - 0.2F;
        living.rotationPitch = var1;
        living.rotationYawHead = living.pl;
        living.prevRotationYawHead = living.pl;
        GlStateManager.translate(0.0F, 0.0F, 0.0F);
        RenderManager rendermanager = aEg.getRenderManager();
        rendermanager.setPlayerViewY(180.0F);
        rendermanager.setRenderShadow(false);
        rendermanager.renderEntityWithPosYaw(living, 0.0, 0.0, 0.0, 0.0F, 1.0F);
        rendermanager.setRenderShadow(true);
        living.renderYawOffset = f;
        living.pl = f1;
        living.rotationPitch = rotationPitch;
        living.prevRotationYawHead = prevRotationYawHead;
        living.rotationYawHead = rotationYawHead;
        GlStateManager.popMatrix();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.disableTexture2D();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
        GlStateManager.resetColor();
    }
}
