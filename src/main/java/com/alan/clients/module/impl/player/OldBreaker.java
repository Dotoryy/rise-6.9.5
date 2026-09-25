package com.alan.clients.module.impl.player;

import com.alan.clients.Client;
import com.alan.clients.component.impl.player.RotationComponent;
import com.alan.clients.component.impl.player.rotationcomponent.MovementFix;
import com.alan.clients.module.Module;
import com.alan.clients.module.api.Category;
import com.alan.clients.module.api.ModuleInfo;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreUpdateEvent;
import com.alan.clients.newevent.impl.other.BlockDamageEvent;
import com.alan.clients.newevent.impl.other.TeleportEvent;
import com.alan.clients.newevent.impl.packet.PacketReceiveEvent;
import com.alan.clients.newevent.impl.render.MouseOverEvent;
import com.alan.clients.newevent.impl.render.Render3DEvent;
import com.alan.clients.util.animation.Animation;
import com.alan.clients.util.animation.Easing;
import com.alan.clients.util.render.RenderUtil;
import com.alan.clients.util.vector.Vector2f;
import com.alan.clients.value.impl.BooleanValue;
import com.alan.clients.value.impl.ListValue;
import com.alan.clients.value.impl.ModeValue;
import com.alan.clients.value.impl.SubMode;
import com.alan.clients.util.RayCastUtil;
import com.alan.clients.util.chat.ChatUtil;
import com.alan.clients.util.packet.PacketUtil;
import com.alan.clients.util.player.PlayerUtil;
import com.alan.clients.util.rotation.RotationUtil;
import com.alan.clients.util.vector.Vector3d;
import com.alan.clients.util.vector.Vector3i;
import com.alan.clients.util.shader.ShaderQueueType;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockBed;
import net.minecraft.block.BlockLiquid;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.network.play.client.C07PacketPlayerDigging.Action;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.GL11;

@ModuleInfo(aliases = "module.player.breaker.name", description = "module.player.breaker.description", category = Category.PLAYER)
public class OldBreaker extends Module {
    public final ModeValue mode = new ModeValue("Mode", this).add(new SubMode("Normal")).add(new SubMode("Instant")).setDefault("Normal");
    public final BooleanValue bed = new BooleanValue("Bed", this, true);
    public final BooleanValue keepBreakProgressWhenOutOfRange = new BooleanValue("Keep Break Progress When Out Of Range", this, true);
    public final BooleanValue cancelVelocityWhilstBreakingSoYouDontSlowDownInAir = new BooleanValue("Cancel velocity whilst breaking, so you don't slow down in air", this, true);
    public final BooleanValue throughWalls = new BooleanValue("Through Walls", this, true);
    private final BooleanValue emptySurrounding = new BooleanValue("Empty Surrounding", this, false, () -> !this.throughWalls.wo());
    public final BooleanValue rotations = new BooleanValue("Rotate", this, true);
    public final BooleanValue onlyRotateAtStartAndStop = new BooleanValue("Only Rotate at Start and Stop", this, true);
    public final BooleanValue whiteListOwnBed = new BooleanValue("Whitelist Own Bed", this, true);
    public final BooleanValue slowDownInAir = new BooleanValue("Slow Down In Air", this, true);
    private final ListValue<MovementFix> movementCorrection = new ListValue<>("Movement Correction", this);
    private Vector3d targetBlock;
    private Vector3d lastBlock;
    private Vector3d home;
    private int breakCooldownTicks;
    private boolean holdingAttackKey;
    private float breakProgress;
    private Animation progressAnimation = new Animation(Easing.LINEAR, 50L);
    @EventLink
    public final Listener<Render3DEvent> onRender3D = var1 -> {
        if (this.targetBlock != null) {
            Vector3i akb = new Vector3i((int)Math.floor(this.targetBlock.getX()), (int)Math.floor(this.targetBlock.getY()), (int)Math.floor(this.targetBlock.getZ()));
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
    @EventLink(value = 4)
    public final Listener<PacketReceiveEvent> onPacketReceive = var1 -> {
        if (this.cancelVelocityWhilstBreakingSoYouDontSlowDownInAir.wo() && this.targetBlock != null) {
            if (var1.getPacket() instanceof S12PacketEntityVelocity s12packetentityvelocity && s12packetentityvelocity.getEntityID() == aEg.thePlayer.getEntityId()) {
                var1.setCancelled();
                s12packetentityvelocity.motionY = 0;
                s12packetentityvelocity.motionX = 0;
                s12packetentityvelocity.motionZ = 0;
            }
        }
    };
    @EventLink(value = 4)
    public final Listener<BlockDamageEvent> onBlockDamage = var1 -> {
        this.breakProgress = aEg.playerController.curBlockDamageMP;
        ChatUtil.b("Updated Damage");
        ChatUtil.b(var1.getBlockPos());
        ChatUtil.b(this.targetBlock.getX() + ", " + this.targetBlock.getY() + ", " + this.targetBlock.getZ());
    };
    @EventLink(value = 4)
    public final Listener<PreUpdateEvent> onPreUpdate = var1 -> {
        this.breakCooldownTicks--;
        if (this.breakCooldownTicks <= 0) {
            if (aEg.playerController.curBlockDamageMP == 0.0F || aEg.playerController.curBlockDamageMP >= 1.0F) {
                this.breakProgress = 0.0F;
            }

            if (this.targetBlock == null
                || aEg.thePlayer.getDistance(this.targetBlock.getX(), this.targetBlock.getY(), this.targetBlock.getZ()) > 4.0
                || PlayerUtil.o(this.targetBlock.getX(), this.targetBlock.getY(), this.targetBlock.getZ()) instanceof BlockAir) {
                this.updateTarget();
                if (this.holdingAttackKey) {
                    aEg.gameSettings.cgK.setPressed(false);
                    this.holdingAttackKey = false;
                }

                if (this.targetBlock == null) {
                    return;
                }
            }

            this.destroy();
        }
    };
    @EventLink
    public final Listener<MouseOverEvent> onMouseOver = var1 -> {
        if (this.targetBlock != null) {
            MovingObjectPosition movingobjectposition = this.getTargetMouseOver();
            if (!this.isHitOnTarget(movingobjectposition)) {
                ChatUtil.b("Not Target");
            } else {
                var1.a(movingobjectposition);
            }
        }
    };
    @EventLink
    public final Listener<TeleportEvent> onTeleport = var1 -> {
        if (aEg.thePlayer.getDistance(var1.getPosX(), var1.getPosY(), var1.getPosZ()) > 40.0) {
            this.home = new Vector3d(var1.getPosX(), var1.getPosY(), var1.getPosZ());
        }
    };

    public OldBreaker() {
        for (MovementFix movementfix : MovementFix.values()) {
            this.movementCorrection.add(movementfix);
        }

        this.movementCorrection.setDefault(MovementFix.OFF);
    }

    @Override
    public void onEnable() {
        this.targetBlock = null;
        this.breakProgress = 0.0F;
        this.breakCooldownTicks = 0;
        this.holdingAttackKey = false;
        this.lastBlock = null;
    }

    @Override
    public void onDisable() {
        this.targetBlock = null;
        if (this.holdingAttackKey) {
            aEg.gameSettings.cgK.setPressed(false);
            this.holdingAttackKey = false;
        }
    }

    public void updateTarget() {
        if (this.targetBlock == null
            || PlayerUtil.o(this.targetBlock.x, this.targetBlock.y, this.targetBlock.z) instanceof BlockAir
            || aEg.thePlayer.getDistance(this.targetBlock.x, this.targetBlock.y - aEg.thePlayer.getEyeHeight(), this.targetBlock.z) > 4.5) {
            if (this.lastBlock != null && !this.keepBreakProgressWhenOutOfRange.wo()) {
                aEg.playerController.curBlockDamageMP = 0.0F;
            }

            this.lastBlock = this.targetBlock;
            this.targetBlock = this.block();
        }
    }

    public void rotate() {
        BlockPos blockpos = new BlockPos(Math.floor(this.targetBlock.getX()), Math.floor(this.targetBlock.getY()), Math.floor(this.targetBlock.getZ()));
        float f = PlayerUtil.block(blockpos).getPlayerRelativeBlockHardness(aEg.thePlayer, aEg.theWorld, blockpos);
        if (!this.onlyRotateAtStartAndStop.wo() || aEg.playerController.curBlockDamageMP == 0.0F || aEg.playerController.curBlockDamageMP >= 1.0F - f - 0.001) {
            if (this.rotations.wo()) {
                RotationComponent.setRotations(this.getRotations(), 10.0, this.movementCorrection.wo());
            }
        }
    }

    public Vector2f getRotations() {
        return RotationUtil.d(
            new Vector3d(
                Math.floor(this.targetBlock.getX()) + 0.5 + (Math.random() - 0.5) / 4.0,
                Math.floor(this.targetBlock.getY()) + 0.1,
                Math.floor(this.targetBlock.getZ()) + 0.5 + (Math.random() - 0.5) / 4.0
            )
        );
    }

    public Vector3d block() {
        if (this.home != null && aEg.thePlayer.getDistanceSq(this.home.getX(), this.home.getY(), this.home.getZ()) < 1225.0 && this.whiteListOwnBed.wo()) {
            return null;
        }

        int i = 0;

        for (int j = -5; j <= 5; j++) {
            for (int k = -5; k <= 5; k++) {
                for (int l = -5; l <= 5; l++) {
                    Block block = PlayerUtil.p(j, k, l);
                    Vector3d akax = new Vector3d(aEg.thePlayer.posX + j, aEg.thePlayer.posY + k, aEg.thePlayer.posZ + l);
                    if (block instanceof BlockBed) {
                        if (++i > 1) {
                            MovingObjectPosition movingobjectposition = RayCastUtil.c(RotationUtil.d(akax), 4.5);
                            if (movingobjectposition != null
                                && !(
                                    movingobjectposition.hitVec
                                            .distanceTo(new Vec3(aEg.thePlayer.posX, aEg.thePlayer.posY - aEg.thePlayer.getEyeHeight(), aEg.thePlayer.posZ))
                                        > 4.5
                                )) {
                                if (this.throughWalls.wo()) {
                                    if (this.emptySurrounding.wo()) {
                                        Vector3d akax2 = akax;
                                        double d0 = Double.MAX_VALUE;
                                        boolean flag = false;

                                        for (int i1 = -4; i1 <= 4; i1++) {
                                            for (int j1 = 0; j1 <= 1; j1++) {
                                                for (int k1 = -4; k1 <= 4; k1++) {
                                                    Block block1 = PlayerUtil.o(akax2.getX() + i1, akax2.getY() + j1, akax2.getZ() + k1);
                                                    if (!(block1 instanceof BlockBed)
                                                        && !flag
                                                        && !(aEg.thePlayer.getDistance(akax2.getX() + i1, akax2.getY() + j1, akax2.getZ()) + k1 > 4.5)
                                                        && !this.getNeighbourBlocks(akax2.v(i1, j1, k1)).stream().noneMatch(var0 -> var0 instanceof BlockBed)) {
                                                        if (!(block1 instanceof BlockAir) && !(block1 instanceof BlockLiquid)) {
                                                            if (!(
                                                                aEg.thePlayer
                                                                        .getDistance(
                                                                            akax2.getX() + i1, akax2.getY() + j1 - aEg.thePlayer.getEyeHeight(), akax2.getZ() + k1
                                                                        )
                                                                    > 4.5
                                                            )) {
                                                                double d1 = block1.wX();
                                                                if (d1 < d0) {
                                                                    d0 = d1;
                                                                    akax2 = akax2.v(i1, j1, k1);
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
                blockpos = new BlockPos(this.targetBlock.getX(), this.targetBlock.getY(), this.targetBlock.getZ());
                aEg.objectMouseOver = this.getTargetMouseOver();
                aEg.playerController.curBlockDamageMP = this.breakProgress;
                String s = this.mode.wo().getName();
                switch (s) {
                    case "Instant":
                        this.rotate();
                        this.dispatchBlockDamage(blockpos);
                        aEg.thePlayer.swingItem();
                        PacketUtil.send(new C07PacketPlayerDigging(Action.START_DESTROY_BLOCK, blockpos, EnumFacing.UP));
                        aEg.thePlayer.swingItem();
                        PacketUtil.send(new C07PacketPlayerDigging(Action.STOP_DESTROY_BLOCK, blockpos, EnumFacing.UP));
                        this.targetBlock = null;
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

    private MovingObjectPosition getTargetMouseOver() {
        MovingObjectPosition movingobjectposition = this.rayTraceTarget(RotationComponent.fk);
        if (!this.isHitOnTarget(movingobjectposition)) {
            movingobjectposition = this.rayTraceTarget(this.getRotations());
        }

        if (!this.isHitOnTarget(movingobjectposition)) {
            movingobjectposition = new MovingObjectPosition(
                new Vec3(this.targetBlock.getX() + Math.random(), this.targetBlock.getY() + 1.0, this.targetBlock.getZ() + Math.random()),
                EnumFacing.UP,
                new BlockPos(this.targetBlock.getX(), this.targetBlock.getY(), this.targetBlock.getZ())
            );
        }

        return movingobjectposition;
    }

    private boolean isHitOnTarget(MovingObjectPosition hit) {
        return hit != null && hit.typeOfHit == MovingObjectType.BLOCK && hit.getBlockPos().h(new Vector3d(this.targetBlock.getX(), this.targetBlock.getY(), this.targetBlock.getZ()));
    }

    private MovingObjectPosition rayTraceTarget(Vector2f vec2) {
        Block block = PlayerUtil.c(this.targetBlock);
        AxisAlignedBB axisalignedbb = block.getCollisionBoundingBox(
            aEg.theWorld, new BlockPos(this.targetBlock.getX(), this.targetBlock.getY(), this.targetBlock.getZ()), block.getDefaultState()
        );
        Vec3 vec3 = aEg.thePlayer.getPositionEyes(1.0F);
        Vec3 vec = aEg.thePlayer.getVectorForRotation(vec2.getY(), vec2.getX());
        Vec3 vec32 = vec3.addVector(vec.xCoord * 4.5, vec.yCoord * 4.5, vec.zCoord * 4.5);
        MovingObjectPosition movingobjectposition = axisalignedbb.calculateIntercept(vec3, vec32);
        if (movingobjectposition != null) {
            movingobjectposition.a(new BlockPos(this.targetBlock.getX(), this.targetBlock.getY(), this.targetBlock.getZ()));
        }

        return movingobjectposition;
    }
}
