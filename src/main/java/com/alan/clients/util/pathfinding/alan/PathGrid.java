package com.alan.clients.util.pathfinding.alan;

import com.alan.clients.util.pathfinding.alan.api.Point;
import net.minecraft.util.Vec3i;

public class PathGrid {
    private final int aND;
    private final int aNE;
    private boolean[][][] aNF;
    public final Vec3i aNG;

    public PathGrid(int var1, Vec3i vec) {
        this.aND = var1;
        this.aNG = vec;
        this.aNE = this.aND * 2 + 2;
        this.aX();
    }

    public void a(Point var1) {
        this.h(var1.getX(), var1.getY(), var1.getZ());
    }

    public void aX() {
        this.aNF = new boolean[this.aNE][this.aNE][this.aNE];
    }

    public void h(int var1, int var2, int var3) {
        int i = var1 - this.aNG.getX();
        int j = var2 - this.aNG.getY();
        int k = var3 - this.aNG.getZ();
        this.k(i, j, k);
        this.aNF[i + this.aND][j + this.aND][k + this.aND] = true;
    }

    public void i(int var1, int var2, int var3) {
        int i = var1 - this.aNG.getX();
        int j = var2 - this.aNG.getY();
        int k = var3 - this.aNG.getZ();
        this.k(i, j, k);
        this.aNF[i + this.aND][j + this.aND][k + this.aND] = false;
    }

    public boolean b(Point var1) {
        return this.j(var1.getX(), var1.getY(), var1.getZ());
    }

    public boolean j(int var1, int var2, int var3) {
        int i = var1 - this.aNG.getX();
        int j = var2 - this.aNG.getY();
        int k = var3 - this.aNG.getZ();
        this.k(i, j, k);
        return this.aNF[i + this.aND][j + this.aND][k + this.aND];
    }

    public void k(int var1, int var2, int var3) {
        if (Math.abs(var1) > this.aND || Math.abs(var2) > this.aND || Math.abs(var3) > this.aND) {
            System.out.println("Outside of size, " + var1 + " " + var2 + " " + var3);
        }
    }
}
