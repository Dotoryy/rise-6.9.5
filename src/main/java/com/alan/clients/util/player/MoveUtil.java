package com.alan.clients.util.player;

import com.alan.clients.Client;
import com.alan.clients.module.impl.player.Scaffold;
import com.alan.clients.newevent.impl.input.MoveInputEvent;
import com.alan.clients.newevent.impl.other.MoveEvent;
import com.alan.clients.util.interfaces.InstanceAccess;
import com.alan.clients.util.vector.Vector2f;
import com.alan.clients.util.value.ConstantManager;
import com.alan.clients.util.math.MathUtil;
import com.alan.clients.util.player.PlayerUtil;
import com.alan.clients.util.vector.Vector3d;
import java.util.Arrays;
import javax.vecmath.Vector2d;
import lombok.Generated;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.network.NetworkManager;
import net.minecraft.potion.Potion;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovementInput;
import net.minecraft.util.MovementInputFromOptions;

public final class MoveUtil implements InstanceAccess {
    private static float aOp;
    private static float aOq;
    public static final double aOr = 0.221;
    public static final double aOs = 0.66;
    public static final double aOt = 1.3F;
    public static final double aOu = 0.3F;
    public static final double aOv = 2.5;
    public static final double aOw = 0.4751131221719457;
    public static final double aOx = 0.42F;
    public static final double aOy = 159.9F;
    public static final double aOz = 1.0E-5;
    public static final double aOA = 0.0626;
    public static final double aOB = 0.015625;
    public static final double aOC = 0.98F;
    public static final double aOD = 0.8F;
    public static final double aOE = 0.5;
    public static final double aOF = 0.5203620003898759;
    public static final double[] MOD_DEPTH_STRIDER = new double[]{1.0, 1.4304347400741908, 1.7347825295420372, 1.9217390955733897};
    public static final double aOH = -0.09800000190735147;
    public static final double aOI = -0.0784000015258789;

    public static boolean isMoving() {
        return aEg.thePlayer.moveForward != 0.0F || aEg.thePlayer.moveStrafing != 0.0F;
    }

    public static boolean enoughMovementForSprinting() {
        return Math.abs(aEg.thePlayer.moveForward) >= 0.8F || Math.abs(aEg.thePlayer.moveStrafing) >= 0.8F;
    }

    public static boolean canSprint(boolean var0) {
        return var0
            ? aEg.thePlayer.moveForward >= 0.8F
                && !aEg.thePlayer.isCollidedHorizontally
                && (aEg.thePlayer.getFoodStats().getFoodLevel() > 6 || aEg.thePlayer.capabilities.allowFlying)
                && !aEg.thePlayer.isPotionActive(Potion.blindness)
                && !aEg.thePlayer.isUsingItem()
                && !aEg.thePlayer.isSneaking()
            : enoughMovementForSprinting();
    }

    public static double movementDelta() {
        return Math.hypot(aEg.thePlayer.posX - aEg.thePlayer.prevPosX, aEg.thePlayer.posZ - aEg.thePlayer.prevPosZ);
    }

    public static double speedPotionAmp(double var0) {
        return aEg.thePlayer.isPotionActive(Potion.moveSpeed) ? (aEg.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1) * var0 : 0.0;
    }

    public static double jumpMotion() {
        return jumpBoostMotion(0.42F);
    }

    public static double jumpBoostMotion(double var0) {
        return aEg.thePlayer.isPotionActive(Potion.jump) ? var0 + (aEg.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.1F : var0;
    }

    public static Vector2d getMotion(double var0) {
        MovementInput movementinput = aEg.thePlayer.movementInput;
        double d0 = movementinput.moveForward;
        double d1 = movementinput.moveStrafe;
        double d2 = aEg.thePlayer.pp;
        if (d0 == 0.0 && d1 == 0.0) {
            return new Vector2d(0.0, 0.0);
        }

        if (d1 > 0.0) {
            d1 = 1.0;
        } else if (d1 < 0.0) {
            d1 = -1.0;
        }

        if (d0 != 0.0) {
            if (d1 > 0.0) {
                d2 += d0 > 0.0 ? -45.0 : 45.0;
            } else if (d1 < 0.0) {
                d2 += d0 > 0.0 ? 45.0 : -45.0;
            }

            d1 = 0.0;
            if (d0 > 0.0) {
                d0 = 1.0;
            } else if (d0 < 0.0) {
                d0 = -1.0;
            }
        }

        double cos = Math.cos(Math.toRadians(d2 + ConstantManager.J));
        double sin = Math.sin(Math.toRadians(d2 + ConstantManager.J));
        return new Vector2d(d0 * var0 * cos + d1 * var0 * sin, d0 * var0 * sin - d1 * var0 * cos);
    }

    public static int depthStriderLevel() {
        return EnchantmentHelper.getDepthStriderModifier(aEg.thePlayer);
    }

    public static float fallDistanceForDamage() {
        float f = 3.0F;
        if (aEg.thePlayer.isPotionActive(Potion.jump)) {
            int i = aEg.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier();
            f += i + 1;
        }

        return f;
    }

    public static double roundToGround(double var0) {
        return Math.round(var0 / 0.015625) * 0.015625;
    }

    public static double predictedMotion(double var0) {
        return (var0 - 0.08) * 0.98F;
    }

    public static void forward(double var0) {
        double d0 = direction();
        aEg.thePlayer.motionX = -Math.sin(d0) * var0;
        aEg.thePlayer.motionZ = Math.cos(d0) * var0;
    }

    public static double predictedMotion(double var0, int var2) {
        if (var2 == 0) {
            return var0;
        }

        double d0 = var0;

        for (int i = 0; i < var2; i++) {
            d0 = (d0 - 0.08) * 0.98F;
        }

        return d0;
    }

    public static float getMoveX(float var0) {
        return (float)(-Math.sin(Math.toRadians(var0)));
    }

    public static float getMoveZ(float var0) {
        return (float)Math.cos(Math.toRadians(var0));
    }

    public static float getMoveX() {
        return getMoveX(aEg.thePlayer.pp);
    }

    public static float getMoveZ() {
        return getMoveZ(aEg.thePlayer.pp);
    }

    public static double getLastVelocityDelta() {
        return Math.hypot(aEg.thePlayer.crI, aEg.thePlayer.crI);
    }

    public static double vd() {
        double d0 = 0.2873;
        if (aEg.thePlayer.isPotionActive(Potion.moveSpeed)) {
            d0 *= 1.0 + 0.2 * (aEg.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1);
        }

        return d0;
    }

    public static double getAllowedHorizontalDistance() {
        boolean flag = false;
        double d0;
        if (aEg.thePlayer.isInWeb) {
            d0 = 0.105;
        } else if (PlayerUtil.vj()) {
            d0 = 0.11500000208616258;
            int i = depthStriderLevel();
            if (i > 0) {
                d0 *= MOD_DEPTH_STRIDER[i];
                flag = true;
            }
        } else if (aEg.thePlayer.isSneaking()) {
            d0 = 0.0663000026345253;
        } else {
            d0 = 0.221;
            flag = true;
        }

        if (flag) {
            if (canSprint(false)) {
                d0 *= 1.3F;
            }

            Scaffold scaffold = Client.a.g().c(Scaffold.class);
            if (aEg.thePlayer.isPotionActive(Potion.moveSpeed)
                && aEg.thePlayer.getActivePotionEffect(Potion.moveSpeed).duration > 0
                && (!scaffold.isEnabled() || !scaffold.agt.wo())) {
                d0 *= 1.0 + 0.2 * (aEg.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1);
            }

            if (aEg.thePlayer.isPotionActive(Potion.moveSlowdown)) {
                d0 = 0.29;
            }
        }

        return d0;
    }

    public static void jumpRandom(double var0) {
        aEg.thePlayer.motionY = var0 + Math.random() / 500.0;
    }

    public static void strafe() {
        a(speed(), aEg.thePlayer);
    }

    public static void u(Entity entity) {
        a(speed(), entity);
    }

    public static void strafe(double var0) {
        a(var0, aEg.thePlayer);
    }

    public static void a(double var0, Entity entity) {
        if (isMoving()) {
            double d0 = direction();
            entity.motionX = -MathHelper.sin((float)d0) * var0;
            entity.motionZ = MathHelper.cos((float)d0) * var0;
        }
    }

    public static void a(double var0, float var2) {
        if (isMoving()) {
            var2 = (float)Math.toRadians(var2);
            aEg.thePlayer.motionX = -MathHelper.sin(var2) * var0;
            aEg.thePlayer.motionZ = MathHelper.cos(var2) * var0;
        }
    }

    public static void stop() {
        aEg.thePlayer.motionX = 0.0;
        aEg.thePlayer.motionZ = 0.0;
    }

    public static double direction() {
        float f = aEg.thePlayer.pp;
        if (aEg.thePlayer.moveForward < 0.0F) {
            f += 180.0F;
        }

        float f1 = 1.0F;
        if (aEg.thePlayer.moveForward < 0.0F) {
            f1 = -0.5F;
        } else if (aEg.thePlayer.moveForward > 0.0F) {
            f1 = 0.5F;
        }

        if (aEg.thePlayer.moveStrafing > 0.0F) {
            f -= 90.0F * f1;
        }

        if (aEg.thePlayer.moveStrafing < 0.0F) {
            f += 90.0F * f1;
        }

        return Math.toRadians(f);
    }

    public static float moveYaw(double var0, double var2) {
        return var0 == 0.0 && var2 == 0.0 ? aEg.thePlayer.pl : (float)(Math.toDegrees(Math.atan2(var2, var0)) - 90.0);
    }

    public static double m(MoveInputEvent event) {
        float f = aEg.thePlayer.pp;
        if (event.getForward() < 0.0F) {
            f += 180.0F;
        }

        float f1 = 1.0F;
        if (event.getForward() < 0.0F) {
            f1 = -0.5F;
        } else if (event.getForward() > 0.0F) {
            f1 = 0.5F;
        }

        if (event.getStrafe() > 0.0F) {
            f -= 70.0F * f1;
        }

        if (event.getStrafe() < 0.0F) {
            f += 70.0F * f1;
        }

        return Math.toRadians(f);
    }

    public static double g(float var0, float var1) {
        float f = aEg.thePlayer.pp;
        if (var0 < 0.0F) {
            f += 180.0F;
        }

        float f1 = 1.0F;
        if (var0 < 0.0F) {
            f1 = -0.5F;
        } else if (var0 > 0.0F) {
            f1 = 0.5F;
        }

        if (var1 > 0.0F) {
            f -= 70.0F * f1;
        }

        if (var1 < 0.0F) {
            f += 70.0F * f1;
        }

        return Math.toRadians(f);
    }

    public static double wrappedDirection() {
        float f = aEg.thePlayer.pp;
        if (aEg.thePlayer.moveForward < 0.0F && aEg.thePlayer.moveStrafing == 0.0F) {
            f += 180.0F;
        }

        if (aEg.thePlayer.moveStrafing > 0.0F) {
            f -= 90.0F;
        }

        if (aEg.thePlayer.moveStrafing < 0.0F) {
            f += 90.0F;
        }

        return Math.toRadians(f);
    }

    public static double direction(float var0, double var1, double var3) {
        if (var1 < 0.0) {
            var0 += 180.0F;
        }

        float f = 1.0F;
        if (var1 < 0.0) {
            f = -0.5F;
        } else if (var1 > 0.0) {
            f = 0.5F;
        }

        if (var3 > 0.0) {
            var0 -= 90.0F * f;
        }

        if (var3 < 0.0) {
            var0 += 90.0F * f;
        }

        return Math.toRadians(var0);
    }

    public static double speed() {
        return Math.hypot(aEg.thePlayer.motionX, aEg.thePlayer.motionZ);
    }

    public static void setMoveEvent(MoveEvent moveEvent, double var1) {
        setMoveEvent(moveEvent, var1, aEg.thePlayer.pp, aEg.thePlayer.movementInput.moveStrafe, aEg.thePlayer.movementInput.moveForward);
    }

    public static void setMoveEvent(MoveEvent moveEvent, double var1, float var3, double var4, double var6) {
        double d0 = var6;
        double d1 = var4;
        float f = var3;
        if (d0 != 0.0) {
            if (d1 > 0.0) {
                f += d0 > 0.0 ? -45.0F : 45.0F;
            } else if (d1 < 0.0) {
                f += d0 > 0.0 ? 45.0F : -45.0F;
            }

            d1 = 0.0;
            if (d0 > 0.0) {
                d0 = 1.0;
            } else if (d0 < 0.0) {
                d0 = -1.0;
            }
        }

        double cos = Math.cos(Math.toRadians(f + ConstantManager.J));
        double sin = Math.sin(Math.toRadians(f + ConstantManager.J));
        moveEvent.setPosX(d0 * var1 * cos + d1 * var1 * sin);
        moveEvent.setPosZ(d0 * var1 * sin - d1 * var1 * cos);
    }

    public static void fixMovement(MoveInputEvent moveInputEvent, float var1) {
        float f = moveInputEvent.getForward();
        float strafe = moveInputEvent.getStrafe();
        double d0 = MathHelper.wrapAngleTo180_double(Math.toDegrees(direction(aEg.thePlayer.pl, f, strafe)));
        if (f != 0.0F || strafe != 0.0F) {
            float f2 = 0.0F;
            float f3 = 0.0F;
            float f4 = Float.MAX_VALUE;

            for (float f5 = -1.0F; f5 <= 1.0F; f5++) {
                for (float f6 = -1.0F; f6 <= 1.0F; f6++) {
                    if (f6 != 0.0F || f5 != 0.0F) {
                        double d1 = MathHelper.wrapAngleTo180_double(Math.toDegrees(direction(var1, f5, f6)));
                        double d2 = MathUtil.n(d0, d1);
                        if (d2 < f4) {
                            f4 = (float)d2;
                            f2 = f5;
                            f3 = f6;
                        }
                    }
                }
            }

            if (f3 == -1.0F) {
            } else {
            }

            aOp = f3;
            aOq = f2;
            moveInputEvent.setForward(f2);
            moveInputEvent.setStrafe(f3);
        }
    }

    public static double getMCFriction() {
        float f = 0.91F;
        if (aEg.thePlayer.onGround) {
            f = aEg.theWorld
                    .getBlockState(
                        new BlockPos(
                            MathHelper.floor_double(aEg.thePlayer.posX),
                            MathHelper.floor_double(aEg.thePlayer.getEntityBoundingBox().minY) - 1,
                            MathHelper.floor_double(aEg.thePlayer.posZ)
                        )
                    )
                    .getBlock()
                    .slipperiness
                * 0.91F;
        }

        return f;
    }

    public static double[] moveFlying(float var0, float var1, boolean var2, float var3, boolean var4) {
        float f = 0.02F;
        float f1 = aEg.thePlayer.getAIMoveSpeed();
        if (var2) {
            f = f1 / 2.0F * 0.9999998F;
        }

        if (var4) {
            f = (float)(f + (var2 ? f1 / 2.0F : 0.02F) * 0.3);
        }

        float f5 = var0 * var0 + var1 * var1;
        if (f5 >= 1.0E-4F) {
            f5 = MathHelper.sqrt_float(f5);
            if (f5 < 1.0F) {
                f5 = 1.0F;
            }

            f5 = f / f5;
            var0 *= f5;
            var1 *= f5;
            float sin = MathHelper.sin(var3 * (float)ConstantManager.aHb / 180.0F);
            float cos = MathHelper.cos(var3 * (float)ConstantManager.aHb / 180.0F);
            double d0 = var0 * cos - var1 * sin;
            double d1 = var1 * cos + var0 * sin;
            return new double[]{d0, d1};
        }
        return null;
    }

    public static com.alan.clients.util.vector.Vector2d moveFlyingVec(float var0, float var1, boolean var2, float var3, boolean var4) {
        double[] adouble = moveFlying(var0, var1, var2, var3, var4);
        return adouble == null ? null : new com.alan.clients.util.vector.Vector2d(adouble[0], adouble[1]);
    }

    public static Double moveFlyingSpeed(float var0, float var1, boolean var2, float var3, boolean var4) {
        double[] adouble = moveFlying(var0, var1, var2, var3, var4);
        return adouble == null ? null : Math.hypot(adouble[0], adouble[1]);
    }

    public static Double moveFlyingSpeed(boolean var0) {
        double[] adouble = moveFlying(0.98F, 0.98F, aEg.thePlayer.onGround, 180.0F, var0);
        return adouble == null ? null : Math.hypot(adouble[0], adouble[1]);
    }

    public static void partialStrafeMax(double var0) {
        b(var0, aEg.thePlayer);
    }

    public static void b(double var0, Entity entity) {
        double d0 = entity.motionX;
        double d1 = entity.motionZ;
        u(entity);
        entity.motionX = d0 + Math.max(-var0, Math.min(var0, entity.motionX - d0));
        entity.motionZ = d1 + Math.max(-var0, Math.min(var0, entity.motionZ - d1));
    }

    public static void partialStrafePercent(double var0) {
        var0 /= 100.0;
        var0 = Math.min(1.0, Math.max(0.0, var0));
        double d0 = aEg.thePlayer.motionX;
        double d1 = aEg.thePlayer.motionZ;
        strafe();
        aEg.thePlayer.motionX = d0 + (aEg.thePlayer.motionX - d0) * var0;
        aEg.thePlayer.motionZ = d1 + (aEg.thePlayer.motionZ - d1) * var0;
    }

    public static double moveMaxFlying(boolean var0) {
        float f = 0.02F;
        float f1 = aEg.thePlayer.getAIMoveSpeed() / 2.0F;
        float f2 = 0.98F;
        float f3 = 0.98F;
        float f4 = 180.0F;
        if (var0) {
            f = f1 * 0.9999998F;
        }

        f = (float)(f + (var0 ? f1 : 0.02F) * 0.3);
        float f8 = f2 * f2 + f3 * f3;
        if (f8 >= 1.0E-4F) {
            f8 = (float)Math.sqrt(f8);
            if (f8 < 1.0F) {
                f8 = 1.0F;
            }

            f8 = f / f8;
            f2 *= f8;
            f3 *= f8;
            float sin = MathHelper.sin(f4 * (float)ConstantManager.aHb / 180.0F);
            float cos = MathHelper.cos(f4 * (float)ConstantManager.aHb / 180.0F);
            double d0 = f2 * cos - f3 * sin;
            double d1 = f3 * cos + f2 * sin;
            return Math.hypot(d0, d1);
        }
        return 0.0;
    }

    public static float simulationStrafeAngle(float var0, float var1) {
        float degrees = (float)Math.toDegrees(direction());
        if (Math.abs(var0 - degrees) <= var1) {
            var0 = degrees;
        } else if (var0 > degrees) {
            var0 -= var1;
        } else {
            var0 += var1;
        }

        float f = var0;
        a(speed(), f);
        return f;
    }

    public static float simulationStrafe(float var0) {
        double d0 = 0.02599999835384377;
        double d1 = 0.91F;
        double d2 = aEg.thePlayer.He * d1;
        double d3 = aEg.thePlayer.Hg * d1;
        float f = var0;
        float degrees = (float)Math.toDegrees(direction());

        for (int i = 0; i <= 360; i++) {
            a(speed(), var0);
            double d4 = Math.abs(aEg.thePlayer.motionX);
            double d5 = Math.abs(aEg.thePlayer.motionZ);
            double d6 = d2 - d0;
            double d7 = d3 - d0;
            if (var0 == degrees || d4 < d6 || d5 < d7) {
                break;
            }

            f = var0;
            if (Math.abs(var0 - degrees) <= 1.0F) {
                var0 = degrees;
            } else if (var0 > degrees) {
                var0--;
            } else {
                var0++;
            }
        }

        a(speed(), f);
        return f;
    }

    public static void preventDiagonalSpeed() {
        KeyBinding[] akeybinding = new KeyBinding[]{
            aEg.gameSettings.keyBindForward, aEg.gameSettings.keyBindRight, aEg.gameSettings.keyBindBack, aEg.gameSettings.keyBindLeft
        };
        int[] aint = new int[]{0};
        Arrays.stream(akeybinding).forEach(var1x -> aint[0] += var1x.isKeyDown() ? 1 : 0);
        boolean flag = aint[0] == 1;
        if (!flag) {
            double d0 = 0.0026000750109401644;
            double d1 = 5.199896488849598E-4;
            double d2 = aEg.thePlayer.onGround ? d0 : d1;
            moveFlying(-d2);
        }
    }

    public static void useDiagonalSpeed() {
        KeyBinding[] akeybinding = new KeyBinding[]{
            aEg.gameSettings.keyBindForward, aEg.gameSettings.keyBindRight, aEg.gameSettings.keyBindBack, aEg.gameSettings.keyBindLeft
        };
        int[] aint = new int[]{0};
        Arrays.stream(akeybinding).forEach(var1x -> aint[0] += var1x.isKeyDown() ? 1 : 0);
        boolean flag = aint[0] == 1;
        if (flag) {
            double d0 = 0.0026000750109401644;
            double d1 = 5.199896488849598E-4;
            double d2 = aEg.thePlayer.onGround ? d0 : d1;
            moveFlying(d2);
        }
    }

    public static void c(double var0, Entity entity) {
        if (isMoving()) {
            double d0 = getMCFriction();
            double d1 = d0 > 1.0E-6 ? var0 / d0 : var0;
            double d2 = direction();
            entity.motionX = -MathHelper.sin((float)d2) * d1;
            entity.motionZ = MathHelper.cos((float)d2) * d1;
        }
    }

    public static void strafeNoFriction(double var0) {
        c(var0, aEg.thePlayer);
    }

    public static void moveFlying(double var0) {
        if (isMoving()) {
            double d0 = direction();
            aEg.thePlayer.motionX = aEg.thePlayer.motionX + -MathHelper.sin((float)d0) * var0;
            aEg.thePlayer.motionZ = aEg.thePlayer.motionZ + MathHelper.cos((float)d0) * var0;
        }
    }

    public static void moveFlying2(double var0, float var2) {
        aEg.thePlayer.motionX = aEg.thePlayer.motionX + -MathHelper.sin((float)Math.toRadians(var2)) * var0;
        aEg.thePlayer.motionZ = aEg.thePlayer.motionZ + MathHelper.cos((float)Math.toRadians(var2)) * var0;
    }

    public static Vector3d a(Entity entity, Vector2f vec2, int var2, boolean var3) {
        double dx = entity.posX - entity.lastTickPosX;
        double dz = entity.posZ - entity.lastTickPosZ;
        return a(new Vector3d(entity.posX, entity.posY, entity.posZ), new Vector3d(dx, 0.0, dz), entity.pl, vec2, var2, var3);
    }

    public static Vector3d a(Vector3d var0, Vector3d var1, float var2, Vector2f vec2) {
        return a(var0, var1, var2, vec2, 1, vec2.x != 0.0F || vec2.y != 0.0F);
    }

    public static Vector3d a(Vector3d var0, Vector3d var1, float var2, Vector2f vec2, int var4, boolean var5) {
        for (int i = 0; i <= var4; i++) {
            var1.z *= 0.54600006F;
            var1.x *= 0.54600006F;
            double[] adouble = moveFlying(vec2.x, vec2.y, true, var2, var5);
            if (adouble == null) {
                return var0;
            }

            var1.x = var1.x + adouble[0];
            var1.y = 0.0;
            var1.z = var1.z + adouble[1];
            var0 = var0.e(var1);
        }

        return var0;
    }

    public static Vector3d ve() {
        EntityPlayerSP entityplayersp = new EntityPlayerSP(
            aEg,
            aEg.theWorld,
            new NetHandlerPlayClient(
                aEg, aEg.currentScreen, new NetworkManager(aEg.getNetHandler().getNetworkManager().direction), aEg.thePlayer.getGameProfile()
            ),
            aEg.thePlayer.getStatFileWriter()
        );
        aEg.theWorld.addEntityToWorld(-99999, entityplayersp);
        entityplayersp.motionX = aEg.thePlayer.motionX;
        entityplayersp.motionY = aEg.thePlayer.motionY;
        entityplayersp.motionZ = aEg.thePlayer.motionZ;
        entityplayersp.setPosition(aEg.thePlayer.posX, aEg.thePlayer.posY, aEg.thePlayer.posZ);
        entityplayersp.pl = entityplayersp.pp = aEg.thePlayer.pp;
        entityplayersp.onGround = aEg.thePlayer.onGround;
        entityplayersp.setSprinting(aEg.thePlayer.isSprinting());
        entityplayersp.bjQ = aEg.thePlayer.bjQ;
        entityplayersp.movementInput = new MovementInputFromOptions(aEg.gameSettings);
        entityplayersp.movementInput.updatePlayerMoveState();
        entityplayersp.moveForward = entityplayersp.movementInput.moveForward;
        entityplayersp.moveStrafing = entityplayersp.movementInput.moveStrafe;
        entityplayersp.onUpdate();
        aEg.theWorld.removeEntityFromWorld(-99999);
        if (aEg.gameSettings.keyBindJump.isKeyDown() && aEg.thePlayer.onGround && aEg.thePlayer.jumpTicks <= 1) {
            entityplayersp.setPosition(entityplayersp.posX, entityplayersp.posY + jumpMotion(), entityplayersp.posZ);
        }

        return new Vector3d(entityplayersp.posX, entityplayersp.posY, entityplayersp.posZ);
    }

    @Generated
    private MoveUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
