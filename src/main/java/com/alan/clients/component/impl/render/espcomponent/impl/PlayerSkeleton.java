package com.alan.clients.component.impl.render.espcomponent.impl;

import com.alan.clients.Client;
import com.alan.clients.util.interfaces.InstanceAccess;
import com.alan.clients.util.math.MathInterpolation;
import com.alan.clients.util.render.ColorUtil;
import com.alan.clients.component.impl.combat.TargetComponent;
import com.alan.clients.component.impl.render.espcomponent.api.ESP;
import com.alan.clients.component.impl.render.espcomponent.api.ESPColor;
import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.src.a;
import org.lwjgl.opengl.GL11;

public class PlayerSkeleton extends ESP implements InstanceAccess {
    private final Map<EntityPlayer, float[][]> limbAngles = new HashMap<>();
    private static final float RAD_TO_DEG = 180.0F / (float)Math.PI;

    public PlayerSkeleton(ESPColor espColor) {
        super(espColor);
    }

    @Override
    public void render3D() {
        GL11.glPushMatrix();
        this.begin();

        for (Entity entity : TargetComponent.bR()) {
            if (entity instanceof EntityPlayer) {
                this.a((EntityPlayer)entity, a.bWm);
            }
        }

        this.end();
        GL11.glPopMatrix();
    }

    private void begin() {
        GL11.glLineWidth(Client.a.g().c(com.alan.clients.module.impl.render.ESP.class).width.wo().floatValue());
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        ColorUtil.glColor(Client.a.g().c(com.alan.clients.module.impl.render.ESP.class).whiteColor.wo() ? Color.WHITE : this.espColor.getColor());
        GL11.glDisable(2929);
        GL11.glDisable(3553);
        GL11.glDepthMask(false);
    }

    private void end() {
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glEnable(3553);
        GL11.glDisable(2848);
        GL11.glEnable(2929);
    }

    @Override
    public void a(EntityPlayer player, ModelBiped modelBiped) {
        this.limbAngles
            .put(
                player,
                new float[][]{
                    {modelBiped.bxN.rotateAngleX, modelBiped.bxN.rotateAngleY, modelBiped.bxN.rotateAngleZ},
                    {modelBiped.bxQ.rotateAngleX, modelBiped.bxQ.rotateAngleY, modelBiped.bxQ.rotateAngleZ},
                    {modelBiped.bxR.rotateAngleX, modelBiped.bxR.rotateAngleY, modelBiped.bxR.rotateAngleZ},
                    {modelBiped.bxS.rotateAngleX, modelBiped.bxS.rotateAngleY, modelBiped.bxS.rotateAngleZ},
                    {modelBiped.bxT.rotateAngleX, modelBiped.bxT.rotateAngleY, modelBiped.bxT.rotateAngleZ}
                }
            );
    }

    private void a(EntityPlayer player, float var2) {
        float[][] afloat = this.limbAngles.get(player);
        if (afloat != null) {
            GL11.glPushMatrix();
            float f = (float)(MathInterpolation.interpolate(player.posX, player.prevPosX, var2) - RenderManager.bUO);
            float f1 = (float)(MathInterpolation.interpolate(player.posY, player.prevPosY, var2) - RenderManager.bUP);
            float f2 = (float)(MathInterpolation.interpolate(player.posZ, player.prevPosZ, var2) - RenderManager.bUQ);
            GL11.glTranslated(f, f1, f2);
            boolean flag = player.isSneaking();
            float rotationYawHead = player.rotationYawHead;
            float renderYawOffset = player.renderYawOffset;
            float prevRenderYawOffset = player.prevRenderYawOffset;
            float f6 = MathInterpolation.interpolate(renderYawOffset, prevRenderYawOffset, var2);
            float f7 = flag ? 0.6F : 0.75F;
            GL11.glRotatef(-f6, 0.0F, 1.0F, 0.0F);
            GL11.glTranslatef(0.0F, 0.0F, flag ? -0.235F : 0.0F);
            this.a(afloat, f7, flag, f6, rotationYawHead);
            GL11.glPopMatrix();
        }
    }

    private void a(float[][] var1, float var2, boolean var3, float var4, float var5) {
        for (int i = 1; i <= 2; i++) {
            this.a(var1[i + 2], i == 1 ? -0.125F : 0.125F, var2);
        }

        GL11.glTranslatef(0.0F, 0.0F, var3 ? 0.25F : 0.0F);
        GL11.glPushMatrix();
        GL11.glTranslatef(0.0F, var3 ? -0.05F : 0.0F, var3 ? -0.01725F : 0.0F);

        for (int j = 1; j <= 2; j++) {
            this.b(var1[j], j == 1 ? -0.375F : 0.375F, var2 + 0.55F);
        }

        GL11.glRotatef(var4 - var5, 0.0F, 1.0F, 0.0F);
        this.a(var1[0], var2);
        GL11.glPopMatrix();
        this.b(var2);
    }

    private void a(float[] var1, float var2, float var3) {
        GL11.glPushMatrix();
        GL11.glTranslatef(var2, var3, 0.0F);
        this.a(var1);
        GL11.glBegin(3);
        GL11.glVertex3i(0, 0, 0);
        GL11.glVertex3f(0.0F, -var3, 0.0F);
        GL11.glEnd();
        GL11.glPopMatrix();
    }

    private void b(float[] var1, float var2, float var3) {
        GL11.glPushMatrix();
        GL11.glTranslatef(var2, var3, 0.0F);
        this.a(var1);
        GL11.glBegin(3);
        GL11.glVertex3i(0, 0, 0);
        GL11.glVertex3f(0.0F, -0.5F, 0.0F);
        GL11.glEnd();
        GL11.glPopMatrix();
    }

    private void a(float[] var1, float var2) {
        GL11.glPushMatrix();
        GL11.glTranslatef(0.0F, var2 + 0.55F, 0.0F);
        this.a(var1);
        GL11.glBegin(3);
        GL11.glVertex3i(0, 0, 0);
        GL11.glVertex3f(0.0F, 0.3F, 0.0F);
        GL11.glEnd();
        GL11.glPopMatrix();
    }

    private void a(float[] var1) {
        if (var1[0] != 0.0F) {
            GL11.glRotatef(var1[0] * (180.0F / (float)Math.PI), 1.0F, 0.0F, 0.0F);
        }

        if (var1[1] != 0.0F) {
            GL11.glRotatef(var1[1] * (180.0F / (float)Math.PI), 0.0F, 1.0F, 0.0F);
        }

        if (var1[2] != 0.0F) {
            GL11.glRotatef(var1[2] * (180.0F / (float)Math.PI), 0.0F, 0.0F, 1.0F);
        }
    }

    private void b(float var1) {
        GL11.glPushMatrix();
        GL11.glTranslated(0.0, var1, 0.0);
        GL11.glBegin(3);
        GL11.glVertex3f(-0.125F, 0.0F, 0.0F);
        GL11.glVertex3f(0.125F, 0.0F, 0.0F);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        GL11.glTranslatef(0.0F, var1, 0.0F);
        GL11.glBegin(3);
        GL11.glVertex3i(0, 0, 0);
        GL11.glVertex3f(0.0F, 0.55F, 0.0F);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        GL11.glTranslatef(0.0F, var1 + 0.55F, 0.0F);
        GL11.glBegin(3);
        GL11.glVertex3f(-0.375F, 0.0F, 0.0F);
        GL11.glVertex3f(0.375F, 0.0F, 0.0F);
        GL11.glEnd();
        GL11.glPopMatrix();
    }
}
