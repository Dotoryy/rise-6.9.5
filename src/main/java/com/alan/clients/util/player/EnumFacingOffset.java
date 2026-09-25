package com.alan.clients.util.player;

import lombok.Generated;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;

public class EnumFacingOffset {
    public EnumFacing enumFacing;
    private final Vec3 offset;

    public EnumFacingOffset(EnumFacing facing, Vec3 vec) {
        this.enumFacing = facing;
        this.offset = vec;
    }

    @Generated
    public EnumFacing getEnumFacing() {
        return this.enumFacing;
    }

    @Generated
    public Vec3 getOffset() {
        return this.offset;
    }
}
