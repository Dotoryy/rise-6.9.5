package com.alan.clients.module.impl.movement.phase;

import com.alan.clients.module.impl.movement.Phase;
import com.alan.clients.value.Mode;
import com.alan.clients.util.packet.PacketUtil;
import net.minecraft.network.play.client.C07PacketPlayerDigging.Action;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class BreakBlockPhase extends Mode<Phase> {
    public BreakBlockPhase(String var1, Phase phase) {
        super(var1, phase);
    }

    @Override
    public void onEnable() {
        if (aEg.thePlayer != null && aEg.theWorld != null) {
            BlockPos blockpos = new BlockPos(aEg.thePlayer.posX, aEg.thePlayer.posY - 1.0, aEg.thePlayer.posZ);
            PacketUtil.send(new C07PacketPlayerDigging(Action.START_DESTROY_BLOCK, blockpos, EnumFacing.DOWN));
            aEg.thePlayer.swingItem();
            PacketUtil.send(new C07PacketPlayerDigging(Action.STOP_DESTROY_BLOCK, blockpos, EnumFacing.DOWN));
            aEg.playerController.onPlayerDestroyBlock(blockpos, EnumFacing.DOWN);
            double d0 = blockpos.getX() + 0.5;
            double d1 = blockpos.getZ() + 0.5;
            aEg.thePlayer.setPosition(d0, aEg.thePlayer.posY, d1);
        } else {
            this.toggle();
        }
    }

    @Override
    public void onDisable() {
    }
}
