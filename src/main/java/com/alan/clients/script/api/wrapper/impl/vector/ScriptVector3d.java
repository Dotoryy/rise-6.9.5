package com.alan.clients.script.api.wrapper.impl.vector;

import lombok.Generated;

public class ScriptVector3d {
    private double x;
    private double y;
    private double z;

    public double getX() {
        return this.x;
    }

    public void setX(double var1) {
        this.x = var1;
    }

    public double getY() {
        return this.y;
    }

    public void setY(double var1) {
        this.y = var1;
    }

    public double getZ() {
        return this.z;
    }

    public void setZ(double var1) {
        this.z = var1;
    }

    public void add(ScriptVector3d scriptVector3d) {
        this.x = this.x + scriptVector3d.getX();
        this.y = this.y + scriptVector3d.getY();
        this.z = this.z + scriptVector3d.getZ();
    }

    @Generated
    @Override
    public boolean equals(Object var1) {
        if (var1 == this) {
            return true;
        } else if (!(var1 instanceof ScriptVector3d scriptvector3d)) {
            return false;
        } else if (!scriptvector3d.canEqual(this)) {
            return false;
        } else if (Double.compare(this.getX(), scriptvector3d.getX()) != 0) {
            return false;
        } else {
            return Double.compare(this.getY(), scriptvector3d.getY()) != 0 ? false : Double.compare(this.getZ(), scriptvector3d.getZ()) == 0;
        }
    }

    @Generated
    protected boolean canEqual(Object var1) {
        return var1 instanceof ScriptVector3d;
    }

    @Generated
    @Override
    public int hashCode() {
        long i = Double.doubleToLongBits(this.getX());
        int j = 59 + (int)(i >>> 32 ^ i);
        long k = Double.doubleToLongBits(this.getY());
        int l = j * 59 + (int)(k >>> 32 ^ k);
        long i1 = Double.doubleToLongBits(this.getZ());
        return l * 59 + (int)(i1 >>> 32 ^ i1);
    }

    @Generated
    @Override
    public String toString() {
        return "ScriptVector3d(x=" + this.getX() + ", y=" + this.getY() + ", z=" + this.getZ() + ")";
    }

    @Generated
    public ScriptVector3d(double var1, double var3, double var5) {
        this.x = var1;
        this.y = var3;
        this.z = var5;
    }
}
