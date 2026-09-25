package com.alan.clients.component.impl.render.espcomponent.impl;

import com.alan.clients.util.render.RenderUtil;
import com.alan.clients.component.impl.render.espcomponent.api.ESP;
import com.alan.clients.component.impl.render.espcomponent.api.ESPColor;
import java.awt.Color;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import org.lwjgl.opengl.GL11;

public class SigmaRing extends ESP {
    public SigmaRing(ESPColor espColor) {
        super(espColor);
    }

    public SigmaRing(Entity target, ESPColor espColor) {
        super(espColor);
        this.target = target;
    }

    @Override
    public void render3D() {
        float f = aEg.timer.bWm;
        EntityLivingBase entitylivingbase = (EntityLivingBase)this.target;
        if (aEg.getRenderManager() != null && entitylivingbase != null) {
            double d12 = entitylivingbase.prevPosX + (entitylivingbase.posX - entitylivingbase.prevPosX) * f;
            aEg.getRenderManager();
            double d0 = d12 - RenderManager.bUO;
            d12 = entitylivingbase.prevPosZ + (entitylivingbase.posZ - entitylivingbase.prevPosZ) * f;
            aEg.getRenderManager();
            double d1 = d12 - RenderManager.bUQ;
            float f1 = entitylivingbase.width;
            float height = entitylivingbase.height;
            double d2 = f1 * 1.1;
            double d3 = height * 0.5445 + Math.sin(System.currentTimeMillis() / 200.0);
            double d4 = Math.cos(System.currentTimeMillis() / 200.0) * 0.5;
            d12 = entitylivingbase.prevPosY + (entitylivingbase.posY - entitylivingbase.prevPosY) * f + d3;
            aEg.getRenderManager();
            double d5 = d12 - RenderManager.bUP;
            double d6 = d5 - d4;
            Color color = this.espColor.getColor();
            Color color1 = this.espColor.cs();
            long now = System.currentTimeMillis();
            double d7 = 0.002;
            byte b0 = 64;
            String s = GL11.glGetString(7936);
            boolean flag = s != null && s.toLowerCase().contains("nvidia");
            GL11.glPushMatrix();
            GL11.glDisable(3553);
            GL11.glEnable(2848);
            if (!flag) {
                GL11.glEnable(2881);
            }

            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 771);
            GL11.glDepthMask(false);
            GL11.glDisable(2929);
            GlStateManager.disableCull();
            GL11.glShadeModel(7425);
            GL11.glBegin(5);

            for (int j = 0; j <= b0; j++) {
                double d8 = j * ((Math.PI * 2) / b0);
                double d9 = (double)j / b0 * Math.PI * 2.0;
                float f3 = (float)((Math.sin(now * d7 + d9) + 1.0) * 0.5);
                int k = (int)(color.getRed() + (color1.getRed() - color.getRed()) * f3);
                int l = (int)(color.getGreen() + (color1.getGreen() - color.getGreen()) * f3);
                int i1 = (int)(color.getBlue() + (color1.getBlue() - color.getBlue()) * f3);
                byte b1 = 63;
                byte b2 = 0;
                RenderUtil.color(new Color(k, l, i1, b1));
                GL11.glVertex3d(d0 + d2 * Math.cos(d8), d5, d1 + d2 * Math.sin(d8));
                RenderUtil.color(new Color(k, l, i1, b2));
                GL11.glVertex3d(d0 + d2 * Math.cos(d8), d6, d1 + d2 * Math.sin(d8));
            }

            GL11.glEnd();
            GL11.glLineWidth(0.5F);
            GL11.glBegin(2);

            for (int j1 = 0; j1 < b0; j1++) {
                double d10 = j1 * ((Math.PI * 2) / b0);
                double d11 = (double)j1 / b0 * Math.PI * 2.0;
                float f4 = (float)((Math.sin(now * d7 + d11) + 1.0) * 0.5);
                int k1 = (int)(color.getRed() + (color1.getRed() - color.getRed()) * f4);
                int l1 = (int)(color.getGreen() + (color1.getGreen() - color.getGreen()) * f4);
                int i2 = (int)(color.getBlue() + (color1.getBlue() - color.getBlue()) * f4);
                RenderUtil.color(new Color(k1, l1, i2, 255));
                GL11.glVertex3d(d0 + d2 * Math.cos(d10), d5, d1 + d2 * Math.sin(d10));
            }

            GL11.glEnd();
            GL11.glLineWidth(1.0F);
            GL11.glShadeModel(7424);
            GL11.glDisable(3042);
            GL11.glDepthMask(true);
            GL11.glEnable(2929);
            GlStateManager.enableCull();
            GL11.glDisable(2848);
            if (!flag) {
                GL11.glDisable(2881);
            }

            GL11.glEnable(3553);
            GL11.glPopMatrix();
        }
    }
}
