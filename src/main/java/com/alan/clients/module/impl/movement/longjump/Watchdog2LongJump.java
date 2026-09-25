package com.alan.clients.module.impl.movement.longjump;

import com.alan.clients.component.impl.player.BlinkComponent;
import com.alan.clients.module.impl.movement.LongJump;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.input.MoveInputEvent;
import com.alan.clients.newevent.impl.motion.JumpEvent;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.newevent.impl.motion.PreUpdateEvent;
import com.alan.clients.newevent.impl.motion.StrafeEvent;
import com.alan.clients.newevent.impl.other.MoveEvent;
import com.alan.clients.newevent.impl.packet.PacketReceiveEvent;
import com.alan.clients.newevent.impl.render.Render3DEvent;
import com.alan.clients.util.player.MoveUtil;
import com.alan.clients.util.render.RenderUtil;
import com.alan.clients.value.Mode;
import com.alan.clients.value.impl.BooleanValue;
import com.alan.clients.value.impl.NumberValue;
import com.alan.clients.util.chat.ChatUtil;
import com.alan.clients.util.packet.PacketUtil;
import com.alan.clients.component.impl.render.NotificationComponent;
import com.alan.clients.component.impl.render.ProgressBarComponent;
import com.alan.clients.component.impl.render.SmoothCameraComponent;
import com.alan.clients.util.shader.ShaderQueueType;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockSnow;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.Blocks;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.potion.Potion;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import org.lwjgl.opengl.GL11;

public class Watchdog2LongJump extends Mode<LongJump>
{
    public static int ticks;
    public BooleanValue flyMode;
    public boolean onHalfBlock;
    @EventLink
    public Listener<Render3DEvent> onRender3D;
    public static boolean launched;
    public static double knockbackSpeed;
    public static boolean LX;
    @EventLink
    public Listener<PreMotionEvent> onPreMotion;
    public double lockedPitch;
    @EventLink
    public Listener<PreUpdateEvent> onPreUpdate;
    @EventLink
    public Listener<MoveEvent> onMove;
    public boolean blocksScanned;
    @EventLink
    public Listener<JumpEvent> onJump;
    public double takeoffY;
    public double startY;
    public NumberValue delayBoostGoFurtherWithLessHe;
    public double startZ;
    @EventLink
    public Listener<MoveInputEvent> onMoveInput;
    public List<BlockPos> halfBlocks;
    @EventLink
    public Listener<StrafeEvent> onStrafe;
    public double startX;
    @EventLink
    public Listener<PacketReceiveEvent> onPacketReceive;
    public static int boostTick;
    public static int jumpTick;
    public BooleanValue allowDamageMove;
    public double lockedYaw;
    public BooleanValue timer;

    @Override
    public void onDisable() {
        final double n = Watchdog2LongJump.aEg.thePlayer.posX - this.startX;
        final double n2 = Watchdog2LongJump.aEg.thePlayer.posY - this.startY;
        final double n3 = Watchdog2LongJump.aEg.thePlayer.posZ - this.startZ;
        final double sqrt = Math.sqrt(n * n + n2 * n2 + n3 * n3);
        Watchdog2LongJump.launched = false;
        this.onHalfBlock = false;
        ChatUtil.b(Watchdog2LongJump.aEg.thePlayer.ae, new Object[0]);
        ChatUtil.c("Distance traveled: " + sqrt, new Object[0]);
        if (Watchdog2LongJump.aEg.thePlayer.onGround) {
            MoveUtil.stop();
        }
        this.halfBlocks.clear();
        this.blocksScanned = false;
    }

    public void scanHalfBlocks() {
        this.halfBlocks.clear();
        int limit = 30;
        int n10_lo = -limit;
        for (; n10_lo < limit; n10_lo = n10_lo + 1) {
            int j = -limit;
            for (; j < limit; j++) {
                int n_lo = -limit;
                for (; n_lo < limit; n_lo = n_lo + 1) {
                    final BlockPos blockPos = new BlockPos(Watchdog2LongJump.aEg.thePlayer.posX + n10_lo, Watchdog2LongJump.aEg.thePlayer.posY + j, Watchdog2LongJump.aEg.thePlayer.posZ + n_lo);
                    final IBlockState blockState = Watchdog2LongJump.aEg.theWorld.getBlockState(blockPos);
                    final Block block = blockState.getBlock();
                    if (block != Blocks.air) {
                        int n_hi = 0;
                        if (block instanceof BlockSlab) {
                            n_hi = blockState.getProperties().containsKey(BlockSlab.HALF) ? 0 : 1;
                        }
                        if (block instanceof BlockStairs || block instanceof BlockSnow || n_hi != 0) {
                            this.halfBlocks.add(blockPos);
                        }
                    }
                }
            }
        }
        this.blocksScanned = true;
    }

    public Watchdog2LongJump(final String s, final LongJump longJump) {
        super(s, longJump);
        this.timer = new BooleanValue("Timer", this, false);
        this.flyMode = new BooleanValue("Fly Mode", this, false);
        this.allowDamageMove = new BooleanValue("Allow Damage Move", this, false);
        this.delayBoostGoFurtherWithLessHe = new NumberValue("Delay Boost (go further with less height)", this, 2, 0, 8, 1);
        this.halfBlocks = new ArrayList<BlockPos>();
        this.blocksScanned = false;
        this.onPacketReceive = (packetReceiveEvent -> {
            final Packet packet = packetReceiveEvent.getPacket();
            Objects.requireNonNull(packet);
            switch (packet) {
                case S12PacketEntityVelocity s12PacketEntityVelocity: {
                    if (!packetReceiveEvent.isCancelled() && s12PacketEntityVelocity.getEntityID() == Watchdog2LongJump.aEg.thePlayer.getEntityId()) {
                        Watchdog2LongJump.knockbackSpeed = Math.hypot(s12PacketEntityVelocity.getMotionX() / 8000.0, s12PacketEntityVelocity.getMotionZ() / 8000.0);
                        if (Watchdog2LongJump.ticks > Watchdog2LongJump.boostTick && Watchdog2LongJump.knockbackSpeed > 0.0 && s12PacketEntityVelocity.getMotionY() / 8000.0 > 0.4) {
                            ChatUtil.c(MoveUtil.speed(), new Object[0]);
                            MoveUtil.strafe(0.459999);
                            Watchdog2LongJump.aEg.thePlayer.motionY = s12PacketEntityVelocity.getMotionY() / 8000.0;
                            Watchdog2LongJump.aEg.thePlayer.ae = 0;
                            Watchdog2LongJump.launched = true;
                            ChatUtil.c(MoveUtil.speed(), new Object[0]);
                        }
                        else {
                            Watchdog2LongJump.launched = false;
                            NotificationComponent.a("Watchdog Longjump", "Bad knockback detected from the server... Cancelling and trying to save you from the void", 5000);
                        }
                        packetReceiveEvent.setCancelled();
                        break;
                    }
                    else {
                        break;
                    }
                }
                default:
                    break;
            }
            return;
        });
        this.onMoveInput = (moveInputEvent -> {
            if (Watchdog2LongJump.ticks < Watchdog2LongJump.jumpTick && Watchdog2LongJump.ticks > 7) {
                moveInputEvent.setForward(100.0f);
                moveInputEvent.setJump(false);
            }
            return;
        });
        this.onMove = (moveEvent -> {
            if (Watchdog2LongJump.ticks < Watchdog2LongJump.jumpTick && (Watchdog2LongJump.ticks > 7 || !this.allowDamageMove.wo())) {
                moveEvent.setPosZ(0.0);
                moveEvent.setPosX(0.0);
            }
            return;
        });
        this.onStrafe = (p0 -> {
            if ((Watchdog2LongJump.aEg.thePlayer.posY > this.takeoffY || Watchdog2LongJump.aEg.thePlayer.tR < 14) && this.flyMode.wo()) {
                SmoothCameraComponent.cn();
            }
            final int tr = Watchdog2LongJump.aEg.thePlayer.tR;
            MoveUtil.useDiagonalSpeed();
            if (Watchdog2LongJump.ticks < 8) {
                if (Watchdog2LongJump.aEg.thePlayer.onGround) {
                    MoveUtil.strafe();
                }
                if (Watchdog2LongJump.aEg.thePlayer.tR == 1) {
                    MoveUtil.strafe();
                    final EntityPlayerSP thePlayer = Watchdog2LongJump.aEg.thePlayer;
                    thePlayer.motionY += 0.05700000002980232;
                }
                if (Watchdog2LongJump.aEg.thePlayer.tR == 3) {
                    final EntityPlayerSP thePlayer2 = Watchdog2LongJump.aEg.thePlayer;
                    thePlayer2.motionY -= 0.13089999556541443;
                }
                if (Watchdog2LongJump.aEg.thePlayer.tR == 4) {
                    final EntityPlayerSP thePlayer3 = Watchdog2LongJump.aEg.thePlayer;
                    thePlayer3.motionY -= 0.2;
                }
            }
            return;
        });
        this.onPreUpdate = (p0 -> {
            if (Watchdog2LongJump.ticks > 44) {
                final int hq = Watchdog2LongJump.ticks;
            }
            return;
        });
        this.onPreMotion = (preMotionEvent -> {
            final AxisAlignedBB axisAlignedBB = Watchdog2LongJump.aEg.thePlayer.getEntityBoundingBox();
            final WorldClient theWorld = Watchdog2LongJump.aEg.theWorld;
            double minX = axisAlignedBB.minX;
            int n_hi = 0;
            while (minX < axisAlignedBB.maxX) {
                double minZ = axisAlignedBB.minZ;
                while (minZ < axisAlignedBB.maxZ) {
                    final Block block = theWorld.getBlockState(new BlockPos(minX, axisAlignedBB.minY - 0.05, minZ)).getBlock();
                    if (block instanceof BlockStairs || block instanceof BlockSnow || (block instanceof BlockSlab && Math.abs(Watchdog2LongJump.aEg.thePlayer.posY - Math.round(Watchdog2LongJump.aEg.thePlayer.posY)) <= 0.03)) {
                        n_hi = 1;
                        break;
                    }
                    else {
                        minZ += 0.3;
                    }
                }
                if (n_hi != 0) {
                    break;
                }
                else {
                    minX += 0.3;
                }
            }
            if (n_hi != 0) {
                this.onHalfBlock = true;
                Watchdog2LongJump.boostTick = 35;
                Watchdog2LongJump.jumpTick = 34;
            }
            else if (Watchdog2LongJump.aEg.thePlayer.onGround) {
                Watchdog2LongJump.boostTick = 67;
                Watchdog2LongJump.jumpTick = 66;
            }
            if (Watchdog2LongJump.aEg.thePlayer.tR > 10 && !MoveUtil.isMoving()) {
                MoveUtil.stop();
            }
            if (Watchdog2LongJump.ticks < 7) {
                this.lockedYaw = Watchdog2LongJump.aEg.thePlayer.pl;
                this.lockedPitch = Watchdog2LongJump.aEg.thePlayer.rotationPitch;
            }
            else if (Watchdog2LongJump.ticks > 8 && Watchdog2LongJump.ticks <= Watchdog2LongJump.boostTick + this.delayBoostGoFurtherWithLessHe.wo().intValue()) {
                Watchdog2LongJump.aEg.thePlayer.pl = (float)this.lockedYaw;
            }
            preMotionEvent.setPitch((float)this.lockedPitch);
            preMotionEvent.setYaw((float)this.lockedYaw);
            final int hq2 = Watchdog2LongJump.ticks;
            if ((Watchdog2LongJump.aEg.thePlayer.onGround && ((Watchdog2LongJump.ticks > 67 && !this.onHalfBlock) || (Watchdog2LongJump.ticks > 40 && this.onHalfBlock))) || Watchdog2LongJump.aEg.thePlayer.Zl == 1) {
                MoveUtil.stop();
                this.getParent().setEnabled(false);
            }
            if (this.flyMode.wo()) {
                Watchdog2LongJump.aEg.thePlayer.cameraYaw = 0.1f;
            }
            ++Watchdog2LongJump.ticks;
            if (Watchdog2LongJump.ticks < Watchdog2LongJump.boostTick + this.delayBoostGoFurtherWithLessHe.wo().intValue()) {
                BlinkComponent.a(500, true, true, true, true);
            }
            if (Watchdog2LongJump.ticks < Watchdog2LongJump.boostTick) {
                if (Watchdog2LongJump.aEg.thePlayer.onGround) {
                    double n2;
                    if (this.onHalfBlock) {
                        n2 = 34.0;
                    }
                    else {
                        n2 = 66.0;
                    }
                    ProgressBarComponent.a((float)(Watchdog2LongJump.ticks / n2));
                }
                if (Watchdog2LongJump.aEg.thePlayer.onGround && Watchdog2LongJump.ticks > 3) {
                    if (!this.onHalfBlock) {
                        preMotionEvent.setPosY(preMotionEvent.getPosY() + ((Watchdog2LongJump.ticks % 2 != 0) ? 0.0625 : (Math.random() / 5000.0)));
                    }
                    else {
                        preMotionEvent.setPosY(preMotionEvent.getPosY() + ((Watchdog2LongJump.ticks % 2 != 0) ? 0.14499999582767487 : (Math.random() / 5000.0)));
                    }
                    preMotionEvent.setOnGround(false);
                }
                if (Watchdog2LongJump.ticks == Watchdog2LongJump.jumpTick) {
                    if (!Watchdog2LongJump.aEg.thePlayer.isPotionActive(Potion.moveSpeed)) {
                        MoveUtil.strafe(MoveUtil.getAllowedHorizontalDistance() - 0.01);
                    }
                    else if (Watchdog2LongJump.aEg.thePlayer.getActivePotionEffect(Potion.moveSpeed).amplifier + 1 >= 2) {
                        MoveUtil.strafe(MoveUtil.getAllowedHorizontalDistance() - 0.01);
                    }
                    else {
                        MoveUtil.strafe(MoveUtil.getAllowedHorizontalDistance() - 0.045);
                    }
                    Watchdog2LongJump.aEg.thePlayer.jump();
                }
            }
            else if (Watchdog2LongJump.ticks == Watchdog2LongJump.boostTick) {
                preMotionEvent.setPosY(preMotionEvent.getPosY() + 1.0E-13);
                PacketUtil.send(new C03PacketPlayer(true));
                if (this.timer.wo()) {
                    Watchdog2LongJump.aEg.timer.dzD = 0.5f;
                }
                Watchdog2LongJump.launched = true;
            }
            int ae2 = Watchdog2LongJump.aEg.thePlayer.ae;
            if (((ae2 > 3 && ae2 < 38) || (ae2 > 37 && Watchdog2LongJump.aEg.thePlayer.motionY <= 0.0)) && Watchdog2LongJump.ticks > 40) {
                final EntityPlayerSP thePlayer4 = Watchdog2LongJump.aEg.thePlayer;
                thePlayer4.motionY += 0.0283;
            }
            switch (ae2) {
                case 1: {
                    final EntityPlayerSP thePlayer5 = Watchdog2LongJump.aEg.thePlayer;
                    thePlayer5.motionX *= 2.1;
                    final EntityPlayerSP thePlayer6 = Watchdog2LongJump.aEg.thePlayer;
                    thePlayer6.motionZ *= 2.1;
                }
                case 6: {
                    Watchdog2LongJump.aEg.thePlayer.motionY = 0.031;
                    break;
                }
                case 7: {
                    final EntityPlayerSP thePlayer7 = Watchdog2LongJump.aEg.thePlayer;
                    thePlayer7.motionX *= 1.02;
                    final EntityPlayerSP thePlayer8 = Watchdog2LongJump.aEg.thePlayer;
                    thePlayer8.motionZ *= 1.02;
                    Watchdog2LongJump.aEg.thePlayer.motionY = 0.01;
                    break;
                }
                case 8: {
                    final EntityPlayerSP thePlayer9 = Watchdog2LongJump.aEg.thePlayer;
                    thePlayer9.motionX *= 1.01;
                    final EntityPlayerSP thePlayer10 = Watchdog2LongJump.aEg.thePlayer;
                    thePlayer10.motionZ *= 1.01;
                    Watchdog2LongJump.aEg.thePlayer.motionY = 0.0;
                    break;
                }
                case 9: {
                    final EntityPlayerSP thePlayer11 = Watchdog2LongJump.aEg.thePlayer;
                    thePlayer11.motionY += 0.015;
                    break;
                }
                case 10: {
                    final EntityPlayerSP thePlayer12 = Watchdog2LongJump.aEg.thePlayer;
                    thePlayer12.motionX *= 1.12;
                    final EntityPlayerSP thePlayer13 = Watchdog2LongJump.aEg.thePlayer;
                    thePlayer13.motionZ *= 1.12;
                    final EntityPlayerSP thePlayer14 = Watchdog2LongJump.aEg.thePlayer;
                    thePlayer14.motionY += 0.015;
                    break;
                }
                case 11: {
                    Watchdog2LongJump.aEg.thePlayer.motionY = 0.0;
                    final EntityPlayerSP thePlayer15 = Watchdog2LongJump.aEg.thePlayer;
                    thePlayer15.motionX *= 1.04;
                    final EntityPlayerSP thePlayer16 = Watchdog2LongJump.aEg.thePlayer;
                    thePlayer16.motionZ *= 1.04;
                    break;
                }
                case 12: {
                    Watchdog2LongJump.aEg.thePlayer.motionY = 0.0;
                    final EntityPlayerSP thePlayer17 = Watchdog2LongJump.aEg.thePlayer;
                    thePlayer17.motionX *= 1.04;
                    final EntityPlayerSP thePlayer18 = Watchdog2LongJump.aEg.thePlayer;
                    thePlayer18.motionZ *= 1.04;
                    break;
                }
                case 13: {
                    final EntityPlayerSP thePlayer19 = Watchdog2LongJump.aEg.thePlayer;
                    thePlayer19.motionY += 0.01;
                    break;
                }
                case 14: {
                    final EntityPlayerSP thePlayer20 = Watchdog2LongJump.aEg.thePlayer;
                    thePlayer20.motionY += 0.015;
                    break;
                }
                case 15: {
                    final EntityPlayerSP thePlayer21 = Watchdog2LongJump.aEg.thePlayer;
                    thePlayer21.motionY += 0.01;
                    break;
                }
                case 18: {
                    final EntityPlayerSP thePlayer22 = Watchdog2LongJump.aEg.thePlayer;
                    thePlayer22.motionY += 0.032;
                    break;
                }
            }
            return;
        });
        this.onJump = (jumpEvent -> {
            if (Watchdog2LongJump.ticks > 45) {
                jumpEvent.setJumpMotion(0.42f);
            }
            return;
        });
        this.onRender3D = (p0 -> {
            if (!this.blocksScanned) {
                this.scanHalfBlocks();
            }
            this.b(ShaderQueueType.BLOOM).c(() -> {
                final Iterator iterator = this.halfBlocks.iterator();
                while (iterator.hasNext()) {
                    final BlockPos blockPos = (BlockPos)iterator.next();
                    final AxisAlignedBB axisAlignedBB2 = new AxisAlignedBB(blockPos, blockPos.add(1, 1, 1));
                    if (!RenderUtil.isInViewFrustrum(axisAlignedBB2)) {
                        continue;
                    }
                    else {
                        GlStateManager.pushMatrix();
                        GlStateManager.pushAttrib();
                        GlStateManager.enableBlend();
                        GlStateManager.disableTexture2D();
                        GlStateManager.disableLighting();
                        GL11.glDepthMask(false);
                        RenderUtil.color(this.rz().rA());
                        RenderUtil.drawBoundingBox(axisAlignedBB2);
                        GlStateManager.enableTexture2D();
                        GlStateManager.enableLighting();
                        GlStateManager.disableBlend();
                        GL11.glDepthMask(true);
                        GlStateManager.popAttrib();
                        GlStateManager.popMatrix();
                        GlStateManager.resetColor();
                    }
                }
            });
        });
    }


    @Override
    public void onEnable() {
        this.lockedYaw = Watchdog2LongJump.aEg.thePlayer.pl;
        this.lockedPitch = Watchdog2LongJump.aEg.thePlayer.rotationPitch;
        Watchdog2LongJump.aEg.thePlayer.pl = (float)this.lockedYaw;
        if (Watchdog2LongJump.aEg.thePlayer.onGround) {
            Watchdog2LongJump.aEg.thePlayer.jump();
        }
        else {
            ChatUtil.b("start on the ground", new Object[0]);
            this.getParent().setEnabled(false);
        }
        this.startX = Watchdog2LongJump.aEg.thePlayer.posX;
        this.startY = Watchdog2LongJump.aEg.thePlayer.posY;
        this.startZ = Watchdog2LongJump.aEg.thePlayer.posZ;
        this.takeoffY = Watchdog2LongJump.aEg.thePlayer.posY;
        Watchdog2LongJump.launched = false;
        Watchdog2LongJump.ticks = 0;
    }

    static {
    }

}
