package com.alan.clients.module.impl.player;

import com.alan.clients.Client;
import com.alan.clients.component.impl.player.BlinkComponent;
import com.alan.clients.component.impl.player.RotationComponent;
import com.alan.clients.component.impl.player.rotationcomponent.MovementFix;
import com.alan.clients.module.Module;
import com.alan.clients.module.api.Category;
import com.alan.clients.module.api.ModuleInfo;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.newevent.impl.motion.PreUpdateEvent;
import com.alan.clients.newevent.impl.motion.StrafeEvent;
import com.alan.clients.newevent.impl.other.BlockDamageEvent;
import com.alan.clients.newevent.impl.other.TeleportEvent;
import com.alan.clients.newevent.impl.packet.PacketReceiveEvent;
import com.alan.clients.newevent.impl.packet.PacketSendEvent;
import com.alan.clients.newevent.impl.render.MouseOverEvent;
import com.alan.clients.newevent.impl.render.Render3DEvent;
import com.alan.clients.util.animation.Animation;
import com.alan.clients.util.animation.Easing;
import com.alan.clients.util.render.RenderUtil;
import com.alan.clients.util.vector.Vector2f;
import com.alan.clients.value.impl.BooleanValue;
import com.alan.clients.value.impl.ListValue;
import com.alan.clients.value.impl.ModeValue;
import com.alan.clients.value.impl.NumberValue;
import com.alan.clients.value.impl.SubMode;
import com.alan.clients.util.RayCastUtil;
import com.alan.clients.util.chat.ChatUtil;
import com.alan.clients.util.packet.PacketUtil;
import com.alan.clients.util.player.PlayerUtil;
import com.alan.clients.util.rotation.RotationUtil;
import com.alan.clients.util.vector.Vector3d;
import com.alan.clients.util.vector.Vector3i;
import com.alan.clients.component.impl.render.ProgressBarComponent;
import com.alan.clients.util.shader.ShaderQueueType;
import com.alan.clients.module.impl.player.breaker.BreakCandidate;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockBed;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.init.Blocks;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C07PacketPlayerDigging.Action;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S23PacketBlockChange;
import net.minecraft.network.play.server.S32PacketConfirmTransaction;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.GL11;

@ModuleInfo(aliases = "module.player.breaker.name", description = "module.player.breaker.description", category = Category.PLAYER)
public class Breaker extends Module {
    public final ModeValue mode = new ModeValue("Mode", this).add(new SubMode("Normal")).add(new SubMode("Instant")).setDefault("Normal");
    public final NumberValue range = new NumberValue("Range", this, 4.5, 1, 6, 0.1);
    public final BooleanValue bed = new BooleanValue("Bed", this, true);
    public final BooleanValue keepBreakProgressWhenOutOfRange = new BooleanValue("Keep Break Progress When Out Of Range", this, true);
    public final BooleanValue cancelVelocityWhilstBreakingSoYouDontSlowDownInAir = new BooleanValue("Cancel velocity whilst breaking, so you don't slow down in air", this, true);
    public final BooleanValue delayVelocityUntilBedBroken = new BooleanValue("Delay Velocity Until Bed Broken", this, false);
    public final BooleanValue throughWalls = new BooleanValue("Through Walls", this, true);
    private final BooleanValue emptySurrounding = new BooleanValue("Empty Surrounding", this, false, () -> !this.throughWalls.wo());
    private final ModeValue surroundings = new ModeValue("Surroundings", this).add(new SubMode("Single")).add(new SubMode("Full")).setDefault("Single");
    private final NumberValue surroundingLayers = new NumberValue("Surrounding Layers", this, 3, 1, 6, 1, () -> !this.surroundings.wo().getName().equals("Full"));
    private final BooleanValue showBestStandPosition = new BooleanValue("Show Best Stand Position", this, false, () -> !this.surroundings.wo().getName().equals("Full"));
    private final BooleanValue heypixel = new BooleanValue("Heypixel", this, false, () -> !this.throughWalls.wo() || !this.emptySurrounding.wo());
    private final BooleanValue watchdogGroundSpoof = new BooleanValue("Watchdog Ground Spoof", this, false);
    public final BooleanValue attackWhileBreaking = new BooleanValue("Attack While Breaking", this, true);
    private final BooleanValue showBreakerPercentage = new BooleanValue("Show Breaker Percentage", this, true);
    public final BooleanValue rotations = new BooleanValue("Rotate", this, true);
    public final BooleanValue onlyRotateAtStartAndStop = new BooleanValue("Only Rotate at Start and Stop", this, true);
    public final BooleanValue whiteListOwnBed = new BooleanValue("Whitelist Own Bed", this, true);
    public final BooleanValue slowDownInAir = new BooleanValue("Slow Down In Air", this, true);
    private final ListValue<MovementFix> movementCorrection = new ListValue<>("Movement Correction", this);
    public static Vector3d targetBlock;
    public static Vector3d lastBlock;
    public static Vector3d home;
    private int breakCooldownTicks;
    public static boolean spoofingGround;
    public static boolean bedBroken;
    public static boolean breakingBed = false;
    private int lastDiggingTick;
    private boolean holdingAttackKey;
    private float breakProgress;
    public static boolean flushingPackets;
    private Animation progressAnimation = new Animation(Easing.LINEAR, 50L);
    private BlockPos blockPos;
    private Vec3 lastCheckPosition;
    private int lastStandSearchTick;
    private int lastSurroundingSearchTick;
    private final ArrayList<Packet<?>> delayedPackets = new ArrayList<>();
    private boolean hasDelayedVelocity = false;
    @EventLink
    public final Listener<Render3DEvent> onRender3D = var1 -> {
        if (targetBlock != null) {
            if (Client.a.g().c(FastBreak.class).isEnabled() && this.showBreakerPercentage.wo()) {
                ProgressBarComponent.a((float)(1.21 * this.breakProgress + (Double)Client.a.g().c(FastBreak.class).speed.wo() / 100.0));
            } else if (this.showBreakerPercentage.wo()) {
                ProgressBarComponent.a((float)(1.21 * this.breakProgress));
            }

            Vector3i akb = new Vector3i((int)Math.floor(targetBlock.getX()), (int)Math.floor(targetBlock.getY()), (int)Math.floor(targetBlock.getZ()));
            this.progressAnimation.Q(this.breakProgress);
            this.b(ShaderQueueType.BLOOM).c(() -> {
                GlStateManager.pushMatrix();
                GlStateManager.pushAttrib();
                GlStateManager.enableBlend();
                GlStateManager.disableTexture2D();
                GlStateManager.disableLighting();
                GL11.glDepthMask(false);
                RenderUtil.color(this.rz().rA());
                RenderUtil.drawBoundingBox(new AxisAlignedBB(akb.we(), akb.wf(), akb.wi(), akb.we() + 1, akb.wf() + 1.0 * this.progressAnimation.getValue(), akb.wi() + 1));
                GlStateManager.enableTexture2D();
                GlStateManager.enableLighting();
                GlStateManager.disableBlend();
                GL11.glDepthMask(true);
                GlStateManager.popAttrib();
                GlStateManager.popMatrix();
                GlStateManager.resetColor();
            });
        }
    };
    @EventLink
    public final Listener<Render3DEvent> onRender3DMedium = var1 -> {
        BlockPos blockpos = this.blockPos;
        if (blockpos != null) {
            AxisAlignedBB axisalignedbb = new AxisAlignedBB(blockpos, blockpos.add(1, 1, 1));
            this.drawSelectionBox(axisalignedbb, this.rz().rA());
            this.b(ShaderQueueType.BLOOM).c(() -> this.drawSelectionBox(axisalignedbb, this.rz().rA()));
        }
    };
    @EventLink(value = 2)
    public final Listener<PacketReceiveEvent> onPacketReceive = var1 -> {
        Packet packet = var1.getPacket();
        if (!flushingPackets) {
            if (this.delayVelocityUntilBedBroken.wo()) {
                if (packet instanceof S12PacketEntityVelocity s12packetentityvelocity) {
                    if (s12packetentityvelocity.getEntityID() == aEg.thePlayer.getEntityId() && breakingBed) {
                        this.hasDelayedVelocity = true;
                        this.delayedPackets.add(packet);
                        var1.setCancelled();
                    }
                } else if (packet instanceof S32PacketConfirmTransaction && this.hasDelayedVelocity) {
                    ChatUtil.b(aEg.thePlayer.ticksExisted);
                    this.delayedPackets.add(packet);
                    var1.setCancelled();
                }
            }

            if (targetBlock != null && this.cancelVelocityWhilstBreakingSoYouDontSlowDownInAir.wo()) {
                if (packet instanceof S12PacketEntityVelocity s12packetentityvelocity1 && s12packetentityvelocity1.getEntityID() == aEg.thePlayer.getEntityId()
                    )
                 {
                    var1.setCancelled();
                }

                if (packet instanceof S23PacketBlockChange s23packetblockchange) {
                    BlockPos blockpos = s23packetblockchange.getBlockPosition();
                    IBlockState iblockstate = s23packetblockchange.getBlockState();
                    if (targetBlock != null
                        && blockpos.getX() == (int)targetBlock.getX()
                        && blockpos.getY() == (int)targetBlock.getY()
                        && blockpos.getZ() == (int)targetBlock.getZ()
                        && iblockstate.getBlock() == Blocks.air) {
                        targetBlock = null;
                        if (this.block() == null) {
                            breakingBed = false;
                        }
                    }
                }
            }
        }
    };
    @EventLink(value = 4)
    public final Listener<PacketSendEvent> onPacketSend = var1 -> {
        Packet packet = var1.dq();
        if (packet instanceof C07PacketPlayerDigging && targetBlock != null && this.watchdogGroundSpoof.wo()) {
            C07PacketPlayerDigging c07packetplayerdigging = (C07PacketPlayerDigging)packet;
            if (c07packetplayerdigging.getStatus() == Action.START_DESTROY_BLOCK || c07packetplayerdigging.getStatus() == Action.STOP_DESTROY_BLOCK) {
                spoofingGround = true;
                this.lastDiggingTick = aEg.thePlayer.ticksExisted;
            }
        }
    };
    @EventLink(value = 0)
    public final Listener<PreMotionEvent> onPreMotion = var0 -> {
        float f;
        int i = (f = aEg.playerController.curBlockDamageMP - 0.0F) == 0.0F ? 0 : (f < 0.0F ? -1 : 1);
        if (spoofingGround) {
            BlinkComponent.blink();
            var0.setOnGround(true);
        }

        if (aEg.thePlayer.onGround && spoofingGround) {
            BlinkComponent.dispatch();
            spoofingGround = false;
        }
    };
    @EventLink
    public final Listener<StrafeEvent> onStrafe = var0 -> {
        if (spoofingGround) {
            ;
        }
    };
    @EventLink(value = 4)
    public final Listener<BlockDamageEvent> onBlockDamage = var1 -> this.breakProgress = aEg.playerController.curBlockDamageMP;
    @EventLink(value = 4)
    public final Listener<PreUpdateEvent> onPreUpdate = var1 -> {
        this.updateStandPosition();
        if (this.delayVelocityUntilBedBroken.wo() && !breakingBed && this.hasDelayedVelocity) {
            flushingPackets = true;
            this.delayedPackets.forEach(PacketUtil::receive);
            this.delayedPackets.clear();
            this.hasDelayedVelocity = false;
            flushingPackets = false;
            ChatUtil.b(aEg.thePlayer.ticksExisted);
        }

        this.breakCooldownTicks--;
        if (this.breakCooldownTicks <= 0) {
            this.revalidateTarget();
            if (targetBlock == null
                || aEg.thePlayer.getDistance(targetBlock.getX(), targetBlock.getY(), targetBlock.getZ()) > this.range.wo().doubleValue() + 2.5
                || PlayerUtil.o(targetBlock.getX(), targetBlock.getY(), targetBlock.getZ()) instanceof BlockAir) {
                this.updateTarget();
                if (this.holdingAttackKey) {
                    aEg.gameSettings.cgK.setPressed(false);
                    this.holdingAttackKey = false;
                }

                if (targetBlock == null) {
                    return;
                }
            }

            this.destroy();
        }
    };
    @EventLink
    public final Listener<MouseOverEvent> onMouseOver = var1 -> {
        if (targetBlock != null) {
            BlockPos blockpos = new BlockPos(targetBlock.getX(), targetBlock.getY(), targetBlock.getZ());
            Vec3 vec3 = aEg.thePlayer.getPositionEyes(1.0F);
            Vector2f vector2f = this.onlyRotateAtStartAndStop.wo() ? this.getRotations() : RotationComponent.fk;
            Vec3 vec31 = aEg.thePlayer.getVectorForRotation(vector2f.getY(), vector2f.getX());
            double d0 = this.range.wo().doubleValue() + 1.0;
            Vec3 vec32 = vec3.addVector(vec31.xCoord * d0, vec31.yCoord * d0, vec31.zCoord * d0);
            MovingObjectPosition movingobjectposition = PlayerUtil.block(blockpos).collisionRayTrace(aEg.theWorld, blockpos, aEg.thePlayer.getPositionEyes(1.0F), vec32);
            var1.a(movingobjectposition);
        }
    };
    @EventLink
    public final Listener<TeleportEvent> onTeleport = var0 -> {
        if (aEg.thePlayer.getDistance(var0.getPosX(), var0.getPosY(), var0.getPosZ()) > 40.0) {
            home = new Vector3d(var0.getPosX(), var0.getPosY(), var0.getPosZ());
        }
    };

    public Breaker() {
        for (MovementFix movementfix : MovementFix.values()) {
            this.movementCorrection.add(movementfix);
        }

        this.movementCorrection.setDefault(MovementFix.OFF);
    }

    @Override
    public void onEnable() {
        targetBlock = null;
        this.breakProgress = 0.0F;
        this.breakCooldownTicks = 0;
        this.blockPos = null;
        this.lastCheckPosition = null;
        breakingBed = false;
        this.delayedPackets.clear();
        this.delayedPackets.forEach(PacketUtil::receive);
        this.hasDelayedVelocity = false;
    }

    @Override
    public void onDisable() {
        targetBlock = null;
        spoofingGround = false;
        breakingBed = false;
        this.blockPos = null;
        this.lastCheckPosition = null;
        if (!this.delayedPackets.isEmpty()) {
            this.delayedPackets.forEach(PacketUtil::receive);
            this.delayedPackets.clear();
        }

        this.hasDelayedVelocity = false;
        if (this.holdingAttackKey) {
            aEg.gameSettings.cgK.setPressed(false);
            this.holdingAttackKey = false;
        }
    }

    private void drawSelectionBox(AxisAlignedBB box, Color color) {
        double d0 = aEg.getRenderManager().viewerPosX;
        double d1 = aEg.getRenderManager().viewerPosY;
        double d2 = aEg.getRenderManager().viewerPosZ;
        GlStateManager.pushMatrix();
        GlStateManager.pushAttrib();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.disableLighting();
        GlStateManager.disableDepth();
        GL11.glDepthMask(false);
        GL11.glLineWidth(1.5F);
        RenderUtil.color(color);
        RenderGlobal.drawSelectionBoundingBox(box.expand(0.002, 0.002, 0.002).offset(-d0, -d1, -d2));
        GL11.glDepthMask(true);
        GlStateManager.enableDepth();
        GlStateManager.enableTexture2D();
        GlStateManager.enableLighting();
        GlStateManager.disableBlend();
        GlStateManager.popAttrib();
        GlStateManager.popMatrix();
        GlStateManager.resetColor();
    }

    public void updateTarget() {
        if (targetBlock == null
            || PlayerUtil.o(targetBlock.x, targetBlock.y, targetBlock.z) instanceof BlockAir
            || aEg.thePlayer.getDistance(targetBlock.x, targetBlock.y - aEg.thePlayer.getEyeHeight(), targetBlock.z) > this.range.wo().doubleValue() + 2.5) {
            if (lastBlock != null && !this.keepBreakProgressWhenOutOfRange.wo()) {
                aEg.playerController.curBlockDamageMP = 0.0F;
            }

            lastBlock = targetBlock;
            Vector3d aka = this.block();
            if (aka != null) {
                breakingBed = true;
            } else {
                breakingBed = false;
            }

            targetBlock = aka;
        }
    }

    public void rotate() {
        BlockPos blockpos = new BlockPos(targetBlock.getX(), targetBlock.getY(), targetBlock.getZ());
        float f = PlayerUtil.block(blockpos).getPlayerRelativeBlockHardness(aEg.thePlayer, aEg.theWorld, blockpos);
        if (!this.onlyRotateAtStartAndStop.wo() || aEg.playerController.curBlockDamageMP == 0.0F || !(aEg.playerController.curBlockDamageMP <= 1.0F - f - 0.001)) {
            if (this.rotations.wo()) {
                RotationComponent.setRotations(this.getRotations(), 10.0, this.movementCorrection.wo());
            }
        }
    }

    public Vector3d block() {
        if (home != null && aEg.thePlayer.getDistanceSq(home.getX(), home.getY(), home.getZ()) < 1225.0 && this.whiteListOwnBed.wo()) {
            return null;
        }

        if (this.throughWalls.wo() && this.emptySurrounding.wo() && this.surroundings.wo().getName().equals("Full")) {
            return this.getBestCandidateBlock();
        }

        int i = 0;

        for (int j = -5; j <= 5; j++) {
            for (int k = -5; k <= 5; k++) {
                for (int l = -5; l <= 5; l++) {
                    Block block = PlayerUtil.p(j, k, l);
                    Vector3d akax = new Vector3d(aEg.thePlayer.posX + j, aEg.thePlayer.posY + k, aEg.thePlayer.posZ + l);
                    if (block instanceof BlockBed) {
                        if (++i > 1) {
                            MovingObjectPosition movingobjectposition = RayCastUtil.c(RotationUtil.d(akax), this.range.wo().floatValue() + 1.0F);
                            if (movingobjectposition != null
                                && !(
                                    movingobjectposition.hitVec
                                            .distanceTo(new Vec3(aEg.thePlayer.posX, aEg.thePlayer.posY - aEg.thePlayer.getEyeHeight(), aEg.thePlayer.posZ))
                                        > this.range.wo().doubleValue() + 2.0
                                )) {
                                if (this.throughWalls.wo()) {
                                    if (this.emptySurrounding.wo()) {
                                        Vector3d akax2 = akax;
                                        double d0 = Double.MAX_VALUE;
                                        boolean flag = false;
                                        int i1 = this.heypixel.wo() ? 0 : 4;
                                        int j1 = this.heypixel.wo() ? 1 : 0;

                                        for (int k1 = -i1; k1 <= i1; k1++) {
                                            for (int l1 = j1; l1 <= 1; l1++) {
                                                for (int i2 = -i1; i2 <= i1; i2++) {
                                                    Block block1 = PlayerUtil.o(akax2.getX() + k1, akax2.getY() + l1, akax2.getZ() + i2);
                                                    if (!(block1 instanceof BlockBed)
                                                        && !flag
                                                        && !(aEg.thePlayer.getDistance(akax2.getX() + k1, akax2.getY() + l1, akax2.getZ()) + i2 > 46.0)
                                                        && !this.getNeighbourBlocks(akax2.v(k1, l1, i2)).stream().noneMatch(var0 -> var0 instanceof BlockBed)) {
                                                        if (!(block1 instanceof BlockAir) && !(block1 instanceof BlockLiquid)) {
                                                            if (!(
                                                                aEg.thePlayer
                                                                        .getDistance(
                                                                            akax2.getX() + k1, akax2.getY() + l1 - aEg.thePlayer.getEyeHeight(), akax2.getZ() + i2
                                                                        )
                                                                    > this.range.wo().doubleValue() + 2.0
                                                            )) {
                                                                double d1 = block1.wX();
                                                                if (d1 < d0) {
                                                                    d0 = d1;
                                                                    akax2 = akax2.v(k1, l1, i2);
                                                                }
                                                            }
                                                        } else {
                                                            flag = true;
                                                        }
                                                    }
                                                }
                                            }
                                        }

                                        if (!flag) {
                                            if (akax2.equals(akax2)) {
                                                return null;
                                            }

                                            return akax2;
                                        }
                                    }

                                    return akax;
                                }

                                BlockPos blockpos = movingobjectposition.getBlockPos();
                                if (blockpos.h(akax)) {
                                    return akax;
                                }
                            }
                        }
                    }
                }
            }
        }

        return null;
    }

    private Vector3d getBestCandidateBlock() {
        BreakCandidate tg = this.getBestCandidate(aEg.thePlayer.getPositionEyes(1.0F), this.getBedPositions());
        if (tg == null) {
            return null;
        }

        BlockPos blockpos = tg.blocksToBreak.get(0);
        return new Vector3d(blockpos.getX() + 0.5, blockpos.getY() + 0.5, blockpos.getZ() + 0.5);
    }

    private BreakCandidate getBestCandidate(Vec3 vec, List<BlockPos> poses) {
        BreakCandidate tgx = null;

        for (BreakCandidate tgx2 : this.getCandidates(vec, poses)) {
            if (tgx == null || tgx2.breakTime < tgx.breakTime) {
                tgx = tgx2;
            }
        }

        return tgx;
    }

    private List<BreakCandidate> getCandidates(Vec3 vec, List<BlockPos> poses) {
        ArrayList arraylist = new ArrayList();

        for (BlockPos blockpos : poses) {
            for (Vec3 vec3 : this.getSamplePoints(blockpos)) {
                BreakCandidate tg = this.buildCandidate(vec, blockpos, vec3);
                if (tg != null) {
                    arraylist.add(tg);
                }
            }
        }

        return arraylist;
    }

    private BreakCandidate buildCandidate(Vec3 vec, BlockPos pos, Vec3 var3) {
        double range = this.range.wo().doubleValue();
        int i = this.surroundingLayers.wo().intValue();
        ArrayList arraylist = new ArrayList();
        double d1 = 0.0;

        for (BlockPos blockpos : this.traceBlocks(vec, var3)) {
            if (this.distanceToBlock(vec, blockpos) > range) {
                return null;
            }

            if (blockpos.equals(pos)) {
                arraylist.add(pos);
                return new BreakCandidate(pos, arraylist, d1);
            }

            if (!this.isPassable(blockpos)) {
                Block block = PlayerUtil.block(blockpos);
                if (block instanceof BlockBed || block.getBlockHardness(aEg.theWorld, blockpos) < 0.0F || arraylist.size() >= i) {
                    return null;
                }

                arraylist.add(blockpos);
                d1 += 1.0F / Math.max(block.getPlayerRelativeBlockHardness(aEg.thePlayer, aEg.theWorld, blockpos), 1.0E-4F);
            }
        }

        return null;
    }

    private List<BlockPos> traceBlocks(Vec3 vec, Vec3 var2) {
        ArrayList arraylist = new ArrayList();
        int i = MathHelper.floor_double(vec.xCoord);
        int j = MathHelper.floor_double(vec.yCoord);
        int k = MathHelper.floor_double(vec.zCoord);
        int l = MathHelper.floor_double(var2.xCoord);
        int floorYCoord = MathHelper.floor_double(var2.yCoord);
        int floorZCoord = MathHelper.floor_double(var2.zCoord);
        double d0 = var2.xCoord - vec.xCoord;
        double d1 = var2.yCoord - vec.yCoord;
        double d2 = var2.zCoord - vec.zCoord;
        int k1 = d0 > 0.0 ? 1 : (d0 < 0.0 ? -1 : 0);
        int l1 = d1 > 0.0 ? 1 : (d1 < 0.0 ? -1 : 0);
        int i2 = d2 > 0.0 ? 1 : (d2 < 0.0 ? -1 : 0);
        double d3 = k1 == 0 ? Double.MAX_VALUE : 1.0 / Math.abs(d0);
        double d4 = l1 == 0 ? Double.MAX_VALUE : 1.0 / Math.abs(d1);
        double d5 = i2 == 0 ? Double.MAX_VALUE : 1.0 / Math.abs(d2);
        double d6 = k1 == 0 ? Double.MAX_VALUE : (k1 > 0 ? i + 1 - vec.xCoord : vec.xCoord - i) / Math.abs(d0);
        double d7 = l1 == 0 ? Double.MAX_VALUE : (l1 > 0 ? j + 1 - vec.yCoord : vec.yCoord - j) / Math.abs(d1);
        double d8 = i2 == 0 ? Double.MAX_VALUE : (i2 > 0 ? k + 1 - vec.zCoord : vec.zCoord - k) / Math.abs(d2);
        arraylist.add(new BlockPos(i, j, k));

        for (; (i != l || j != floorYCoord || k != floorZCoord) && arraylist.size() < 64; arraylist.add(new BlockPos(i, j, k))) {
            if (d6 <= d7 && d6 <= d8) {
                i += k1;
                d6 += d3;
            } else if (d7 <= d8) {
                j += l1;
                d7 += d4;
            } else {
                k += i2;
                d8 += d5;
            }
        }

        return arraylist;
    }

    private void revalidateTarget() {
        if (targetBlock != null && this.throughWalls.wo() && this.emptySurrounding.wo() && this.surroundings.wo().getName().equals("Full")) {
            Vec3 vec3 = new Vec3(aEg.thePlayer.posX, aEg.thePlayer.posY, aEg.thePlayer.posZ);
            if (this.lastCheckPosition != null && !(this.lastCheckPosition.squareDistanceTo(vec3) < 0.25) && aEg.thePlayer.ticksExisted - this.lastSurroundingSearchTick >= 5) {
                this.lastCheckPosition = vec3;
                this.lastSurroundingSearchTick = aEg.thePlayer.ticksExisted;
                BlockPos blockpos = new BlockPos(targetBlock.getX(), targetBlock.getY(), targetBlock.getZ());
                BreakCandidate tgxx = null;
                BreakCandidate tgx = null;

                for (BreakCandidate tgxx2 : this.getCandidates(aEg.thePlayer.getPositionEyes(1.0F), this.getBedPositions())) {
                    if (tgx == null || tgxx2.breakTime < tgx.breakTime) {
                        tgx = tgxx2;
                    }

                    if (tgxx2.blocksToBreak.get(0).equals(blockpos) && (tgxx == null || tgxx2.breakTime < tgxx.breakTime)) {
                        tgxx = tgxx2;
                    }
                }

                if (tgx != null && !tgx.blocksToBreak.get(0).equals(blockpos)) {
                    if (tgxx != null) {
                        double d0 = aEg.playerController.curBlockDamageMP
                            / Math.max(PlayerUtil.block(blockpos).getPlayerRelativeBlockHardness(aEg.thePlayer, aEg.theWorld, blockpos), 1.0E-4F);
                        if (tgx.breakTime >= (tgxx.breakTime - d0) * 0.9) {
                            return;
                        }
                    }

                    aEg.playerController.curBlockDamageMP = 0.0F;
                    this.breakProgress = 0.0F;
                    if (this.holdingAttackKey) {
                        aEg.gameSettings.cgK.setPressed(false);
                        this.holdingAttackKey = false;
                    }

                    targetBlock = null;
                }
            } else {
                if (this.lastCheckPosition == null) {
                    this.lastCheckPosition = vec3;
                }
            }
        } else {
            this.lastCheckPosition = null;
        }
    }

    private void updateStandPosition() {
        if (!this.showBestStandPosition.wo() || !this.throughWalls.wo() || !this.emptySurrounding.wo() || !this.surroundings.wo().getName().equals("Full")) {
            this.blockPos = null;
        } else if (aEg.thePlayer.ticksExisted - this.lastStandSearchTick >= 10) {
            this.lastStandSearchTick = aEg.thePlayer.ticksExisted;
            this.blockPos = this.findBestStandPosition();
        }
    }

    private BlockPos findBestStandPosition() {
        double range = this.range.wo().doubleValue();
        int i = (int)Math.ceil(range) + 1;
        List list = this.getBedPositions();
        if (list.isEmpty()) {
            return null;
        }

        BlockPos blockpos = (BlockPos)list.get(0);
        BlockPos blockpos1 = null;
        double d1 = 0.0;
        double d2 = 0.0;

        for (int j = -i; j <= i; j++) {
            for (int k = -2; k <= 2; k++) {
                for (int l = -i; l <= i; l++) {
                    BlockPos blockpos2 = blockpos.add(j, k, l);
                    Vec3 vec3 = new Vec3(blockpos2.getX() + 0.5, blockpos2.getY() + aEg.thePlayer.getEyeHeight(), blockpos2.getZ() + 0.5);
                    boolean flag = false;

                    for (BlockPos blockpos3 : (Iterable<BlockPos>)list) {
                        if (this.distanceToBlock(vec3, blockpos3) <= range) {
                            flag = true;
                            break;
                        }
                    }

                    if (flag && this.canStandAt(blockpos2)) {
                        BreakCandidate tg = this.getBestCandidate(vec3, list);
                        if (tg != null) {
                            double d3 = aEg.thePlayer.getDistanceSq(blockpos2.getX() + 0.5, blockpos2.getY(), blockpos2.getZ() + 0.5);
                            if (blockpos1 == null || tg.breakTime < d1 - 0.001 || Math.abs(tg.breakTime - d1) <= 0.001 && d3 < d2) {
                                blockpos1 = blockpos2;
                                d1 = tg.breakTime;
                                d2 = d3;
                            }
                        }
                    }
                }
            }
        }

        return blockpos1;
    }

    private boolean canStandAt(BlockPos pos) {
        return this.isPassable(pos)
            && this.isPassable(pos.up())
            && PlayerUtil.block(pos.down()).getCollisionBoundingBox(aEg.theWorld, pos.down(), aEg.theWorld.getBlockState(pos.down())) != null;
    }

    private boolean isPassable(BlockPos pos) {
        return PlayerUtil.block(pos).getCollisionBoundingBox(aEg.theWorld, pos, aEg.theWorld.getBlockState(pos)) == null;
    }

    private double distanceToBlock(Vec3 vec, BlockPos pos) {
        double d0 = Math.max(pos.getX() - vec.xCoord, Math.max(0.0, vec.xCoord - (pos.getX() + 1)));
        double d1 = Math.max(pos.getY() - vec.yCoord, Math.max(0.0, vec.yCoord - (pos.getY() + 1)));
        double d2 = Math.max(pos.getZ() - vec.zCoord, Math.max(0.0, vec.zCoord - (pos.getZ() + 1)));
        return Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
    }

    private List<BlockPos> getBedPositions() {
        ArrayList arraylist = new ArrayList();

        for (int i = -5; i <= 5; i++) {
            for (int j = -5; j <= 5; j++) {
                for (int k = -5; k <= 5; k++) {
                    if (PlayerUtil.p(i, j, k) instanceof BlockBed) {
                        arraylist.add(new BlockPos(aEg.thePlayer.posX + i, aEg.thePlayer.posY + j, aEg.thePlayer.posZ + k));
                    }
                }
            }
        }

        return arraylist;
    }

    private Vec3[] getSamplePoints(BlockPos pos) {
        return new Vec3[]{
            new Vec3(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5),
            new Vec3(pos.getX() + 0.1, pos.getY() + 0.1, pos.getZ() + 0.1),
            new Vec3(pos.getX() + 0.9, pos.getY() + 0.1, pos.getZ() + 0.9),
            new Vec3(pos.getX() + 0.1, pos.getY() + 0.5, pos.getZ() + 0.9),
            new Vec3(pos.getX() + 0.9, pos.getY() + 0.5, pos.getZ() + 0.1)
        };
    }

    public List<Block> getNeighbourBlocks(Vector3d var1) {
        ArrayList arraylist = new ArrayList();

        for (EnumFacing enumfacing : EnumFacing.values()) {
            if (enumfacing != EnumFacing.UP) {
                Vector3d aka = var1.e(new Vector3d(enumfacing.getDirectionVec().getX(), enumfacing.getDirectionVec().getY(), enumfacing.getDirectionVec().getZ()));
                arraylist.add(PlayerUtil.c(aka));
            }
        }

        return arraylist;
    }

    public void dispatchBlockDamage(BlockPos pos) {
        BlockDamageEvent blockdamageevent = new BlockDamageEvent(aEg.thePlayer, aEg.thePlayer.worldObj, pos);
        Client.a.e().d(blockdamageevent);
    }

    public void destroy() {
        boolean flag = this.slowDownInAir.wo();
        boolean onGround = aEg.thePlayer.onGround;
        if (!flag) {
            aEg.thePlayer.onGround = true;
        }

        label26: {
            BlockPos blockpos;
            {
                blockpos = new BlockPos(targetBlock.getX(), targetBlock.getY(), targetBlock.getZ());
                String s = this.mode.wo().getName();
                switch (s) {
                    case "Instant":
                        this.rotate();
                        this.dispatchBlockDamage(blockpos);
                        aEg.thePlayer.swingItem();
                        PacketUtil.send(new C07PacketPlayerDigging(Action.START_DESTROY_BLOCK, blockpos, EnumFacing.UP));
                        aEg.thePlayer.swingItem();
                        PacketUtil.send(new C07PacketPlayerDigging(Action.STOP_DESTROY_BLOCK, blockpos, EnumFacing.UP));
                        targetBlock = null;
                        this.breakCooldownTicks = 20;
                        aEg.playerController.onPlayerDestroyBlock(blockpos, EnumFacing.DOWN);
                        break label26;
                    case "Normal":
                        break;
                    default:
                        break label26;
                }
            }

            this.dispatchBlockDamage(blockpos);
            this.rotate();
            aEg.gameSettings.cgK.setPressed(true);
            this.holdingAttackKey = true;
        }

        aEg.thePlayer.onGround = onGround;
    }

    public Vector2f getRotations() {
        return RotationUtil.d(
            new Vector3d(
                Math.floor(targetBlock.getX()) + 0.5 + (Math.random() - 0.5) / 4.0,
                Math.floor(targetBlock.getY()) + 0.1,
                Math.floor(targetBlock.getZ()) + 0.5 + (Math.random() - 0.5) / 4.0
            )
        );
    }
}
