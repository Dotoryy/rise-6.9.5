package com.alan.clients.module.impl.render.jumpcircles;

import net.minecraft.util.Vec3;

public final class JumpCircle {
    private final Vec3 position;
    public double radius;
    public float alpha;

    public JumpCircle(Vec3 vec, double var2, float var4) {
        this.position = vec;
        this.radius = var2;
        this.alpha = var4;
    }

    public void y(double var1) {
        this.radius += var1;
    }

    public Vec3 getPosition() {
        return this.position;
    }

    public double getRadius() {
        return this.radius;
    }

    public float mc() {
        return this.alpha;
    }

    public void setAlpha(float var1) {
        this.alpha = var1;
    }
}
