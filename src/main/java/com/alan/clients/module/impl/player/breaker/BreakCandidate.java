package com.alan.clients.module.impl.player.breaker;

import java.util.List;
import net.minecraft.util.BlockPos;

public final class BreakCandidate {
    private final BlockPos targetPos;
    public final List<BlockPos> blocksToBreak;
    public final double breakTime;

    public BreakCandidate(BlockPos pos, List<BlockPos> poses, double var3) {
        this.targetPos = pos;
        this.blocksToBreak = poses;
        this.breakTime = var3;
    }
}
