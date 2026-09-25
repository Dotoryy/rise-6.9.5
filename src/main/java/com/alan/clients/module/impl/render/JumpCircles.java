package com.alan.clients.module.impl.render;

import com.alan.clients.module.Module;
import com.alan.clients.module.api.Category;
import com.alan.clients.module.api.ModuleInfo;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PostMotionEvent;
import com.alan.clients.newevent.impl.render.Render3DEvent;
import com.alan.clients.util.animation.Animation;
import com.alan.clients.util.animation.Easing;
import com.alan.clients.util.vector.Vector2d;
import com.alan.clients.util.math.MathUtil;
import com.alan.clients.module.impl.render.jumpcircles.JumpCircle;
import java.awt.Color;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.GL11;

@ModuleInfo(aliases = "Jump Circles", description = "Draws circles around the player when they jump", category = Category.RENDER)
public final class JumpCircles extends Module {
    private final Queue<JumpCircle> circles = new ConcurrentLinkedQueue<>();
    private final Animation alphaAnimation = new Animation(Easing.EASE_IN_OUT_CUBIC, 300L);
    private boolean wasAirborne = false;
    private static final float GROWTH_RATE = 0.004F;
    private static final int DRAW_MODE = 2;
    private static final float LINE_WIDTH = 2.0F;
    private static final float ALPHA_DIVISOR = 4.0F;
    @EventLink
    private final Listener<PostMotionEvent> onPostMotion = var1 -> {
        if (aEg.thePlayer.onGround && this.wasAirborne) {
            double d0 = MathUtil.m(aEg.thePlayer.prevPosX, aEg.thePlayer.posX, aEg.timer.bWm);
            double d1 = MathUtil.m(aEg.thePlayer.prevPosY, aEg.thePlayer.posY, aEg.timer.bWm);
            double d2 = MathUtil.m(aEg.thePlayer.prevPosZ, aEg.thePlayer.posZ, aEg.timer.bWm);
            this.circles.add(new JumpCircle(new Vec3(d0, d1, d2), 0.0, 255.0F));
            this.wasAirborne = false;
        } else if (!aEg.thePlayer.onGround) {
            this.wasAirborne = true;
        }

        Iterator iterator = this.circles.iterator();

        while (iterator.hasNext()) {
            if (((JumpCircle)iterator.next()).alpha > 0.0F) {
                return;
            }

            this.circles.clear();
        }
    };
    @EventLink
    private final Listener<Render3DEvent> onRender3D = var1 -> {
        for (JumpCircle xl : this.circles) {
            Vec3 vec3 = xl.getPosition();
            double d1 = vec3.yCoord;
            aEg.getRenderManager();
            double d0 = d1 - RenderManager.bUP;
            xl.y(0.004F);
            if (xl.getRadius() <= 2.0) {
                this.beginRender();
                this.alphaAnimation.Q(xl.alpha = xl.alpha - (float)(xl.getRadius() / 4.0));
                if (xl.mc() > 0.0F) {
                    xl.setAlpha(xl.alpha = xl.alpha - (float)(xl.getRadius() / 4.0));
                }

                this.drawCircle(xl, vec3, d0);
                this.endRender();
            } else {
                this.circles.remove();
            }
        }
    };

    public JumpCircles() {
    }

    @Override
    public void onDisable() {
        if (!this.circles.isEmpty()) {
            this.circles.clear();
        }
    }

    private void beginRender() {
        GL11.glPushMatrix();
        GL11.glDisable(2929);
        GL11.glDisable(3553);
        GL11.glDisable(3008);
        GL11.glEnable(2848);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(770, 771);
    }

    private void drawCircle(JumpCircle var1, Vec3 vec, double var3) {
        GL11.glLineWidth(2.0F);
        GL11.glBegin(2);

        for (int i = 0; i <= 360; i++) {
            Color color = this.e(Interface.class).rz().getAccentColor(new Vector2d(i, i));
            double[] adouble = this.circlePoint(vec.xCoord, vec.zCoord, i, var1.radius);
            double d2 = adouble[0];
            aEg.getRenderManager();
            double d0 = d2 - RenderManager.bUO;
            d2 = adouble[1];
            aEg.getRenderManager();
            double d1 = d2 - RenderManager.bUQ;
            GL11.glColor4f(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, var1.mc() / 255.0F);
            GL11.glVertex3d(d0, var3, d1);
        }

        GL11.glEnd();
        GL11.glPopMatrix();
    }

    private void endRender() {
        GlStateManager.disableBlend();
        GL11.glDisable(2848);
        GL11.glEnable(3008);
        GL11.glEnable(3553);
        GL11.glEnable(2929);
    }

    public double[] circlePoint(double var1, double var3, double var5, double var7) {
        double d0 = MathHelper.wrapAngleTo180_double(var5);
        double d1 = d0 * Math.PI / 180.0;
        double d2 = var1 - Math.sin(d1) * var7;
        double d3 = var3 + Math.cos(d1) * var7;
        return new double[]{d2, d3};
    }
}
