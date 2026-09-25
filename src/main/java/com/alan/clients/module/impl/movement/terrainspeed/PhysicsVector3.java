package com.alan.clients.module.impl.movement.terrainspeed;

public final class PhysicsVector3 {
    double x;
    public double y;
    double z;

    public PhysicsVector3(double var1, double var3, double var5) {
        this.set(var1, var3, var5);
    }

    public PhysicsVector3 add(PhysicsVector3 var1) {
        this.x = this.x + var1.x;
        this.y = this.y + var1.y;
        this.z = this.z + var1.z;
        return this;
    }

    PhysicsVector3 scale(double var1) {
        this.x *= var1;
        this.y *= var1;
        this.z *= var1;
        return this;
    }

    public PhysicsVector3 set(double var1, double var3, double var5) {
        this.x = var1;
        this.y = var3;
        this.z = var5;
        return this;
    }
}
