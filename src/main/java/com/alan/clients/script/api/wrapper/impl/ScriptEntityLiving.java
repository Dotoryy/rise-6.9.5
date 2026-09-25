package com.alan.clients.script.api.wrapper.impl;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;

public class ScriptEntityLiving extends ScriptEntity {
    public EntityLivingBase wrappedLiving;

    public ScriptEntityLiving(EntityLivingBase wrappedLiving) {
        super(wrappedLiving);
        this.wrappedLiving = wrappedLiving;
    }

    private static ScriptEntityLiving tryInstantiate(Entity entity) {
        return entity instanceof EntityLiving ? new ScriptEntityLiving((EntityLiving)entity) : null;
    }

    public boolean isAnimal() {
        return this.wrappedLiving instanceof EntityAnimal;
    }

    public boolean isMob() {
        return this.wrappedLiving instanceof EntityMob;
    }

    public boolean isPlayer() {
        return this.wrappedLiving instanceof EntityPlayer;
    }

    public float getHealth() {
        return this.wrappedLiving.getHealth();
    }

    public float getMaxHealth() {
        return this.wrappedLiving.getMaxHealth();
    }

    public int getHurtTime() {
        return this.wrappedLiving.hurtTime;
    }

    public int getMaxHurtTime() {
        return this.wrappedLiving.maxHurtTime;
    }

    public int getLastHurtTime() {
        return this.wrappedLiving.zf;
    }

    public ScriptItemStack getHeldItemStack() {
        return new ScriptItemStack(this.wrappedLiving.getHeldItem());
    }

    @Override
    public boolean isDead() {
        return this.wrappedLiving.isDead;
    }
}
