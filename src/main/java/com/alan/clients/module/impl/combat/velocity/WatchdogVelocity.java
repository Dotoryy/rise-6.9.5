package com.alan.clients.module.impl.combat.velocity;

import com.alan.clients.component.impl.player.BlinkComponent;
import com.alan.clients.module.impl.combat.KillAura;
import com.alan.clients.module.impl.combat.Velocity;
import com.alan.clients.module.impl.movement.Flight;
import com.alan.clients.module.impl.movement.Jesus;
import com.alan.clients.module.impl.movement.LongJump;
import com.alan.clients.module.impl.movement.Speed;
import com.alan.clients.module.impl.movement.jesus.WatchdogDolphin118Jesus;
import com.alan.clients.module.impl.movement.speed.WatchdogSpeed;
import com.alan.clients.module.impl.player.Breaker;
import com.alan.clients.module.impl.player.Scaffold;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.JumpEvent;
import com.alan.clients.newevent.impl.motion.PostStrafeEvent;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.newevent.impl.motion.PreUpdateEvent;
import com.alan.clients.newevent.impl.packet.PacketReceiveEvent;
import com.alan.clients.newevent.impl.render.Render3DEvent;
import com.alan.clients.util.player.MoveUtil;
import com.alan.clients.util.render.RenderUtil;
import com.alan.clients.util.vector.Vector2d;
import com.alan.clients.value.Mode;
import com.alan.clients.value.impl.BooleanValue;
import com.alan.clients.value.impl.NumberValue;
import com.alan.clients.util.packet.PacketUtil;
import com.alan.clients.util.player.PlayerUtil;
import com.alan.clients.util.render.ColorUtil;
import com.alan.clients.component.impl.combat.TargetComponent;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S14PacketEntity.S15PacketEntityRelMove;
import net.minecraft.network.play.server.S14PacketEntity.S16PacketEntityLook;
import net.minecraft.network.play.server.S14PacketEntity.S17PacketEntityLookMove;
import net.minecraft.network.play.server.S14PacketEntity;
import net.minecraft.network.play.server.S32PacketConfirmTransaction;
import net.minecraft.network.play.server.a;
import net.minecraft.network.play.server.aa;
import net.minecraft.network.play.server.z;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.GL11;

public final class WatchdogVelocity extends Mode<Velocity> {
    private long backtrackStart = -1L;
    boolean inBacktrackRange = false;
    boolean backtrackBlocked = false;
    public final BooleanValue stack = new BooleanValue("Stack", this, true);
    boolean onOffsetGround;
    public final BooleanValue pingSpoof = new BooleanValue("Ping Spoof", this, true);
    public final BooleanValue alwaysCancelVertical = new BooleanValue("Always Cancel Vertical", this, false);
    public final BooleanValue watchdogBacktrack = new BooleanValue("Watchdog Backtrack", this, false);
    public final BooleanValue damageBoostNotForHypixel = new BooleanValue("Damage Boost (not for hypixel)", this, false);
    private final NumberValue damageBoostSpeed = new NumberValue("Damage Boost Speed", this, 1, 1, 10, 0.01);
    public static boolean dj;
    public static boolean releasing;
    public static boolean velocityCancelled;
    private Vec3 backtrackPos = new Vec3(0.0, 0.0, 0.0);
    public Entity target;
    private int stackCount;
    private final ArrayList<Packet<?>> heldPackets = new ArrayList<>();
    @EventLink
    public final Listener<PacketReceiveEvent> onPacketReceive = var1x -> {
        if (!releasing
            && !this.e(LongJump.class).isEnabled()
            && !this.e(Flight.class).isEnabled()
            && (
                !this.e(Jesus.class).isEnabled()
                    || !this.e(Jesus.class).mode.wo().getName().equals("Watchdog Dolphin 1.18+")
                    || WatchdogDolphin118Jesus.outOfWaterTicks >= 30
            )) {
            switch (var1x.getPacket()) {
                case S12PacketEntityVelocity s12packetentityvelocity:
                    if (s12packetentityvelocity.getEntityID() == aEg.thePlayer.getEntityId()
                        && !PlayerUtil.vk()
                        && (
                            aEg.thePlayer.cqL <= 0
                                || !this.alwaysCancelVertical.wo()
                                || this.e(Speed.class).isEnabled()
                                || aEg.thePlayer.isJumping
                                || aEg.gameSettings.keyBindJump.isKeyDown()
                                || this.onOffsetGround
                                || !(s12packetentityvelocity.getMotionY() / 8000.0 > 0.08)
                                || aEg.thePlayer.Zl <= 11
                        )) {
                        if (this.stack.wo() && this.stackCount < 1 && !aEg.thePlayer.onGround && Math.random() < 0.73 && aEg.thePlayer.tR <= 13
                            || aEg.thePlayer.inWater
                            || this.e(Scaffold.class).isEnabled()
                                && (
                                    aEg.thePlayer.tR <= 3
                                        || !MoveUtil.isMoving()
                                        || !this.e(Speed.class).isEnabled()
                                        || !WatchdogSpeed.damageBoostEnabled
                                        || !(
                                            Math.hypot(
                                                    MoveUtil.predictedMotion(s12packetentityvelocity.getMotionX() / 8000.0, 1),
                                                    MoveUtil.predictedMotion(s12packetentityvelocity.getMotionZ() / 8000.0, 1)
                                                )
                                                > MoveUtil.speed()
                                        )
                                )) {
                            this.stackCount++;
                            velocityCancelled = true;
                            var1x.setCancelled();
                            velocityCancelled = false;
                        }

                        if (var1x.isCancelled()
                            || this.e(Scaffold.class).isEnabled()
                            || aEg.thePlayer.tR > 11
                            || aEg.thePlayer.onGround && (!aEg.thePlayer.onGround || MoveUtil.isMoving())
                            || s12packetentityvelocity.getMotionY() / 8000.0 < 0.08 && !this.e(Speed.class).isEnabled() && !this.e(LongJump.class).isEnabled()
                            || !this.e(Scaffold.class).isEnabled()
                                && aEg.thePlayer.tR > 3
                                && MoveUtil.isMoving()
                                && this.e(Speed.class).isEnabled()
                                && WatchdogSpeed.damageBoostEnabled
                                && Math.hypot(s12packetentityvelocity.getMotionX() / 8000.0, s12packetentityvelocity.getMotionZ() / 8000.0) > MoveUtil.speed()) {
                            if (!var1x.isCancelled()) {
                                if (!this.e(Scaffold.class).isEnabled()) {
                                    aEg.thePlayer.motionY = s12packetentityvelocity.getMotionY() / 8000.0;
                                }

                                if (!this.e(Speed.class).isEnabled()) {
                                    var1x.setCancelled();
                                }
                            }
                        } else {
                            this.stackCount = 0;
                            dj = true;
                            this.heldPackets.add(s12packetentityvelocity);
                            var1x.setCancelled();
                        }
                    } else if (!var1x.isCancelled() && PlayerUtil.vk()) {
                        var1x.setCancelled();
                    } else if (!var1x.isCancelled()
                        && s12packetentityvelocity.getEntityID() == aEg.thePlayer.getEntityId()
                        && !this.e(LongJump.class).isEnabled()) {
                        s12packetentityvelocity.motionY *= 0;
                        aEg.thePlayer.motionX *= 0.9;
                        aEg.thePlayer.motionZ *= 0.9;
                        var1x.setPacket(s12packetentityvelocity);
                    }
                    break;
                case S32PacketConfirmTransaction s32packetconfirmtransaction:
                    if (dj && this.pingSpoof.wo()) {
                        this.heldPackets.add(s32packetconfirmtransaction);
                        var1x.setCancelled();
                    }
                    break;
                case a a:
                    if (dj && this.pingSpoof.wo()) {
                        var1x.setCancelled();
                    }
                    break;
                case S16PacketEntityLook s16packetentitylook:
                    if ((this.inBacktrackRange || dj) && this.watchdogBacktrack.wo() && !this.backtrackBlocked) {
                        this.heldPackets.add(s16packetentitylook);
                        var1x.setCancelled();
                    }
                    break;
                case S15PacketEntityRelMove s15packetentityrelmove:
                    if ((this.inBacktrackRange || dj) && this.watchdogBacktrack.wo() && !this.backtrackBlocked) {
                        this.heldPackets.add(s15packetentityrelmove);
                        var1x.setCancelled();
                    }
                    break;
                case S17PacketEntityLookMove s17packetentitylookmove:
                    if ((this.inBacktrackRange || dj) && this.watchdogBacktrack.wo() && !this.backtrackBlocked) {
                        this.heldPackets.add(s17packetentitylookmove);
                        var1x.setCancelled();
                    }
                    break;
                case aa aa:
                    if ((this.inBacktrackRange || dj) && this.watchdogBacktrack.wo() && !this.backtrackBlocked) {
                        this.heldPackets.add(aa);
                        var1x.setCancelled();
                    }
                    break;
                default:
            }

            List list = TargetComponent.f(12.0);
            list.sort(Comparator.comparingDouble(var0 -> ((EntityLivingBase)var0).hurtTime));
            if (list.isEmpty()) {
                this.target = null;
                if (this.watchdogBacktrack.wo()) {
                    releasing = true;
                    this.heldPackets
                        .stream()
                        .filter(
                            var1xx -> var1xx instanceof S16PacketEntityLook
                                || var1xx instanceof S15PacketEntityRelMove
                                || var1xx instanceof S17PacketEntityLookMove
                                || var1xx instanceof aa
                                || var1xx instanceof z
                        )
                        .forEach(PacketUtil::receive);
                    this.heldPackets.clear();
                    releasing = false;
                    this.inBacktrackRange = false;
                    this.backtrackBlocked = false;
                }
            } else {
                Entity entity = (Entity)list.get(0);
                if (this.watchdogBacktrack.wo()) {
                    if (entity != this.target) {
                        this.target = entity;
                        this.backtrackPos.xCoord = entity.posX;
                        this.backtrackPos.yCoord = entity.posY;
                        this.backtrackPos.zCoord = entity.posZ;
                    }

                    Vec3 vec3 = aEg.thePlayer.getPositionEyes(1.0F);
                    Vec3 vec31 = new Vec3(this.backtrackPos.xCoord, this.backtrackPos.yCoord, this.backtrackPos.zCoord);
                    double d0 = vec3.distanceTo(vec31);
                    if ((!(d0 > 4.5) || !dj) && (aEg.thePlayer.tR <= 9 || !dj)) {
                        if (dj) {
                            this.backtrackBlocked = false;
                        }
                    } else {
                        this.backtrackBlocked = true;
                    }

                    this.inBacktrackRange = d0 < 4.5;
                    if (aEg.thePlayer.isSwingInProgress && d0 > 3.1 && d0 < 4.5) {
                        this.e(KillAura.class).isEnabled();
                    }

                    if (this.inBacktrackRange) {
                        if (this.backtrackStart == -1L) {
                            this.backtrackStart = System.currentTimeMillis();
                        }

                        if (System.currentTimeMillis() - this.backtrackStart >= Math.round(Math.random() * 150.0) + 100L) {
                            releasing = true;
                            this.heldPackets
                                .stream()
                                .filter(
                                    var1xx -> var1xx instanceof S16PacketEntityLook
                                        || var1xx instanceof S15PacketEntityRelMove
                                        || var1xx instanceof S17PacketEntityLookMove
                                        || var1xx instanceof aa
                                        || var1xx instanceof z
                                )
                                .forEach(PacketUtil::receive);
                            this.heldPackets.clear();
                            releasing = false;
                            this.inBacktrackRange = false;
                            this.backtrackStart = -1L;
                        }
                    } else {
                        this.backtrackStart = -1L;
                    }

                    if (this.backtrackBlocked && dj) {
                        this.inBacktrackRange = false;
                        releasing = true;
                        this.heldPackets
                            .stream()
                            .filter(
                                var1xx -> var1xx instanceof S16PacketEntityLook
                                    || var1xx instanceof S15PacketEntityRelMove
                                    || var1xx instanceof S17PacketEntityLookMove
                                    || var1xx instanceof aa
                                    || var1xx instanceof z
                            )
                            .forEach(PacketUtil::receive);
                        this.heldPackets.clear();
                        releasing = false;
                    }

                    if (aEg.thePlayer.ticksExisted < 100) {
                        this.inBacktrackRange = false;
                        releasing = true;
                        this.heldPackets
                            .stream()
                            .filter(
                                var1xx -> var1xx instanceof S16PacketEntityLook
                                    || var1xx instanceof S15PacketEntityRelMove
                                    || var1xx instanceof S17PacketEntityLookMove
                                    || var1xx instanceof aa
                                    || var1xx instanceof z
                            )
                            .forEach(PacketUtil::receive);
                        this.heldPackets.clear();
                        releasing = false;
                    }

                    if (aEg.thePlayer.hurtTime == 9) {
                        this.inBacktrackRange = false;
                        releasing = true;
                        this.heldPackets
                            .stream()
                            .filter(
                                var1xx -> var1xx instanceof S16PacketEntityLook
                                    || var1xx instanceof S15PacketEntityRelMove
                                    || var1xx instanceof S17PacketEntityLookMove
                                    || var1xx instanceof aa
                                    || var1xx instanceof z
                            )
                            .forEach(PacketUtil::receive);
                        this.heldPackets.clear();
                        releasing = false;
                    }

                    if (d0 < 2.5) {
                        this.inBacktrackRange = false;
                        releasing = true;
                        this.heldPackets
                            .stream()
                            .filter(
                                var1xx -> var1xx instanceof S16PacketEntityLook
                                    || var1xx instanceof S15PacketEntityRelMove
                                    || var1xx instanceof S17PacketEntityLookMove
                                    || var1xx instanceof aa
                                    || var1xx instanceof z
                            )
                            .forEach(PacketUtil::receive);
                        this.heldPackets.clear();
                        releasing = false;
                    }

                    if (d0 > 4.5) {
                        this.inBacktrackRange = false;
                        releasing = true;
                        this.heldPackets
                            .stream()
                            .filter(
                                var1xx -> var1xx instanceof S16PacketEntityLook
                                    || var1xx instanceof S15PacketEntityRelMove
                                    || var1xx instanceof S17PacketEntityLookMove
                                    || var1xx instanceof aa
                                    || var1xx instanceof z
                            )
                            .forEach(PacketUtil::receive);
                        this.heldPackets.clear();
                        releasing = false;
                    }

                    Packet packet = var1x.getPacket();
                    if (packet instanceof S14PacketEntity s14packetentity) {
                        if (s14packetentity.entityId == this.target.getEntityId()) {
                            this.backtrackPos.xCoord = this.backtrackPos.xCoord + s14packetentity.agC() / 32.0;
                            this.backtrackPos.yCoord = this.backtrackPos.yCoord + s14packetentity.agD() / 32.0;
                            this.backtrackPos.zCoord = this.backtrackPos.zCoord + s14packetentity.agE() / 32.0;
                        }
                    } else if (packet instanceof z z && z.getEntityId() == this.target.getEntityId()) {
                        this.backtrackPos = new Vec3(z.we() / 32.0, z.wf() / 32.0, z.wi() / 32.0);
                    }
                }
            }
        }
    };
    @EventLink(value = 4)
    public final Listener<JumpEvent> onJumpVeryHigh = var0 -> {};
    @EventLink(value = 4)
    public final Listener<PreMotionEvent> onPreMotion = var1x -> {
        if (Math.abs(aEg.thePlayer.posY - Math.round(aEg.thePlayer.posY)) > 0.01 && aEg.thePlayer.onGround) {
            this.onOffsetGround = true;
        } else {
            this.onOffsetGround = false;
        }

        if (aEg.thePlayer.ae == 1
            && MoveUtil.isMoving()
            && (Math.abs(aEg.thePlayer.posY - Math.floor(aEg.thePlayer.posY)) < 0.03 || aEg.thePlayer.motionY < 0.0)
            && this.alwaysCancelVertical.wo()) {
            MoveUtil.strafe();
        } else if (aEg.thePlayer.ae == 1
            && (Math.abs(aEg.thePlayer.posY - Math.floor(aEg.thePlayer.posY)) < 0.03 || aEg.thePlayer.motionY < 0.0)
            && this.alwaysCancelVertical.wo()) {
            aEg.thePlayer.motionX *= -1.0;
            aEg.thePlayer.motionZ *= -1.0;
        }

        if (this.alwaysCancelVertical.wo() && aEg.thePlayer.ae < 5 && aEg.thePlayer.onGround) {
            aEg.thePlayer.crd = true;
        }
    };
    @EventLink(value = 4)
    public final Listener<PreUpdateEvent> onPreUpdate = var1x -> {
        if (this.inBacktrackRange && !dj && this.watchdogBacktrack.wo()) {
            BlinkComponent.a(50, true, false, false, false, false, false);
        }

        if (Breaker.bedBroken && dj) {
            dj = false;
            releasing = true;
            Vector2d vector2d = new Vector2d(aEg.thePlayer.motionX, aEg.thePlayer.motionZ);
            double d0 = aEg.thePlayer.motionY;
            this.heldPackets.forEach(PacketUtil::receive);
            this.heldPackets.clear();
            aEg.thePlayer.motionY = d0;
            aEg.thePlayer.motionX = vector2d.getX();
            aEg.thePlayer.motionZ = vector2d.getY();
            releasing = false;
        }

        if (aEg.thePlayer.onGround && dj && !this.e(Speed.class).isEnabled()) {
            dj = false;
            releasing = true;
            this.backtrackBlocked = false;
            BlinkComponent.dispatch();
            Vector2d vector2d1 = new Vector2d(aEg.thePlayer.motionX, aEg.thePlayer.motionZ);
            double d1 = aEg.thePlayer.motionY;
            this.heldPackets.forEach(PacketUtil::receive);
            this.heldPackets.clear();
            aEg.thePlayer.jump();
            aEg.thePlayer.motionX = vector2d1.getX();
            aEg.thePlayer.motionZ = vector2d1.getY();
            if (aEg.thePlayer.isJumping) {
                float f = aEg.thePlayer.pp * (float) (Math.PI / 180.0);
                Minecraft minecraft = Minecraft.getMinecraft();
                if (minecraft.thePlayer.bjQ) {
                    float f1 = (float)(MoveUtil.direction() * (180.0 / Math.PI));
                    f = f1 * (float) (Math.PI / 180.0);
                }

                minecraft.thePlayer.motionX = minecraft.thePlayer.motionX - MathHelper.sin(f) * 0.2F;
                minecraft.thePlayer.motionZ = minecraft.thePlayer.motionZ + MathHelper.cos(f) * 0.2F;
            }

            releasing = false;
        }
    };
    @EventLink(value = 2)
    public final Listener<JumpEvent> onJump = var1x -> {
        if (aEg.thePlayer.onGround && dj && this.e(Speed.class).isEnabled()) {
            dj = false;
            releasing = true;
            this.backtrackBlocked = false;
            BlinkComponent.dispatch();
            Vector2d vector2d = new Vector2d(aEg.thePlayer.motionX, aEg.thePlayer.motionZ);
            this.heldPackets.forEach(PacketUtil::receive);
            this.heldPackets.clear();
            aEg.thePlayer.motionX = vector2d.getX();
            aEg.thePlayer.motionZ = vector2d.getY();
            releasing = false;
        }
    };
    @EventLink(value = 2)
    public final Listener<PostStrafeEvent> onPostStrafe = var1x -> {
        if (aEg.thePlayer.tR > 12 && dj) {
            dj = false;
            releasing = true;
            Vector2d vector2d = new Vector2d(aEg.thePlayer.motionX, aEg.thePlayer.motionZ);
            BlinkComponent.dispatch();
            this.heldPackets.forEach(PacketUtil::receive);
            this.heldPackets.clear();
            if (!this.e(Speed.class).isEnabled()) {
                aEg.thePlayer.motionX = vector2d.getX();
                aEg.thePlayer.motionZ = vector2d.getY();
            }

            releasing = false;
        }
    };
    @EventLink
    public final Listener<Render3DEvent> onRender3D = var1x -> {
        if (this.target != null && this.watchdogBacktrack.wo() && (this.inBacktrackRange || dj)) {
            GlStateManager.pushMatrix();
            GlStateManager.pushAttrib();
            GlStateManager.enableBlend();
            GlStateManager.disableTexture2D();
            GlStateManager.disableLighting();
            GL11.glDepthMask(false);
            double d0 = -0.14;
            RenderUtil.color(ColorUtil.withAlpha(this.rz().rB(), 100));
            RenderUtil.drawBoundingBox(
                aEg.thePlayer
                    .getEntityBoundingBox()
                    .offset(-aEg.thePlayer.posX, -aEg.thePlayer.posY, -aEg.thePlayer.posZ)
                    .offset(this.backtrackPos.xCoord, this.backtrackPos.yCoord, this.backtrackPos.zCoord)
                    .expand(d0, -0.3, d0)
            );
            GlStateManager.enableTexture2D();
            GlStateManager.enableLighting();
            GlStateManager.disableBlend();
            GL11.glDepthMask(true);
            GlStateManager.popAttrib();
            GlStateManager.popMatrix();
            GlStateManager.resetColor();
        }
    };

    public WatchdogVelocity(String var1, Velocity velocity) {
        super(var1, velocity);
    }
}
