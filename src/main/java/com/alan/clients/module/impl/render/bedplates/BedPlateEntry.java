package com.alan.clients.module.impl.render.bedplates;

import com.alan.clients.util.vector.Vector3d;

public class BedPlateEntry {
    private final Vector3d position;
    private final double distanceSquared;
    private final BedPlateInfo info;
    private boolean visible = true;
    private double[] projectedPosition = null;
    private int cacheAge = 999;

    public BedPlateEntry(Vector3d var1, double var2, BedPlateInfo var4) {
        this.position = var1;
        this.distanceSquared = var2;
        this.info = var4;
    }

    public Vector3d getPosition() {
        return this.position;
    }

    public double getDistanceSquared() {
        return this.distanceSquared;
    }

    public BedPlateInfo getInfo() {
        return this.info;
    }

    public boolean isVisible() {
        return this.visible;
    }

    public void v(boolean var1) {
        this.visible = var1;
    }

    public double[] getProjectedPosition() {
        return this.projectedPosition;
    }

    public void a(double[] var1) {
        this.projectedPosition = var1;
        this.cacheAge = 0;
    }

    public int getCacheAge() {
        return this.cacheAge;
    }

    public void incrementCacheAge() {
        this.cacheAge++;
    }
}
