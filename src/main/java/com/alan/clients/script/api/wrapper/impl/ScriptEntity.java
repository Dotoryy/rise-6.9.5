package com.alan.clients.script.api.wrapper.impl;

import com.alan.clients.script.api.wrapper.ScriptWrapper;
import com.alan.clients.script.api.wrapper.impl.vector.ScriptVector2f;
import com.alan.clients.script.api.wrapper.impl.vector.ScriptVector3d;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;

public class ScriptEntity extends ScriptWrapper<Entity> {
    public ScriptEntity(Entity entity) {
        super(entity);
    }

    static ScriptEntity getById(int var0) {
        return tryInstantiate(MC.theWorld.getEntityByID(var0));
    }

    static ScriptEntity getByName(String var0) {
        return tryInstantiate(MC.theWorld.getPlayerEntityByName(var0));
    }

    private static ScriptEntity tryInstantiate(Entity entity) {
        return new ScriptEntity(entity);
    }

    public boolean isLiving() {
        return this.wrapped instanceof EntityLivingBase;
    }

    public void setYaw(float var1) {
        this.wrapped.pl = var1;
    }

    public void setPitch(float pitch) {
        this.wrapped.rotationPitch = pitch;
    }

    public ScriptVector3d getPosition() {
        return new ScriptVector3d(this.wrapped.posX, this.wrapped.posY, this.wrapped.posZ);
    }

    public ScriptVector3d getLastPosition() {
        return new ScriptVector3d(this.wrapped.lastTickPosX, this.wrapped.lastTickPosY, this.wrapped.lastTickPosZ);
    }

    public ScriptVector3d getMotion() {
        return new ScriptVector3d(this.wrapped.motionX, this.wrapped.motionY, this.wrapped.motionZ);
    }

    public void setMotion(ScriptVector3d motion) {
        this.wrapped.motionX = motion.getX();
        this.wrapped.motionY = motion.getY();
        this.wrapped.motionZ = motion.getZ();
    }

    public void setMotionX(double var1) {
        this.wrapped.motionX = var1;
    }

    public void setMotionY(double var1) {
        this.wrapped.motionY = var1;
    }

    public void setMotionZ(double var1) {
        this.wrapped.motionZ = var1;
    }

    public int getAirTicks() {
        return this.wrapped.tR;
    }

    public int getGroundTicks() {
        return this.wrapped.cqL;
    }

    public boolean isSprinting() {
        return this.wrapped.isSprinting();
    }

    public boolean isSneaking() {
        return this.wrapped.isSneaking();
    }

    public boolean isInvisible() {
        return this.wrapped.isInvisible();
    }

    public boolean isDead() {
        return this.wrapped.isDead;
    }

    public boolean isRiding() {
        return this.wrapped.isRiding();
    }

    public boolean isEating() {
        return this.wrapped.isEating();
    }

    public boolean isBurning() {
        return this.wrapped.isBurning();
    }

    public ScriptVector2f getRotation() {
        return new ScriptVector2f(this.wrapped.pl, this.wrapped.rotationPitch);
    }

    public ScriptVector2f getLastRotation() {
        return new ScriptVector2f(this.wrapped.prevRotationYaw, this.wrapped.prevRotationPitch);
    }

    public int getTicksExisted() {
        return this.wrapped.ticksExisted;
    }

    public int getEntityId() {
        return this.wrapped.getEntityId();
    }

    public String getDisplayName() {
        return this.wrapped.getDisplayName().getUnformattedTextForChat();
    }

    public float getDistanceToEntity(ScriptEntity scriptEntity) {
        float f = (float)(this.wrapped.posX - scriptEntity.getPosition().getX());
        float dy = (float)(this.wrapped.posY - scriptEntity.getPosition().getY());
        float dz = (float)(this.wrapped.posZ - scriptEntity.getPosition().getZ());
        return MathHelper.sqrt_float(f * f + dy * dy + dz * dz);
    }

    public double getDistance(double var1, double var3, double var5) {
        double d0 = this.wrapped.posX - var1;
        double d1 = this.wrapped.posY - var3;
        double d2 = this.wrapped.posZ - var5;
        return MathHelper.sqrt_double(d0 * d0 + d1 * d1 + d2 * d2);
    }
}
