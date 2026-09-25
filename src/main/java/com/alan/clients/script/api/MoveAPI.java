package com.alan.clients.script.api;

import com.alan.clients.script.api.wrapper.impl.vector.ScriptVector2d;
import com.alan.clients.script.api.wrapper.impl.vector.ScriptVector3d;
import com.alan.clients.util.interfaces.InstanceAccess;
import com.alan.clients.util.player.MoveUtil;
import com.alan.clients.util.vector.Vector3d;
import javax.vecmath.Vector2d;

public class MoveAPI implements InstanceAccess {
    public MoveAPI() {
    }

    public boolean isMoving() {
        return MoveUtil.isMoving();
    }

    public boolean enoughMovementForSprinting() {
        return MoveUtil.enoughMovementForSprinting();
    }

    public boolean canSprint(boolean var1) {
        return MoveUtil.canSprint(var1);
    }

    public double speed() {
        return MoveUtil.speed();
    }

    public double movementDelta() {
        return MoveUtil.movementDelta();
    }

    public double getBaseMoveSpeed() {
        return MoveUtil.vd();
    }

    public double getAllowedHorizontalDistance() {
        return MoveUtil.getAllowedHorizontalDistance();
    }

    public double getLastVelocityDelta() {
        return MoveUtil.getLastVelocityDelta();
    }

    public void strafe() {
        MoveUtil.strafe();
    }

    public void strafe(double var1) {
        MoveUtil.strafe(var1);
    }

    public void strafeNoFriction(double var1) {
        MoveUtil.strafeNoFriction(var1);
    }

    public void stop() {
        MoveUtil.stop();
    }

    public void forward(double var1) {
        MoveUtil.forward(var1);
    }

    public double jumpMotion() {
        return MoveUtil.jumpMotion();
    }

    public double jumpBoostMotion(double var1) {
        return MoveUtil.jumpBoostMotion(var1);
    }

    public void jumpRandom(double var1) {
        MoveUtil.jumpRandom(var1);
    }

    public double predictedMotion(double var1) {
        return MoveUtil.predictedMotion(var1);
    }

    public double predictedMotion(double var1, int var3) {
        return MoveUtil.predictedMotion(var1, var3);
    }

    public double direction() {
        return MoveUtil.direction();
    }

    public double wrappedDirection() {
        return MoveUtil.wrappedDirection();
    }

    public double direction(float var1, double var2, double var4) {
        return MoveUtil.direction(var1, var2, var4);
    }

    public float moveYaw(double var1, double var3) {
        return MoveUtil.moveYaw(var1, var3);
    }

    public float getMoveX() {
        return MoveUtil.getMoveX();
    }

    public float getMoveZ() {
        return MoveUtil.getMoveZ();
    }

    public float getMoveX(float var1) {
        return MoveUtil.getMoveX(var1);
    }

    public float getMoveZ(float var1) {
        return MoveUtil.getMoveZ(var1);
    }

    public void partialStrafeMax(double var1) {
        MoveUtil.partialStrafeMax(var1);
    }

    public void partialStrafePercent(double var1) {
        MoveUtil.partialStrafePercent(var1);
    }

    public void moveFlying(double var1) {
        MoveUtil.moveFlying(var1);
    }

    public void moveFlying2(double var1, float var3) {
        MoveUtil.moveFlying2(var1, var3);
    }

    public void preventDiagonalSpeed() {
        MoveUtil.preventDiagonalSpeed();
    }

    public void useDiagonalSpeed() {
        MoveUtil.useDiagonalSpeed();
    }

    public double getMCFriction() {
        return MoveUtil.getMCFriction();
    }

    public double roundToGround(double var1) {
        return MoveUtil.roundToGround(var1);
    }

    public double speedPotionAmp(double var1) {
        return MoveUtil.speedPotionAmp(var1);
    }

    public int depthStriderLevel() {
        return MoveUtil.depthStriderLevel();
    }

    public float fallDistanceForDamage() {
        return MoveUtil.fallDistanceForDamage();
    }

    public ScriptVector2d getMotion(double var1) {
        Vector2d vector2d = MoveUtil.getMotion(var1);
        return new ScriptVector2d(vector2d.x, vector2d.y);
    }

    public ScriptVector2d moveFlyingVec(float var1, float var2, boolean var3, float var4, boolean var5) {
        com.alan.clients.util.vector.Vector2d vector2d = MoveUtil.moveFlyingVec(var1, var2, var3, var4, var5);
        return vector2d == null ? null : new ScriptVector2d(vector2d.x, vector2d.y);
    }

    public Double moveFlyingSpeed(float var1, float var2, boolean var3, float var4, boolean var5) {
        return MoveUtil.moveFlyingSpeed(var1, var2, var3, var4, var5);
    }

    public Double moveFlyingSpeed(boolean var1) {
        return MoveUtil.moveFlyingSpeed(var1);
    }

    public double moveMaxFlying(boolean var1) {
        return MoveUtil.moveMaxFlying(var1);
    }

    public float simulationStrafeAngle(float var1, float var2) {
        return MoveUtil.simulationStrafeAngle(var1, var2);
    }

    public float simulationStrafe(float var1) {
        return MoveUtil.simulationStrafe(var1);
    }

    public ScriptVector3d predictedPosition() {
        Vector3d aka = MoveUtil.ve();
        return new ScriptVector3d(aka.x, aka.y, aka.z);
    }

    public double getWalkSpeed() {
        return 0.221;
    }

    public double getBunnySlope() {
        return 0.66;
    }

    public double getModSprinting() {
        return 1.3F;
    }

    public double getModSneak() {
        return 0.3F;
    }

    public double getModIce() {
        return 2.5;
    }

    public double getModWeb() {
        return 0.4751131221719457;
    }

    public double getJumpHeight() {
        return 0.42F;
    }

    public double getBunnyFriction() {
        return 159.9F;
    }

    public double getYOnGroundMin() {
        return 1.0E-5;
    }

    public double getYOnGroundMax() {
        return 0.0626;
    }

    public double getAirFriction() {
        return 0.98F;
    }

    public double getWaterFriction() {
        return 0.8F;
    }

    public double getLavaFriction() {
        return 0.5;
    }

    public double getModSwim() {
        return 0.5203620003898759;
    }

    public double getUnloadedChunkMotion() {
        return -0.09800000190735147;
    }

    public double getHeadHitterMotion() {
        return -0.0784000015258789;
    }
}
