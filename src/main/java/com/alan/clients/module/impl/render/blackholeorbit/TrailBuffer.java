package com.alan.clients.module.impl.render.blackholeorbit;

public final class TrailBuffer {
    final int capacity;
    final double[] xValues;
    final double[] yValues;
    int writeIndex = 0;
    boolean wrapped = false;

    public TrailBuffer(int var1) {
        this.capacity = var1;
        this.xValues = new double[var1];
        this.yValues = new double[var1];
    }

    public void h(double var1, double var3) {
        this.xValues[this.writeIndex] = var1;
        this.yValues[this.writeIndex] = var3;
        this.writeIndex++;
        if (this.writeIndex >= this.capacity) {
            this.writeIndex = 0;
            this.wrapped = true;
        }
    }

    public void clear() {
        this.writeIndex = 0;
        this.wrapped = false;
    }

    public int size() {
        return this.wrapped ? this.capacity : this.writeIndex;
    }

    public double getX(int var1) {
        int i = this.resolveIndex(var1);
        return this.xValues[i];
    }

    public double getY(int var1) {
        int i = this.resolveIndex(var1);
        return this.yValues[i];
    }

    int resolveIndex(int var1) {
        int i = this.wrapped ? this.capacity : this.writeIndex;
        return i <= 0 ? 0 : ((this.writeIndex - 1 + this.capacity) % this.capacity - (i - 1 - var1) + this.capacity) % this.capacity;
    }
}
