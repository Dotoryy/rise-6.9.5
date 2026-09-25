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
import com.alan.clients.component.impl.combat.TargetComponent;
import java.awt.Color;
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

public final class VulcanVelocity extends Mode<Velocity> {
    private long backtrackStartTime = -1L;
    boolean inBacktrackRange = false;
    boolean pendingVelocity;
    boolean backtrackBlocked = false;
    public final BooleanValue stack = new BooleanValue("Stack", this, true);
    boolean vh;
    public final BooleanValue pingSpoof = new BooleanValue("Ping Spoof", this, true);
    private double velocityX;
    private double velocityZ;
    private double velocityY;
    public final BooleanValue alwaysCancelVertical = new BooleanValue("Always Cancel Vertical", this, true);
    public final BooleanValue backtrack = new BooleanValue("Backtrack", this, false);
    public final BooleanValue damageBoost = new BooleanValue("Damage Boost", this, false);
    private final NumberValue damageBoostSpeed = new NumberValue("Damage Boost Speed", this, 1, 1, 10, 0.01);
    private boolean blinking;
    private boolean releasing;
    private boolean cancelling;
    private Vec3 targetPosition = new Vec3(0.0, 0.0, 0.0);
    public Entity target;
    private int stackedVelocities;
    private final ArrayList<Packet<?>> heldPackets = new ArrayList<>();
    @EventLink
    public final Listener<PacketReceiveEvent> onPacketReceiveEvent = var1x -> {
        if (!this.releasing
            && !this.e(LongJump.class).isEnabled()
            && !this.e(Flight.class).isEnabled()
            && (
                !this.e(Jesus.class).isEnabled()
                    || !this.e(Jesus.class).mode.wo().getName().equals("Watchdog Dolphin 1.18+")
                    || WatchdogDolphin118Jesus.outOfWaterTicks >= 30
            )
            && !this.e(Speed.class).isEnabled()) {
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
                                || this.vh
                                || !(s12packetentityvelocity.getMotionY() / 8000.0 > 0.08)
                                || aEg.thePlayer.Zl <= 11
                        )) {
                        if (this.stack.wo() && this.stackedVelocities < 1 && !aEg.thePlayer.onGround && Math.random() < 0.73 && aEg.thePlayer.tR <= 13
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
                            this.stackedVelocities++;
                            this.cancelling = true;
                            var1x.setCancelled();
                            this.cancelling = false;
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
                            this.stackedVelocities = 0;
                            this.blinking = true;
                            this.heldPackets.add(s12packetentityvelocity);
                            var1x.setCancelled();
                        }
                    } else if (!var1x.isCancelled() && PlayerUtil.vk()) {
                        var1x.setCancelled();
                    } else if (!var1x.isCancelled()
                        && s12packetentityvelocity.getEntityID() == aEg.thePlayer.getEntityId()
                        && !this.e(LongJump.class).isEnabled()) {
                        this.pendingVelocity = true;
                        this.heldPackets.add(s12packetentityvelocity);
                        this.velocityX = s12packetentityvelocity.getMotionX() / 8000.0;
                        this.velocityZ = s12packetentityvelocity.getMotionZ() / 8000.0;
                        this.velocityY = s12packetentityvelocity.getMotionY() / 8000.0;
                        var1x.setCancelled();
                    }
                    break;
                case S32PacketConfirmTransaction s32packetconfirmtransaction:
                    if (this.blinking && this.pingSpoof.wo()) {
                        this.heldPackets.add(s32packetconfirmtransaction);
                        var1x.setCancelled();
                    }
                    break;
                case a a:
                    if (this.blinking && this.pingSpoof.wo()) {
                        var1x.setCancelled();
                    }
                    break;
                case S16PacketEntityLook s16packetentitylook:
                    if ((this.inBacktrackRange || this.blinking) && this.backtrack.wo() && !this.backtrackBlocked) {
                        this.heldPackets.add(s16packetentitylook);
                        var1x.setCancelled();
                    }
                    break;
                case S15PacketEntityRelMove s15packetentityrelmove:
                    if ((this.inBacktrackRange || this.blinking) && this.backtrack.wo() && !this.backtrackBlocked) {
                        this.heldPackets.add(s15packetentityrelmove);
                        var1x.setCancelled();
                    }
                    break;
                case S17PacketEntityLookMove s17packetentitylookmove:
                    if ((this.inBacktrackRange || this.blinking) && this.backtrack.wo() && !this.backtrackBlocked) {
                        this.heldPackets.add(s17packetentitylookmove);
                        var1x.setCancelled();
                    }
                    break;
                case aa aa:
                    if ((this.inBacktrackRange || this.blinking) && this.backtrack.wo() && !this.backtrackBlocked) {
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
                if (this.backtrack.wo()) {
                    this.releasing = true;
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
                    this.releasing = false;
                    this.inBacktrackRange = false;
                    this.backtrackBlocked = false;
                }
            } else {
                Entity entity = (Entity)list.get(0);
                if (this.backtrack.wo()) {
                    if (entity != this.target) {
                        this.target = entity;
                        this.targetPosition.xCoord = entity.posX;
                        this.targetPosition.yCoord = entity.posY;
                        this.targetPosition.zCoord = entity.posZ;
                    }

                    Vec3 vec3 = aEg.thePlayer.getPositionEyes(1.0F);
                    Vec3 vec31 = new Vec3(this.targetPosition.xCoord, this.targetPosition.yCoord, this.targetPosition.zCoord);
                    double d0 = vec3.distanceTo(vec31);
                    if ((!(d0 > 4.5) || !this.blinking) && (aEg.thePlayer.tR <= 9 || !this.blinking)) {
                        if (this.blinking) {
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
                        if (this.backtrackStartTime == -1L) {
                            this.backtrackStartTime = System.currentTimeMillis();
                        }

                        if (System.currentTimeMillis() - this.backtrackStartTime >= Math.round(Math.random() * 150.0) + 100L) {
                            this.releasing = true;
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
                            this.releasing = false;
                            this.inBacktrackRange = false;
                            this.backtrackStartTime = -1L;
                        }
                    } else {
                        this.backtrackStartTime = -1L;
                    }

                    if (this.backtrackBlocked && this.blinking) {
                        this.inBacktrackRange = false;
                        this.releasing = true;
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
                        this.releasing = false;
                    }

                    if (aEg.thePlayer.ticksExisted < 100) {
                        this.inBacktrackRange = false;
                        this.releasing = true;
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
                        this.releasing = false;
                    }

                    if (aEg.thePlayer.hurtTime == 9) {
                        this.inBacktrackRange = false;
                        this.releasing = true;
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
                        this.releasing = false;
                    }

                    if (d0 < 2.5) {
                        this.inBacktrackRange = false;
                        this.releasing = true;
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
                        this.releasing = false;
                    }

                    if (d0 > 4.5) {
                        this.inBacktrackRange = false;
                        this.releasing = true;
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
                        this.releasing = false;
                    }

                    Packet packet = var1x.getPacket();
                    if (packet instanceof S14PacketEntity s14packetentity) {
                        if (s14packetentity.entityId == this.target.getEntityId()) {
                            this.targetPosition.xCoord = this.targetPosition.xCoord + s14packetentity.agC() / 32.0;
                            this.targetPosition.yCoord = this.targetPosition.yCoord + s14packetentity.agD() / 32.0;
                            this.targetPosition.zCoord = this.targetPosition.zCoord + s14packetentity.agE() / 32.0;
                        }
                    } else if (packet instanceof z z && z.getEntityId() == this.target.getEntityId()) {
                        this.targetPosition = new Vec3(z.we() / 32.0, z.wf() / 32.0, z.wi() / 32.0);
                    }
                }
            }
        }
    };
    @EventLink(value = 4)
    public final Listener<JumpEvent> onJump = var0 -> {};
    @EventLink(value = 4)
    public final Listener<PreMotionEvent> onPreMotion = var1x -> {
        if ((aEg.thePlayer.ae != 1 || !MoveUtil.isMoving()) && aEg.thePlayer.ae == 1) {
            aEg.thePlayer.motionX *= -1.0;
            aEg.thePlayer.motionZ *= -1.0;
        }

        if (this.pendingVelocity && aEg.thePlayer.onGround) {
            this.pendingVelocity = false;
            this.releasing = true;
            Vector2d vector2d = new Vector2d(aEg.thePlayer.motionX, aEg.thePlayer.motionZ);
            double d0 = aEg.thePlayer.motionY;
            this.heldPackets.forEach(PacketUtil::receive);
            this.heldPackets.clear();
            aEg.thePlayer.motionY = d0;
            aEg.thePlayer.motionX = vector2d.getX();
            aEg.thePlayer.motionZ = vector2d.getY();
            var1x.setPosY(var1x.getPosY() + this.velocityY);
            this.releasing = false;
        }
    };
    @EventLink(value = 4)
    public final Listener<PreUpdateEvent> onPreUpdate = var1x -> {
        if (Breaker.bedBroken && this.blinking) {
            this.blinking = false;
            this.releasing = true;
            Vector2d vector2d = new Vector2d(aEg.thePlayer.motionX, aEg.thePlayer.motionZ);
            double d0 = aEg.thePlayer.motionY;
            this.heldPackets.forEach(PacketUtil::receive);
            this.heldPackets.clear();
            aEg.thePlayer.motionY = d0;
            aEg.thePlayer.motionX = vector2d.getX();
            aEg.thePlayer.motionZ = vector2d.getY();
            this.releasing = false;
        }

        if (aEg.thePlayer.onGround && this.blinking && !this.pendingVelocity && !this.e(Speed.class).isEnabled()) {
            this.blinking = false;
            this.releasing = true;
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

            this.releasing = false;
        }

        if (this.inBacktrackRange && !this.blinking && this.backtrack.wo()) {
            BlinkComponent.a(50, true, false, false, false, true, false);
        }
    };
    @EventLink(value = 2)
    public final Listener<PostStrafeEvent> onPostStrafe = var1x -> {
        if (aEg.thePlayer.tR > 12 && this.blinking) {
            this.blinking = false;
            this.releasing = true;
            Vector2d vector2d = new Vector2d(aEg.thePlayer.motionX, aEg.thePlayer.motionZ);
            BlinkComponent.dispatch();
            this.heldPackets.forEach(PacketUtil::receive);
            this.heldPackets.clear();
            if (!this.e(Speed.class).isEnabled()) {
                aEg.thePlayer.motionX = vector2d.getX();
                aEg.thePlayer.motionZ = vector2d.getY();
            }

            this.releasing = false;
        }
    };
    @EventLink
    public final Listener<Render3DEvent> onRender3D = var1x -> {
        if (this.target != null && this.backtrack.wo() && (this.inBacktrackRange || this.blinking)) {
            GlStateManager.pushMatrix();
            GlStateManager.pushAttrib();
            GlStateManager.enableBlend();
            GlStateManager.disableTexture2D();
            GlStateManager.disableLighting();
            GL11.glDepthMask(false);
            double d0 = -0.14;
            RenderUtil.color(Color.red, 45);
            RenderUtil.drawBoundingBox(
                aEg.thePlayer
                    .getEntityBoundingBox()
                    .offset(-aEg.thePlayer.posX, -aEg.thePlayer.posY, -aEg.thePlayer.posZ)
                    .offset(this.targetPosition.xCoord, this.targetPosition.yCoord, this.targetPosition.zCoord)
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

    public VulcanVelocity(String var1, Velocity velocity) {
        super(var1, velocity);
    }
}
