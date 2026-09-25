package com.alan.clients.module.impl.movement.jesus;

import com.alan.clients.module.impl.movement.Jesus;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.other.BlockAABBEvent;
import com.alan.clients.value.Mode;
import net.minecraft.block.BlockLiquid;
import net.minecraft.util.AxisAlignedBB;

public class VanillaJesus extends Mode<Jesus> {
    @EventLink
    public final Listener<BlockAABBEvent> onBlockAABB = var0 -> {
        if (var0.getBlock() instanceof BlockLiquid && !aEg.gameSettings.keyBindSneak.isKeyDown()) {
            int i = var0.getBlockPos().getX();
            int j = var0.getBlockPos().getY();
            int k = var0.getBlockPos().getZ();
            var0.setBoundingBox(AxisAlignedBB.fromBounds(i, j, k, i + 1, j + 1, k + 1));
        }
    };

    public VanillaJesus(String var1, Jesus jesus) {
        super(var1, jesus);
    }
}
