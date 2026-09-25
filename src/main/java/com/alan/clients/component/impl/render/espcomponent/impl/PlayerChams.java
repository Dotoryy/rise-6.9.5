package com.alan.clients.component.impl.render.espcomponent.impl;

import com.alan.clients.Client;
import com.alan.clients.util.interfaces.InstanceAccess;
import com.alan.clients.util.render.RenderUtil;
import com.alan.clients.util.render.ColorUtil;
import com.alan.clients.component.impl.render.espcomponent.api.ESP;
import com.alan.clients.component.impl.render.espcomponent.api.ESPColor;
import java.awt.Color;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.player.EntityPlayer;

public class PlayerChams extends ESP implements InstanceAccess {
    public PlayerChams(ESPColor espColor) {
        super(espColor);
    }

    @Override
    public void render3D() {
        boolean flag = Client.a.g().c(com.alan.clients.module.impl.render.ESP.class).staticColor.wo();
        float f = aEg.timer.bWm;

        for (EntityPlayer entityplayer : aEg.theWorld.playerEntities) {
            Render render = aEg.getRenderManager().getEntityRenderObject(entityplayer);
            if (aEg.getRenderManager() != null
                && render != null
                && (entityplayer != aEg.thePlayer || aEg.gameSettings.thirdPersonView != 0)
                && RenderUtil.isInViewFrustrum(entityplayer)
                && !entityplayer.isDead) {
                Color color = entityplayer.hurtTime > 0 ? Color.RED : ColorUtil.a(this.getColor(entityplayer), Color.WHITE, 0.4);
                if (color.getAlpha() > 0) {
                    double d0 = entityplayer.prevPosX + (entityplayer.posX - entityplayer.prevPosX) * f;
                    double d1 = entityplayer.prevPosY + (entityplayer.posY - entityplayer.prevPosY) * f;
                    double d2 = entityplayer.prevPosZ + (entityplayer.posZ - entityplayer.prevPosZ) * f;
                    float f1 = entityplayer.prevRotationYaw + (entityplayer.pl - entityplayer.prevRotationYaw) * f;
                    if (flag) {
                        RendererLivingEntity.setShaderBrightness(color);
                    }

                    aEg.getRenderManager();
                    double d3 = d0 - RenderManager.bUO;
                    aEg.getRenderManager();
                    double d4 = d1 - RenderManager.bUP;
                    aEg.getRenderManager();
                    render.doRender(entityplayer, d3, d4, d2 - RenderManager.bUQ, f1, f);
                    if (flag) {
                        RendererLivingEntity.No();
                    }

                    entityplayer.Tb();
                }
            }
        }

        RenderHelper.disableStandardItemLighting();
        aEg.entityRenderer.IU();
    }
}
