package com.alan.clients.module.impl.render.bedplates;

import net.minecraft.util.BlockPos;

public class BedPosition {
    private final BlockPos position;

    public BedPosition(BlockPos pos) {
        this.position = pos;
    }

    public BlockPos getPosition() {
        return this.position;
    }
}
