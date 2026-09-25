package com.alan.clients.module.impl.combat.velocity;

import com.alan.clients.component.impl.player.BlinkComponent;
import com.alan.clients.component.impl.player.RotationComponent;
import com.alan.clients.component.impl.player.rotationcomponent.MovementFix;
import com.alan.clients.module.impl.combat.KillAura;
import com.alan.clients.module.impl.combat.Velocity;
import com.alan.clients.module.impl.movement.Speed;
import com.alan.clients.module.impl.movement.speed.GrimSpeed;
import com.alan.clients.module.impl.player.Scaffold;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.input.MoveInputEvent;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.newevent.impl.motion.PreUpdateEvent;
import com.alan.clients.newevent.impl.packet.PacketReceiveEvent;
import com.alan.clients.util.vector.Vector2f;
import com.alan.clients.value.Mode;
import com.alan.clients.value.impl.BooleanValue;
import com.alan.clients.value.impl.NumberValue;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import de.florianmichael.vialoadingbase.ViaLoadingBase;
import com.alan.clients.util.packet.PacketUtil;
import com.alan.clients.util.player.PlayerUtil;
import com.alan.clients.util.rotation.RotationUtil;
import com.alan.clients.component.impl.player.BadPacketsComponent;
import com.alan.clients.component.impl.combat.TargetComponent;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.m;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S14PacketEntity;
import net.minecraft.network.play.server.S32PacketConfirmTransaction;
import net.minecraft.network.play.server.ad;
import net.minecraft.network.play.server.z;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

public class GrimReduceVelocity
extends Mode<Velocity> {
    public static boolean dj;
    public ArrayList<Packet<?>> delayedPackets = new ArrayList();
    public NumberValue reduceTicks;
    @EventLink
    public Listener<PacketReceiveEvent> onPacketReceive;
    public boolean shouldReduce;
    @EventLink(value=0)
    public Listener<PreUpdateEvent> onPreUpdateVeryLow;
    @EventLink
    public Listener<PreMotionEvent> onPreMotion;
    @EventLink
    public Listener<MoveInputEvent> onMoveInput;
    public BooleanValue delayPlus;
    public BooleanValue rotations;
    public static float jq;
    public BooleanValue delayTillGround = new BooleanValue("Delay till Ground", (Mode<?>)this, (Boolean)true);
    public NumberValue range;
    public static float jp;
    public BooleanValue stopSprint;
    public BooleanValue jumpReset = new BooleanValue("Jump Reset", (Mode<?>)this, (Boolean)true);
    public BooleanValue extraHit;
    public static boolean dk;
    @EventLink(value=2)
    public Listener<PreUpdateEvent> onPreUpdate;
    public static boolean flushingPackets;
    public NumberValue teleportDisableTicks;
    public BooleanValue onSwingDisableOnAura;
    public boolean pendingJumpReset;

    public static Vec3 closestPointOnBox(Vec3 vec, AxisAlignedBB axisAlignedBB) {
        double d5 = GrimReduceVelocity.clamp(vec.xCoord, axisAlignedBB.minX, axisAlignedBB.maxX);
        double d6 = GrimReduceVelocity.clamp(vec.yCoord, axisAlignedBB.minY, axisAlignedBB.maxY);
        double d7 = GrimReduceVelocity.clamp(vec.zCoord, axisAlignedBB.minZ, axisAlignedBB.maxZ);
        return new Vec3(d5, d6, d7);
    }

    static {
        dj = false;
    }


    public GrimReduceVelocity(String string, Velocity velocity) {
        super(string, velocity);
        this.reduceTicks = new NumberValue("Reduce Ticks", this, (Number)14, (Number)1, (Number)20, (Number)1);
        this.teleportDisableTicks = new NumberValue("Teleport Disable Ticks", this, (Number)2, (Number)1, (Number)7, (Number)1);
        this.onSwingDisableOnAura = new BooleanValue("On Swing Disable on Aura", (Mode<?>)this, (Boolean)true);
        this.rotations = new BooleanValue("Rotate", (Mode<?>)this, (Boolean)false);
        this.delayPlus = new BooleanValue("Delay Plus", (Mode<?>)this, (Boolean)false);
        this.extraHit = new BooleanValue("Extra Hit", (Mode<?>)this, (Boolean)true);
        this.stopSprint = new BooleanValue("Stop Sprint", (Mode<?>)this, (Boolean)true);
        this.range = new NumberValue("Range", this, (Number)8, (Number)1, (Number)100, (Number)1);
        this.onPacketReceive = packetReceiveEvent -> {
            Packet<?> packet;
            Packet<?> packet2;
            Packet<?> packet3;
            Packet<?> packet4;
            S12PacketEntityVelocity s12PacketEntityVelocity;
            Speed speed = this.e(Speed.class);
            int wo2 = speed.isEnabled() && speed.getMode().wo() instanceof GrimSpeed && (Boolean)((GrimSpeed)speed.getMode().wo()).fastFall.wo() != false ? 1 : 0;
            if (flushingPackets || GrimReduceVelocity.aEg.thePlayer.Zl < 3 || GrimReduceVelocity.aEg.thePlayer.isInWeb || !((Boolean)this.delayTillGround.wo()).booleanValue() && !((Boolean)this.delayPlus.wo()).booleanValue() || wo2 != 0) {
                return;
            }
            Packet<?> packet5 = packetReceiveEvent.getPacket();
            if (packet5 instanceof S12PacketEntityVelocity && (s12PacketEntityVelocity = (S12PacketEntityVelocity)packet5).getEntityID() == GrimReduceVelocity.aEg.thePlayer.getEntityId()) {
                this.pendingJumpReset = true;
                this.delayedPackets.add((Packet<?>)s12PacketEntityVelocity);
                dj = true;
                packetReceiveEvent.setCancelled();
                boolean unused0 = GrimReduceVelocity.aEg.thePlayer.onGround;
            }
            if ((packet4 = packetReceiveEvent.getPacket()) instanceof S32PacketConfirmTransaction) {
                S32PacketConfirmTransaction s32PacketConfirmTransaction = (S32PacketConfirmTransaction)packet4;
                if (dj) {
                    this.delayedPackets.add((Packet<?>)s32PacketConfirmTransaction);
                    packetReceiveEvent.setCancelled();
                }
            }
            if ((packet3 = packetReceiveEvent.getPacket()) instanceof S14PacketEntity) {
                S14PacketEntity s14PacketEntity = (S14PacketEntity)packet3;
                if (dj) {
                    this.delayedPackets.add((Packet<?>)s14PacketEntity);
                    packetReceiveEvent.setCancelled();
                }
            }
            if ((packet2 = packetReceiveEvent.getPacket()) instanceof ad) {
                ad ad2 = (ad)packet2;
                if (dj) {
                    this.delayedPackets.add((Packet<?>)ad2);
                    packetReceiveEvent.setCancelled();
                }
            }
            if ((packet = packetReceiveEvent.getPacket()) instanceof z) {
                z z2 = (z)packet;
                if (dj) {
                    this.delayedPackets.add((Packet<?>)z2);
                    packetReceiveEvent.setCancelled();
                }
            }
        };
        this.onPreUpdateVeryLow = preUpdateEvent -> {
            List<EntityLivingBase> list;
            EntityLivingBase entityLivingBase;
            this.shouldReduce = false;
            KillAura killAura = this.e(KillAura.class);
            List<EntityLivingBase> list2 = TargetComponent.f(((Number)this.range.wo()).intValue());
            List<EntityLivingBase> list3 = TargetComponent.bR();
            EntityLivingBase entityLivingBase2 = killAura.isEnabled() && killAura.jE != null ? killAura.jE : this.getClosest(list3);
            if (entityLivingBase2 == null) {
                return;
            }
            if (GrimReduceVelocity.aEg.thePlayer.ae < 7 && ((Boolean)this.rotations.wo()).booleanValue() && !this.e(Scaffold.class).isEnabled()) {
                this.lookAt((Entity)entityLivingBase2);
            }
            if (((Boolean)((Velocity)this.getParent()).onSwing.wo()).booleanValue() && !GrimReduceVelocity.aEg.thePlayer.isSwingInProgress) {
                if (this.e(KillAura.class).jE == null) return;
                if (!((Boolean)this.onSwingDisableOnAura.wo()).booleanValue()) {
                    return;
                }
            }
            Speed speed = this.e(Speed.class);
            int wo2 = speed.isEnabled() && speed.getMode().wo() instanceof GrimSpeed && (Boolean)((GrimSpeed)speed.getMode().wo()).fastFall.wo() != false ? 1 : 0;
            if (GrimReduceVelocity.aEg.thePlayer.ticksExisted <= 20) return;
            if (wo2 != 0) {
                return;
            }
            entityLivingBase = killAura.isEnabled() && killAura.jE != null ? killAura.jE : this.getClosest(list2);
            if (GrimReduceVelocity.aEg.thePlayer.ae <= ((Number)this.reduceTicks.wo()).intValue() && !BadPacketsComponent.bad(false, false, false, true, false) && !this.e(Scaffold.class).isEnabled() && GrimReduceVelocity.aEg.thePlayer.Zl > ((Number)this.teleportDisableTicks.wo()).intValue()) {
                this.shouldReduce = true;
            }
            if ((list = TargetComponent.f(((Number)this.range.wo()).intValue())) == null || list.isEmpty()) {
                if (killAura == null) return;
                if (killAura.jE == null) {
                    return;
                }
            }
            if (killAura.isEnabled() && killAura.jE != null) {
                EntityLivingBase entityLivingBase4 = killAura.jE;
                MovingObjectPosition movingObjectPosition = GrimReduceVelocity.aEg.objectMouseOver;
                if (!((double)GrimReduceVelocity.aEg.thePlayer.getDistanceToEntity((Entity)entityLivingBase4) <= 3.0 || movingObjectPosition != null && movingObjectPosition.entityHit == entityLivingBase4 || ((Number)this.range.wo()).intValue() <= 3)) {
                    RotationComponent.d(false);
                    RotationComponent.setRotations(new Vector2f(GrimReduceVelocity.aEg.thePlayer.pl, (float)(90.0 - Math.random() * 0.1)), 10.0, MovementFix.NORMAL);
                }
            } else {
                EntityLivingBase entityLivingBase5 = list.get(0);
                Vec3 vec3 = GrimReduceVelocity.aEg.thePlayer.getPositionEyes(1.0f);
                AxisAlignedBB axisAlignedBB = entityLivingBase5.getEntityBoundingBox().expand(0.1, 0.1, 0.1);
                Vec3 vec = new Vec3((axisAlignedBB.minX + axisAlignedBB.maxX) * 0.5, (axisAlignedBB.minY + axisAlignedBB.maxY) * 0.5, (axisAlignedBB.minZ + axisAlignedBB.maxZ) * 0.5).subtract(vec3).normalize();
                Vec3 vec33 = vec3.addVector(vec.xCoord * 3.0, vec.yCoord * 3.0, vec.zCoord * 3.0);
                MovingObjectPosition movingObjectPosition = axisAlignedBB.calculateIntercept(vec3, vec33);
                if (!((movingObjectPosition != null ? vec3.distanceTo(movingObjectPosition.hitVec) : vec3.distanceTo(GrimReduceVelocity.closestPointOnBox(vec3, axisAlignedBB))) <= 3.0) && GrimReduceVelocity.aEg.thePlayer.ae <= ((Number)this.reduceTicks.wo()).intValue() && !BadPacketsComponent.bad(false, false, false, true, false)) {
                    RotationComponent.d(false);
                    RotationComponent.setRotations(new Vector2f(GrimReduceVelocity.aEg.thePlayer.pl, (float)(90.0 - Math.random() * 0.2)), 10.0, MovementFix.NORMAL);
                }
            }
            if (entityLivingBase == null) return;
            if (GrimReduceVelocity.aEg.thePlayer.ae > ((Number)this.reduceTicks.wo()).intValue() + 1) return;
            if (BadPacketsComponent.bad(false, false, false, true, false)) return;
            if (this.e(Scaffold.class).isEnabled()) return;
            if (GrimReduceVelocity.aEg.thePlayer.Zl <= ((Number)this.teleportDisableTicks.wo()).intValue()) return;
            if (ViaLoadingBase.getInstance().getTargetVersion().newerThan(ProtocolVersion.v1_8)) {
                if ((Boolean)this.extraHit.wo() == false) return;
                GrimReduceVelocity.aEg.playerController.attackEntity((EntityPlayer)GrimReduceVelocity.aEg.thePlayer, (Entity)entityLivingBase);
                PacketUtil.send(new m());
                return;
            }
            if ((Boolean)this.extraHit.wo() == false) return;
            PacketUtil.send(new m());
            GrimReduceVelocity.aEg.playerController.attackEntity((EntityPlayer)GrimReduceVelocity.aEg.thePlayer, (Entity)entityLivingBase);
        };
        this.onPreUpdate = preUpdateEvent -> {
            if (GrimReduceVelocity.aEg.thePlayer.onGround && dj && (Boolean)this.delayPlus.wo() == false || GrimReduceVelocity.aEg.thePlayer.Zl < 3 && dj || ((Boolean)this.delayPlus.wo()).booleanValue() && (GrimReduceVelocity.aEg.thePlayer.onGround || !((Boolean)this.delayTillGround.wo()).booleanValue()) && dj && (this.e(KillAura.class).jE == null || PlayerUtil.v((Entity)this.e(KillAura.class).jE) < 2.7 || GrimReduceVelocity.aEg.thePlayer.aY == 1)) {
                dj = false;
                flushingPackets = true;
                BlinkComponent.dispatch();
                this.delayedPackets.forEach(p -> PacketUtil.receive(p));
                this.delayedPackets.clear();
                flushingPackets = false;
            }
            if (GrimReduceVelocity.aEg.thePlayer.tR > 25 && dj) {
                dj = false;
                flushingPackets = true;
                BlinkComponent.dispatch();
                this.delayedPackets.forEach(p -> PacketUtil.receive(p));
                this.delayedPackets.clear();
                flushingPackets = false;
            }
        };
        this.onPreMotion = preMotionEvent -> {
            this.pendingJumpReset = false;
        };
        this.onMoveInput = moveInputEvent -> {
            if (this.pendingJumpReset && ((Boolean)this.jumpReset.wo()).booleanValue()) {
                moveInputEvent.setJump(true);
            }
            if (GrimReduceVelocity.aEg.thePlayer.ae < 7 && ((Boolean)this.rotations.wo()).booleanValue() && !this.e(Scaffold.class).isEnabled()) {
                moveInputEvent.setForward(1.0f);
                moveInputEvent.setStrafe(0.0f);
            }
        };
    }

    public static double clamp(double d2, double d3, double d4) {
        double d5;
        if (d2 < d3) {
            d5 = d3;
            return d5;
        }
        d5 = Math.min(d2, d4);
        return d5;
    }

    public EntityLivingBase getClosest(List<EntityLivingBase> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        return list.stream().min(Comparator.comparingDouble(entityLivingBase -> GrimReduceVelocity.aEg.thePlayer.getDistanceToEntity((Entity)entityLivingBase))).orElse(null);
    }

    public void lookAt(Entity entity) {
        if (entity == null) {
            return;
        }
        Vector2f vector2f = RotationUtil.y(entity);
        RotationComponent.d(false);
        RotationComponent.setRotations(new Vector2f(vector2f.x, GrimReduceVelocity.aEg.thePlayer.rotationPitch), 10.0, MovementFix.NORMAL);
    }

}
