package com.alan.clients.util.math;

import net.minecraft.util.MathHelper;

public class MathConst {
    public static final float PI = (float) Math.PI;
    public static final float TO_RADIANS = (float) (Math.PI / 180.0);
    public static final float TO_DEGREES = 180.0F / (float)Math.PI;
    public static final float[] COSINE = new float[361];
    public static final float[] SINE = new float[361];

    public MathConst() {
    }

    public static int toIntDegree(float var0) {
        return (int)(var0 % 360.0F + 360.0F) % 360;
    }

    static {
        for (int i = 0; i <= 360; i++) {
            COSINE[i] = MathHelper.cos(i * (float) (Math.PI / 180.0));
            SINE[i] = MathHelper.sin(i * (float) (Math.PI / 180.0));
        }
    }
}
