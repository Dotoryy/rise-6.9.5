package com.alan.clients.util;

import com.alan.clients.util.interfaces.InstanceAccess;
import com.alan.clients.util.vector.Vector2f;
import com.google.common.base.Predicates;
import com.alan.clients.util.math.MathUtil;
import com.alan.clients.util.rotation.RotationUtil;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

public final class RayCastUtil
implements InstanceAccess {
    public static MovingObjectPosition c(Vector2f vector2f, double d2) {
        return RayCastUtil.rayCast(vector2f, d2, 0.0f);
    }

    public static boolean t(Entity entity) {
        int n2 = 16 * RayCastUtil.aEg.gameSettings.renderDistanceChunks;
        Vector2f vector2f = RotationUtil.y(entity);
        if (MathUtil.n(RayCastUtil.aEg.thePlayer.pl, vector2f.x) > (double)RayCastUtil.aEg.gameSettings.fovSetting) {
            return false;
        }
        if (entity.crB > 100.0 || !(entity instanceof EntityPlayer)) {
            MovingObjectPosition movingObjectPosition = RayCastUtil.rayCast(vector2f, n2, 0.2f);
            if (movingObjectPosition == null) return false;
            if (movingObjectPosition.typeOfHit != MovingObjectPosition.MovingObjectType.ENTITY) return false;
            return true;
        }
        double d2 = 1.0;
        while (d2 >= -1.0) {
            for (double d3 = 1.0; d3 >= -1.0; d3 -= 1.0) {
                for (double d4 = 1.0; d4 >= -1.0; d4 -= 1.0) {
                    MovingObjectPosition movingObjectPosition = RayCastUtil.rayCast(RotationUtil.d(entity.Ty().v((entity.getEntityBoundingBox().maxX - entity.getEntityBoundingBox().minX) * d3, (entity.getEntityBoundingBox().maxY - entity.getEntityBoundingBox().minY) * d2, (entity.getEntityBoundingBox().maxZ - entity.getEntityBoundingBox().minZ) * d4)), n2, 0.2f);
                    if (movingObjectPosition == null || movingObjectPosition.typeOfHit != MovingObjectPosition.MovingObjectType.ENTITY) continue;
                    return true;
                }
            }
            d2 -= 0.5;
        }
        return false;
    }

    public static MovingObjectPosition rayCast(Vector2f vector2f, double d2, float f2) {
        return RayCastUtil.rayCast(vector2f, d2, f2, RayCastUtil.aEg.thePlayer);
    }

    public static MovingObjectPosition rayCast(Vector2f vector2f, double d2, float f2, Entity entity) {
        return RayCastUtil.rayCast(vector2f, d2, f2, entity, false);
    }

    public static MovingObjectPosition rayCast(Vector2f vector2f, double d2, float f2, Entity entity, boolean bl) {
        float f3 = RayCastUtil.aEg.timer.bWm;
        MovingObjectPosition movingObjectPosition = null;
        if (entity == null) return null;
        if (RayCastUtil.aEg.theWorld == null) {
            return null;
        }
        if (!bl) {
            movingObjectPosition = entity.rayTraceCustom(d2, vector2f.x, vector2f.y);
        }
        double d3 = d2;
        Vec3 vec3 = entity.getPositionEyes(f3);
        if (movingObjectPosition != null) {
            d3 = movingObjectPosition.hitVec.distanceTo(vec3);
        }
        Vec3 vec = RayCastUtil.aEg.thePlayer.getVectorForRotation(vector2f.y, vector2f.x);
        Vec3 vec33 = vec3.addVector(vec.xCoord * d2, vec.yCoord * d2, vec.zCoord * d2);
        Entity entity2 = null;
        Vec3 vec34 = null;
        List<Entity> list = RayCastUtil.aEg.theWorld.getEntitiesInAABBexcluding(entity, entity.getEntityBoundingBox().addCoord(vec.xCoord * d2, vec.yCoord * d2, vec.zCoord * d2).expand(1.0, 1.0, 1.0), Predicates.and(EntitySelectors.NOT_SPECTATING, Entity::canBeCollidedWith));
        double d4 = d3;
        for (Entity entity3 : list) {
            double d5;
            float f4 = entity3.getCollisionBorderSize() + f2;
            AxisAlignedBB axisAlignedBB = entity3.getEntityBoundingBox().expand(f4, f4, f4);
            MovingObjectPosition movingObjectPosition2 = axisAlignedBB.calculateIntercept(vec3, vec33);
            if (axisAlignedBB.isVecInside(vec3)) {
                if (!(d4 >= 0.0)) continue;
                entity2 = entity3;
                vec34 = movingObjectPosition2 == null ? vec3 : movingObjectPosition2.hitVec;
                d4 = 0.0;
                continue;
            }
            if (movingObjectPosition2 == null || !((d5 = vec3.distanceTo(movingObjectPosition2.hitVec)) < d4) && d4 != 0.0) continue;
            entity2 = entity3;
            vec34 = movingObjectPosition2.hitVec;
            d4 = d5;
        }
        if (entity2 == null) return movingObjectPosition;
        if (d4 < d3) return new MovingObjectPosition(entity2, vec34);
        if (movingObjectPosition != null) return movingObjectPosition;
        return new MovingObjectPosition(entity2, vec34);
    }

    public static boolean a(Vector2f vector2f, EnumFacing enumFacing, BlockPos blockPos, boolean bl) {
        MovingObjectPosition movingObjectPosition = RayCastUtil.aEg.thePlayer.rayTraceCustom(4.5, vector2f.x, vector2f.y);
        if (movingObjectPosition == null) {
            return false;
        }
        if (movingObjectPosition.hitVec == null) {
            return false;
        }
        if (!movingObjectPosition.getBlockPos().equals(blockPos)) return false;
        if (!bl) return true;
        if (movingObjectPosition.sideHit != enumFacing) return false;
        return true;
    }

    public static boolean overBlock(EnumFacing enumFacing, BlockPos blockPos, boolean bl) {
        MovingObjectPosition movingObjectPosition = RayCastUtil.aEg.objectMouseOver;
        if (movingObjectPosition == null) {
            return false;
        }
        if (movingObjectPosition.hitVec == null) return false;
        if (movingObjectPosition.getBlockPos() == null) {
            return false;
        }
        if (!movingObjectPosition.getBlockPos().equals(blockPos)) return false;
        if (!bl) return true;
        if (movingObjectPosition.sideHit != enumFacing) return false;
        return true;
    }

    public static Boolean overBlock(Vector2f vector2f, BlockPos blockPos) {
        return RayCastUtil.a(vector2f, EnumFacing.UP, blockPos, false);
    }

    public static Boolean a(Vector2f vector2f, BlockPos blockPos, EnumFacing enumFacing) {
        return RayCastUtil.a(vector2f, enumFacing, blockPos, true);
    }
}
