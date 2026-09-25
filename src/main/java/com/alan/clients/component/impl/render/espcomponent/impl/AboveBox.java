package com.alan.clients.component.impl.render.espcomponent.impl;

import com.alan.clients.util.interfaces.InstanceAccess;
import com.alan.clients.util.render.RenderUtil;
import com.alan.clients.util.render.ColorUtil;
import com.alan.clients.component.impl.render.espcomponent.api.ESP;
import com.alan.clients.component.impl.render.espcomponent.api.ESPColor;
import java.awt.Color;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.AxisAlignedBB;
import org.lwjgl.opengl.GL11;

public class AboveBox extends ESP implements InstanceAccess {
    public AboveBox(ESPColor espColor) {
        super(espColor);
    }

    public AboveBox(EntityLivingBase target, ESPColor espColor) {
        super(espColor);
        this.target = target;
    }

    @Override
    public void render3D() {
        EntityLivingBase entitylivingbase = (EntityLivingBase)this.target;
        if (aEg.getRenderManager() != null && entitylivingbase != null) {
            Color color = entitylivingbase.hurtTime > 0 ? Color.red : this.rz().rA();
            GL11.glPushMatrix();
            GL11.glEnable(3042);
            GL11.glLineWidth(1.8F);
            GL11.glBlendFunc(770, 771);
            GL11.glEnable(2848);
            GlStateManager.depthMask(true);
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 771);
            GL11.glDisable(3553);
            GL11.glEnable(2848);
            GL11.glDisable(2929);
            GL11.glDepthMask(false);
            float f = aEg.timer.bWm;
            double d0 = this.target.lastTickPosX + (this.target.posX - this.target.lastTickPosX) * f;
            double d1 = this.target.lastTickPosY + (this.target.posY - this.target.lastTickPosY) * f + entitylivingbase.getEyeHeight() * 1.2;
            double d2 = this.target.lastTickPosZ + (this.target.posZ - this.target.lastTickPosZ) * f;
            float f1 = this.target.width;
            float f2 = this.target.height + (this.target.isSneaking() ? -0.2F : 0.1F);
            RenderUtil.color(ColorUtil.withAlpha(color, 40));
            RenderUtil.drawBoundingBox(new AxisAlignedBB(d0 - f1 / 1.75, d1, d2 - f1 / 1.75, d0 + f1 / 1.75, d1 + 0.1, d2 + f1 / 1.75));
            GL11.glDisable(2848);
            GL11.glEnable(3553);
            GL11.glEnable(2929);
            GL11.glDepthMask(true);
            GL11.glDisable(3042);
            GL11.glDisable(3042);
            GL11.glDisable(2848);
            GL11.glPopMatrix();
            RenderUtil.color(Color.WHITE);
        }
    }
}
