package com.alan.clients.module.impl.movement.phase;

import com.alan.clients.module.impl.movement.Phase;
import com.alan.clients.module.impl.player.Scaffold;
import com.alan.clients.newevent.CancellableEvent;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.input.MoveInputEvent;
import com.alan.clients.newevent.impl.motion.PreUpdateEvent;
import com.alan.clients.newevent.impl.motion.PushOutOfBlockEvent;
import com.alan.clients.newevent.impl.other.BlockAABBEvent;
import com.alan.clients.newevent.impl.other.MoveEvent;
import com.alan.clients.newevent.impl.packet.PacketReceiveEvent;
import com.alan.clients.util.player.MoveUtil;
import com.alan.clients.value.Mode;
import com.alan.clients.value.impl.BooleanValue;
import com.alan.clients.util.packet.PacketUtil;
import com.alan.clients.util.player.PlayerUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockSnow;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;
import net.minecraft.network.play.client.C03PacketPlayer.C06PacketPlayerPosLook;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import rip.vantage.commons.util.time.StopWatch;

public class Watchdog1171FullBlockPhase extends Mode<Phase> {
    public final BooleanValue smartModeIfYouWantItToBeAlwaysBeToggledOn = new BooleanValue("Smart Mode (if you want it to be always be toggled on)", this, false);
    public final BooleanValue silent = new BooleanValue("Silent", this, false);
    private boolean readyToTeleport;
    private final StopWatch Oy = new StopWatch();
    private boolean ys;
    private static final int SEARCH_RADIUS = 10;
    private static final double BACKWARD_ANGLE_COS = Math.cos(Math.toRadians(130.0));
    @EventLink
    public final Listener<PreUpdateEvent> onPreUpdate = var1x -> {
        AxisAlignedBB axisalignedbb = aEg.thePlayer.getEntityBoundingBox().expand(0.05, 0.0, 0.05);
        boolean flag = !aEg.theWorld.getCollidingBoundingBoxes(aEg.thePlayer, axisalignedbb).isEmpty();
        if ((
                (aEg.thePlayer.isCollidedHorizontally && this.silent.wo() || aEg.thePlayer.isCollidedHorizontally && !this.silent.wo())
                        && this.readyToTeleport
                        && aEg.thePlayer.cqL > 2
                        && !aEg.gameSettings.keyBindJump.isKeyDown()
                        && !this.e(Scaffold.class).isEnabled()
                        && !(aEg.currentScreen instanceof GuiContainer)
                    || !this.smartModeIfYouWantItToBeAlwaysBeToggledOn.wo() && flag && this.readyToTeleport
            )
            && !PlayerUtil.vk()) {
            this.readyToTeleport = false;
            BlockPos blockpos = this.u(10);
            if (blockpos != null) {
                double d0 = blockpos.getX() + 0.5;
                double d1 = blockpos.getY();
                double d2 = blockpos.getZ() + 0.5;
                PacketUtil.sendNoEvent(new C04PacketPlayerPosition(d0, d1, d2, aEg.thePlayer.onGround));
            } else {
                PacketUtil.sendNoEvent(new C04PacketPlayerPosition(aEg.thePlayer.posX + 0.5, aEg.thePlayer.posY, aEg.thePlayer.posZ + 0.5, aEg.thePlayer.onGround));
            }
        }
    };
    @EventLink
    public final Listener<PacketReceiveEvent> onPacketReceive = var1x -> {
        Packet packet = var1x.getPacket();
        if (packet instanceof S08PacketPlayerPosLook && !this.readyToTeleport && (!this.silent.wo() || PlayerUtil.vk())) {
            S08PacketPlayerPosLook s08packetplayerposlook = (S08PacketPlayerPosLook)packet;
            var1x.setCancelled();
            double d0 = s08packetplayerposlook.getX();
            double d1 = s08packetplayerposlook.getY();
            double d2 = s08packetplayerposlook.getZ();
            float f = s08packetplayerposlook.getYaw();
            float f1 = s08packetplayerposlook.getPitch();
            this.readyToTeleport = true;
            PacketUtil.sendNoEvent(new C06PacketPlayerPosLook(d0, d1, d2, f, f1, aEg.thePlayer.onGround));
            if (!this.silent.wo()) {
                aEg.thePlayer.setPosition(d0, d1, d2);
            }
        }
    };
    @EventLink
    public final Listener<MoveEvent> onMove = var1x -> {
        if (!this.readyToTeleport && !this.silent.wo()) {
            var1x.setPosZ(0.0);
            var1x.setPosX(0.0);
        }
    };
    @EventLink
    public final Listener<BlockAABBEvent> onBlockAABB = var1x -> {
        if (!this.readyToTeleport && var1x.getBlock() instanceof Block && this.silent.wo()) {
            BlockPos blockpos = var1x.getBlockPos();
            BlockPos blockpos1 = new BlockPos(
                MathHelper.floor_double(aEg.thePlayer.posX),
                (int)(aEg.thePlayer.getEntityBoundingBox().minY - 0.49),
                MathHelper.floor_double(aEg.thePlayer.posZ)
            );
            if (!blockpos.equals(blockpos1) && this.silent.wo()) {
                if (PlayerUtil.vk()) {
                    MoveUtil.strafe(-0.1);
                }

                var1x.setCancelled();
            }
        }
    };
    @EventLink
    public final Listener<MoveInputEvent> onMoveInput = var1x -> {};
    @EventLink
    public final Listener<PushOutOfBlockEvent> onPushOutOfBlock = CancellableEvent::setCancelled;

    public Watchdog1171FullBlockPhase(String var1, Phase phase) {
        super(var1, phase);
    }

    private boolean isBehind(double var1, double var3) {
        double d0 = aEg.thePlayer.posX;
        double d1 = aEg.thePlayer.posZ;
        double radians = Math.toRadians(aEg.thePlayer.pl);
        double d3 = -Math.sin(radians);
        double cos = Math.cos(radians);
        double d5 = var1 - d0;
        double d6 = var3 - d1;
        double d7 = d3 * d5 + cos * d6;
        double d8 = Math.sqrt(d3 * d3 + cos * cos);
        double d9 = Math.sqrt(d5 * d5 + d6 * d6);
        return d9 < 1.0E-8 ? false : d7 / (d8 * d9) <= BACKWARD_ANGLE_COS;
    }

    private BlockPos u(int var1) {
        double d0 = aEg.thePlayer.posX;
        double d1 = aEg.thePlayer.posY;
        double d2 = aEg.thePlayer.posZ;
        int i = MathHelper.floor_double(d1);
        double d3 = Double.MAX_VALUE;
        BlockPos blockpos = null;

        for (int j = -var1; j <= var1; j++) {
            for (int k = -var1; k <= var1; k++) {
                for (int l = -var1; l <= var1; l++) {
                    int i1 = MathHelper.floor_double(d0) + j;
                    int j1 = i + k;
                    int k1 = MathHelper.floor_double(d2) + l;
                    BlockPos blockpos1 = new BlockPos(i1, j1, k1);
                    Block block = aEg.theWorld.getBlockState(blockpos1).getBlock();
                    if (block.getMaterial().isSolid() && block.isFullBlock() && !(block instanceof BlockLeaves) && !(block instanceof BlockSnow)) {
                        double d4 = aEg.thePlayer.getDistance(blockpos1.getX() + 0.5, blockpos1.getY() + 0.5, blockpos1.getZ() + 0.5);
                        if (d4 >= 1.0 && d4 <= 10.0 && this.isBehind(blockpos1.getX() + 0.5, blockpos1.getZ() + 0.5) && d4 < d3) {
                            d3 = d4;
                            blockpos = blockpos1;
                        }
                    }
                }
            }
        }

        return blockpos;
    }

    @Override
    public void onEnable() {
        this.readyToTeleport = true;
    }
}
