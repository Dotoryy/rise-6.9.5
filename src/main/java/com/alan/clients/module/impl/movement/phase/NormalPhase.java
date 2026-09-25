package com.alan.clients.module.impl.movement.phase;

import com.alan.clients.module.impl.movement.Phase;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.newevent.impl.other.BlockAABBEvent;
import com.alan.clients.value.Mode;
import com.alan.clients.util.packet.PacketUtil;
import com.alan.clients.util.player.PlayerUtil;
import net.minecraft.block.BlockAir;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;
import net.minecraft.util.AxisAlignedBB;

public class NormalPhase extends Mode<Phase> {
    private boolean phasing;
    @EventLink
    public final Listener<PreMotionEvent> onPreMotionEvent = var1x -> {
        this.phasing = false;
        double d0 = Math.toRadians(aEg.thePlayer.pl);
        double d1 = Math.sin(d0);
        double d2 = Math.cos(d0);
        if (aEg.thePlayer.isCollidedHorizontally) {
            aEg.thePlayer.setPosition(aEg.thePlayer.posX - d1 * 0.005, aEg.thePlayer.posY, aEg.thePlayer.posZ + d2 * 0.005);
            this.phasing = true;
        } else if (PlayerUtil.vk()) {
            PacketUtil.sendNoEvent(new C04PacketPlayerPosition(aEg.thePlayer.posX - d1 * 3.5, aEg.thePlayer.posY, aEg.thePlayer.posZ + d2 * 3.5, false));
            aEg.thePlayer.motionX *= 0.3;
            aEg.thePlayer.motionZ *= 0.3;
            this.phasing = true;
        }
    };
    @EventLink
    public final Listener<BlockAABBEvent> onBlockAABB = var1x -> {
        if (var1x.getBlock() instanceof BlockAir && this.phasing) {
            double d0 = var1x.getBlockPos().getX();
            double d1 = var1x.getBlockPos().getY();
            double d2 = var1x.getBlockPos().getZ();
            if (d1 < aEg.thePlayer.posY) {
                var1x.setBoundingBox(AxisAlignedBB.fromBounds(-15.0, -1.0, -15.0, 15.0, 1.0, 15.0).offset(d0, d1, d2));
            }
        }
    };

    public NormalPhase(String var1, Phase phase) {
        super(var1, phase);
    }
}
