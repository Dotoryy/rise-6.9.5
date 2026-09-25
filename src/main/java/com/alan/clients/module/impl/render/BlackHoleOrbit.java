package com.alan.clients.module.impl.render;

import com.alan.clients.module.Module;
import com.alan.clients.module.api.Category;
import com.alan.clients.module.api.ModuleInfo;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.render.Render2DEvent;
import com.alan.clients.util.render.RenderUtil;
import com.alan.clients.util.vector.Vector2d;
import com.alan.clients.value.impl.DragValue;
import com.alan.clients.ui.theme.Themes;
import com.alan.clients.util.render.ColorUtil;
import com.alan.clients.util.font.FontManager;
import com.alan.clients.util.font.FontWeight;
import com.alan.clients.util.shader.ShaderQueueType;
import com.alan.clients.module.impl.render.blackholeorbit.OrbitBody;
import com.alan.clients.module.impl.render.blackholeorbit.OrbitParticle;
import com.alan.clients.module.impl.render.blackholeorbit.TrailBuffer;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

@ModuleInfo(aliases = "module.render.blackholeorbit.name", description = "module.render.blackholeorbit.description", category = Category.RENDER)
public final class BlackHoleOrbit extends Module {
    private final DragValue position = new DragValue("Position", this, new Vector2d(260.0, 140.0), false);
    private Interface interfaceModule;
    private static final double PANEL_WIDTH = 190.0;
    private static final double PANEL_HEIGHT = 135.0;
    private static final double GRAVITY = 120000.0;
    private static final double SOFTENING = 3.0;
    private static final double HORIZON_RADIUS = 13.2;
    private static final double CAPTURE_RADIUS = 14.0;
    private static final double BODY_SIZE = 4.4;
    private static final double DRAG = 12.0;
    private static final double TIDAL_SCALE = 0.12;
    private static final double BURST_THRESHOLD = 2.35;
    private static final double BODY_GLOW = 4000.0;
    private static final int CELL_SIZE = 8;
    private final OrbitBody primaryBody = new OrbitBody();
    private final OrbitBody secondaryBody = new OrbitBody();
    private final TrailBuffer primaryTrail = new TrailBuffer(72);
    private final TrailBuffer secondaryTrail = new TrailBuffer(72);
    private final ArrayList<OrbitParticle> particles = new ArrayList<>();
    private long lastFrameTime = -1L;
    @EventLink
    public final Listener<Render2DEvent> onRender2D = var1 -> {
        this.position.n(new Vector2d(190.0, 135.0));
        Vector2d vector2d = this.position.apP;
        this.b(ShaderQueueType.BLUR).c(() -> this.renderBlur(vector2d));
        this.b(ShaderQueueType.BLOOM).c(() -> this.renderBloom(vector2d));
        this.b(ShaderQueueType.REGULAR).c(() -> this.c(vector2d));
    };

    public BlackHoleOrbit() {
    }

    @Override
    public void onEnable() {
        this.lastFrameTime = -1L;
        this.reset();
    }

    private void c(Vector2d var1) {
        double d0 = this.getRound();
        RenderUtil.roundedRectangle(var1.x, var1.y, 190.0, 135.0, d0, ColorUtil.withAlpha(Themes.rK(), 140));
        String s = this.getName();
        FontManager.MAIN.a(18, FontWeight.BOLD).b(s, var1.x + 10.0, var1.y + 11.0, this.rz().rA().getRGB());
        double d1 = var1.x + 10.0;
        double d2 = var1.y + 28.0;
        RenderUtil.roundedRectangle(d1, d2, 170.0, 97.0, Math.max(3.0, d0 - 1.0), ColorUtil.withAlpha(Color.BLACK, 55));
        double d3 = d1 + 85.0;
        double d4 = d2 + 52.38;
        RenderUtil.g(d1, d2, 170.0, 97.0);

        try {
            this.drawGrid(d1, d2, 170.0, 97.0, d3, d4);
            this.updatePhysics();
            this.renderBodies(d3, d4);
            this.renderHole(d3, d4);
        } finally {
            GL11.glDisable(3089);
        }
    }

    private void renderBlur(Vector2d vector2d) {
        RenderUtil.roundedRectangle(vector2d.x, vector2d.y, 190.0, 135.0, this.getRound(), Color.BLACK);
    }

    private void renderBloom(Vector2d vector2d) {
        double d0 = this.getRound();
        RenderUtil.roundedRectangle(vector2d.x + 0.5, vector2d.y + 0.5, 189.0, 134.0, d0, this.rz().rE());
        double d1 = vector2d.x + 10.0;
        double d2 = vector2d.y + 28.0;
        double d3 = d1 + 85.0;
        double d4 = d2 + 52.38;
        Color color = ColorUtil.withAlpha(this.rz().rA(), 55);
        RenderUtil.c(d3, d4, 26.0, color);
    }

    private void renderHole(double var1, double var3) {
        Color color = this.rz().rA();
        Color color1 = this.rz().rB();
        RenderUtil.c(var1, var3, 22.0, ColorUtil.withAlpha(ColorUtil.a(color, color1, 0.5), 35));
        RenderUtil.c(var1, var3, 18.0, ColorUtil.withAlpha(ColorUtil.a(color1, color, 0.65), 28));
        RenderUtil.c(var1, var3, 15.5, ColorUtil.withAlpha(new Color(10, 10, 12), 255));
        RenderUtil.c(var1, var3, 13.0, ColorUtil.withAlpha(Color.BLACK, 255));
    }

    private void updatePhysics() {
        long now = System.nanoTime();
        if (this.lastFrameTime < 0L) {
            this.lastFrameTime = now;
        } else {
            double d0 = (now - this.lastFrameTime) / 1.0E9;
            this.lastFrameTime = now;
            double d1 = c(d0, 0.004166666666666667, 0.05);
            this.updateBody(this.primaryBody, d1);
            this.updateBody(this.secondaryBody, d1);
            if (this.primaryBody.active) {
                this.primaryTrail.h(this.primaryBody.x, this.primaryBody.y);
            }

            if (this.secondaryBody.active) {
                this.secondaryTrail.h(this.secondaryBody.x, this.secondaryBody.y);
            }

            Iterator iterator = this.particles.iterator();

            while (iterator.hasNext()) {
                OrbitParticle wo = (OrbitParticle)iterator.next();
                wo.age += d1;
                double d2 = Math.sqrt(wo.x * wo.x + wo.y * wo.y + 9.0);
                if (!(wo.age >= wo.lifetime) && !(d2 <= 14.0)) {
                    this.updateParticle(wo, d1);
                    wo.trail.h(wo.x, wo.y);
                } else {
                    iterator.remove();
                }
            }
        }
    }

    private void updateBody(OrbitBody var1, double var2) {
        if (!var1.active) {
            var1.respawnTimer -= var2;
            if (var1.respawnTimer <= 0.0) {
                var1.active = true;
                this.resetBody(var1, var1.index);
            }
        } else {
            double d0 = var1.x * var1.x + var1.y * var1.y + 9.0;
            double d1 = Math.sqrt(d0);
            if (d1 <= 14.0) {
                var1.active = false;
                var1.respawnTimer = 0.65;
            } else {
                double d2 = 1.0 / (d0 * d1);
                double d3 = -120000.0 * var1.x * d2;
                double d4 = -120000.0 * var1.y * d2;
                double d5 = 12.0 / d0;
                double d6 = -d5 * var1.velocityX;
                double d7 = -d5 * var1.velocityY;
                var1.velocityX += (d3 + d6) * var2;
                var1.velocityY += (d4 + d7) * var2;
                var1.x = var1.x + var1.velocityX * var2;
                var1.y = var1.y + var1.velocityY * var2;
                double d8 = d0 * d1;
                double d9 = 240000.0 / d8;
                double d10 = 0.5 * d9 * 0.12 * 0.12;
                double d11 = c(1.0 + d10, 1.0, 6.0);
                if (!var1.burst && d11 >= 2.35) {
                    var1.burst = true;
                    this.spawnBurst(var1, d11);
                    var1.active = false;
                    var1.respawnTimer = 1.1;
                }
            }
        }
    }

    private void updateParticle(OrbitParticle var1, double var2) {
        double d0 = var1.x * var1.x + var1.y * var1.y + 9.0;
        double d1 = Math.sqrt(d0);
        double d2 = 1.0 / (d0 * d1);
        double d3 = -120000.0 * var1.x * d2;
        double d4 = -120000.0 * var1.y * d2;
        double d5 = 19.200000000000003 / d0;
        var1.velocityX = var1.velocityX + (d3 - d5 * var1.velocityX) * var2;
        var1.velocityY = var1.velocityY + (d4 - d5 * var1.velocityY) * var2;
        var1.x = var1.x + var1.velocityX * var2;
        var1.y = var1.y + var1.velocityY * var2;
    }

    private void renderBodies(double var1, double var3) {
        this.drawBody(var1, var3, this.primaryBody, this.primaryTrail, this.rz().rA());
        this.drawBody(var1, var3, this.secondaryBody, this.secondaryTrail, this.rz().rB());

        for (OrbitParticle wo : this.particles) {
            this.drawParticle(var1, var3, wo);
        }

        RenderUtil.c(var1, var3, 13.2, ColorUtil.withAlpha(Color.WHITE, 10));
    }

    private void drawBody(double var1, double var3, OrbitBody var5, TrailBuffer var6, Color var7) {
        if (var5.active) {
            int i = var6.size();
            if (i > 1) {
                for (int j = 0; j < i; j++) {
                    double d0 = (double)j / (i - 1);
                    double d1 = 160.0 * (d0 * d0);
                    double d2 = 1.2 + 1.2 * d0;
                    double d3 = var1 + var6.getX(j);
                    double d4 = var3 + var6.getY(j);
                    RenderUtil.c(d3, d4, d2, ColorUtil.withAlpha(var7, (int)d1));
                }
            }

            double d5 = var5.x * var5.x + var5.y * var5.y + 9.0;
            double d6 = Math.sqrt(d5);
            double d7 = d5 * d6;
            double d8 = 240000.0 / d7;
            double d9 = -120000.0 / d7;
            double d10 = 0.5 * d8 * 0.12 * 0.12;
            double d11 = 0.5 * d9 * 0.12 * 0.12;
            double d12 = c(1.0 + d10, 1.0, 6.0);
            double d13 = c(1.0 + d11, 0.12, 1.0);
            double d14 = u((d6 - 13.2) / 6.0);
            int k = (int)(215.0 * d14);
            double d15 = Math.atan2(var5.y, var5.x);
            double d16 = var1 + var5.x;
            double d17 = var3 + var5.y;
            Color color = ColorUtil.withAlpha(var7, k);
            this.drawEllipse(d16, d17, 4.4 * d12, 4.4 * d13, d15, color);
            Color color1 = ColorUtil.withAlpha(Color.WHITE, (int)(110.0 * d14));
            this.drawEllipse(d16 - 0.7, d17 - 0.7, Math.max(1.0, 2.4200000000000004), Math.max(0.8, 1.2320000000000002), d15, color1);
        }
    }

    private void drawParticle(double var1, double var3, OrbitParticle var5) {
        int i = var5.trail.size();
        if (i > 1) {
            for (int j = 0; j < i; j++) {
                double d0 = (double)j / (i - 1);
                double d1 = 110.0 * (d0 * d0);
                double d2 = 0.8 + 0.9 * d0;
                RenderUtil.c(var1 + var5.trail.getX(j), var3 + var5.trail.getY(j), d2, ColorUtil.withAlpha(var5.color, (int)d1));
            }
        }

        double d3 = var5.x * var5.x + var5.y * var5.y + 9.0;
        double d4 = Math.sqrt(d3);
        double d5 = d3 * d4;
        double d6 = 240000.0 / d5;
        double d7 = -120000.0 / d5;
        double d8 = 0.5 * d6 * 0.12 * 0.12;
        double d9 = 0.5 * d7 * 0.12 * 0.12;
        double d10 = c(1.0 + d8, 1.0, 5.0);
        double d11 = c(1.0 + d9, 0.18, 1.0);
        double d12 = u(1.0 - var5.age / var5.lifetime);
        double d13 = u((d4 - 13.2) / 5.0) * d12;
        int k = (int)(170.0 * d13);
        double d14 = Math.atan2(var5.y, var5.x);
        this.drawEllipse(var1 + var5.x, var3 + var5.y, var5.size * d10, var5.size * d11, d14, ColorUtil.withAlpha(var5.color, k));
    }

    private void drawGrid(double var1, double var3, double var5, double var7, double var9, double var11) {
        int i = (int)Math.ceil(var5 / 8.0);
        int j = (int)Math.ceil(var7 / 8.0);
        Color color = ColorUtil.withAlpha(Color.BLACK, 18);
        Color color1 = ColorUtil.withAlpha(Color.BLACK, 10);
        Color color2 = ColorUtil.withAlpha(this.rz().rA(), 55);

        for (int k = 0; k < j; k++) {
            for (int l = 0; l < i; l++) {
                double d0 = var1 + l * 8;
                double d1 = var3 + k * 8;
                double d2 = d0 + 4.0;
                double d3 = d1 + 4.0;
                double d4 = d2 - var9;
                double d5 = d3 - var11;
                double d6 = d4 * d4 + d5 * d5 + 9.0;
                double d7 = 120000.0 / d6;
                if (this.primaryBody.active) {
                    d7 += 4000.0 / (v(d2 - (var9 + this.primaryBody.x)) + v(d3 - (var11 + this.primaryBody.y)) + 25.0);
                }

                if (this.secondaryBody.active) {
                    d7 += 4000.0 / (v(d2 - (var9 + this.secondaryBody.x)) + v(d3 - (var11 + this.secondaryBody.y)) + 25.0);
                }

                for (OrbitParticle wo : this.particles) {
                    d7 += 1400.0 / (v(d2 - (var9 + wo.x)) + v(d3 - (var11 + wo.y)) + 20.0);
                }

                double d8 = u((d7 - 18.0) / 140.0);
                double d9 = 1.0 / Math.max(1.0, d6);
                double d10 = -120000.0 * d4 * d9 * d9;
                double d11 = -120000.0 * d5 * d9 * d9;
                double d12 = c(d10 * 8.0E-4, -1.6, 1.6);
                double d13 = c(d11 * 8.0E-4, -1.6, 1.6);
                Color color3 = (l + k & 1) == 0 ? color : color1;
                Color color4 = ColorUtil.withAlpha(ColorUtil.a(color2, color3, d8), (int)(30.0 + 110.0 * d8));
                RenderUtil.d(d0 + d12, d1 + d13, 7.0, 7.0, color4);
            }
        }
    }

    private void spawnBurst(OrbitBody var1, double var2) {
        double d0 = Math.atan2(var1.y, var1.x);
        double cos = Math.cos(d0);
        double sin = Math.sin(d0);
        double d3 = -sin;
        double d4 = cos;
        double d5 = c(var2, 1.0, 4.5);

        for (int i = 0; i < 9; i++) {
            double d6 = (i / 8.0 * 2.0 - 1.0) * (2.6 * d5);
            double d7 = ThreadLocalRandom.current().nextDouble(-0.8, 0.8);
            OrbitParticle wo = new OrbitParticle();
            wo.x = var1.x + cos * d6 + d3 * d7;
            wo.y = var1.y + sin * d6 + d4 * d7;
            double d8 = ThreadLocalRandom.current().nextDouble(6.0, 18.0);
            double d9 = ThreadLocalRandom.current().nextDouble(-14.0, 14.0);
            wo.velocityX = var1.velocityX + -cos * d8 + d3 * d9 * 0.25;
            wo.velocityY = var1.velocityY + -sin * d8 + d4 * d9 * 0.25;
            wo.size = ThreadLocalRandom.current().nextDouble(1.0, 1.8);
            wo.lifetime = 1.6 + ThreadLocalRandom.current().nextDouble(0.0, 1.0);
            wo.age = 0.0;
            wo.color = ColorUtil.withAlpha(ColorUtil.a(this.rz().rA(), this.rz().rB(), ThreadLocalRandom.current().nextDouble()), 255);
            wo.trail = new TrailBuffer(54);
            this.particles.add(wo);
        }
    }

    private void drawEllipse(double var1, double var3, double var5, double var7, double var9, Color color) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(var1, var3, 0.0);
        GlStateManager.rotate((float)Math.toDegrees(var9), 0.0F, 0.0F, 1.0F);
        RenderUtil.rt();
        ColorUtil.glColor(color);
        GL11.glBegin(6);
        GL11.glVertex2d(0.0, 0.0);

        for (int i = 0; i <= 36; i++) {
            double d0 = i / 36.0 * (Math.PI * 2);
            GL11.glVertex2d(Math.cos(d0) * var5, Math.sin(d0) * var7);
        }

        GL11.glEnd();
        RenderUtil.stop();
        GlStateManager.popMatrix();
    }

    private void reset() {
        this.primaryBody.index = 1;
        this.secondaryBody.index = 2;
        this.resetBody(this.primaryBody, this.primaryBody.index);
        this.resetBody(this.secondaryBody, this.secondaryBody.index);
        this.primaryTrail.clear();
        this.secondaryTrail.clear();
        this.particles.clear();
    }

    private void resetBody(OrbitBody var1, int var2) {
        double d0 = var2 == 1 ? 52.0 : 44.0;
        double d1 = ThreadLocalRandom.current().nextDouble(0.0, Math.PI * 2);
        var1.x = Math.cos(d1) * d0;
        var1.y = Math.sin(d1) * d0;
        var1.burst = false;
        var1.active = true;
        var1.respawnTimer = 0.0;
        double d2 = Math.sqrt(120000.0 / d0);
        double d3 = -Math.sin(d1);
        double cos = Math.cos(d1);
        double d5 = (var2 == 1 ? 1.0 : -1.0) * 0.06;
        var1.velocityX = d3 * d2 * (1.0 + d5);
        var1.velocityY = cos * d2 * (1.0 + d5);
    }

    private double getRound() {
        if (this.interfaceModule == null) {
            this.interfaceModule = this.e(Interface.class);
        }

        return this.interfaceModule != null ? this.interfaceModule.getRoundingRadius() : 4.0;
    }

    private static double u(double var0) {
        return var0 < 0.0 ? 0.0 : (var0 > 1.0 ? 1.0 : var0);
    }

    private static double c(double var0, double var2, double var4) {
        return var0 < var2 ? var2 : (var0 > var4 ? var4 : var0);
    }

    private static double v(double var0) {
        return var0 * var0;
    }
}
