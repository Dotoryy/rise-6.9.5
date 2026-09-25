package com.alan.clients.util.math;

import com.alan.clients.util.vector.Vector3d;
import net.minecraft.entity.Entity;

public class MathInterpolation {
    public MathInterpolation() {
    }

    public static double interpolate(double var0, double var2, double var4) {
        return var2 + (var0 - var2) * var4;
    }

    public static float interpolate(float var0, float var1, float var2) {
        return var1 + (var0 - var1) * var2;
    }

    public static Vector3d a(Vector3d var0, Vector3d var1, double var2) {
        return new Vector3d(interpolate(var0.getX(), var1.getX(), var2), interpolate(var0.getY(), var1.getY(), var2), interpolate(var0.getZ(), var1.getZ(), var2));
    }

    public static Vector3d a(Entity entity, float var1) {
        return new Vector3d(interpolate(entity.posX, entity.prevPosX, var1), interpolate(entity.posY, entity.prevPosY, var1), interpolate(entity.posZ, entity.prevPosZ, var1));
    }
}
