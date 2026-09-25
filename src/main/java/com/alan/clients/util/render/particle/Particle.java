package com.alan.clients.util.render.particle;

import com.alan.clients.util.interfaces.InstanceAccess;
import com.alan.clients.util.render.RenderUtil;
import com.alan.clients.util.vector.Vector2f;
import com.alan.clients.util.render.ColorUtil;
import java.awt.Color;
import rip.vantage.commons.util.time.StopWatch;

public class Particle implements InstanceAccess {
    private final Vector2f aPu;
    private final Vector2f velocity;
    private final float scale;
    private final Color aPx;
    private Color aPy;
    private Color aPz;
    public StopWatch bN = new StopWatch();
    public StopWatch hj = new StopWatch();
    public float alpha;

    public Particle(Vector2f vec2, Color color, Vector2f var3, float var4) {
        this.aPu = vec2;
        this.aPx = color;
        this.velocity = var3;
        this.scale = var4;
        this.bN.aX();
        this.alpha = color.getAlpha();
    }

    public Particle(Vector2f vec2, Vector2f var2) {
        this.aPu = vec2;
        this.aPx = ColorUtil.withAlpha(ColorUtil.a(this.rz().rA(), this.rz().rB(), Math.random()), (int)(Math.random() * 255.0));
        this.velocity = var2;
        this.scale = (float)(2.0 + Math.random() * 3.0);
        this.bN.aX();
        this.alpha = this.aPx.getAlpha();
    }

    public void cj() {
        if (this.aPy != null && this.aPz != null) {
            RenderUtil.roundedRectangle(this.aPu.x, this.aPu.y, this.scale, this.scale, this.scale / 2.0F, this.aPy);
        }
    }

    public void ci() {
        if (this.aPy != null && this.aPz != null) {
            RenderUtil.roundedRectangle(this.aPu.x, this.aPu.y, this.scale, this.scale, this.scale / 2.0F, this.aPz);
        }
    }

    public void ju() {
        for (int i = 0; i <= this.bN.getElapsedTime(); i++) {
            this.aPu.setX(this.aPu.getX() + this.velocity.getX() / 10.0F);
            this.aPu.setY(this.aPu.getY() + this.velocity.getY() / 10.0F);
            this.velocity.setX(this.velocity.getX() * 0.999F);
            this.velocity.setY(this.velocity.getY() * 0.999F);
        }

        this.aPz = ColorUtil.withAlpha(this.aPx, (int)this.alpha * 3);
        this.aPy = ColorUtil.withAlpha(this.aPx, (int)this.alpha);
        this.alpha = Math.max(this.alpha - (float)this.bN.getElapsedTime() / 18.0F, 0.0F);
        this.bN.aX();
    }
}
