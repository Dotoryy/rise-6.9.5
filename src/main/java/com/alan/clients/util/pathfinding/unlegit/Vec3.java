package com.alan.clients.util.pathfinding.unlegit;

import lombok.Generated;

public class Vec3 {
    private double x;
    private double y;
    private double z;

    public Vec3 n(double var1, double var3, double var5) {
        return new Vec3(this.x + var1, this.y + var3, this.z + var5);
    }

    public Vec3 uX() {
        return new Vec3(Math.floor(this.x), Math.floor(this.y), Math.floor(this.z));
    }

    public double c(Vec3 var1) {
        return Math.pow(var1.x - this.x, 2.0) + Math.pow(var1.y - this.y, 2.0) + Math.pow(var1.z - this.z, 2.0);
    }

    public Vec3 d(Vec3 var1) {
        return this.n(var1.getX(), var1.getY(), var1.getZ());
    }

    public net.minecraft.util.Vec3 uY() {
        return new net.minecraft.util.Vec3(this.x, this.y, this.z);
    }

    @Override
    public String toString() {
        return "[" + this.x + ";" + this.y + ";" + this.z + "]";
    }

    @Generated
    public double getX() {
        return this.x;
    }

    @Generated
    public double getY() {
        return this.y;
    }

    @Generated
    public double getZ() {
        return this.z;
    }

    @Generated
    public void setX(double var1) {
        this.x = var1;
    }

    @Generated
    public void setY(double var1) {
        this.y = var1;
    }

    @Generated
    public void setZ(double var1) {
        this.z = var1;
    }

    @Generated
    public Vec3(double var1, double var3, double var5) {
        this.x = var1;
        this.y = var3;
        this.z = var5;
    }
}
