package com.alan.clients.module.impl.movement.flight;

import com.alan.clients.module.impl.movement.Flight;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.input.MoveInputEvent;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.newevent.impl.other.BlockAABBEvent;
import com.alan.clients.newevent.impl.other.MoveEvent;
import com.alan.clients.util.player.MoveUtil;
import com.alan.clients.value.Mode;
import com.alan.clients.value.impl.ModeValue;
import com.alan.clients.value.impl.SubMode;
import net.minecraft.block.BlockAir;
import net.minecraft.util.AxisAlignedBB;

public class VerusFlight extends Mode<Flight> {
    private final ModeValue mode = new ModeValue("Sub-Mode", this).add(new SubMode("Fast")).setDefault("Fast");
    private int ticks = 0;
    @EventLink
    public final Listener<PreMotionEvent> onPreMotionEvent = var1x -> {
        if (this.mode.wo().getName().equals("Fast") && aEg.gameSettings.keyBindJump.isKeyDown() && aEg.thePlayer.ticksExisted % 2 == 0) {
            aEg.thePlayer.motionY = 0.42F;
        }

        this.ticks++;
    };
    @EventLink
    public final Listener<MoveEvent> onMove = var1x -> {
        if (this.mode.wo().getName().equals("Fast")) {
            if (aEg.thePlayer.onGround && this.ticks % 14 == 0) {
                var1x.setPosY(0.42F);
                MoveUtil.strafe(0.69);
                aEg.thePlayer.motionY = -(aEg.thePlayer.posY - Math.floor(aEg.thePlayer.posY));
            } else if (aEg.thePlayer.onGround) {
                MoveUtil.strafe(1.01 + MoveUtil.speedPotionAmp(0.15));
            } else {
                MoveUtil.strafe(0.41 + MoveUtil.speedPotionAmp(0.05));
            }

            aEg.thePlayer.setSprinting(true);
            aEg.thePlayer.bjQ = true;
        }

        this.ticks++;
    };
    @EventLink
    public final Listener<BlockAABBEvent> onBlockAABB = var1x -> {
        if (this.mode.wo().getName().equals("Fast")
            && (var1x.getBlock() instanceof BlockAir && !aEg.gameSettings.keyBindSneak.isKeyDown() || aEg.gameSettings.keyBindJump.isKeyDown())) {
            double d0 = var1x.getBlockPos().getX();
            double d1 = var1x.getBlockPos().getY();
            double d2 = var1x.getBlockPos().getZ();
            if (d1 < aEg.thePlayer.posY) {
                var1x.setBoundingBox(AxisAlignedBB.fromBounds(-15.0, -1.0, -15.0, 15.0, 1.0, 15.0).offset(d0, d1, d2));
            }
        }
    };
    @EventLink
    public final Listener<MoveInputEvent> onMoveInput = var0 -> var0.setSneak(false);

    public VerusFlight(String var1, Flight flight) {
        super(var1, flight);
    }

    @Override
    public void onDisable() {
        MoveUtil.stop();
    }
}
