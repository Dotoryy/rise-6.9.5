package com.alan.clients.module.impl.movement.flight;

import com.alan.clients.module.impl.movement.Flight;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.other.BlockAABBEvent;
import com.alan.clients.value.Mode;
import net.minecraft.block.BlockAir;
import net.minecraft.util.AxisAlignedBB;

public final class AirWalkFlight extends Mode<Flight> {
    @EventLink
    public final Listener<BlockAABBEvent> onBlockAABB = var0 -> {
        if (var0.getBlock() instanceof BlockAir && !aEg.thePlayer.isSneaking() && aEg.thePlayer.Zl > 2) {
            double d0 = var0.getBlockPos().getX();
            double d1 = var0.getBlockPos().getY();
            double d2 = var0.getBlockPos().getZ();
            if (d1 < aEg.thePlayer.posY) {
                var0.setBoundingBox(AxisAlignedBB.fromBounds(-15.0, -1.0, -15.0, 15.0, 1.0, 15.0).offset(d0, d1, d2));
            }
        }
    };

    public AirWalkFlight(String var1, Flight flight) {
        super(var1, flight);
    }
}
