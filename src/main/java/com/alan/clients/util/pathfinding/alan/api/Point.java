package com.alan.clients.util.pathfinding.alan.api;

import net.minecraft.util.Vec3i;

public class Point extends Vec3i {
    public Point(int var1, int var2, int var3) {
        super(var1, var2, var3);
    }

    public Point(double var1, double var3, double var5) {
        super(var1, var3, var5);
    }

    public Point(Vec3i vec) {
        super(vec.getX(), vec.getY(), vec.getZ());
    }
}
