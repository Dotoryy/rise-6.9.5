package com.alan.clients.newevent.impl.other;

import com.alan.clients.newevent.CancellableEvent;
import lombok.Generated;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public final class BlockDamageEvent extends CancellableEvent {
    private EntityPlayerSP player;
    private World world;
    private BlockPos blockPos;

    public BlockDamageEvent(EntityPlayerSP player, World world, BlockPos blockPos) {
        this.player = player;
        this.world = world;
        this.blockPos = blockPos;
    }

    @Generated
    public EntityPlayerSP getPlayer() {
        return this.player;
    }

    @Generated
    public World getWorld() {
        return this.world;
    }

    @Generated
    public BlockPos getBlockPos() {
        return this.blockPos;
    }

    @Generated
    public void setPlayer(EntityPlayerSP player) {
        this.player = player;
    }

    @Generated
    public void setWorld(World world) {
        this.world = world;
    }

    @Generated
    public void setBlockPos(BlockPos blockPos) {
        this.blockPos = blockPos;
    }
}
