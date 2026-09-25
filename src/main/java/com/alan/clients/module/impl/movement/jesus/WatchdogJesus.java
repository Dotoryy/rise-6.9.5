package com.alan.clients.module.impl.movement.jesus;

import com.alan.clients.module.impl.movement.Jesus;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.newevent.impl.motion.StrafeEvent;
import com.alan.clients.newevent.impl.other.BlockAABBEvent;
import com.alan.clients.newevent.impl.packet.PacketReceiveEvent;
import com.alan.clients.util.player.MoveUtil;
import com.alan.clients.value.Mode;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import de.florianmichael.vialoadingbase.ViaLoadingBase;
import com.alan.clients.util.player.PlayerUtil;
import net.minecraft.block.BlockLiquid;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.util.AxisAlignedBB;

public class WatchdogJesus extends Mode<Jesus> {
    private Boolean standingOnWater = false;
    @EventLink
    public final Listener<BlockAABBEvent> onBlockAABB = var1x -> {
        if (var1x.getBlock() instanceof BlockLiquid && !aEg.thePlayer.inWater) {
            this.standingOnWater = true;
            int i = var1x.getBlockPos().getX();
            int j = var1x.getBlockPos().getY();
            int k = var1x.getBlockPos().getZ();
            var1x.setBoundingBox(AxisAlignedBB.fromBounds(i, j, k, i + 1, j + 1, k + 1));
        } else {
            this.standingOnWater = false;
        }
    };
    @EventLink
    public final Listener<PreMotionEvent> onPreMotion = var0 -> {
        if (PlayerUtil.vl() && !aEg.thePlayer.inWater) {
            aEg.thePlayer.onGround = true;
            var0.setPosY(var0.getPosY() - (aEg.thePlayer.ticksExisted % 2 == 0 ? 0.0625 : 0.0325));
            var0.setOnGround(false);
        }
    };
    @EventLink
    public final Listener<StrafeEvent> onStrafe = var0 -> {
        if (PlayerUtil.vl() && aEg.thePlayer.ae > 10 && !aEg.thePlayer.inWater) {
            if (ViaLoadingBase.getInstance().getTargetVersion().newerThanOrEqualTo(ProtocolVersion.v1_13)) {
                var0.setSpeed(0.204);
            } else {
                var0.setSpeed(0.152);
            }
        } else if (PlayerUtil.vl() && aEg.thePlayer.ae > 1) {
            MoveUtil.strafe();
        }

        if (aEg.thePlayer.inWater && ViaLoadingBase.getInstance().getTargetVersion().newerThanOrEqualTo(ProtocolVersion.v1_13)) {
            MoveUtil.strafe(0.18);
        }
    };
    @EventLink(value = 0)
    public final Listener<PacketReceiveEvent> onPacketReceive = var1x -> {
        Packet packet = var1x.getPacket();
        if (packet instanceof S12PacketEntityVelocity && this.standingOnWater || packet instanceof S12PacketEntityVelocity && (PlayerUtil.vl() || aEg.thePlayer.inWater)) {
            S12PacketEntityVelocity s12packetentityvelocity = (S12PacketEntityVelocity)packet;
            if (s12packetentityvelocity.getEntityID() == aEg.thePlayer.getEntityId()) {
                s12packetentityvelocity.motionY = 0;
                var1x.setCancelled();
                return;
            }
        }
    };

    public WatchdogJesus(String var1, Jesus jesus) {
        super(var1, jesus);
    }
}
