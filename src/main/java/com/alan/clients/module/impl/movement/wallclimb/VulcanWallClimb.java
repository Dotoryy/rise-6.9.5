package com.alan.clients.module.impl.movement.wallclimb;

import com.alan.clients.module.impl.movement.WallClimb;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.util.player.MoveUtil;
import com.alan.clients.value.Mode;
import net.minecraft.util.MathHelper;

public class VulcanWallClimb extends Mode<WallClimb> {
    @EventLink
    public final Listener<PreMotionEvent> onPreMotionEvent = var0 -> {
        if (aEg.thePlayer.isCollidedHorizontally) {
            if (aEg.thePlayer.ticksExisted % 2 == 0) {
                var0.setOnGround(true);
                aEg.thePlayer.motionY = MoveUtil.jumpMotion();
            }

            double d0 = MoveUtil.direction();
            var0.setPosX(var0.getPosX() - -MathHelper.sin((float)d0) * 0.1F);
            var0.setPosZ(var0.getPosZ() - MathHelper.cos((float)d0) * 0.1F);
        }
    };

    public VulcanWallClimb(String var1, WallClimb wallClimb) {
        super(var1, wallClimb);
    }
}
