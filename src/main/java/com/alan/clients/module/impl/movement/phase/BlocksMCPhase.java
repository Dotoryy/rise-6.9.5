package com.alan.clients.module.impl.movement.phase;

import com.alan.clients.module.impl.movement.Phase;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PushOutOfBlockEvent;
import com.alan.clients.newevent.impl.motion.StrafeEvent;
import com.alan.clients.newevent.impl.other.BlockAABBEvent;
import com.alan.clients.newevent.impl.other.TickEvent;
import com.alan.clients.value.Mode;
import net.minecraft.block.BlockLiquid;

public class BlocksMCPhase extends Mode<Phase> {
    private int stage = 1;
    @EventLink
    public final Listener<TickEvent> onTick = var1x -> {
        if (aEg.thePlayer != null && aEg.theWorld != null) {
            switch (this.stage) {
                case 1:
                    if (aEg.gameSettings.keyBindJump.isKeyDown() && !aEg.theWorld.isAirBlock(aEg.thePlayer.getPosition())) {
                        aEg.thePlayer.motionY = 0.42;
                        this.stage++;
                    }

                    aEg.thePlayer.onGround = true;
                    break;
                case 2:
                    aEg.thePlayer.motionY = 0.33;
                    this.stage++;
                    break;
                case 3:
                    aEg.thePlayer.motionY = 0.25;
                    this.stage++;
            }

            if (this.stage > 3 || this.stage == 0) {
                this.stage = 1;
            }

            aEg.thePlayer.noClip = true;
        }
    };
    @EventLink
    public final Listener<StrafeEvent> onStrafe = var0 -> {
        if (aEg.thePlayer != null) {
            if (aEg.thePlayer.isSneaking()) {
                var0.setSpeed(0.179);
            }
        }
    };
    @EventLink
    public final Listener<BlockAABBEvent> onBlockAABB = var0 -> {
        if (aEg.thePlayer != null) {
            if (!(var0.getBlock() instanceof BlockLiquid)) {
                if (var0.getBlockPos().getY() >= aEg.thePlayer.posY) {
                    var0.setBoundingBox(null);
                }
            }
        }
    };
    @EventLink
    public final Listener<PushOutOfBlockEvent> onPushOutOfBlock = var0 -> var0.setCancelled();

    public BlocksMCPhase(String var1, Phase phase) {
        super(var1, phase);
    }

    @Override
    public void onEnable() {
        this.stage = 1;
        if (aEg.thePlayer != null) {
            aEg.thePlayer.motionY = 0.0;
        }
    }

    @Override
    public void onDisable() {
        if (aEg.thePlayer != null) {
            aEg.thePlayer.noClip = false;
        }

        this.stage = 1;
    }
}
