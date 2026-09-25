package com.alan.clients.module.impl.movement.longjump;

import com.alan.clients.module.impl.movement.LongJump;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.JumpEvent;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.newevent.impl.motion.StrafeEvent;
import com.alan.clients.newevent.impl.other.BlockAABBEvent;
import com.alan.clients.util.player.MoveUtil;
import com.alan.clients.value.Mode;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.network.Packet;
import net.minecraft.potion.Potion;
import net.minecraft.util.BlockPos;

public class WatchdogGlideLongJump extends Mode<LongJump> {
    private final List<Packet<?>> packets = new ArrayList<>();
    private double Lx;
    private boolean Eo;
    private boolean LL;
    private int ug;
    private int LM;
    private double LN;
    private int tickCounter;
    @EventLink(value = 3)
    public final Listener<PreMotionEvent> onPreMotion = var1x -> {
        this.tickCounter++;
        if (this.tickCounter > 2) {
            this.tickCounter = -1;
        }
    };
    @EventLink
    public final Listener<StrafeEvent> onStrafe = var0 -> {
        if (aEg.thePlayer.onGround) {
            MoveUtil.strafe(MoveUtil.getAllowedHorizontalDistance() - 0.001);
            aEg.thePlayer.jump();
        }

        if (aEg.thePlayer.tR == 1) {
            if (aEg.thePlayer.isPotionActive(Potion.moveSpeed) && aEg.thePlayer.getActivePotionEffect(Potion.moveSpeed).amplifier + 1 >= 2) {
                MoveUtil.strafe(0.48);
            } else if (aEg.thePlayer.isPotionActive(Potion.moveSpeed) && aEg.thePlayer.getActivePotionEffect(Potion.moveSpeed).amplifier + 1 == 1) {
                MoveUtil.strafe(0.4);
            } else {
                MoveUtil.strafe(0.33);
            }

            aEg.thePlayer.jump();
        }

        MoveUtil.useDiagonalSpeed();
        if (aEg.thePlayer.motionY <= 0.0) {
            aEg.thePlayer.motionY += 0.0284;
        }

        double d0;
        int i = (d0 = aEg.thePlayer.motionY - 0.0) == 0.0 ? 0 : (d0 < 0.0 ? -1 : 1);
        if (aEg.thePlayer.tR == 11) {
            aEg.thePlayer.motionY += 0.0904;
        }

        if (aEg.thePlayer.tR == 12) {
            aEg.thePlayer.motionY += 0.0904;
        }

        if (aEg.thePlayer.tR >= 10) {
            i = aEg.thePlayer.tR % 2;
        }
    };
    @EventLink
    public final Listener<BlockAABBEvent> onBlockAABB = var0 -> {
        Block block = var0.getBlock();
        BlockPos blockpos = var0.getBlockPos();
        WorldClient worldclient = aEg.theWorld;
        IBlockState iblockstate = worldclient.getBlockState(blockpos);
        block.getCollisionBoundingBox(worldclient, blockpos, iblockstate);
    };
    @EventLink
    public final Listener<JumpEvent> onJump = var0 -> var0.setJumpMotion(0.42F);

    public WatchdogGlideLongJump(String var1, LongJump longJump) {
        super(var1, longJump);
    }
}
