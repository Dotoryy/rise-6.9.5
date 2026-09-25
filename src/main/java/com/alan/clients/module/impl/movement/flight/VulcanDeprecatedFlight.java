package com.alan.clients.module.impl.movement.flight;

import com.alan.clients.module.impl.movement.Flight;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.input.KeyboardInputEvent;
import com.alan.clients.newevent.impl.motion.StrafeEvent;
import com.alan.clients.newevent.impl.other.BlockAABBEvent;
import com.alan.clients.newevent.impl.other.TeleportEvent;
import com.alan.clients.value.Mode;
import com.alan.clients.value.impl.NumberValue;
import com.alan.clients.util.vector.Vector3d;
import net.minecraft.block.BlockAir;
import net.minecraft.util.AxisAlignedBB;

public class VulcanDeprecatedFlight extends Mode<Flight> {
    private final NumberValue speed = new NumberValue("Speed", this, 1, 1, 10, 0.1);
    private Vector3d lastTeleportPos;
    private boolean jumped;
    private int timerTicks;
    @EventLink
    public final Listener<StrafeEvent> onStrafe = var1x -> {
        if (this.timerTicks > 0 && !this.jumped) {
            this.timerTicks--;
            aEg.timer.dzD = this.speed.wo().floatValue();
        }
    };
    @EventLink
    public final Listener<BlockAABBEvent> onBlockAABB = var0 -> {
        if (var0.getBlock() instanceof BlockAir && !aEg.thePlayer.isSneaking()) {
            double d0 = var0.getBlockPos().getX();
            double d1 = var0.getBlockPos().getY();
            double d2 = var0.getBlockPos().getZ();
            if (d1 < aEg.thePlayer.posY) {
                var0.setBoundingBox(AxisAlignedBB.fromBounds(-15.0, -1.0, -15.0, 15.0, 1.0, 15.0).offset(d0, d1, d2));
            }
        }
    };
    @EventLink
    public final Listener<TeleportEvent> onTeleport = var1x -> {
        Vector3d aka = new Vector3d(var1x.getPosX(), var1x.getPosY(), var1x.getPosZ());
        if (this.lastTeleportPos == null) {
            this.lastTeleportPos = aka;
            var1x.setCancelled();
            this.timerTicks += 2;
        } else if (!this.lastTeleportPos.equals(aka)) {
            this.getParent().toggle();
        } else {
            var1x.setCancelled();
            this.timerTicks += 2;
        }
    };
    @EventLink
    public final Listener<KeyboardInputEvent> onKeyboardInput = var1x -> {
        if (var1x.getKeyCode() == this.getParent().getKey() && !this.jumped) {
            var1x.setCancelled();
            aEg.thePlayer.jump();
            this.jumped = true;
        }
    };

    public VulcanDeprecatedFlight(String var1, Flight flight) {
        super(var1, flight);
    }

    @Override
    public void onEnable() {
        this.lastTeleportPos = null;
        this.jumped = false;
        this.timerTicks = 0;
    }
}
