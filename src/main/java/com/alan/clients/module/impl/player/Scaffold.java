package com.alan.clients.module.impl.player;

import com.alan.clients.component.impl.player.RotationComponent;
import com.alan.clients.component.impl.player.SlotComponent;
import com.alan.clients.component.impl.player.rotationcomponent.MovementFix;
import com.alan.clients.module.Module;
import com.alan.clients.module.api.Category;
import com.alan.clients.module.api.ModuleInfo;
import com.alan.clients.module.impl.movement.Flight;
import com.alan.clients.module.impl.movement.Speed;
import com.alan.clients.module.impl.movement.Sprint;
import com.alan.clients.module.impl.player.scaffold.downwards.NormalDownwards;
import com.alan.clients.module.impl.player.scaffold.downwards.VerusDownwards;
import com.alan.clients.module.impl.player.scaffold.downwards.WatchdogDownwards;
import com.alan.clients.module.impl.player.scaffold.sprint.BypassSprint;
import com.alan.clients.module.impl.player.scaffold.sprint.DisabledSprint;
import com.alan.clients.module.impl.player.scaffold.sprint.LegitSprint;
import com.alan.clients.module.impl.player.scaffold.sprint.MatrixSprint;
import com.alan.clients.module.impl.player.scaffold.sprint.VerusSprint;
import com.alan.clients.module.impl.player.scaffold.sprint.VulcanSprint;
import com.alan.clients.module.impl.player.scaffold.sprint.WatchdogJumpSprint;
import com.alan.clients.module.impl.player.scaffold.sprint.WatchdogPredictiSprint;
import com.alan.clients.module.impl.player.scaffold.sprint.WatchdogSprint;
import com.alan.clients.module.impl.player.scaffold.tower.AirJumpTower;
import com.alan.clients.module.impl.player.scaffold.tower.LegitTower;
import com.alan.clients.module.impl.player.scaffold.tower.MMCTower;
import com.alan.clients.module.impl.player.scaffold.tower.MatrixTower;
import com.alan.clients.module.impl.player.scaffold.tower.NCPTower;
import com.alan.clients.module.impl.player.scaffold.tower.NormalTower;
import com.alan.clients.module.impl.player.scaffold.tower.VanillaTower;
import com.alan.clients.module.impl.player.scaffold.tower.VerusTower;
import com.alan.clients.module.impl.player.scaffold.tower.VulcanTower;
import com.alan.clients.module.impl.player.scaffold.tower.WatchdogTower;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.input.MoveInputEvent;
import com.alan.clients.newevent.impl.motion.PostMotionEvent;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.newevent.impl.motion.PreUpdateEvent;
import com.alan.clients.newevent.impl.motion.StrafeEvent;
import com.alan.clients.newevent.impl.other.TeleportEvent;
import com.alan.clients.newevent.impl.packet.PacketReceiveEvent;
import com.alan.clients.newevent.impl.packet.PacketSendEvent;
import com.alan.clients.util.player.MoveUtil;
import com.alan.clients.util.vector.Vector2f;
import com.alan.clients.value.impl.BooleanValue;
import com.alan.clients.value.impl.BoundsNumberValue;
import com.alan.clients.value.impl.ModeValue;
import com.alan.clients.value.impl.NumberValue;
import com.alan.clients.value.impl.SubMode;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import de.florianmichael.vialoadingbase.ViaLoadingBase;
import com.alan.clients.util.RayCastUtil;
import com.alan.clients.util.chat.ChatUtil;
import com.alan.clients.util.math.MathUtil;
import com.alan.clients.util.packet.PacketUtil;
import com.alan.clients.util.player.EnumFacingOffset;
import com.alan.clients.util.player.PlayerUtil;
import com.alan.clients.util.player.SlotUtil;
import com.alan.clients.util.rotation.RotationUtil;
import com.alan.clients.util.vector.Vector3d;
import com.alan.clients.component.impl.player.BadPacketsComponent;
import com.alan.clients.newevent.impl.input.RightClickEvent;
import com.alan.clients.newevent.impl.motion.SprintEvent;
import com.alan.clients.module.impl.player.ScaffoldSwitchMap;
import com.alan.clients.module.impl.player.scaffold.tower.WatchdogPrediction18Tower;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
import java.util.Optional;
import lombok.Generated;
import net.minecraft.block.BlockAir;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer.C06PacketPlayerPosLook;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.m;
import net.minecraft.potion.Potion;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.util.Vec3i;
import org.lwjgl.input.Keyboard;
import rip.vantage.network.core.VantageNetwork;

@ModuleInfo(aliases = "module.player.scaffold.name", description = "module.player.scaffold.description", category = Category.PLAYER)
public class Scaffold extends Module {
    public BooleanValue sneak;
    @EventLink
    public Listener<TeleportEvent> onTeleport;
    public static boolean ahf;
    public ModeValue mode = new ModeValue("Mode", this)
        .add(new SubMode("Normal"))
        .add(new SubMode("Godbridge"))
        .add(new SubMode("Breesily"))
        .add(new SubMode("Snap"))
        .add(new SubMode("Telly"))
        .add(new SubMode("Eagle"))
        .setDefault("Normal");
    public boolean rotationBoostPending;
    public Vector3d placeOffset;
    public int extraPlacements;
    @EventLink(value = 4)
    public Listener<PreUpdateEvent> onPreUpdate;
    public float prevVisualPitch;
    public double startY;
    public BooleanValue agt;
    public BooleanValue visualBackRotations;
    public BoundsNumberValue age;
    public float prevVisualYaw;
    public ModeValue rotationMode = new ModeValue("Rotation Mode", this).add(new SubMode("Normal")).add(new SubMode("Grim")).setDefault("Normal");
    @EventLink
    public Listener<PacketReceiveEvent> onPacketReceiveEvent;
    public NumberValue timer;
    public Vec3 targetBlock;
    public int pauseTicks;
    public BooleanValue blockDiagonalAscend;
    @EventLink
    public Listener<StrafeEvent> onStrafe;
    public float forward;
    public BooleanValue boostOnlyWhileHoldingJump;
    @EventLink
    public Listener<RightClickEvent> onRightClick;
    public BoundsNumberValue startSneaking;
    public BlockPos blockFace;
    @EventLink
    public Listener<SprintEvent> onSprint;
    public BooleanValue safeWalk;
    public float visualYaw;
    public float targetPitch;
    public BooleanValue disableOnFlag;
    public ModeValue downwardsPressS;
    public EnumFacingOffset enumFacing;
    public ModeValue sprint;
    public ModeValue rayCast = new ModeValue("Ray Cast", this).add(new SubMode("Off")).add(new SubMode("Normal")).add(new SubMode("Strict")).setDefault("Strict");
    public BooleanValue watchdogPrediction;
    public BooleanValue rotationBlockBoost;
    public ModeValue yawOffset;
    public EnumFacingOffset lastEnumFacing;
    public NumberValue sneakingSpeed;
    public int placementIndex;
    public BooleanValue keepYBypass;
    public BoundsNumberValue rotationSpeed;
    public BoundsNumberValue stopSneaking;
    public BooleanValue movementCorrection;
    public BooleanValue advanced;
    public boolean agx;
    public int blocksUntilSneak;
    @EventLink
    public Listener<PostMotionEvent> onPostMotion;
    public int ticksOnAir;
    public int godBridgeSneakTicks;
    public boolean readyToPlace;
    @EventLink
    public Listener<MoveInputEvent> onMoveInput;
    public BooleanValue tellyOnlyOnRightClick;
    @EventLink
    public Listener<PacketSendEvent> onPacketSend;
    public float strafe;
    public BooleanValue extendBlockReachOnWatchdogTelly;
    public int sneakingTicks;
    public BoundsNumberValue placeDelay;
    public ModeValue tower;
    public float randomPitchOffset;
    public BooleanValue watchdogTelly;
    public int lastFlagTick;
    public int predictionPlaceDelay;
    public BooleanValue bypassRaycastWhenFalling;
    public int blockInputTicks;
    public BooleanValue render;
    public NumberValue expand;
    public BoundsNumberValue sneakEvery;
    public int tellyTicks;
    public BooleanValue upSideDown;
    public float randomYawOffset;
    public int tellyJumpDelay;
    @EventLink
    public Listener<MoveInputEvent> onMoveInputMedium;
    public int flagDisableTicks;
    public BooleanValue newWatchdogRots;
    @EventLink
    public Listener<PreMotionEvent> onPreMotionEvent;
    public BoundsNumberValue watchdogTellyRotationSpeed;
    public BoundsNumberValue rotationBlockRotationSpeed;
    public ModeValue sameY;
    public float visualPitch;
    public BooleanValue dontForceRaycastOnWatchdogTelly;
    public float targetYaw;
    public boolean visualRotationsActive;

    public void D(int var1) {
        EnumFacingOffset aib = this.enumFacing != null ? this.enumFacing : this.lastEnumFacing;
        if (aib != null) {
            if (this.watchdogPrediction.wo()) {
                ArrayList arraylist = new ArrayList();
                EntityPlayerSP entityplayersp1 = aEg.thePlayer;
                double d3 = entityplayersp1.posY + entityplayersp1.getEyeHeight() - this.targetBlock.yCoord - 0.5 - (Math.random() - 0.5) * 0.1;

                for (int i = -180 + var1; i <= 180; i += 45) {
                    entityplayersp1.setPosition(entityplayersp1.posX, entityplayersp1.posY - d3, entityplayersp1.posZ);
                    MovingObjectPosition movingobjectposition1 = RayCastUtil.c(new Vector2f(entityplayersp1.pl + i * 3, 0.0F), 6.0);
                    entityplayersp1.setPosition(entityplayersp1.posX, entityplayersp1.posY + d3, entityplayersp1.posZ);
                    if (movingobjectposition1 != null && movingobjectposition1.hitVec != null) {
                        Vector2f vector2f7 = RotationUtil.h(movingobjectposition1.hitVec);
                        if (RayCastUtil.a(vector2f7, this.blockFace, aib.getEnumFacing())) {
                            arraylist.add(vector2f7);
                        }
                    }
                }

                if (!arraylist.isEmpty()) {
                    Float f7 = RotationComponent.fk != null ? RotationComponent.fk.x : aEg.thePlayer.pl;
                    Float f6 = RotationComponent.fk != null ? RotationComponent.fk.y : aEg.thePlayer.rotationPitch;
                    Vector2f vector2f = (Vector2f)arraylist.get(0);
                    Float f8 = Math.abs(MathHelper.wrapAngleTo180_float(vector2f.x - f7));
                    Float f9 = Math.abs(vector2f.y - f6);
                    float f10 = f8 * f8 + f9 * f9;
                    Iterator iterator = arraylist.iterator();

                    while (iterator.hasNext()) {
                        Vector2f vector2f1 = (Vector2f)iterator.next();
                        float f11 = Math.abs(MathHelper.wrapAngleTo180_float(vector2f1.x - f7));
                        Float f12 = Math.abs(vector2f1.y - f6);
                        Float f13 = f11 * f11 + f12 * f12;
                        if (f13 < f10) {
                            f10 = f13;
                            vector2f = vector2f1;
                        }
                    }

                    this.targetYaw = vector2f.x;
                    this.targetPitch = vector2f.y;
                } else {
                    Vector2f vector2f2 = RotationUtil.a(new Vector3d(this.blockFace.getX(), this.blockFace.getY(), this.blockFace.getZ()), aib.getEnumFacing());
                    this.targetYaw = vector2f2.x;
                    this.targetPitch = vector2f2.y;
                }
            } else {
                EntityPlayerSP entityplayersp = aEg.thePlayer;
                Double d1 = entityplayersp.posY + entityplayersp.getEyeHeight() - this.targetBlock.yCoord - 0.5 - (Math.random() - 0.5) * 0.1;
                Float f14 = aEg.thePlayer.pl;
                Float wrapped = MathHelper.wrapAngleTo180_float(f14);
                Double d2 = MathHelper.wrapAngleTo180_double(Math.toDegrees(Math.atan2(aEg.thePlayer.motionZ, aEg.thePlayer.motionX)) - 90.0);
                float f24;
                int j3 = (f24 = Math.abs(wrapped - this.targetYaw) - 100.0F) == 0.0F ? 0 : (f24 < 0.0F ? -1 : 1);
                int notAbs = !(Math.abs(wrapped % 90.0F) <= 10.0F) && !(Math.abs(wrapped % 90.0F) >= 80.0F) ? 0 : 1;
                int j2_hi = notAbs != 0 ? -135 + var1 : -180 + var1;
                int limit = notAbs != 0 ? 135 : 180;
                ArrayList arraylist1 = new ArrayList();

                for (int i2 = j2_hi; i2 <= limit; i2 += 45) {
                    entityplayersp.setPosition(entityplayersp.posX, entityplayersp.posY - d1, entityplayersp.posZ);
                    MovingObjectPosition movingobjectposition = RayCastUtil.c(new Vector2f(entityplayersp.pl + i2 * 3, 0.0F), this.extendBlockReachOnWatchdogTelly.wo() ? 4.5 : 5.5);
                    entityplayersp.setPosition(entityplayersp.posX, entityplayersp.posY + d1, entityplayersp.posZ);
                    if (movingobjectposition != null && movingobjectposition.hitVec != null) {
                        Vector2f vector2f3 = RotationUtil.h(movingobjectposition.hitVec);
                        if (RayCastUtil.a(vector2f3, this.blockFace, aib.getEnumFacing())) {
                            arraylist1.add(vector2f3);
                        }
                    }
                }

                if (!arraylist1.isEmpty()) {
                    Float f16 = RotationComponent.fk != null ? RotationComponent.fk.x : aEg.thePlayer.pl;
                    Float f17 = RotationComponent.fk != null ? RotationComponent.fk.y : aEg.thePlayer.rotationPitch;
                    Vector2f vector2f4 = (Vector2f)arraylist1.get(0);
                    Float f18 = Math.abs(MathHelper.wrapAngleTo180_float(vector2f4.x - f16));
                    Float f19 = Math.abs(vector2f4.y - f17);
                    float f20 = f18 * f18 + f19 * f19;
                    Iterator iterator1 = arraylist1.iterator();

                    while (iterator1.hasNext()) {
                        Vector2f vector2f5 = (Vector2f)iterator1.next();
                        float f21 = Math.abs(MathHelper.wrapAngleTo180_float(vector2f5.x - f16));
                        float f22 = Math.abs(vector2f5.y - f17);
                        float f23 = f21 * f21 + f22 * f22;
                        if (f23 < f20) {
                            f20 = f23;
                            vector2f4 = vector2f5;
                        }
                    }

                    this.targetYaw = vector2f4.x;
                    this.targetPitch = vector2f4.y;
                } else {
                    Vector2f vector2f6 = RotationUtil.a(new Vector3d(this.blockFace.getX(), this.blockFace.getY(), this.blockFace.getZ()), aib.getEnumFacing());
                    if (!RayCastUtil.a(new Vector2f(this.targetYaw, this.targetPitch), this.blockFace, aib.getEnumFacing())) {
                        this.targetYaw = vector2f6.x;
                        this.targetPitch = vector2f6.y;
                    }
                }
            }
        }
    }

    @Override
    public void onEnable() {
        this.targetYaw = aEg.thePlayer.pl - 180.0F + Integer.parseInt(this.yawOffset.wo().getName());
        this.targetPitch = 90.0F;
        this.randomPitchOffset = (float)((Math.random() - 0.5) * (Math.random() - 0.5) * 10.0);
        this.randomYawOffset = (float)((Math.random() - 0.5) * (Math.random() - 0.5) * 10.0);
        this.tellyTicks = 0;
        this.startY = Math.floor(aEg.thePlayer.posY);
        this.targetBlock = null;
        this.lastEnumFacing = null;
        if (this.rotationBlockBoost.wo()) {
            this.rotationBoostPending = true;
        } else {
            this.rotationBoostPending = false;
        }

        this.tellyJumpDelay = this.age.wv().intValue();
        this.h(new Vector2f(this.targetYaw, this.targetPitch));
        this.sneakingTicks = -1;
        this.extraPlacements = 0;
        this.blocksUntilSneak = 0;
        aEg.thePlayer.crd = false;
        Object object = this.sprint.wo();
        if (object instanceof WatchdogJumpSprint) {
            ((WatchdogJumpSprint)object).getHelper().onEnable();
        }
    }

    public boolean isStrictRayCast() {
        return this.rayCast.wo().getName().equals("Strict");
    }

    public Vector3d getInterpolatedEyePosition(EntityPlayerSP player, float var2) {
        double d3 = player.prevPosX + (player.posX - player.prevPosX) * var2;
        double d4 = player.prevPosY + (player.posY - player.prevPosY) * var2 + player.getEyeHeight();
        double d5 = player.prevPosZ + (player.posZ - player.prevPosZ) * var2;
        return new Vector3d(d3, d4, d5);
    }

    public boolean isDisabledByFlag() {
        return this.watchdogTelly.wo() && this.disableOnFlag.wo() && this.flagDisableTicks > 0;
    }

    public boolean isBlockPlacement(C08PacketPlayerBlockPlacement packet) {
        return !packet.getPosition().h(new Vector3d(-1.0, -1.0, -1.0)) && packet.getStack() != null && packet.getStack().getItem() instanceof ItemBlock;
    }

    public void placeBlock() {
        if (this.pauseTicks > 3) {
            ChatUtil.c("Pause is greater than 3");
        } else if (this.enumFacing != null) {
            int equals2 = (int)(this.rotationMode.wo().getName().equals("Grim") ? 1L : 0L);
            Vec3 hitVec = this.getHitVec();
            if (equals2 != 0) {
                float f1 = aEg.thePlayer.pl + MathHelper.wrapAngleTo180_float(this.targetYaw - aEg.thePlayer.pl);
                PacketUtil.send(new C06PacketPlayerPosLook(aEg.thePlayer.posX, aEg.thePlayer.posY, aEg.thePlayer.posZ, f1, this.targetPitch, aEg.thePlayer.onGround));
                PlayerControllerMP playercontrollermp1 = aEg.playerController;
                EntityPlayerSP entityplayersp1 = aEg.thePlayer;
                WorldClient worldclient1 = aEg.theWorld;
                SlotComponent slotcomponent1 = this.d(SlotComponent.class);
                if (playercontrollermp1.onPlayerRightClick(entityplayersp1, worldclient1, SlotComponent.getItemStack(), this.blockFace, this.enumFacing.getEnumFacing(), hitVec)) {
                    PacketUtil.send(new m());
                }

                PacketUtil.send(
                    new C06PacketPlayerPosLook(
                        aEg.thePlayer.posX,
                        aEg.thePlayer.posY,
                        aEg.thePlayer.posZ,
                        (float)(aEg.thePlayer.pl + Math.random() * 0.03),
                        (float)Math.clamp(aEg.thePlayer.rotationPitch - Math.random(), -90.0, 90.0),
                        aEg.thePlayer.onGround
                    )
                );
            } else if (this.rayCast.wo().getName().equals("Strict")) {
                aEg.Az();
            } else {
                PlayerControllerMP playercontrollermp = aEg.playerController;
                EntityPlayerSP entityplayersp = aEg.thePlayer;
                WorldClient worldclient = aEg.theWorld;
                SlotComponent slotcomponent = this.d(SlotComponent.class);
                if (playercontrollermp.onPlayerRightClick(entityplayersp, worldclient, SlotComponent.getItemStack(), this.blockFace, this.enumFacing.getEnumFacing(), hitVec)) {
                    PacketUtil.send(new m());
                }
            }
        }
    }

    public boolean ki() {
        return aEg.thePlayer != null && this.isEnabled() && this.watchdogTelly.wo() && "Telly".equals(this.getDisplayName()) && MoveUtil.enoughMovementForSprinting();
    }

    public Vector3d getEyePosition(EntityPlayerSP player) {
        return new Vector3d(player.posX, player.posY + player.getEyeHeight(), player.posZ);
    }

    public boolean isOffsetReplaceableBelow(int var1) {
        return PlayerUtil.p(this.placeOffset.getX(), -var1 + this.placeOffset.getY(), this.placeOffset.getZ()).isReplaceable(aEg.theWorld, new BlockPos(aEg.thePlayer).down(var1));
    }

    public boolean isMovingAwayFrom(EnumFacing facing) {
        Vec3 vec3 = new Vec3(aEg.thePlayer.motionX, 0.0, aEg.thePlayer.motionZ);
        if (vec3.lengthVector() < 1.0E-4) {
            return false;
        }

        Vec3 vec = vec3.normalize();
        Vec3i vec3i = facing.getDirectionVec();
        Vec3 vec32 = new Vec3(vec3i.getX(), 0.0, vec3i.getZ()).normalize();
        return vec.dotProduct(vec32) < 0.0;
    }

    public void runMode() {
        if (this.getDisplayName().equals("Telly")) {
            float wrapped = MathHelper.wrapAngleTo180_float(aEg.thePlayer.pl);
            int notAbs = !(Math.abs(wrapped % 90.0F) <= 10.0F) && !(Math.abs(wrapped % 90.0F) >= 80.0F) ? 0 : 1;
            if (aEg.thePlayer.onGround) {
                MoveUtil.enoughMovementForSprinting();
            }
        }
    }

    public boolean isReplaceableAtStartY() {
        return PlayerUtil.o(aEg.thePlayer.posX, this.startY - 1.0, aEg.thePlayer.posZ)
            .isReplaceable(aEg.thePlayer.worldObj, new BlockPos(aEg.thePlayer.posX, this.startY - 1.0, aEg.thePlayer.posZ));
    }

    public String getDisplayName() {
        this.updateFlagState();
        String s = this.mode.wo().getName();
        if (s.equals("Telly")) {
            if (this.isDisabledByFlag()) {
                return "Normal";
            }

            if (this.tellyOnlyOnRightClick.wo()) {
                return aEg.gameSettings.cgI.isKeyDown() ? "Telly" : "Normal";
            }
        }

        return s;
    }

    public float c(float var1, float var2, float var3) {
        float wrapped = MathHelper.wrapAngleTo180_float(var2 - var1);
        MathHelper.wrapAngleTo180_double(var2);
        MathHelper.wrapAngleTo180_double(Math.toDegrees(Math.atan2(aEg.thePlayer.motionZ, aEg.thePlayer.motionX)) - 90.0);
        float f3 = MathHelper.wrapAngleTo180_float(aEg.thePlayer.pl);
        int notAbs = !(Math.abs(f3 % 90.0F) <= 15.0F) && !(Math.abs(f3 % 90.0F) >= 75.0F) ? 0 : 1;
        float f4 = MathHelper.clamp_float(wrapped, -var3, var3);
        return var1 + f4;
    }

    public void applyNewWatchdogRotations(BlockPos pos, float var2) {
        this.getInterpolatedEyePosition(aEg.thePlayer, aEg.timer.bWm);
        Vector3d akax2 = new Vector3d(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
        float f6 = aEg.thePlayer.pl;
        double radians = Math.toRadians(f6);
        Vector3d akaxx = new Vector3d(-Math.sin(radians), 0.0, Math.cos(radians));
        double d5 = akaxx.wg();
        if (d5 != 0.0) {
            akaxx = new Vector3d(akaxx.x / d5, 0.0, akaxx.z / d5);
        }

        int toRadians2 = Math.cos(Math.toRadians(f6)) * 1.0 < 0.0 ? 1 : 0;
        Vector3d akaxxx = new Vector3d(akaxx.z, 0.0, -akaxx.x);
        double d6 = akaxxx.wg();
        if (d6 != 0.0) {
            akaxxx = new Vector3d(akaxxx.x / d6, 0.0, akaxxx.z / d6);
        }

        double d7 = -0.49999999;
        Vector3d akaxxxx = akax2.e(akaxxx.ag(-d7));
        Vector3d akaxxxxx = akaxxxx.v(0.0, (Math.random() - 0.5) * 0.05, 0.0);
        float[] afloat = this.rotationsBetween(akax2, akaxxxxx);
        float f7 = this.targetYaw;
        float f8 = this.targetPitch;
        float f9 = this.c(f7, afloat[0], var2);
        float f10 = this.c(f8, afloat[1], var2);
        this.targetYaw = f9;
        this.targetPitch = f10;
        RotationComponent.d(false);
        float wrapped = MathHelper.wrapAngleTo180_float(aEg.thePlayer.pl);
        int notAbs = !(Math.abs(wrapped % 90.0F) <= 10.0F) && !(Math.abs(wrapped % 90.0F) >= 80.0F) ? 0 : 1;
        if (!aEg.gameSettings.keyBindLeft.isKeyDown()
            && !aEg.gameSettings.keyBindRight.isKeyDown()
            && (!MoveUtil.enoughMovementForSprinting() || MoveUtil.speed() < 0.09 || notAbs != 0)) {
            if ((MoveUtil.speed() < 0.21 || !aEg.thePlayer.onGround) && !aEg.gameSettings.keyBindJump.isKeyDown()) {
                RotationComponent.setRotations(new Vector2f(this.targetYaw - 30.0F, this.targetPitch), 10.0, this.movementCorrection.wo() ? MovementFix.NORMAL : MovementFix.OFF);
            } else if (MoveUtil.speed() < 0.3 && !aEg.gameSettings.keyBindJump.isKeyDown()) {
                RotationComponent.setRotations(new Vector2f(this.targetYaw - 45.0F, this.targetPitch), 10.0, this.movementCorrection.wo() ? MovementFix.NORMAL : MovementFix.OFF);
            }

            if (aEg.gameSettings.keyBindJump.isKeyDown() && this.e(Speed.class).isEnabled()) {
                RotationComponent.setRotations(new Vector2f(this.targetYaw - 10.0F, this.targetPitch), 10.0, this.movementCorrection.wo() ? MovementFix.NORMAL : MovementFix.OFF);
            } else if (aEg.gameSettings.keyBindJump.isKeyDown()) {
                RotationComponent.setRotations(new Vector2f(this.targetYaw - 10.0F, this.targetPitch), 10.0, this.movementCorrection.wo() ? MovementFix.NORMAL : MovementFix.OFF);
            }
        }
    }

    public float b(float var1, float var2, float var3) {
        float f1 = MathHelper.wrapAngleTo180_float(var2 - var1);
        if (f1 > var3) {
            f1 = var3;
        }

        if (f1 < -var3) {
            f1 = -var3;
        }

        return var1 + f1;
    }

    public static float clampPitch(float var0) {
        if (var0 > 89.9F) {
            return 89.9F;
        }
        return var0 < -89.9F ? -89.9F : var0;
    }

    public boolean isReplaceableBelow(int var1) {
        return aEg.thePlayer
            .worldObj
            .getBlockState(
                new BlockPos(
                    MathHelper.floor_double(aEg.thePlayer.posX),
                    MathHelper.floor_double(aEg.thePlayer.posY) - var1,
                    MathHelper.floor_double(aEg.thePlayer.posZ)
                )
            )
            .getBlock()
            .isReplaceable(
                aEg.thePlayer.worldObj,
                new BlockPos(
                    MathHelper.floor_double(aEg.thePlayer.posX),
                    MathHelper.floor_double(aEg.thePlayer.posY) - var1,
                    MathHelper.floor_double(aEg.thePlayer.posZ)
                )
            );
    }

    public Vector2f findPitch(BlockPos pos, EnumFacing facing, float var3, boolean var4) {
        return this.findPitchNear(pos, facing, var3, var4, 82.0F);
    }

    public float[] rotationsBetween(Vector3d var1, Vector3d var2) {
        double d4 = var2.x - var1.x;
        double d5 = var2.y - var1.y;
        double d6 = var2.z - var1.z;
        double d7 = Math.sqrt(d4 * d4 + d6 * d6);
        float degrees = (float)Math.toDegrees(Math.atan2(d6, d4)) - 90.0F;
        float f3 = (float)(-Math.toDegrees(Math.atan2(d5, d7)));
        float wrapped = MathHelper.wrapAngleTo180_float(degrees);
        float f5 = MathHelper.clamp_float(f3, -90.0F, 90.0F);
        return new float[]{wrapped, f5};
    }

    @Generated
    public boolean isReadyToPlace() {
        return this.readyToPlace;
    }

    @Override
    public void onDisable() {
        if (aEg.thePlayer.ticksExisted % 2 == 0) {
            this.agx = false;
        }

        this.h(new Vector2f(aEg.thePlayer.pl, aEg.thePlayer.rotationPitch));
        this.resetAllKeyBinds();
        aEg.thePlayer.crd = false;
    }

    public Vector2f getTellyRotation(BlockPos pos, EnumFacingOffset var2, float var3, float var4, float var5, int var6) {
        float wrapped = MathHelper.wrapAngleTo180_float(aEg.thePlayer.pl);
        float f3 = MathHelper.clamp_float((float)(85.0 + Math.random() * 0.1), var4, var5);
        return RotationUtil.m(new Vector2f(wrapped, f3));
    }

    public Vector2f findPitchNear(BlockPos pos, EnumFacing facing, float var3, boolean var4, float var5) {
        float[] afloat = new float[]{0.0F, 1.5F, -1.5F, 3.0F, -3.0F, 4.5F, -4.5F};
        int count = afloat.length;

        for (int limit = 0; limit < count; limit = limit + 1) {
            float f2 = afloat[limit];
            float f3 = clampPitch(var5 + f2);
            Vector2f vector2f = new Vector2f(var3, f3);
            int k_hi = (int)(RayCastUtil.a(vector2f, facing, pos, var4) ? 1L : 0L);
            if (k_hi == 0) {
                MovingObjectPosition movingobjectposition = RayCastUtil.c(vector2f, this.extendBlockReachOnWatchdogTelly.wo() ? 4.5 : 5.5);
                k_hi = movingobjectposition != null && pos.equals(movingobjectposition.getBlockPos()) && movingobjectposition.sideHit == facing ? 1 : 0;
            }

            if (k_hi != 0) {
                return vector2f;
            }
        }

        return null;
    }


    public void updateFlagState() {
        if (aEg.thePlayer != null && this.watchdogTelly.wo() && this.disableOnFlag.wo()) {
            if (aEg.thePlayer.Zl == 1 && this.lastFlagTick != aEg.thePlayer.ticksExisted) {
                this.flagDisableTicks = 10;
                this.lastFlagTick = aEg.thePlayer.ticksExisted;
            }
        } else {
            this.flagDisableTicks = 0;
            this.lastFlagTick = -1;
        }
    }

    static {
    }

    @Generated
    public int getTicksOnAir() {
        return this.ticksOnAir;
    }

    public void c(boolean var1, boolean var2, boolean var3, boolean var4, boolean var5, boolean var6) {
        if (var1) {
            aEg.gameSettings.keyBindSneak.setPressed(Keyboard.isKeyDown(aEg.gameSettings.keyBindSneak.getKeyCode()));
        }

        if (var2) {
            aEg.gameSettings.keyBindJump.setPressed(Keyboard.isKeyDown(aEg.gameSettings.keyBindJump.getKeyCode()));
        }

        if (var3) {
            aEg.gameSettings.keyBindRight.setPressed(Keyboard.isKeyDown(aEg.gameSettings.keyBindRight.getKeyCode()));
        }

        if (var4) {
            aEg.gameSettings.keyBindLeft.setPressed(Keyboard.isKeyDown(aEg.gameSettings.keyBindLeft.getKeyCode()));
        }

        if (var5) {
            aEg.gameSettings.keyBindForward.setPressed(Keyboard.isKeyDown(aEg.gameSettings.keyBindForward.getKeyCode()));
        }

        if (var6) {
            aEg.gameSettings.keyBindBack.setPressed(Keyboard.isKeyDown(aEg.gameSettings.keyBindBack.getKeyCode()));
        }
    }

    public void h(Vector2f vec2) {
        this.visualRotationsActive = false;
        this.prevVisualYaw = vec2.x;
        this.prevVisualPitch = vec2.y;
        this.visualYaw = vec2.x;
        this.visualPitch = vec2.y;
    }

    public Scaffold() {
        this.sprint = new ModeValue("Sprint", this)
            .add(new SubMode("Normal"))
            .add(new DisabledSprint("Disabled", this))
            .add(new LegitSprint("Legit", this))
            .add(new BypassSprint("Bypass", this))
            .add(new VulcanSprint("Vulcan", this))
            .add(new VerusSprint("Verus", this))
            .add(new MatrixSprint("Matrix", this))
            .add(new WatchdogPredictiSprint("Watchdog Prediction", this))
            .add(new WatchdogJumpSprint("Watchdog Jump", this))
            .add(new WatchdogSprint("Watchdog", this))
            .setDefault("Normal");
        this.tower = new ModeValue("Tower", this)
            .add(new SubMode("Disabled"))
            .add(new VulcanTower("Vulcan", this))
            .add(new VanillaTower("Vanilla", this))
            .add(new NormalTower("Normal", this))
            .add(new AirJumpTower("Air Jump", this))
            .add(new WatchdogTower("Watchdog", this))
            .add(new MMCTower("MMC", this))
            .add(new NCPTower("NCP", this))
            .add(new MatrixTower("Matrix", this))
            .add(new LegitTower("Legit", this))
            .add(new VerusTower("Verus", this))
            .add(new WatchdogPrediction18Tower("Watchdog Prediction 1.8", this))
            .setDefault("Disabled");
        this.sameY = new ModeValue("Same Y", this).add(new SubMode("Off")).add(new SubMode("On")).add(new SubMode("Auto Jump")).setDefault("Off");
        this.downwardsPressS = new ModeValue("Downwards (Press Sneak)", this)
            .add(new SubMode("Off"))
            .add(new NormalDownwards("Normal", this))
            .add(new WatchdogDownwards("Watchdog", this))
            .add(new VerusDownwards("Verus", this))
            .setDefault("Off");
        this.rotationSpeed = new BoundsNumberValue("Rotation Speed", this, 5, 10, 0, 10, 1);
        this.placeDelay = new BoundsNumberValue("Place Delay", this, 0, 0, 0, 5, 1);
        this.timer = new NumberValue("Timer", this, 1, 0.1, 10, 0.1);
        this.expand = new NumberValue("Expand", this, 0, 0, 4, 1);
        this.movementCorrection = new BooleanValue("Movement Correction", this, false);
        this.safeWalk = new BooleanValue("Safe Walk", this, true);
        this.newWatchdogRots = new BooleanValue("New Watchdog Rots", this, false);
        this.keepYBypass = new BooleanValue("Keep-Y bypass", this, true);
        this.watchdogPrediction = new BooleanValue("Watchdog Prediction", this, false);
        this.watchdogTelly = new BooleanValue("Watchdog Telly", this, false);
        this.visualBackRotations = new BooleanValue("Visual Back Rotations", this, true, () -> {
            int k;
            if (!this.watchdogTelly.wo()) {
                k = 1;
            } else {
                byte b0 = -110;
                int i = b0 ^ 51;
                int j = i - -95;
                k = j;
            }

            return k != 0;
        });
        this.blockDiagonalAscend = new BooleanValue("Block Diagonal Ascend", this, true, () -> !this.watchdogTelly.wo());
        this.dontForceRaycastOnWatchdogTelly = new BooleanValue("Don't force raycast on Watchdog Telly", this, false, () -> !this.watchdogTelly.wo());
        this.extendBlockReachOnWatchdogTelly = new BooleanValue("Extend Block Reach on Watchdog Telly", this, true, () -> !this.watchdogTelly.wo());
        this.age = new BoundsNumberValue("Watchdog Telly Jump Delay", this, 0, 0, 0, 5, 1, () -> !this.watchdogTelly.wo());
        this.disableOnFlag = new BooleanValue("Disable On Flag", this, true, () -> !this.watchdogTelly.wo());
        this.watchdogTellyRotationSpeed = new BoundsNumberValue("Watchdog Telly Rotation Speed", this, 35, 38, 0, 180, 1, () -> !this.watchdogTelly.wo());
        this.rotationBlockBoost = new BooleanValue("Rotation Block Boost", this, false, () -> {
            int k;
            if (!this.watchdogTelly.wo()) {
                k = 1;
            } else {
                byte b0 = 38;
                int i = b0 + 48;
                int j = i - 86;
                k = j;
            }

            return k != 0;
        });
        this.rotationBlockRotationSpeed = new BoundsNumberValue("Rotation Block Rotation Speed", this, 122, 128, 0, 180, 1, () -> !this.watchdogTelly.wo() || !this.rotationBlockBoost.wo());
        this.boostOnlyWhileHoldingJump = new BooleanValue("Boost Only While Holding Jump", this, false, () -> !this.watchdogTelly.wo() || !this.rotationBlockBoost.wo());
        this.sneak = new BooleanValue("Sneak", this, false);
        this.startSneaking = new BoundsNumberValue("Start Sneaking", this, 0, 0, 0, 5, 1, () -> !this.sneak.wo());
        this.stopSneaking = new BoundsNumberValue("Stop Sneaking", this, 0, 0, 0, 5, 1, () -> !this.sneak.wo() ? true : true ^ true);
        this.sneakEvery = new BoundsNumberValue("Sneak every x blocks", this, 1, 1, 1, 10, 1, () -> !this.sneak.wo());
        this.sneakingSpeed = new NumberValue("Sneaking Speed", this, 0.2, 0.2, 1, 0.05, () -> !this.sneak.wo());
        this.tellyOnlyOnRightClick = new BooleanValue("Telly Only on Right Click", this, false, () -> !this.mode.wo().getName().equals("Telly"));
        this.render = new BooleanValue("Render", this, true);
        this.advanced = new BooleanValue("Advanced", this, false);
        this.yawOffset = new ModeValue("Yaw Offset", this, () -> !this.advanced.wo()).add(new SubMode("0")).add(new SubMode("45")).add(new SubMode("-45")).setDefault("0");
        this.agt = new BooleanValue("Ignore Speed Effect", this, false, () -> !this.advanced.wo());
        this.upSideDown = new BooleanValue("Up Side Down", this, false, () -> !this.advanced.wo());
        this.bypassRaycastWhenFalling = new BooleanValue("Bypass Raycast When Falling", this, false, () -> !this.advanced.wo());
        this.placeOffset = new Vector3d(0.0, 0.0, 0.0);
        this.onPacketReceiveEvent = PacketUtil::j;
        this.onPreMotionEvent = var1 -> {
            this.e(Flight.class).isEnabled();
            this.placeOffset = new Vector3d(0.0, 0.0, 0.0);
            if (this.targetBlock != null && this.enumFacing != null && this.blockFace != null) {
                aEg.thePlayer.cHT.aX();
                if (this.timer.wo().floatValue() != 1.0F) {
                    aEg.timer.dzD = this.timer.wo().floatValue();
                }
            }
        };
        this.onPostMotion = var1 -> {
            float f = 0.0F;
            Object object = null;
            Object object1 = null;
            EnumFacingOffset aib = this.enumFacing != null ? this.enumFacing : this.lastEnumFacing;
            Vector2f vector2f = RotationComponent.fn != null
                ? new Vector2f(RotationComponent.fn)
                : (RotationComponent.fk != null ? new Vector2f(RotationComponent.fk) : new Vector2f(aEg.thePlayer.pl, aEg.thePlayer.rotationPitch));
            if (this.watchdogTelly.wo() && this.visualBackRotations.wo() && this.getDisplayName().equals("Telly")) {
                if (this.blockFace != null && aib != null) {
                    int notKeyDown = !MoveUtil.enoughMovementForSprinting() || !aEg.thePlayer.onGround || !aEg.gameSettings.keyBindJump.isKeyDown() && aEg.thePlayer.cqL > 1 && this.tellyTicks > 3 && (this.tellyJumpDelay <= 0 || aEg.thePlayer.cqL != this.tellyJumpDelay) ? 0 : 1;
                    Vector2f vector2f1 = this.findPlacementRotation(
                        this.blockFace,
                        aib,
                        !RayCastUtil.a(vector2f, aib.getEnumFacing(), this.blockFace, this.isStrictRayCast()) && this.isStrictRayCast(),
                        this.visualRotationsActive ? this.visualYaw : MathHelper.wrapAngleTo180_float(vector2f.x - 135.0F),
                        this.visualRotationsActive ? this.visualPitch : 84.0F,
                        45.0F
                    );
                    if (vector2f1 == null) {
                        vector2f1 = RotationUtil.a(new Vector3d(this.blockFace.getX(), this.blockFace.getY(), this.blockFace.getZ()), aib.getEnumFacing());
                    }

                    Vector2f vector2f2 = new Vector2f(vector2f1.x, MathHelper.clamp_float(vector2f1.y, 80.0F, 89.9F));
                    int notWrapAngleTo180_float = !(Math.abs(MathHelper.wrapAngleTo180_float(vector2f.x - vector2f2.x)) > 22.5F) && notKeyDown == 0 ? 0 : 1;
                    Vector2f vector2f3 = notWrapAngleTo180_float != 0 ? vector2f2 : vector2f;
                    if (notWrapAngleTo180_float == 0 && !this.visualRotationsActive) {
                        this.h(vector2f);
                    } else {
                        if (!this.visualRotationsActive) {
                            this.h(vector2f);
                        }

                        int wo2 = this.rotationBoostPending && this.rotationBlockBoost.wo() && !aEg.thePlayer.onGround ? 1 : 0;
                        if (this.boostOnlyWhileHoldingJump.wo()) {
                            wo2 = wo2 != 0 && aEg.gameSettings.keyBindJump.isKeyDown() ? 1 : 0;
                        }

                        float f1 = (wo2 != 0 ? this.rotationBlockRotationSpeed.wv().floatValue() : this.watchdogTellyRotationSpeed.wv().floatValue()) * (wo2 != 0 ? 0.42F : 0.3F);
                        this.prevVisualYaw = this.visualYaw;
                        this.prevVisualPitch = this.visualPitch;
                        this.visualRotationsActive = true;
                        this.visualYaw = this.b(this.visualYaw, vector2f3.x, Math.max(1.5F, f1));
                        this.visualPitch = this.b(this.visualPitch, vector2f3.y, Math.max(1.0F, f1 * 0.65F));
                        if (notWrapAngleTo180_float == 0
                            && Math.abs(MathHelper.wrapAngleTo180_float(this.visualYaw - vector2f.x)) <= 2.0F
                            && Math.abs(this.visualPitch - vector2f.y) <= 2.0F) {
                            this.h(vector2f);
                        }
                    }
                } else {
                    this.h(vector2f);
                }

                if (this.visualRotationsActive && aEg.gameSettings.thirdPersonView != 0) {
                    aEg.thePlayer.prevRotationYawHead = this.prevVisualYaw;
                    aEg.thePlayer.rotationYawHead = this.visualYaw;
                    aEg.thePlayer.prevRenderYawOffset = this.prevVisualYaw;
                    aEg.thePlayer.renderYawOffset = this.visualYaw;
                    aEg.thePlayer.bjJ = this.prevVisualPitch;
                    aEg.thePlayer.po = this.visualPitch;
                }
            } else {
                this.h(vector2f);
            }
        };
        this.onTeleport = var0 -> {
            double d0;
            int i = (d0 = var0.getPosY() - (aEg.thePlayer.posY - 2.0)) == 0.0 ? 0 : (d0 < 0.0 ? -1 : 1);
        };
        this.onRightClick = var1 -> {
            if (this.tellyOnlyOnRightClick.wo() && !this.rayCast.wo().getName().equals("Strict")) {
                var1.setCancelled();
            }
        };
        this.onPreUpdate = var1 -> {
            int i = 1804046949;
            int j = 298231907;
            int k = 1561466657;
            int i1 = 1905985305;
            int j1 = 705811684;
            if (this.rotationMode.wo().getName().equals("Grim") && aEg.thePlayer.ticksExisted % 20 == 0) {
                if (ViaLoadingBase.getInstance().getTargetVersion().newerThanOrEqualTo(ProtocolVersion.v1_17)
                    && !ViaLoadingBase.getInstance().getTargetVersion().newerThan(ProtocolVersion.v1_20_5)) {
                } else {
                }

                ChatUtil.b("OnTick rotation only works correctly on versions 1.17-1.20.6. Please switch to a version in that range.");
            }

            this.placementIndex = 0;

            while (this.placementIndex <= this.extraPlacements) {
                boolean flag5 = aEg.thePlayer.motionY < -0.1 && aEg.thePlayer.tR > 3;
                if (this.isOffsetReplaceableBelow(2)
                    && this.isOffsetReplaceableBelow(3)
                    && !this.e(Speed.class).isEnabled()
                    && !aEg.gameSettings.keyBindSneak.isKeyDown()
                    && !Objects.equals(VantageNetwork.aKB().bX(), "")) {
                    aEg.thePlayer.crd = this.safeWalk.wo();
                }

                this.c(false, false, true, true, false, false);
                if (this.upSideDown.wo()) {
                    this.placeOffset.setY(3.0);
                }

                if (this.expand.wo().intValue() != 0 && MoveUtil.isMoving()) {
                    double d0 = MoveUtil.direction(
                        aEg.thePlayer.pl,
                        aEg.gameSettings.keyBindForward.isKeyDown() ? 1.0 : (aEg.gameSettings.keyBindBack.isKeyDown() ? -1.0 : 0.0),
                        aEg.gameSettings.keyBindRight.isKeyDown() ? -1.0 : (aEg.gameSettings.keyBindLeft.isKeyDown() ? 1.0 : 0.0)
                    );

                    for (double d1 = 0.0; d1 <= this.expand.wo().intValue(); d1 += 1.0) {
                        if (PlayerUtil.blockAheadOfPlayer(d1, this.placeOffset.getY() - 0.5) instanceof BlockAir) {
                            this.placeOffset = this.placeOffset.e(new Vector3d((int)(-Math.sin(d0) * (d1 + 1.0)), this.placeOffset.getY(), (int)(Math.cos(d0) * (d1 + 1.0))));
                            break;
                        }
                    }
                }

                float f = aEg.thePlayer.pl;
                float f1 = MathHelper.wrapAngleTo180_float(f);
                double d2 = MathHelper.wrapAngleTo180_double(Math.toDegrees(Math.atan2(aEg.thePlayer.motionZ, aEg.thePlayer.motionX)) - 90.0);
                float f11;
                int i2 = (f11 = Math.abs(f1 - this.targetYaw) - 100.0F) == 0.0F ? 0 : (f11 < 0.0F ? -1 : 1);
                if (!(Math.abs(f1 % 90.0F) <= 10.0F) && !(Math.abs(f1 % 90.0F) >= 80.0F)) {
                } else {
                }

                MathHelper.wrapAngleTo180_double(Math.toDegrees(MoveUtil.direction()));
                if (this.sprint.wo().getName().equals("Watchdog Jump") && (Math.abs(d2 - this.targetYaw) > 85.0 || aEg.thePlayer.cqL < 2)) {
                    float wrapped = MathHelper.wrapAngleTo180_float(aEg.thePlayer.pl);
                    if (!(Math.abs(wrapped % 90.0F) <= 10.0F) && !(Math.abs(wrapped % 90.0F) >= 80.0F)) {
                    } else {
                    }

                    if (this.e(Scaffold.class).sameY.wo().getName().equals("On") && aEg.thePlayer.isPotionActive(Potion.moveSpeed)) {
                        ;
                    }

                    this.e(Speed.class).isEnabled();
                    if (aEg.thePlayer.cqL > 1) {
                        if (aEg.thePlayer.Zl < 43 && !aEg.gameSettings.keyBindJump.isKeyDown() && !aEg.gameSettings.keyBindJump.isPressed()) {
                            MoveUtil.stop();
                        }

                        RotationComponent.d(false);
                        if (aEg.thePlayer.cqL <= 10) {
                            RotationComponent.setRotations(
                                new Vector2f((float)(aEg.thePlayer.pl - 99.99999999999999 + (Math.random() - 0.5) * 3.0), 85.0F + (float)(Math.random() * 1.0)),
                                10.0,
                                MovementFix.OFF
                            );
                        } else {
                            float f3 = aEg.thePlayer.pl;
                            float f4 = MathHelper.wrapAngleTo180_float(f3);
                            boolean flag6 = Math.abs(f4 % 90.0F) <= 10.0F || Math.abs(f4 % 90.0F) >= 80.0F;
                            aEg.thePlayer.motionX *= 0.9895;
                            aEg.thePlayer.motionZ *= 0.9895;
                            float f5 = 140.0F;
                            if (aEg.gameSettings.keyBindRight.isKeyDown()) {
                                aEg.gameSettings.keyBindForward.isKeyDown();
                            }

                            if (aEg.gameSettings.keyBindRight.isKeyDown() && aEg.gameSettings.keyBindForward.isKeyDown()) {
                                RotationComponent.d(false);
                                RotationComponent.setRotations(
                                    new Vector2f((float)(f3 - 69.99999999999999 + (Math.random() - 0.5) * 3.0), 85.0F + (float)(Math.random() * 1.0)),
                                    2.0,
                                    MovementFix.OFF
                                );
                            }

                            if (aEg.gameSettings.keyBindLeft.isKeyDown() && aEg.gameSettings.keyBindForward.isKeyDown() && !flag6) {
                                RotationComponent.d(false);
                                RotationComponent.setRotations(
                                    new Vector2f((float)(f3 - 150.0 + (Math.random() - 0.5) * 3.0), 85.0F + (float)(Math.random() * 1.0)), 2.0, MovementFix.OFF
                                );
                            } else if (aEg.gameSettings.keyBindLeft.isKeyDown() && aEg.gameSettings.keyBindForward.isKeyDown() && flag6) {
                                RotationComponent.d(false);
                                RotationComponent.setRotations(
                                    new Vector2f((float)(f3 - 160.0 + (Math.random() - 0.5) * 3.0), 85.0F + (float)(Math.random() * 1.0)), 2.0, MovementFix.OFF
                                );
                            }

                            if (!aEg.gameSettings.keyBindRight.isKeyDown() || !aEg.gameSettings.keyBindLeft.isKeyDown()) {
                                RotationComponent.setRotations(
                                    new Vector2f((float)(f3 - (f5 - 1.0E-14) + (Math.random() - 0.5) * 3.0), 85.0F + (float)(Math.random() * 1.0)),
                                    2.0,
                                    MovementFix.OFF
                                );
                            }

                            if (aEg.gameSettings.keyBindRight.isKeyDown() && !aEg.gameSettings.keyBindForward.isKeyDown()) {
                                RotationComponent.d(false);
                                RotationComponent.setRotations(
                                    new Vector2f((float)(f3 - 140.0 + (Math.random() - 0.5) * 3.0), 85.0F + (float)(Math.random() * 1.0)), 2.0, MovementFix.OFF
                                );
                            }

                            if (aEg.gameSettings.keyBindLeft.isKeyDown() && !aEg.gameSettings.keyBindForward.isKeyDown()) {
                                RotationComponent.d(false);
                                RotationComponent.setRotations(
                                    new Vector2f((float)(f3 + 9.99999999999999 + (Math.random() - 0.5) * 3.0), 85.0F + (float)(Math.random() * 1.0)),
                                    2.0,
                                    MovementFix.OFF
                                );
                            }

                            if (aEg.gameSettings.keyBindLeft.isKeyDown() && aEg.gameSettings.keyBindBack.isKeyDown()) {
                                if (this.e(Speed.class).isEnabled()) {
                                    aEg.thePlayer.motionX *= 0.9895;
                                    aEg.thePlayer.motionZ *= 0.9895;
                                }

                                RotationComponent.d(false);
                                RotationComponent.setRotations(
                                    new Vector2f((float)(f3 + 190.0 + (Math.random() - 0.5) * 3.0), 85.0F + (float)(Math.random() * 1.0)), 2.0, MovementFix.OFF
                                );
                            }

                            if (aEg.gameSettings.keyBindRight.isKeyDown() && aEg.gameSettings.keyBindForward.isKeyDown() && !flag6) {
                                RotationComponent.d(false);
                                RotationComponent.setRotations(
                                    new Vector2f((float)(f3 + 140.0 + (Math.random() - 0.5) * 3.0), 85.0F + (float)(Math.random() * 1.0)), 2.0, MovementFix.OFF
                                );
                            }
                        }
                    } else if (MoveUtil.isMoving() || MoveUtil.speed() > 0.0) {
                        float f7 = aEg.thePlayer.pl;
                        float f9 = MathHelper.wrapAngleTo180_float(f7);
                        RotationComponent.d(false);
                        boolean flag9 = Math.abs(f9 % 90.0F) <= 10.0F || Math.abs(f9 % 90.0F) >= 80.0F;
                        float f10;
                        if (aEg.thePlayer.ticksExisted % 2 == 0 && this.e(Speed.class).isEnabled() && this.keepYBypass.wo()) {
                            f10 = 147.0F;
                        } else if (this.e(Speed.class).isEnabled() && this.keepYBypass.wo()) {
                            f10 = 117.0F;
                        } else {
                            f10 = 140.0F;
                        }

                        if (aEg.gameSettings.keyBindRight.isKeyDown() && aEg.gameSettings.keyBindForward.isKeyDown() && flag9) {
                            RotationComponent.d(false);
                            RotationComponent.setRotations(
                                new Vector2f((float)(f7 - 99.99999999999999 + (Math.random() - 0.5) * 3.0), 85.0F + (float)(Math.random() * 1.0)),
                                2.0,
                                MovementFix.OFF
                            );
                        } else if (aEg.gameSettings.keyBindRight.isKeyDown() && aEg.gameSettings.keyBindForward.isKeyDown()) {
                            RotationComponent.d(false);
                            RotationComponent.setRotations(
                                new Vector2f((float)(f7 - 69.99999999999999 + (Math.random() - 0.5) * 3.0), 85.0F + (float)(Math.random() * 1.0)),
                                2.0,
                                MovementFix.OFF
                            );
                        }

                        if (aEg.gameSettings.keyBindLeft.isKeyDown() && aEg.gameSettings.keyBindForward.isKeyDown() && !flag9) {
                            RotationComponent.d(false);
                            RotationComponent.setRotations(
                                new Vector2f((float)(f7 - 150.0 + (Math.random() - 0.5) * 3.0), 85.0F + (float)(Math.random() * 1.0)), 2.0, MovementFix.OFF
                            );
                        } else if (aEg.gameSettings.keyBindLeft.isKeyDown() && aEg.gameSettings.keyBindForward.isKeyDown()) {
                            RotationComponent.d(false);
                            RotationComponent.setRotations(
                                new Vector2f((float)(f7 - 160.0 + (Math.random() - 0.5) * 3.0), 85.0F + (float)(Math.random() * 1.0)), 2.0, MovementFix.OFF
                            );
                        }

                        if (!aEg.gameSettings.keyBindRight.isKeyDown() || !aEg.gameSettings.keyBindLeft.isKeyDown()) {
                            RotationComponent.setRotations(
                                new Vector2f((float)(f7 - (f10 - 1.0E-14) + (Math.random() - 0.5) * 3.0), 85.0F + (float)(Math.random() * 1.0)),
                                2.0,
                                MovementFix.OFF
                            );
                        }

                        if (aEg.gameSettings.keyBindRight.isKeyDown() && !aEg.gameSettings.keyBindForward.isKeyDown()) {
                            RotationComponent.d(false);
                            RotationComponent.setRotations(
                                new Vector2f((float)(f7 - 140.0 + (Math.random() - 0.5) * 3.0), 85.0F + (float)(Math.random() * 1.0)), 2.0, MovementFix.OFF
                            );
                        }

                        if (aEg.gameSettings.keyBindLeft.isKeyDown() && !aEg.gameSettings.keyBindForward.isKeyDown()) {
                            RotationComponent.d(false);
                            RotationComponent.setRotations(
                                new Vector2f((float)(f7 + 9.99999999999999 + (Math.random() - 0.5) * 3.0), 85.0F + (float)(Math.random() * 1.0)),
                                2.0,
                                MovementFix.OFF
                            );
                        }

                        if (aEg.gameSettings.keyBindLeft.isKeyDown() && aEg.gameSettings.keyBindBack.isKeyDown()) {
                            RotationComponent.d(false);
                            RotationComponent.setRotations(
                                new Vector2f((float)(f7 + 190.0 + (Math.random() - 0.5) * 3.0), 85.0F + (float)(Math.random() * 1.0)), 2.0, MovementFix.OFF
                            );
                        }

                        if (aEg.gameSettings.keyBindRight.isKeyDown() && aEg.gameSettings.keyBindForward.isKeyDown() && !flag9) {
                            RotationComponent.d(false);
                            RotationComponent.setRotations(
                                new Vector2f((float)(f7 + 140.0 + (Math.random() - 0.5) * 3.0), 85.0F + (float)(Math.random() * 1.0)), 2.0, MovementFix.OFF
                            );
                        }
                    }
                }

                if (this.sprint.wo().getName().equals("Watchdog Jump") && !MoveUtil.isMoving() && aEg.gameSettings.keyBindJump.isKeyDown()) {
                    RotationComponent.d(false);
                    RotationComponent.setRotations(
                        new Vector2f((float)(aEg.thePlayer.pl - 150.0 + (Math.random() - 0.5) * 3.0), 85.0F + (float)(Math.random() * 1.0)),
                        10.0,
                        MovementFix.OFF
                    );
                }

                if (this.sprint.wo().getName().equals("Watchdog Jump")
                    && aEg.thePlayer.isPotionActive(Potion.moveSpeed)
                    && aEg.gameSettings.keyBindSneak.isKeyDown()) {
                    aEg.thePlayer.motionX *= 0.75;
                    aEg.thePlayer.motionZ *= 0.75;
                }

                int moving = (!this.sameY.wo().getName().equals("Off") || this.e(Speed.class).isEnabled()) && !aEg.gameSettings.keyBindJump.isKeyDown() && MoveUtil.isMoving() ? 1 : 0;
                if (!this.sprint.wo().getName().equals("Watchdog Prediction")
                    || Sprint.predictionDone
                    || !aEg.thePlayer.onGround
                    || aEg.thePlayer.cqL < 3
                    || Sprint.predictionTicks == 12) {
                    SlotComponent slotcomponent = this.d(SlotComponent.class);
                    SlotComponent.b(SlotUtil.vx(), this.render.wo());
                }

                if (aEg.gameSettings.keyBindJump.isKeyDown() && !MoveUtil.isMoving() && !this.e(Speed.class).isEnabled()) {
                    if (!this.isOffsetReplaceableBelow(1) || moving != 0 && (!this.isOffsetReplaceableBelow(2) || !this.isOffsetReplaceableBelow(3))) {
                        this.ticksOnAir = 0;
                    } else {
                        this.ticksOnAir++;
                    }
                } else {
                    boolean flag7;
                    if (moving != 0) {
                        flag7 = this.isReplaceableAtStartY();
                    } else {
                        flag7 = this.isReplaceableBelow(1) && (moving == 0 || this.isReplaceableAtStartY());
                    }

                    if (flag7) {
                        this.ticksOnAir++;
                    } else {
                        this.ticksOnAir = 0;
                    }
                }

                this.predictionPlaceDelay = aEg.thePlayer.tR >= 2 && !(Math.random() > 0.3) ? 1 : 0;
                if (!this.watchdogPrediction.wo()) {
                    int l1 = flag5 ? 0 : (int)MathUtil.l(this.placeDelay.wo().intValue(), this.placeDelay.wA().intValue());
                    this.readyToPlace = !BadPacketsComponent.bad(false, true, false, false, true) && this.ticksOnAir > l1;
                } else {
                    this.readyToPlace = !BadPacketsComponent.bad(false, true, false, false, true) && this.ticksOnAir > this.predictionPlaceDelay;
                }

                BadPacketsComponent.bad(false, true, false, false, true);
                float f6 = MathHelper.wrapAngleTo180_float(aEg.thePlayer.pl);
                boolean flag8 = Math.abs(f6 % 90.0F) <= 10.0F || Math.abs(f6 % 90.0F) >= 80.0F;
                WatchdogDownwards.fullBlockTicks++;
                if (aEg.thePlayer.posY % 1.0 != 0.0) {
                    WatchdogDownwards.fullBlockTicks = 0;
                }

                if (this.placementIndex == 0) {
                    this.calculateSneaking();
                }

                if (aEg.gameSettings.keyBindSneak.isKeyDown()) {
                    this.extraPlacements = 0;
                }

                if (aEg.gameSettings.keyBindSneak.isKeyDown()
                    && WatchdogDownwards.fullBlockTicks >= 6
                    && flag8
                    && this.downwardsPressS.wo().getName().equals("Watchdog")
                    && (!aEg.gameSettings.keyBindSneak.isPressed() || aEg.thePlayer.ticksExisted % 3 != 1)) {
                    this.extraPlacements = 0;
                    this.placeOffset = new Vector3d(0.0, 0.0, 0.0);
                    aEg.thePlayer.crd = false;
                    this.placeOffset.setY(-1.0);
                }

                this.targetBlock = PlayerUtil.a(
                    this.placeOffset.getX(), this.placeOffset.getY(), this.placeOffset.getZ(), moving != 0 ? (int)Math.floor(this.startY) + (int)this.placeOffset.getY() : null
                );
                if (this.targetBlock == null) {
                    return;
                }

                EnumFacingOffset aib = PlayerUtil.a(this.targetBlock, this.placeOffset.getY() < 0.0);
                int l_hi = 0;
                if (flag5 && aib != null && aib.getEnumFacing() != EnumFacing.UP) {
                    EnumFacingOffset aibx = PlayerUtil.a(this.targetBlock, false);
                    if (aibx != null && aibx.getEnumFacing() == EnumFacing.UP) {
                        aib = aibx;
                    }
                }

                if (aib != null) {
                    this.enumFacing = aib;
                    this.lastEnumFacing = aib;
                } else {
                    EnumFacingOffset aibx = PlayerUtil.a(this.targetBlock, !(this.placeOffset.getY() < 0.0));
                    if (aibx != null) {
                        this.enumFacing = aibx;
                        this.lastEnumFacing = aibx;
                    } else {
                        if (this.lastEnumFacing == null) {
                            this.enumFacing = null;
                            return;
                        }

                        this.enumFacing = this.lastEnumFacing;
                        l_hi = 1;
                    }
                }

                EnumFacingOffset aibx = this.enumFacing != null ? this.enumFacing : this.lastEnumFacing;
                aEg.gameSettings.keyBindSneak.isKeyDown();
                if (aibx != null && aibx.getEnumFacing().getAxis().isHorizontal()) {
                } else {
                }

                BlockPos blockpos = new BlockPos(this.targetBlock.xCoord, this.targetBlock.yCoord, this.targetBlock.zCoord);
                EnumFacingOffset aibxx = this.enumFacing != null ? this.enumFacing : this.lastEnumFacing;
                if (aibxx == null) {
                    return;
                }

                this.blockFace = blockpos.add(aibxx.getOffset().xCoord, aibxx.getOffset().yCoord, aibxx.getOffset().zCoord);
                if (this.blockFace != null && aibxx.getEnumFacing() != null) {
                    if (!this.sprint.wo().getName().equals("Watchdog Jump") || !this.newWatchdogRots.wo() || !MoveUtil.isMoving() && aEg.gameSettings.keyBindJump.isKeyDown()) {
                        this.calculateRotations();
                    } else {
                        BlockPos blockpos1 = this.blockFace;
                        float f8 = 12.0F;
                        this.applyNewWatchdogRotations(blockpos1, f8);
                    }

                    if (this.targetBlock != null && this.enumFacing != null && this.blockFace != null) {
                        SlotComponent slotcomponent1 = this.d(SlotComponent.class);
                        if (SlotComponent.getItemStack() != null) {
                            SlotComponent slotcomponent2 = this.d(SlotComponent.class);
                            if (SlotComponent.getItemStack().getItem() instanceof ItemBlock) {
                                SlotComponent slotcomponent3 = this.d(SlotComponent.class);
                                ItemStack itemstack = SlotComponent.getItemStack();
                                if (itemstack.cWo <= 0) {
                                    ChatUtil.c("RealStackSize is less than or equal to 0");
                                }

                                if (itemstack.getItem() instanceof ItemBlock && itemstack.cWo > 0 && this.enumFacing != null) {
                                    if (!RayCastUtil.overBlock(this.enumFacing.getEnumFacing(), this.blockFace, this.rayCast.wo().getName().equals("Strict"))
                                        && !this.rayCast.wo().getName().equals("Off")
                                        && (l_hi == 0 || moving == 0 || !aEg.thePlayer.onGround)
                                        && (!flag5 || !this.bypassRaycastWhenFalling.wo())
                                        && (!this.rotationMode.wo().getName().equals("Grim") || !RayCastUtil.a(new Vector2f(this.targetYaw, this.targetPitch), this.enumFacing.getEnumFacing(), this.blockFace, false))
                                        )
                                     {
                                    } else if (this.readyToPlace) {
                                        this.placeBlock();
                                    }

                                    if (Math.random() > 0.3
                                        && aEg.objectMouseOver != null
                                        && aEg.objectMouseOver.typeOfHit != null
                                        && Optional.ofNullable(aEg.objectMouseOver.getBlockPos()).map(var1x -> var1x.equals(this.blockFace)).orElse(false)
                                        && this.blockFace != null
                                        && aEg.objectMouseOver.sideHit == EnumFacing.UP
                                        && this.rayCast.wo().getName().equals("Strict")
                                        && !(PlayerUtil.p(0.0, -1.0, 0.0) instanceof BlockAir)) {
                                        aEg.Az();
                                    }
                                }

                                if (aEg.gameSettings.keyBindJump.isKeyDown() && aEg.thePlayer.posY % 1.0 > 0.5) {
                                    this.startY = Math.floor(aEg.thePlayer.posY);
                                }

                                if ((aEg.thePlayer.posY < this.startY || aEg.thePlayer.onGround) && !MoveUtil.isMoving()) {
                                    this.startY = Math.floor(aEg.thePlayer.posY);
                                }

                                if (aEg.thePlayer.cqL > 2 && Math.floor(aEg.thePlayer.posY) != this.startY && !(PlayerUtil.p(0.0, -1.0, 0.0) instanceof BlockAir)) {
                                    this.startY = Math.floor(aEg.thePlayer.posY);
                                }

                                this.placementIndex++;
                                continue;
                            }
                        }

                        return;
                    }

                    return;
                }

                return;
            }
        };
        this.onMoveInput = var1 -> {
            float f = 0.0F;
            if (!this.sneak.wo() && !this.sprint.wo().getName().equals("Matrix")) {
                var1.setSneak(false);
            }

            if (this.getDisplayName().equals("Telly")) {
                float f1 = MathHelper.wrapAngleTo180_float(aEg.thePlayer.pl);
                int notAbs = !(Math.abs(f1 % 90.0F) <= 10.0F) && !(Math.abs(f1 % 90.0F) >= 80.0F) ? 0 : 1;
                if (aEg.thePlayer.onGround && MoveUtil.enoughMovementForSprinting()) {
                    if (this.watchdogTelly.wo()) {
                        if (aEg.thePlayer.cqL >= this.tellyJumpDelay) {
                            var1.setJump(true);
                        }
                    } else {
                        var1.setJump(true);
                    }
                }

                if (aEg.thePlayer.tR == 2 || aEg.thePlayer.tR == 3 || aEg.thePlayer.tR == 4 || aEg.thePlayer.tR == 5) {
                    aEg.gameSettings.keyBindJump.isKeyDown();
                }
            }
        };
        this.onMoveInputMedium = var1 -> {
            double d0 = 0.0;
            this.forward = var1.getForward();
            this.strafe = var1.getStrafe();
            if (this.blockInputTicks-- > 0) {
                var1.setForward(0.0F);
                var1.setStrafe(0.0F);
            }

            if (this.sneak.wo()) {
                double d1 = this.sneakingSpeed.wo().doubleValue();
                if (!(d1 <= 0.2)) {
                    var1.setSneakSlowDownMultiplier(d1);
                }
            }
        };
        this.onStrafe = var1 -> {
            this.runMode();
            if (!Objects.equals(this.yawOffset.wo().getName(), "0") && !this.movementCorrection.wo() && !this.sprint.wo().getName().equals("Watchdog Jump")) {
                MoveUtil.useDiagonalSpeed();
            }

            if (this.sameY.wo().getName().equals("Auto Jump") && aEg.thePlayer.onGround && MoveUtil.isMoving() && aEg.thePlayer.posY == this.startY) {
                aEg.thePlayer.jump();
            }
        };
        this.onPacketSend = var1 -> {
            Object object = null;
            Object object1 = null;
            Packet packet = var1.dq();
            if (packet instanceof C08PacketPlayerBlockPlacement) {
                C08PacketPlayerBlockPlacement c08packetplayerblockplacement = (C08PacketPlayerBlockPlacement)packet;
                if (!c08packetplayerblockplacement.getPosition().h(new Vector3d(-1.0, -1.0, -1.0))) {
                    this.blocksUntilSneak--;
                }

                if (this.isDisabledByFlag() && this.isBlockPlacement(c08packetplayerblockplacement)) {
                    this.flagDisableTicks--;
                }
            }
        };
        this.onSprint = var1 -> {
            float f = 0.0F;
            if (!aEg.thePlayer.onGround) {
                float f1 = MathHelper.wrapAngleTo180_float(aEg.thePlayer.pl);
                int notAbs = !(Math.abs(f1 % 90.0F) <= 10.0F) && !(Math.abs(f1 % 90.0F) >= 80.0F) ? 0 : 1;
                if (notAbs != 0 && aEg.gameSettings.keyBindJump.isKeyDown() && this.watchdogTelly.wo() && !aEg.thePlayer.onGround) {
                    aEg.thePlayer.setSprinting(false);
                }
            }
        };
    }

    public void calculateSneaking() {
        if (!this.downwardsPressS.wo().getName().equals("Watchdog") && this.ticksOnAir == 0) {
            aEg.gameSettings.keyBindSneak.setPressed(false);
        }

        this.sneakingTicks--;
        if (this.sneak.wo() || this.pauseTicks > 0) {
            int intValue2 = this.startSneaking.wv().intValue();
            int intValue3 = this.placeDelay.wv().intValue();
            int intValue4 = this.stopSneaking.wv().intValue();
            if (this.pauseTicks > 0) {
                this.pauseTicks--;
                this.sneakingTicks = 0;
                this.blocksUntilSneak = 0;
            }

            if (this.sneakingTicks >= 0) {
                aEg.gameSettings.keyBindSneak.setPressed(true);
            } else {
                if (this.ticksOnAir > 0) {
                    this.sneakingTicks = (int)(intValue4);
                }

                if ((
                        this.ticksOnAir > 0
                            || PlayerUtil.p(aEg.thePlayer.motionX * intValue2, -0.0784000015258789, aEg.thePlayer.motionZ * intValue2) instanceof BlockAir
                    )
                    && this.blocksUntilSneak <= 0) {
                    this.sneakingTicks = (int)(intValue2 + intValue3 + intValue4);
                    this.blocksUntilSneak = this.sneakEvery.wv().intValue();
                }
            }
        }
    }

    public Vector2f findPlacementRotation(BlockPos pos, EnumFacingOffset var2, boolean var3, float var4, float var5, float var6) {
        if (var2 == null) {
            return new Vector2f(var4, var5);
        }

        EnumFacing enumfacing = var2.getEnumFacing();
        float f13 = aEg.thePlayer.pl;
        float degrees = (float)Math.toDegrees(MoveUtil.direction());
        ArrayList arraylist = new ArrayList();
        arraylist.add(this.snapTo45(f13 + var6));
        arraylist.add(this.snapTo45(f13 + 45.0F + var6));
        arraylist.add(this.snapTo45(f13 - 45.0F + var6));
        arraylist.add(this.snapTo45(degrees + var6));
        ArrayList arraylist1 = new ArrayList();

        for (float f18 : (Iterable<Float>)arraylist) {
            int j_hi = 0;
            Iterator iterator1 = arraylist1.iterator();

            while (iterator1.hasNext()) {
                Float f19 = (Float)iterator1.next();
                if (Math.abs(MathHelper.wrapAngleTo180_float(f18 - f19)) < 0.1F) {
                    j_hi = 1;
                    break;
                }
            }

            if (j_hi == 0) {
                arraylist1.add(f18);
            }
        }

        Vector2f vector2f1 = null;
        float f16 = Float.MAX_VALUE;
        Iterator iterator = arraylist1.iterator();

        while (iterator.hasNext()) {
            Float f8 = (Float)iterator.next();
            Vector2f vector2f = this.findPitch(pos, enumfacing, f8, var3);
            if (vector2f != null) {
                float f9 = Math.abs(MathHelper.wrapAngleTo180_float(vector2f.x - var4));
                float f10 = Math.abs(vector2f.y - var5);
                float f11 = f9 * f9 + f10 * f10;
                float f12 = Math.abs(MathHelper.wrapAngleTo180_float(vector2f.x - this.snapTo45(vector2f.x)));
                float f14 = Math.abs(MathHelper.wrapAngleTo180_float(vector2f.x - f13));
                float f15 = f11 + (f12 * 0.001F + f14 * 5.0E-4F);
                if (f15 < f16) {
                    f16 = f15;
                    vector2f1 = vector2f;
                }
            }
        }

        return vector2f1 != null ? vector2f1 : RotationUtil.a(new Vector3d(pos.getX(), pos.getY(), pos.getZ()), enumfacing);
    }

    @Generated
    public void setTicksOnAir(int var1) {
        this.ticksOnAir = var1;
    }

    public void resetAllKeyBinds() {
        this.c(true, true, true, true, true, true);
    }

    public void calculateRotations() {
        double d = 0.0;
        int i5_hi = 0;
        int name = Integer.parseInt(String.valueOf(this.yawOffset.wo().getName()));
        double rotationSpeed = this.rotationSpeed.wo().doubleValue();
        double d4 = this.rotationSpeed.wA().doubleValue();
        float f6 = (float)MathUtil.l(rotationSpeed, d4);
        MovementFix movementfix = this.movementCorrection.wo() ? MovementFix.NORMAL : MovementFix.OFF;
        EnumFacingOffset aib = this.enumFacing != null ? this.enumFacing : this.lastEnumFacing;
        if (aib != null) {
            String s = this.getDisplayName();
            switch (s) {
                case "Normal":
                    aEg.entityRenderer.getMouseOver(1.0F);
                    if (this.readyToPlace
                        && !aEg.gameSettings.keyBindPickBlock.isKeyDown()
                        && aib != null
                        && (aEg.objectMouseOver.sideHit != aib.getEnumFacing() || !aEg.objectMouseOver.getBlockPos().equals(this.blockFace))) {
                        this.D(name);
                    }
                    break;
                case "Breesily":
                    if (this.readyToPlace && aib != null) {
                        if (aib.getEnumFacing() == EnumFacing.UP) {
                            this.targetPitch = 90.0F;
                        } else {
                            Double d5 = (double)((float)(Math.toDegrees(Math.atan2(aib.getOffset().zCoord, aib.getOffset().xCoord)) % 360.0) - 90.0F);
                            Double d6 = 80.0;
                            this.targetYaw = (float)d5.doubleValue() + this.randomYawOffset;
                            this.targetPitch = (float)d6.doubleValue() + this.randomPitchOffset;
                        }
                    } else if (Math.random() > 0.99 || this.targetPitch % 90.0F == 0.0F) {
                        this.randomYawOffset = (float)(Math.random() - 0.5);
                        this.randomPitchOffset = (float)(Math.random() - 0.5);
                    }

                    if (aEg.gameSettings.keyBindForward.isKeyDown() && !aEg.gameSettings.keyBindJump.isKeyDown()) {
                        Double d7 = 0.0;
                        Double d8 = 0.0;
                        switch (aEg.thePlayer.getHorizontalFacing()) {
                            case NORTH:
                                d7 = aEg.thePlayer.posX - Math.floor(aEg.thePlayer.posX);
                                d8 = aEg.thePlayer.motionZ;
                                break;
                            case EAST:
                                d7 = aEg.thePlayer.posZ - Math.floor(aEg.thePlayer.posZ);
                                d8 = aEg.thePlayer.motionX;
                                break;
                            case SOUTH:
                                d7 = 1.0 - (aEg.thePlayer.posX - Math.floor(aEg.thePlayer.posX));
                                d8 = aEg.thePlayer.motionZ;
                                break;
                            case WEST:
                                d7 = 1.0 - (aEg.thePlayer.posZ - Math.floor(aEg.thePlayer.posZ));
                                d8 = aEg.thePlayer.motionX;
                                break;
                            default:
                                double random = Math.random();
                                ChatUtil.b("Unknown " + random);
                        }

                        Double d10 = Math.abs(d8);
                        if (!(d10 < 0.086) || !(Math.abs(d7 - 0.5) < 0.4) || this.placeDelay.wA().intValue() > 1) {
                            if (d7 < 0.5 + (Math.random() - 0.5) / 10.0) {
                                aEg.gameSettings.keyBindLeft.setPressed(false);
                                aEg.gameSettings.keyBindRight.setPressed(true);
                                aEg.gameSettings.keyBindRight.setPressed(true);
                            } else {
                                aEg.gameSettings.keyBindRight.setPressed(false);
                                aEg.gameSettings.keyBindLeft.setPressed(true);
                            }
                        }
                    }
                    break;
                case "Snap":
                    this.D(name);
                    if (aib != null
                        && (
                            this.ticksOnAir <= 0
                                || RayCastUtil.a(RotationComponent.fk, aib.getEnumFacing(), this.blockFace, this.rayCast.wo().getName().equals("Strict"))
                                || aEg.thePlayer.cqL < 2 && aEg.thePlayer.onGround
                        )) {
                        this.targetYaw = (float)Math.toDegrees(MoveUtil.direction(aEg.thePlayer.pl, this.forward, this.strafe)) - name;
                    }
                    break;
                case "Eagle":
                    Float f7 = (aEg.thePlayer.pl + 1.0E7F) % 360.0F;
                    float f8 = f7 - 180.0F - f7 % 90.0F + 45.0F;
                    Float f9 = 78.0F;
                    int abs2 = Math.min(Math.abs(f7 % 90.0F), Math.abs(90.0F - f7) % 90.0F) < Math.min(Math.abs(f7 + 45.0F) % 90.0F, Math.abs(90.0F - (f7 + 45.0F)) % 90.0F) ? 1 : 0;
                    if (abs2 != 0
                        && RayCastUtil.c(new Vector2f(f8 + 90.0F, f9), 3.0).typeOfHit == MovingObjectType.BLOCK
                        && RayCastUtil.c(new Vector2f(f8, f9), 3.0).typeOfHit != MovingObjectType.BLOCK) {
                        f8 += 90.0F;
                    }

                    movementfix = MovementFix.NORMAL;
                    if (Math.random() > (aEg.thePlayer.onGround ? 0.5 : 0.2)) {
                        SlotComponent slotcomponent = this.d(SlotComponent.class);
                        if (SlotComponent.getItemStack().getItem() instanceof ItemBlock) {
                            aEg.Az();
                        }
                    }

                    aEg.thePlayer.movementInput.sneak = aEg.thePlayer.sendQueue.doneLoadingTerrain;
                    if (aEg.thePlayer.tR >= 4 && MoveUtil.isMoving()) {
                        aEg.gameSettings.keyBindSneak.setPressed(true);
                    }

                    if (aEg.thePlayer.cqL == 1) {
                        aEg.gameSettings.keyBindSneak.setPressed(false);
                    }

                    if (abs2 == 0) {
                        f8 += 90.0F;
                    }

                    this.targetYaw = f8 + this.randomYawOffset / 2.0F;
                    this.targetPitch = f9 + this.randomPitchOffset / 2.0F;
                    break;
                case "Telly":
                    if (this.placementIndex == 0 && this.watchdogTelly.wo()) {
                        this.tellyTicks++;
                        Float f10 = aEg.thePlayer.pl;
                        float wrapped = MathHelper.wrapAngleTo180_float(f10);
                        RotationComponent.d(false);
                        int notAbs = !(Math.abs(wrapped % 90.0F) <= 10.0F) && !(Math.abs(wrapped % 90.0F) >= 80.0F) ? 0 : 1;
                        if (aEg.thePlayer.onGround && aEg.thePlayer.cqL == 1) {
                            this.tellyJumpDelay = this.age.wv().intValue();
                        }

                        int enoughMovementForSprinting2 = aEg.thePlayer.onGround && MoveUtil.enoughMovementForSprinting() && aEg.thePlayer.cqL == this.tellyJumpDelay - 0 && this.tellyJumpDelay > 0 ? 1 : 0;
                        if (enoughMovementForSprinting2 != 0) {
                            float degrees = (float)Math.toDegrees(MoveUtil.direction());
                            Float f13 = (float)(85.0 + Math.random() * 0.5);
                            this.targetYaw = degrees;
                            this.targetPitch = f13;
                            RotationComponent.setRotations(RotationUtil.m(new Vector2f(this.targetYaw, this.targetPitch)), 180.0, MovementFix.NORMAL);
                            int k6_hi = 1;
                            this.rotationBoostPending = true;
                        } else {
                            int notKeyDown = !aEg.thePlayer.onGround || !MoveUtil.enoughMovementForSprinting() || this.blockDiagonalAscend.wo() && aEg.gameSettings.keyBindJump.isKeyDown() && notAbs == 0 || !aEg.gameSettings.keyBindJump.isKeyDown() && aEg.thePlayer.cqL > 1 && this.tellyTicks > 3 ? 0 : 1;
                            if (notKeyDown != 0) {
                                Vector2f vector2f = this.getTellyRotation(this.blockFace, aib, 360.0F, 0.0F, 90.0F, -45);
                                this.targetYaw = vector2f.x;
                                this.targetPitch = (float)(vector2f.y + Math.random() * 0.5);
                                RotationComponent.setRotations(RotationUtil.m(new Vector2f(this.targetYaw, this.targetPitch)), 10.0, MovementFix.NORMAL);
                                int i7_hi = 1;
                                this.rotationBoostPending = true;
                            } else {
                                int kl2 = aib != null && !RayCastUtil.a(RotationComponent.fk, aib.getEnumFacing(), this.blockFace, this.isStrictRayCast()) ? 1 : 0;
                                int keyDown = aEg.thePlayer.onGround && !aEg.gameSettings.keyBindJump.isKeyDown() ? 1 : 0;
                                if ((kl2 != 0 || keyDown != 0 || this.tellyTicks <= 5 || aib == null) && aib != null) {
                                    Vector2f vector2f1 = this.findPlacementRotation(
                                        this.blockFace,
                                        aib,
                                        kl2 != 0 ? this.isStrictRayCast() : false,
                                        (float)(this.targetYaw + Math.random() * 0.5),
                                        (float)(this.targetPitch + Math.random() * 0.5),
                                        45.0F
                                    );
                                    int wo2 = this.rotationBoostPending && this.rotationBlockBoost.wo() && !aEg.thePlayer.onGround ? 1 : 0;
                                    if (this.boostOnlyWhileHoldingJump.wo()) {
                                        wo2 = wo2 != 0 && aEg.gameSettings.keyBindJump.isKeyDown() ? 1 : 0;
                                    }

                                    float f14;
                                    float f15;
                                    if (wo2 != 0) {
                                        f14 = this.rotationBlockRotationSpeed.wo().floatValue();
                                        f15 = this.rotationBlockRotationSpeed.wA().floatValue();
                                        this.rotationBoostPending = false;
                                    } else {
                                        f14 = this.watchdogTellyRotationSpeed.wo().floatValue();
                                        f15 = this.watchdogTellyRotationSpeed.wA().floatValue();
                                    }

                                    this.targetYaw = this.b(this.targetYaw, vector2f1.x, (float)MathUtil.l(f14, f15));
                                    this.targetPitch = this.b(this.targetPitch, vector2f1.y, (float)MathUtil.l(f14, f15));
                                }
                            }
                        }

                        if (!this.dontForceRaycastOnWatchdogTelly.wo()) {
                            if ((aEg.thePlayer.ae >= 10 || aEg.thePlayer.tR <= 6 || !(Math.hypot(aEg.thePlayer.crI, aEg.thePlayer.crK) > 0.2) || !(Math.random() > 0.5))
                                && MoveUtil.enoughMovementForSprinting()
                                && (!(MoveUtil.speed() < 0.15) || aEg.gameSettings.keyBindJump.isKeyDown())
                                && !aEg.thePlayer.isCollidedHorizontally
                                && (this.tellyTicks >= 15 || aEg.thePlayer.tR <= 8 || aEg.gameSettings.keyBindJump.isKeyDown())
                                && (aEg.thePlayer.tR <= 11 || !(Math.random() > 0.5))) {
                                this.rayCast.co("Normal");
                            } else {
                                this.rayCast.co("Off");
                            }
                        }
                    } else if (this.placementIndex == 0 && this.watchdogPrediction.wo()) {
                        int tR2 = aEg.thePlayer.tR;
                        if (tR2 == 1 || tR2 == 0) {
                            aEg.Az();
                        }

                        if (tR2 >= 0
                            && aEg.thePlayer.tR <= (this.sameY.wo().getName().equals("Off") ? 7 : 10)
                            && (!aEg.thePlayer.onGround || !aEg.gameSettings.keyBindJump.isKeyDown())) {
                            if (aib != null && !RayCastUtil.a(RotationComponent.fk, aib.getEnumFacing(), this.blockFace, this.rayCast.wo().getName().equals("Strict"))) {
                                this.D(45);
                            }
                        } else {
                            this.D(Integer.parseInt(String.valueOf(this.yawOffset.wo().getName())));
                            this.targetYaw = aEg.thePlayer.pl;
                        }

                        if (aEg.thePlayer.tR <= 0 && aEg.thePlayer.cqL < 2 || aEg.thePlayer.onGround && aEg.gameSettings.keyBindJump.isKeyDown() && aEg.thePlayer.cqL < 2) {
                            this.readyToPlace = false;
                        }
                    } else if (this.placementIndex == 0) {
                        int tR3 = aEg.thePlayer.tR;
                        if (tR3 == 2 || tR3 == 0) {
                            aEg.Az();
                        }

                        if (tR3 < 3 || aEg.thePlayer.tR > (this.sameY.wo().getName().equals("Off") ? 7 : 10)) {
                            this.D(Integer.parseInt(String.valueOf(this.yawOffset.wo().getName())));
                            this.targetYaw = aEg.thePlayer.pl;
                        } else if (aib != null && !RayCastUtil.a(RotationComponent.fk, aib.getEnumFacing(), this.blockFace, this.rayCast.wo().getName().equals("Strict"))) {
                            this.D(0);
                        }

                        if (aEg.thePlayer.tR <= 3) {
                            this.readyToPlace = false;
                        }
                    }
                    break;
                case "Godbridge":
                    SlotComponent slotcomponent1 = this.d(SlotComponent.class);
                    if (SlotComponent.bP() instanceof ItemBlock && this.readyToPlace) {
                        aEg.Az();
                    }

                    this.targetYaw = aEg.thePlayer.pl - aEg.thePlayer.pl % 90.0F - 180.0F + 45 * (aEg.thePlayer.pl > 0.0F ? 1 : -1);
                    this.targetPitch = 76.4F;
                    movementfix = MovementFix.TRADITIONAL;
                    Double d11 = 0.15;
                    int notAbs2 = !(Math.abs(aEg.thePlayer.posX % 1.0) > 1.0 - d11) && !(Math.abs(aEg.thePlayer.posX % 1.0) < d11) ? 0 : 1;
                    int notAbs3 = !(Math.abs(aEg.thePlayer.posZ % 1.0) > 1.0 - d11) && !(Math.abs(aEg.thePlayer.posZ % 1.0) < d11) ? 0 : 1;
                    aEg.gameSettings
                        .keyBindRight
                        .setPressed(notAbs2 != 0 && notAbs3 != 0 || Keyboard.isKeyDown(aEg.gameSettings.keyBindLeft.getKeyCode()));
                    aEg.gameSettings.keyBindBack.setPressed(Keyboard.isKeyDown(aEg.gameSettings.keyBindForward.getKeyCode()));
                    aEg.gameSettings.keyBindForward.setPressed(Keyboard.isKeyDown(aEg.gameSettings.keyBindBack.getKeyCode()));
                    aEg.gameSettings.keyBindLeft.setPressed(Keyboard.isKeyDown(aEg.gameSettings.keyBindRight.getKeyCode()));
                    this.godBridgeSneakTicks++;
                    if (Math.abs(MathHelper.wrapAngleTo180_double(this.targetYaw - RotationComponent.fn.getX())) > 10.0) {
                        this.godBridgeSneakTicks = (int)(Math.random() * 4.0);
                        this.randomYawOffset = (float)(Math.random() - 0.5) / 10.0F;
                        this.randomPitchOffset = (float)(Math.random() - 0.5) / 10.0F;
                    }

                    if (Math.random() > 0.99) {
                        this.randomYawOffset = (float)(Math.random() - 0.5) / 10.0F;
                        this.randomPitchOffset = (float)(Math.random() - 0.5) / 10.0F;
                    }

                    if (this.godBridgeSneakTicks <= 10) {
                        aEg.gameSettings.keyBindSneak.setPressed(true);
                    } else if (this.godBridgeSneakTicks == 11) {
                        aEg.gameSettings.keyBindSneak.setPressed(false);
                    }

                    this.targetYaw = this.targetYaw + this.randomYawOffset;
                    this.targetPitch = this.targetPitch + this.randomPitchOffset;
            }

            if (!this.rotationMode.wo().getName().equals("Grim")
                && f6 != 0.0F
                && this.blockFace != null
                && (this.enumFacing != null || this.lastEnumFacing != null)
                && (!this.sprint.wo().getName().equals("Watchdog Prediction") || !aEg.thePlayer.onGround)) {
                RotationComponent.setRotations(new Vector2f(this.targetYaw, this.targetPitch), f6, movementfix);
            }
        }
    }

    public Vec3 getHitVec() {
        Vec3 vec3 = new Vec3(this.blockFace.getX() + Math.random(), this.blockFace.getY() + Math.random(), this.blockFace.getZ() + Math.random());
        MovingObjectPosition movingobjectposition = RayCastUtil.c(RotationComponent.fk, aEg.playerController.getBlockReachDistance());
        if (this.enumFacing == null) {
            return vec3;
        }

        switch (this.enumFacing.getEnumFacing()) {
            case NORTH:
                vec3.zCoord = this.blockFace.getZ();
                break;
            case EAST:
                vec3.xCoord = this.blockFace.getX() + 1;
                break;
            case SOUTH:
                vec3.zCoord = this.blockFace.getZ() + 1;
                break;
            case WEST:
                vec3.xCoord = this.blockFace.getX();
                break;
            case DOWN:
                vec3.yCoord = this.blockFace.getY();
                break;
            case UP:
                vec3.yCoord = this.blockFace.getY() + 1;
        }

        if (movingobjectposition != null
            && movingobjectposition.getBlockPos() != null
            && movingobjectposition.hitVec != null
            && movingobjectposition.getBlockPos().equals(this.blockFace)
            && movingobjectposition.sideHit == this.enumFacing.getEnumFacing()) {
            vec3 = movingobjectposition.hitVec;
        }

        return vec3;
    }

    public float snapTo45(float var1) {
        return Math.round(MathHelper.wrapAngleTo180_float(var1) / 45.0F) * 45.0F;
    }

    public boolean shouldDelayTellyJump() {
        int j;
        if (this.watchdogTelly.wo() && this.age.wA().intValue() > 0) {
            j = 1;
        } else {
            byte b0 = -81;
            int i = b0 + 81;
            j = i;
        }

        return j != 0;
    }
}
