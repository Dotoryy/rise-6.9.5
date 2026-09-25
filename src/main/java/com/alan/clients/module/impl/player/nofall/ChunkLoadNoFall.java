package com.alan.clients.module.impl.player.nofall;

import com.alan.clients.module.impl.player.NoFall;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.newevent.impl.other.TeleportEvent;
import com.alan.clients.value.Mode;
import com.alan.clients.util.player.PlayerUtil;
import com.alan.clients.component.impl.player.FallDistanceComponent;
import net.minecraft.util.BlockPos;

public class ChunkLoadNoFall extends Mode<NoFall> {
    private boolean fakeUnloaded;
    @EventLink
    public final Listener<PreMotionEvent> onPreMotionEvent = var1x -> {
        if (this.fakeUnloaded) {
            aEg.thePlayer.motionY = 0.0;
            var1x.setOnGround(false);
            var1x.setPosY(var1x.getPosY() - 0.098F);
            aEg.thePlayer.setPositionAndUpdate(aEg.thePlayer.posX, var1x.getPosY(), aEg.thePlayer.posZ);
        } else if (!(aEg.thePlayer.motionY > 0.0) && !(FallDistanceComponent.cY <= 3.0F)) {
            if (PlayerUtil.block(new BlockPos(var1x.getPosX(), var1x.getPosY() + aEg.thePlayer.motionY, var1x.getPosZ())).getMaterial().isSolid()) {
                FallDistanceComponent.cY = 0.0F;
                this.fakeUnloaded = true;
            }
        }
    };
    @EventLink
    public final Listener<TeleportEvent> onTeleport = var1x -> this.fakeUnloaded = false;

    public ChunkLoadNoFall(String var1, NoFall noFall) {
        super(var1, noFall);
    }
}
