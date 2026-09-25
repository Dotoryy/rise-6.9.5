package com.alan.clients.module.impl.player;

import com.alan.clients.component.impl.player.RotationComponent;
import com.alan.clients.component.impl.player.SlotComponent;
import com.alan.clients.component.impl.player.rotationcomponent.MovementFix;
import com.alan.clients.module.Module;
import com.alan.clients.module.api.Category;
import com.alan.clients.module.api.ModuleInfo;
import com.alan.clients.module.impl.player.Scaffold;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreUpdateEvent;
import com.alan.clients.newevent.impl.other.AttackEvent;
import com.alan.clients.newevent.impl.other.TeleportEvent;
import com.alan.clients.newevent.impl.other.WorldChangeEvent;
import com.alan.clients.newevent.impl.render.Render2DEvent;
import com.alan.clients.newevent.impl.render.Render3DEvent;
import com.alan.clients.util.render.RenderUtil;
import com.alan.clients.value.Mode;
import com.alan.clients.value.impl.BooleanValue;
import com.alan.clients.value.impl.ModeValue;
import com.alan.clients.value.impl.NumberValue;
import com.alan.clients.value.impl.SubMode;
import com.alan.clients.util.packet.PacketUtil;
import com.alan.clients.util.player.PlayerUtil;
import com.alan.clients.util.player.SlotUtil;
import com.alan.clients.util.rotation.RotationUtil;
import com.alan.clients.component.impl.render.NotificationComponent;
import java.awt.Color;
import java.util.ArrayList;
import lombok.Generated;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockBed;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.l;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.util.Vec3i;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

@ModuleInfo(aliases={"module.player.breaker.name"}, description="module.player.breaker.description", category=Category.PLAYER)
public class LegacyBreaker
extends Module {
    public final ModeValue mode = new ModeValue("Mode", this).add(new Mode[]{new SubMode("Through Walls")}).add(new Mode[]{new SubMode("Surroundings")}).setDefault("Through Walls");
    private final NumberValue range = new NumberValue("Range", this, (Number)4, (Number)1, (Number)5, (Number)0.1);
    public final BooleanValue rotations = new BooleanValue("Rotate", (Module)this, (Boolean)true);
    public final BooleanValue movementCorrection = new BooleanValue("Movement Correction", (Module)this, (Boolean)false);
    public final BooleanValue whitelistFriendlyBed = new BooleanValue("Whitelist Friendly Bed", (Module)this, (Boolean)false);
    public final BooleanValue auraCheck = new BooleanValue("Aura Check", (Module)this, (Boolean)false);
    private final NumberValue fastBreak = new NumberValue("FastBreak", this, (Number)0, (Number)0, (Number)1, (Number)0.1);
    private final NumberValue fastBreakBed = new NumberValue("FastBreak bed", this, (Number)0, (Number)0, (Number)1, (Number)0.1);
    private final NumberValue airMultiplier = new NumberValue("Air Multiplier", this, (Number)1, (Number)0, (Number)3, (Number)0.1);
    private float damagePerTick;
    private float breakProgress;
    private int ticksSinceAttack;
    private boolean firstTeleport = true;
    private int aeM;
    private int cooldownTicks;
    private int aeN;
    private Vec3 whitelistedBedPosition;
    private BlockPos nearestBed;
    private BlockPos targetBlock;
    boolean breaking = false;
    @EventLink(value=4)
    public final Listener<WorldChangeEvent> onWorldChange = worldChangeEvent -> {
        this.firstTeleport = true;
    };
    @EventLink(value=4)
    public final Listener<TeleportEvent> onTeleport = teleportEvent -> {
        if (LegacyBreaker.aEg.thePlayer.getDistance(teleportEvent.getPosX(), teleportEvent.getPosY(), teleportEvent.getPosZ()) > 30.0) {
            if (this.firstTeleport) {
                this.firstTeleport = false;
                NotificationComponent.e("Breaker", "Whitelisted bed");
            }
            this.whitelistedBedPosition = new Vec3(teleportEvent.getPosX(), teleportEvent.getPosY(), teleportEvent.getPosZ());
        }
    };
    @EventLink(value=4)
    public final Listener<PreUpdateEvent> onPreUpdate = preUpdateEvent -> {
        block27: {
            ArrayList<BlockPos> arrayList;
            Scaffold scaffold = this.e(Scaffold.class);
            if (scaffold == null || scaffold.isEnabled()) {
                return;
            }
            EntityPlayerSP entityPlayerSP = LegacyBreaker.aEg.thePlayer;
            ++this.ticksSinceAttack;
            --this.cooldownTicks;
            if (this.cooldownTicks > 0 || ((Boolean)this.whitelistFriendlyBed.wo()).booleanValue() && this.whitelistedBedPosition != null && entityPlayerSP.getDistanceSq(this.whitelistedBedPosition.xCoord, this.whitelistedBedPosition.yCoord, this.whitelistedBedPosition.zCoord) < 1500.0) {
                return;
            }
            boolean bl = false;
            int n2 = 0;
            int n3 = 0;
            int n4 = 0;
            int n5 = -((Number)this.range.wo()).intValue() + 1;
            block0: while (true) {
                if (n5 > ((Number)this.range.wo()).intValue() + 1) {
                    arrayList = new ArrayList<BlockPos>();
                    if (bl) break;
                    if (this.breaking) {
                        this.breakProgress = 0.0f;
                    }
                    this.breaking = false;
                    break block27;
                }
                int n6 = -((Number)this.range.wo()).intValue() + 1;
                while (true) {
                    if (n6 <= ((Number)this.range.wo()).intValue() + 1) {
                    } else {
                        ++n5;
                        continue block0;
                    }
                    for (int i2 = -((Number)this.range.wo()).intValue() + 1; i2 <= ((Number)this.range.wo()).intValue() + 1; ++i2) {
                        Block block = PlayerUtil.p(n5, n6, i2);
                        if (!(block instanceof BlockBed)) continue;
                        bl = true;
                        n2 = n5;
                        n3 = n6;
                        n4 = i2;
                        BlockPos blockPos = new BlockPos(entityPlayerSP.posX + (double)n5, entityPlayerSP.posY + (double)n6, entityPlayerSP.posZ + (double)i2);
                        if (!(this.breakProgress <= 0.0f)) continue;
                        if (this.nearestBed != null) {
                            if (!(blockPos.distanceSq((Vec3i)LegacyBreaker.aEg.thePlayer.getPosition()) < this.nearestBed.distanceSq((Vec3i)LegacyBreaker.aEg.thePlayer.getPosition()))) continue;
                            this.nearestBed = blockPos;
                            continue;
                        }
                        this.nearestBed = blockPos;
                    }
                    ++n6;
                }
            }
            this.breaking = true;
            int n7 = 0;
            arrayList.add(new BlockPos(n2 + 1, n3, n4));
            arrayList.add(new BlockPos(n2 - 1, n3, n4));
            arrayList.add(new BlockPos(n2, n3, n4 + 1));
            arrayList.add(new BlockPos(n2, n3, n4 - 1));
            arrayList.add(new BlockPos(n2, n3 + 1, n4));
            int n8 = 0;
            while (true) {
                block29: {
                    block28: {
                        if (n8 >= arrayList.size()) break block28;
                        BlockPos blockPos = (BlockPos)arrayList.get(n8);
                        Block block = PlayerUtil.p(blockPos.getX(), blockPos.getY(), blockPos.getZ());
                        if (!(block instanceof BlockBed)) break block29;
                        arrayList.remove(n8);
                        arrayList.add(new BlockPos(blockPos.getX() + 1, blockPos.getY(), blockPos.getZ()));
                        arrayList.add(new BlockPos(blockPos.getX() - 1, blockPos.getY(), blockPos.getZ()));
                        arrayList.add(new BlockPos(blockPos.getX(), blockPos.getY(), blockPos.getZ() + 1));
                        arrayList.add(new BlockPos(blockPos.getX(), blockPos.getY(), blockPos.getZ() - 1));
                        arrayList.add(new BlockPos(blockPos.getX(), blockPos.getY() + 1, blockPos.getZ()));
                    }
                    for (int i3 = 0; i3 < arrayList.size(); ++i3) {
                        BlockPos blockPos = (BlockPos)arrayList.get(i3);
                        Block block = PlayerUtil.p(blockPos.getX(), blockPos.getY(), blockPos.getZ());
                        if (!(block instanceof BlockAir)) continue;
                        ++n7;
                    }
                    break;
                }
                ++n8;
            }
            if (n7 > 0 || !((Mode)this.mode.wo()).getName().equals("Surroundings")) {
                this.targetBlock = this.nearestBed;
            } else {
                float f2 = 1.0E8f;
                for (BlockPos blockPos : arrayList) {
                    Block block = PlayerUtil.p(blockPos.getX(), blockPos.getY(), blockPos.getZ());
                    if (!(block.wX() < f2) || block instanceof BlockBed) continue;
                    f2 = block.wX();
                    if (!(this.breakProgress <= 0.0f)) continue;
                    this.targetBlock = new BlockPos(entityPlayerSP.posX + (double)blockPos.getX(), entityPlayerSP.posY + (double)blockPos.getY(), entityPlayerSP.posZ + (double)blockPos.getZ());
                }
                for (int i4 = 0; i4 < arrayList.size(); ++i4) {
                    BlockPos blockPos = (BlockPos)arrayList.get(i4);
                    Block block = PlayerUtil.p(blockPos.getX(), blockPos.getY(), blockPos.getZ());
                    if (f2 != block.wX() || !(blockPos.add((Vec3i)entityPlayerSP.getPosition()).j((Vec3i)entityPlayerSP.getPosition()) < this.targetBlock.j((Vec3i)entityPlayerSP.getPosition())) || !(this.breakProgress <= 0.0f)) continue;
                    this.targetBlock = new BlockPos(entityPlayerSP.posX + (double)blockPos.getX(), entityPlayerSP.posY + (double)blockPos.getY(), entityPlayerSP.posZ + (double)blockPos.getZ());
                }
            }
            if (this.targetBlock.j((Vec3i)entityPlayerSP.getPosition()) <= (double)((Number)this.range.wo()).floatValue()) {
                int n9;
                if (((Boolean)this.rotations.wo()).booleanValue()) {
                    this.rotateToBlock(this.targetBlock);
                }
                if ((n9 = SlotUtil.findTool(this.targetBlock)) != -1) {
                    PacketUtil.send(new l(n9));
                }
                if (n9 != -1) {
                    this.damagePerTick = SlotUtil.getPlayerRelativeBlockHardness((EntityPlayer)entityPlayerSP, (World)LegacyBreaker.aEg.theWorld, this.targetBlock, n9);
                } else {
                    WorldClient worldClient = LegacyBreaker.aEg.theWorld;
                    this.d(SlotComponent.class);
                    this.damagePerTick = SlotUtil.getPlayerRelativeBlockHardness((EntityPlayer)entityPlayerSP, (World)worldClient, this.targetBlock, SlotComponent.bQ());
                }
                if (!LegacyBreaker.aEg.thePlayer.onGround) {
                    this.damagePerTick *= ((Number)this.airMultiplier.wo()).floatValue();
                }
                if (this.breakProgress == 0.0f) {
                    PacketUtil.send(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, this.targetBlock, EnumFacing.DOWN));
                }
                LegacyBreaker.aEg.thePlayer.swingItem();
                this.breakProgress += this.damagePerTick;
                LegacyBreaker.aEg.theWorld.sendBlockBreakProgress(entityPlayerSP.getEntityId(), this.targetBlock, (int)(this.breakProgress * 10.0f - 1.0f));
                float f3 = PlayerUtil.p(this.targetBlock.getX(), this.targetBlock.getY(), this.targetBlock.getZ()) instanceof BlockBed ? 1.0f - ((Number)this.fastBreakBed.wo()).floatValue() : 1.0f - ((Number)this.fastBreak.wo()).floatValue();
                if (this.breakProgress >= f3) {
                    this.breakProgress = 0.0f;
                    PacketUtil.send(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, this.targetBlock, EnumFacing.DOWN));
                    LegacyBreaker.aEg.playerController.onPlayerDestroyBlock(this.targetBlock, EnumFacing.UP);
                }
                if (n9 != -1) {
                    this.d(SlotComponent.class);
                    PacketUtil.send(new l(SlotComponent.bQ()));
                }
            }
        }
        LegacyBreaker.aEg.playerController.curBlockDamageMP = this.breakProgress;
    };
    @EventLink
    public final Listener<AttackEvent> onAttack = attackEvent -> {
        if (((Boolean)this.whitelistFriendlyBed.wo()).booleanValue()) {
            this.ticksSinceAttack = 0;
        } else if (this.ticksSinceAttack < 10) {
            ++this.ticksSinceAttack;
        }
        if (this.ticksSinceAttack < 10) {
            this.breaking = true;
            return;
        }
        this.breaking = false;
    };
    @EventLink
    public final Listener<Render2DEvent> onRender2D = render2DEvent -> {
        if (this.breaking) {
            ScaledResolution scaledResolution = render2DEvent.getScaledResolution();
            double d2 = (double)scaledResolution.getScaledHeight() * 0.8;
            RenderUtil.a(((float)scaledResolution.getScaledWidth() - LegacyBreaker.aEg.playerController.curBlockDamageMP * 100.0f) / 2.0f, d2, LegacyBreaker.aEg.playerController.curBlockDamageMP * 100.0f, 10.0, 4.0, this.rz().rA(), this.rz().rB(), true);
            RenderUtil.color(Color.WHITE);
        }
    };
    @EventLink
    public final Listener<Render3DEvent> onRender3D = render3DEvent -> {
        if (!this.breaking) return;
        try {
            double d2 = this.targetBlock.getX();
            aEg.getRenderManager();
            double d3 = d2 - RenderManager.bUO;
            double d4 = this.targetBlock.getY();
            aEg.getRenderManager();
            double d5 = d4 - RenderManager.bUP;
            double d6 = this.targetBlock.getZ();
            aEg.getRenderManager();
            double d7 = d6 - RenderManager.bUQ;
            double d8 = d3 + 1.0;
            double d9 = d5 + 1.0;
            double d10 = d7 + 1.0;
            this.drawBox(d3, d5, d7, d8, d9, d10);
            return;
        }
        catch (NullPointerException nullPointerException) {
            return;
        }
    };

    @Override
    public void onDisable() {
        LegacyBreaker.aEg.playerController.curBlockDamageMP = 0.0f;
    }

    @Override
    public void onEnable() {
        this.damagePerTick = 0.0f;
        this.breakProgress = 0.0f;
    }

    private void drawBox(double d2, double d3, double d4, double d5, double d6, double d7) {
        GL11.glPushMatrix();
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glLineWidth(2.0f);
        GL11.glBegin(1);
        if (this.nearestBed == this.targetBlock) {
            GL11.glColor3f(255.0f, 0.0f, 0.0f);
        } else {
            GL11.glColor3f(255.0f, 255.0f, 255.0f);
        }
        GL11.glVertex3d(d2, d3, d4);
        GL11.glVertex3d(d5, d3, d4);
        GL11.glVertex3d(d5, d3, d4);
        GL11.glVertex3d(d5, d6, d4);
        GL11.glVertex3d(d5, d6, d4);
        GL11.glVertex3d(d2, d6, d4);
        GL11.glVertex3d(d2, d6, d4);
        GL11.glVertex3d(d2, d3, d4);
        GL11.glVertex3d(d2, d3, d4);
        GL11.glVertex3d(d2, d3, d7);
        GL11.glVertex3d(d5, d3, d4);
        GL11.glVertex3d(d5, d3, d7);
        GL11.glVertex3d(d5, d6, d4);
        GL11.glVertex3d(d5, d6, d7);
        GL11.glVertex3d(d2, d6, d4);
        GL11.glVertex3d(d2, d6, d7);
        GL11.glVertex3d(d2, d3, d7);
        GL11.glVertex3d(d5, d3, d7);
        GL11.glVertex3d(d5, d3, d7);
        GL11.glVertex3d(d5, d6, d7);
        GL11.glVertex3d(d5, d6, d7);
        GL11.glVertex3d(d2, d6, d7);
        GL11.glVertex3d(d2, d6, d7);
        GL11.glVertex3d(d2, d3, d7);
        GL11.glEnd();
        GL11.glEnable(3553);
        GL11.glDisable(2848);
        GL11.glPopMatrix();
    }

    public void rotateToBlock(BlockPos blockPos) {
        if (!((Boolean)this.rotations.wo()).booleanValue()) {
            return;
        }
        RotationComponent.setRotations(RotationUtil.s(blockPos), 10.0, (Boolean)this.movementCorrection.wo() != false ? MovementFix.NORMAL : MovementFix.OFF);
        LegacyBreaker.aEg.objectMouseOver.a(blockPos);
        LegacyBreaker.aEg.objectMouseOver.sideHit = EnumFacing.UP;
        LegacyBreaker.aEg.objectMouseOver.hitVec = new Vec3(Math.random(), 1.0, Math.random());
        LegacyBreaker.aEg.objectMouseOver.typeOfHit = MovingObjectPosition.MovingObjectType.BLOCK;
    }

    @Generated
    public boolean isBreaking() {
        return this.breaking;
    }
}
