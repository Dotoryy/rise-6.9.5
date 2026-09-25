package com.alan.clients.script.api.wrapper.impl;

import com.alan.clients.script.api.wrapper.ScriptWrapper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;

public class ScriptWorld extends ScriptWrapper<World> {
    public ScriptWorld(World world) {
        super(world);
    }

    public String getDimensionName() {
        return this.wrapped.provider.getDimensionName();
    }

    public int getDimensionId() {
        return this.wrapped.provider.getDimensionId();
    }

    public long getTime() {
        return this.wrapped.getWorldTime();
    }

    public ScriptEntityLiving[] getLivingEntities() {
        return this.wrapped
            .loadedEntityList
            .stream()
            .filter(var0 -> var0 instanceof EntityLivingBase)
            .map(var0 -> new ScriptEntityLiving((EntityLivingBase)var0))
            .toArray(ScriptEntityLiving[]::new);
    }

    public ScriptEntityLiving[] getPlayers() {
        return this.wrapped.playerEntities.stream().map(ScriptEntityLiving::new).toArray(ScriptEntityLiving[]::new);
    }
}
