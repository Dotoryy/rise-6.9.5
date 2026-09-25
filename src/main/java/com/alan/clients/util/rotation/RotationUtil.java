package com.alan.clients.util.rotation;

import com.alan.clients.component.impl.player.RotationComponent;
import com.alan.clients.util.interfaces.InstanceAccess;
import com.alan.clients.util.vector.Vector2f;
import com.alan.clients.util.RayCastUtil;
import com.alan.clients.util.vector.Vector3d;
import java.util.ArrayList;
import java.util.List;
import lombok.Generated;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

public final class RotationUtil implements InstanceAccess {
    public static Vector2f c(Vector3d var0, Vector3d var1) {
        Vector3d aka = var1.subtract(var0);
        double d0 = Math.hypot(aka.getX(), aka.getZ());
        if (af(aka.getX()) && af(aka.getY()) && af(aka.getZ()) && af(d0)) {
            float f = (float)(MathHelper.atan2(aka.getZ(), aka.getX()) * 180.0F / (float)Math.PI) - 90.0F;
            float f1 = (float)(-(MathHelper.atan2(aka.getY(), d0) * 180.0F / (float)Math.PI));
            return new Vector2f(f, f1);
        }
        return aEg.thePlayer == null ? new Vector2f(0.0F, 0.0F) : new Vector2f(aEg.thePlayer.pl, aEg.thePlayer.rotationPitch);
    }

    public static Vector2f y(Entity entity) {
        if (z(entity) && aEg.thePlayer != null) {
            return d(
                entity.Ty()
                    .v(
                        0.0,
                        Math.max(
                            0.0,
                            Math.min(
                                aEg.thePlayer.posY - entity.posY + aEg.thePlayer.getEyeHeight(),
                                (entity.getEntityBoundingBox().maxY - entity.getEntityBoundingBox().minY) * 0.9
                            )
                        ),
                        0.0
                    )
            );
        }
        return aEg.thePlayer == null ? new Vector2f(0.0F, 0.0F) : new Vector2f(aEg.thePlayer.pl, aEg.thePlayer.rotationPitch);
    }

    public static Vec3 b(BlockPos pos, EnumFacing facing) {
        double d0 = pos.getX() + 0.5;
        double d1 = pos.getY() + 0.5;
        double d2 = pos.getZ() + 0.5;
        double d3 = d0 + facing.getDirectionVec().getX() * 0.5;
        double d4 = d1 + facing.getDirectionVec().getY() * 0.5;
        double d5 = d2 + facing.getDirectionVec().getZ() * 0.5;
        return new Vec3(d3, d4, d5);
    }

    public static Vector2f calculate(Entity entity, boolean var1, double var2) {
        Vector2f vector2f = y(entity);
        if (var1 && z(entity) && af(var2) && !(var2 <= 0.0)) {
            MovingObjectPosition movingobjectposition = RayCastUtil.c(vector2f, var2);
            if (movingobjectposition != null && movingobjectposition.typeOfHit == MovingObjectType.ENTITY) {
                return vector2f;
            }

            for (double d0 = 1.0; d0 >= 0.0; d0 -= 0.25 + Math.random() * 0.1) {
                for (double d1 = 1.0; d1 >= -0.5; d1 -= 0.5) {
                    for (double d2 = 1.0; d2 >= -0.5; d2 -= 0.5) {
                        Vector2f vector2f1 = d(
                            entity.Ty()
                                .v(
                                    (entity.getEntityBoundingBox().maxX - entity.getEntityBoundingBox().minX) * d1,
                                    (entity.getEntityBoundingBox().maxY - entity.getEntityBoundingBox().minY) * d0,
                                    (entity.getEntityBoundingBox().maxZ - entity.getEntityBoundingBox().minZ) * d2
                                )
                        );
                        MovingObjectPosition movingobjectposition1 = RayCastUtil.c(vector2f1, var2);
                        if (movingobjectposition1 != null && movingobjectposition1.typeOfHit == MovingObjectType.ENTITY) {
                            return vector2f1;
                        }
                    }
                }
            }

            return vector2f;
        }
        return vector2f;
    }

    public static Vector2f a(Entity entity, AxisAlignedBB box, boolean var2, double var3, boolean var5) {
        return a(entity, box, var2, var3, var5, 0.0F);
    }

    public static Vector2f a(Entity entity, AxisAlignedBB box, boolean var2, double var3, boolean var5, float var6) {
        if (box != null && !box.hasNaN()) {
            Vec3 vec3 = var2 ? i(box) : h(box);
            return h(a(entity, box, vec3, var3, var5, var6));
        }
        return y(entity);
    }

    public static Vec3 a(Entity entity, AxisAlignedBB box, Vec3 vec, double var3, boolean var5, float var6) {
        if (entity != null && box != null && vec != null) {
            Vec3 vec3 = aEg.thePlayer.getPositionEyes(1.0F);
            Vec3 vec31 = i(box);
            Vector2f vector2f = h(vec);
            if (a(h(vec31), entity, var3, var5, var6)) {
                return vec31;
            }

            Vec3 vec32 = null;
            double d0 = Double.MAX_VALUE;

            for (Vec3 vec33 : a(box, vec)) {
                Vector2f vector2f1 = h(vec33);
                if (a(vector2f1, entity, var3, var5, var6)) {
                    double dx = Math.abs(MathHelper.wrapAngleTo180_float(vector2f1.getX() - vector2f.getX()));
                    double dy = Math.abs(vector2f1.getY() - vector2f.getY());
                    double d3 = vec33.squareDistanceTo(vec3) + (dx + dy) * 1.0E-5;
                    if (d3 < d0) {
                        d0 = d3;
                        vec32 = vec33;
                    }
                }
            }

            return vec32 == null ? vec : vec32;
        }
        return vec;
    }

    public static boolean a(Vector2f vec2, Entity entity, double var2, boolean var4) {
        return a(vec2, entity, var2, var4, 0.0F);
    }

    public static boolean a(Vector2f vec2, Entity entity, double var2, boolean var4, float var5) {
        MovingObjectPosition movingobjectposition = RayCastUtil.rayCast(vec2, var2, var5, aEg.thePlayer, var4);
        return movingobjectposition != null && movingobjectposition.typeOfHit == MovingObjectType.ENTITY && movingobjectposition.entityHit == entity;
    }

    private static Vec3 h(AxisAlignedBB box) {
        double dy = box.maxY - box.minY;
        double d1 = box.minY + Math.max(0.0, Math.min(aEg.thePlayer.posY - box.minY + aEg.thePlayer.getEyeHeight(), dy * 0.9));
        return new Vec3(box.minX + (box.maxX - box.minX) / 2.0, d1, box.minZ + (box.maxZ - box.minZ) / 2.0);
    }

    private static List<Vec3> a(AxisAlignedBB box, Vec3 vec) {
        double[] adouble = new double[]{0.05, 0.25, 0.5, 0.75, 0.95};
        double[] adouble1 = new double[]{0.05, 0.2, 0.35, 0.5, 0.65, 0.8, 0.95};
        ArrayList arraylist = new ArrayList(adouble.length * adouble1.length * 4 + adouble.length * adouble.length * 2 + 2);
        arraylist.add(vec);
        arraylist.add(h(box));

        for (double d0 : adouble1) {
            for (double d1 : adouble) {
                arraylist.add(a(box, 0.01, d0, d1));
                arraylist.add(a(box, 0.99, d0, d1));
                arraylist.add(a(box, d1, d0, 0.01));
                arraylist.add(a(box, d1, d0, 0.99));
            }
        }

        for (double d2 : adouble) {
            for (double d3 : adouble) {
                arraylist.add(a(box, d2, 0.02, d3));
                arraylist.add(a(box, d2, 0.98, d3));
            }
        }

        return arraylist;
    }

    private static Vec3 a(AxisAlignedBB box, double var1, double var3, double var5) {
        return new Vec3(box.minX + (box.maxX - box.minX) * var1, box.minY + (box.maxY - box.minY) * var3, box.minZ + (box.maxZ - box.minZ) * var5);
    }

    public static Vec3 i(AxisAlignedBB box) {
        Vec3 vec3 = aEg.thePlayer.getPositionEyes(1.0F);
        return new Vec3(
            c(vec3.xCoord, box.minX + 0.03, box.maxX - 0.03),
            c(vec3.yCoord, box.minY + 0.03, box.maxY - 0.03),
            c(vec3.zCoord, box.minZ + 0.03, box.maxZ - 0.03)
        );
    }

    private static double c(double var0, double var2, double var4) {
        return var2 > var4 ? (var2 + var4) / 2.0 : Math.max(var2, Math.min(var4, var0));
    }

    private static boolean z(Entity entity) {
        return entity != null && entity.getEntityBoundingBox() != null && !entity.getEntityBoundingBox().hasNaN() && af(entity.posX) && af(entity.posY) && af(entity.posZ);
    }

    private static boolean af(double var0) {
        return !Double.isNaN(var0) && !Double.isInfinite(var0);
    }

    public static Vector2f a(Vec3 vec, EnumFacing facing) {
        return a(new Vector3d(vec.xCoord, vec.yCoord, vec.zCoord), facing);
    }

    public static Vector2f h(Vec3 vec) {
        return c(aEg.thePlayer.Ty().v(0.0, aEg.thePlayer.getEyeHeight(), 0.0), new Vector3d(vec.xCoord, vec.yCoord, vec.zCoord));
    }

    public static Vector2f s(BlockPos pos) {
        return c(aEg.thePlayer.Ty().v(0.0, aEg.thePlayer.getEyeHeight(), 0.0), new Vector3d(pos.getX(), pos.getY(), pos.getZ()).v(0.5, 0.5, 0.5));
    }

    public static Vector2f d(Vector3d var0) {
        return c(aEg.thePlayer.Ty().v(0.0, aEg.thePlayer.getEyeHeight(), 0.0), var0);
    }

    public static Vector2f a(Vector3d var0, EnumFacing facing) {
        double d0 = var0.getX() + 0.5;
        double d1 = var0.getY() + 0.5;
        double d2 = var0.getZ() + 0.5;
        double d3 = d0 + facing.getDirectionVec().getX() * 0.5;
        double d4 = d1 + facing.getDirectionVec().getY() * 0.5;
        double d5 = d2 + facing.getDirectionVec().getZ() * 0.5;
        return d(new Vector3d(d3, d4, d5));
    }

    public static Vector2f m(Vector2f vec2) {
        Vector2f vector2f = aEg.thePlayer.getPreviousRotation();
        float f = (float)(aEg.gameSettings.mouseSensitivity * (1.0 + Math.random() / 1000000.0) * 0.6F + 0.2F);
        double d0 = f * f * f * 8.0F * 0.15;
        float f1 = vector2f.x + (float)(Math.round((vec2.x - vector2f.x) / d0) * d0);
        float f2 = vector2f.y + (float)(Math.round((vec2.y - vector2f.y) / d0) * d0);
        return new Vector2f(f1, MathHelper.clamp_float(f2, -90.0F, 90.0F));
    }

    public static Vector2f applySensitivityPatch(Vector2f vec2, Vector2f var1) {
        float f = (float)(aEg.gameSettings.mouseSensitivity * (1.0 + Math.random() / 1000000.0) * 0.6F + 0.2F);
        double d0 = f * f * f * 8.0F * 0.15;
        float f1 = var1.x + (float)(Math.round((vec2.x - var1.x) / d0) * d0);
        float f2 = var1.y + (float)(Math.round((vec2.y - var1.y) / d0) * d0);
        return new Vector2f(f1, MathHelper.clamp_float(f2, -90.0F, 90.0F));
    }

    public static Vector2f n(Vector2f vec2) {
        Vector2f vector2f = aEg.thePlayer.getPreviousRotation();
        float f = vector2f.x + MathHelper.wrapAngleTo180_float(vec2.x - vector2f.x);
        float f1 = MathHelper.clamp_float(vec2.y, -90.0F, 90.0F);
        return new Vector2f(f, f1);
    }

    public static Vector2f o(Vector2f vec2) {
        if (vec2 == null) {
            return null;
        }

        float f = vec2.x + MathHelper.wrapAngleTo180_float(aEg.thePlayer.pl - vec2.x);
        float rotationPitch = aEg.thePlayer.rotationPitch;
        return new Vector2f(f, rotationPitch);
    }

    public static Vector2f d(Vector2f vec2, double var1) {
        return a(RotationComponent.fl, vec2, var1);
    }

    public static Vector2f a(Vector2f vec2, Vector2f var1, double var2) {
        if (var2 != 0.0) {
            double wrapped = MathHelper.wrapAngleTo180_float(var1.x - vec2.x);
            double d1 = var1.y - vec2.y;
            double d2 = Math.sqrt(wrapped * wrapped + d1 * d1);
            if (d2 < 1.0E-4) {
                return new Vector2f(0.0F, 0.0F);
            }

            double d3 = Math.abs(wrapped / d2);
            double d4 = Math.abs(d1 / d2);
            double d5 = var2 * d3;
            double d6 = var2 * d4;
            float f = (float)Math.max(Math.min(wrapped, d5), -d5);
            float f1 = (float)Math.max(Math.min(d1, d6), -d6);
            return new Vector2f(f, f1);
        }
        return new Vector2f(0.0F, 0.0F);
    }

    public static Vector2f e(Vector2f vec2, double var1) {
        return b(RotationComponent.fl, vec2, var1);
    }

    public static Vector2f b(Vector2f vec2, Vector2f var1, double var2) {
        float f = var1.x;
        float f1 = var1.y;
        float f2 = vec2.x;
        float f3 = vec2.y;
        if (var2 != 0.0) {
            Vector2f vector2f = d(var1, var2);
            f = f2 + vector2f.x;
            f1 = f3 + vector2f.y;

            for (int i = 1; i <= (int)(Minecraft.getDebugFPS() / 20.0F + Math.random() * 10.0); i++) {
                if (Math.abs(vector2f.x) + Math.abs(vector2f.y) > 1.0E-4) {
                    f = (float)(f + (Math.random() - 0.5) / 1000.0);
                    f1 = (float)(f1 - Math.random() / 200.0);
                }

                Vector2f vector2f1 = m(new Vector2f(f, f1));
                f = vector2f1.x;
                f1 = Math.max(-90.0F, Math.min(90.0F, vector2f1.y));
            }
        }

        return new Vector2f(f, f1);
    }

    @Generated
    private RotationUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
