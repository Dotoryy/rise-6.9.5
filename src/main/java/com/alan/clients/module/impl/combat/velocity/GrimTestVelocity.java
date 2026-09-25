package com.alan.clients.module.impl.combat.velocity;

import com.alan.clients.component.impl.player.BlinkComponent;
import com.alan.clients.module.impl.combat.KillAura;
import com.alan.clients.module.impl.combat.Velocity;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.input.MoveInputEvent;
import com.alan.clients.newevent.impl.motion.PreUpdateEvent;
import com.alan.clients.newevent.impl.packet.PacketReceiveEvent;
import com.alan.clients.newevent.impl.packet.PacketSendEvent;
import com.alan.clients.value.Mode;
import com.alan.clients.value.impl.BooleanValue;
import com.alan.clients.value.impl.ModeValue;
import com.alan.clients.value.impl.NumberValue;
import com.alan.clients.value.impl.SubMode;
import com.alan.clients.util.chat.ChatUtil;
import com.alan.clients.util.packet.PacketUtil;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.block.BlockWeb;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer.C05PacketPlayerLook;
import net.minecraft.network.play.client.C03PacketPlayer.C06PacketPlayerPosLook;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0BPacketEntityAction.Action;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.server.S06PacketUpdateHealth;
import net.minecraft.network.play.server.S07PacketRespawn;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S23PacketBlockChange;
import net.minecraft.network.play.server.S29PacketSoundEffect;
import net.minecraft.network.play.server.S3EPacketTeams;
import net.minecraft.network.play.server.a;
import net.minecraft.network.play.server.ab;
import net.minecraft.network.play.server.bt;
import net.minecraft.network.play.server.c;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

public final class GrimTestVelocity extends Mode<Velocity> {
    private final ModeValue mode = new ModeValue("Type", this)
        .add(new SubMode("Reduce"))
        .add(new SubMode("Jump Reset"))
        .add(new SubMode("Grim Full"))
        .setDefault("Reduce");
    private final NumberValue minAttacks = new NumberValue("Min Attacks", this, 2, 1, 5, 1, () -> !this.u("Reduce"));
    private final NumberValue maxAttacks = new NumberValue("Max Attacks", this, 3, 1, 10, 1, () -> !this.u("Reduce"));
    private final BooleanValue delayRelease = new BooleanValue("Delay Release", this, false, () -> !this.u("Reduce"));
    private final NumberValue delaySpeedThreshold = new NumberValue("Delay Speed Threshold", this, 0.6, 0.1, 2, 0.05, () -> !this.u("Reduce") || !this.delayRelease.wo());
    private final NumberValue delayTimeoutMs = new NumberValue("Delay Timeout (ms)", this, 5000, 50, 10000, 50, () -> !this.u("Reduce") || !this.delayRelease.wo());
    private final BooleanValue delayLogging = new BooleanValue("Delay Logging", this, false, () -> !this.u("Reduce") || !this.delayRelease.wo());
    private final NumberValue jumpResetTick = new NumberValue("Jump Reset Tick", this, 1, 0, 9, 1, () -> !this.u("Jump Reset"));
    private final NumberValue grimLevel = new NumberValue("Grim Level", this, 0.001, 0.001, 0.1, 0.001, () -> !this.u("Grim Full"));
    private final BooleanValue logging = new BooleanValue("Logging", this, true);
    private Entity target;
    private boolean velocityReceived;
    private int jumpResetCountdown;
    private double knockbackSpeed;
    private int pendingAttacks;
    private boolean hurtReceived;
    private long enableTime;
    private boolean wasNotSprinting;
    private int attackCount;
    private final Random random = new Random();
    private final Queue<Packet<?>> delayedPackets = new ConcurrentLinkedQueue<>();
    private boolean delaying;
    private Vec3 pendingVelocity;
    private long delayStart;
    private boolean velocityApplied;
    private boolean skipNextDelay;
    @EventLink
    public final Listener<PacketSendEvent> onPacketSend = var1x -> {
        if (aEg.thePlayer != null && aEg.theWorld != null) {
            if (this.u("Grim Full")) {
                if (var1x.dq() instanceof C03PacketPlayer c03packetplayer && c03packetplayer.afG()) {
                    float f = c03packetplayer.getYaw();
                    float f1 = c03packetplayer.getPitch();
                    if (this.h(f) || this.h(f1)) {
                        float f2 = this.grimLevel.wo().floatValue();
                        float f3 = f + (this.random.nextBoolean() ? 1 : -1) * f2;
                        float f4 = f1 + (this.random.nextBoolean() ? 1 : -1) * f2;
                        if (c03packetplayer instanceof C06PacketPlayerPosLook) {
                            var1x.setPacket(
                                new C06PacketPlayerPosLook(
                                    c03packetplayer.afD(), c03packetplayer.afE(), c03packetplayer.afF(), f3, f4, c03packetplayer.isOnGround()
                                )
                            );
                        } else if (c03packetplayer instanceof C05PacketPlayerLook) {
                            var1x.setPacket(new C05PacketPlayerLook(f3, f4, c03packetplayer.isOnGround()));
                        }
                    }
                }
            }
        }
    };
    @EventLink
    public final Listener<PacketReceiveEvent> onPacketReceive = var1x -> {
        if (aEg.thePlayer != null && aEg.theWorld != null) {
            Packet packet = var1x.getPacket();
            if (this.u("Grim Full")) {
                if (packet instanceof S12PacketEntityVelocity s12packetentityvelocity && s12packetentityvelocity.getEntityID() == aEg.thePlayer.getEntityId()) {
                    var1x.setCancelled();
                    return;
                }

                if (packet instanceof S08PacketPlayerPosLook s08packetplayerposlook) {
                    aEg.thePlayer.setPosition(s08packetplayerposlook.getX(), s08packetplayerposlook.getY(), s08packetplayerposlook.getZ());
                    aEg.thePlayer.setVelocity(0.0, 0.0, 0.0);
                    var1x.setCancelled();
                    return;
                }
            }

            if (this.u("Reduce") && this.delayRelease.wo() && packet instanceof S08PacketPlayerPosLook && !this.delaying) {
                this.skipNextDelay = true;
                this.t("Flag detected, skipping next delay.");
            }

            if (packet instanceof ab ab && ab.getEntity(aEg.theWorld) == aEg.thePlayer && ab.getOpCode() == 2) {
                this.hurtReceived = true;
            }

            if (packet instanceof S12PacketEntityVelocity s12packetentityvelocity1) {
                if (s12packetentityvelocity1.getEntityID() != aEg.thePlayer.getEntityId()) {
                    return;
                }

                this.velocityReceived = true;
                KillAura killaura = this.e(KillAura.class);
                this.target = killaura != null ? killaura.jE : null;
                double d0 = s12packetentityvelocity1.getMotionX() / 8000.0;
                double d1 = s12packetentityvelocity1.getMotionY() / 8000.0;
                double d2 = s12packetentityvelocity1.getMotionZ() / 8000.0;
                double d3 = Math.sqrt(d0 * d0 + d2 * d2);
                this.knockbackSpeed = d3;
                if (this.u("Reduce")) {
                    if (this.hurtReceived) {
                        this.hurtReceived = false;
                        if (this.isInLiquidOrWeb()) {
                            return;
                        }

                        if (System.currentTimeMillis() - this.enableTime < 1000L) {
                            return;
                        }

                        if (d3 < 0.1) {
                            return;
                        }

                        if (!aEg.thePlayer.isSprinting()) {
                            return;
                        }

                        if (this.delayRelease.wo() && !this.skipNextDelay) {
                            if (d3 >= this.delaySpeedThreshold.wo().doubleValue()) {
                                var1x.setCancelled();
                                this.delaying = true;
                                this.pendingVelocity = new Vec3(d0, d1, d2);
                                this.delayStart = System.currentTimeMillis();
                                this.t("Strong KB (speed: " + String.format("%.3f", d3) + "), delay started.");
                                return;
                            }

                            this.t("Weak KB (speed: " + String.format("%.3f", d3) + "), normal reduce.");
                        }

                        if (this.skipNextDelay) {
                            this.skipNextDelay = false;
                            this.t("Flagged, using normal reduce this time.");
                        }

                        this.pendingAttacks = this.getRandomAttackCount();
                        this.wasNotSprinting = !aEg.thePlayer.isSprinting();
                    }
                } else if (this.u("Jump Reset")) {
                    this.jumpResetCountdown = this.jumpResetTick.wo().intValue();
                }
            }

            if (this.delaying) {
                if (packet instanceof S23PacketBlockChange || packet instanceof S29PacketSoundEffect || packet instanceof c) {
                    return;
                }

                if (packet instanceof S3EPacketTeams || packet instanceof a) {
                    return;
                }

                if (packet instanceof S06PacketUpdateHealth s06packetupdatehealth) {
                    if (s06packetupdatehealth.getHealth() <= 0.0F) {
                        this.t("Player died, releasing all packets.");
                        this.endDelay(true);
                    }

                    return;
                }

                if (packet instanceof bt || packet instanceof S07PacketRespawn || packet instanceof S08PacketPlayerPosLook) {
                    this.t("Critical packet received, releasing all packets.");
                    this.endDelay(true);
                    return;
                }

                this.delayedPackets.add(packet);
                var1x.setCancelled();
            }
        }
    };
    @EventLink
    public final Listener<PreUpdateEvent> onPreUpdate = var1x -> {
        if (aEg.thePlayer != null) {
            if (aEg.thePlayer.hurtTime == 0) {
                this.velocityReceived = false;
                this.knockbackSpeed = 0.0;
            }

            if (this.jumpResetCountdown > 0) {
                this.jumpResetCountdown--;
            }

            if (this.delaying && this.pendingVelocity != null) {
                MovingObjectPosition movingobjectposition = aEg.objectMouseOver;
                if (movingobjectposition != null && movingobjectposition.entityHit instanceof EntityPlayer entityplayer) {
                    int i = this.getRandomAttackCount();
                    double d0 = 1.0;

                    for (int j = 0; j < i; j++) {
                        if (aEg.thePlayer.isSprinting()) {
                            aEg.thePlayer.setSprinting(false);
                        }

                        aEg.playerController.attackEntity(aEg.thePlayer, entityplayer);
                        aEg.thePlayer.swingItem();
                        d0 *= 0.6;
                    }

                    aEg.thePlayer.setVelocity(this.pendingVelocity.xCoord * d0, this.pendingVelocity.yCoord, this.pendingVelocity.zCoord * d0);
                    this.t("Target acquired, attacks: " + i + ", KB reduced to " + String.format("%.1f%%", d0 * 100.0));
                    this.velocityApplied = true;
                }

                if (!this.velocityApplied) {
                    long k = System.currentTimeMillis() - this.delayStart;
                    if (k >= this.delayTimeoutMs.wo().longValue()) {
                        aEg.thePlayer.setVelocity(this.pendingVelocity.xCoord, this.pendingVelocity.yCoord, this.pendingVelocity.zCoord);
                        this.t("Timeout (" + k + "ms), applying full KB.");
                        this.velocityApplied = true;
                    }
                }

                if (this.velocityApplied) {
                    this.endDelay(true);
                }
            }

            if (this.skipNextDelay && aEg.thePlayer.hurtTime == 0) {
                this.skipNextDelay = false;
            }

            if (this.u("Reduce") && this.target != null && this.pendingAttacks > 0) {
                if (this.wasNotSprinting) {
                    aEg.getNetHandler().addToSendQueue(new C0BPacketEntityAction(aEg.thePlayer, Action.START_SPRINTING));
                }

                while (this.pendingAttacks >= 1) {
                    aEg.getNetHandler().addToSendQueue(new C02PacketUseEntity(this.target, net.minecraft.network.play.client.C02PacketUseEntity.Action.ATTACK));
                    aEg.thePlayer.swingItem();
                    aEg.thePlayer.setVelocity(aEg.thePlayer.motionX * 0.6, aEg.thePlayer.motionY, aEg.thePlayer.motionZ * 0.6);
                    aEg.thePlayer.onGround = false;
                    this.pendingAttacks--;
                }

                if (this.wasNotSprinting) {
                    aEg.getNetHandler().addToSendQueue(new C0BPacketEntityAction(aEg.thePlayer, Action.STOP_SPRINTING));
                    this.wasNotSprinting = false;
                }
            }
        }
    };
    @EventLink
    public final Listener<MoveInputEvent> onMoveInput = var1x -> {
        if (aEg.thePlayer != null && this.u("Jump Reset") && aEg.thePlayer.onGround && this.jumpResetCountdown == 1) {
            var1x.setJump(true);
            this.jumpResetCountdown = 0;
            var1x.setForward(1.0F);
        }
    };

    public GrimTestVelocity(String var1, Velocity velocity) {
        super(var1, velocity);
    }

    @Override
    public void onEnable() {
        this.enableTime = System.currentTimeMillis();
    }

    @Override
    public void onDisable() {
        this.velocityReceived = false;
        this.jumpResetCountdown = 0;
        this.target = null;
        this.knockbackSpeed = 0.0;
        this.pendingAttacks = 0;
        this.hurtReceived = false;
        this.wasNotSprinting = false;
        this.endDelay(true);
    }

    private void endDelay(boolean var1) {
        this.delaying = false;
        if (var1) {
            BlinkComponent.dispatch();

            while (!this.delayedPackets.isEmpty()) {
                Packet packet = this.delayedPackets.poll();
                if (packet != null) {
                    PacketUtil.receive(packet);
                }
            }
        } else {
            this.delayedPackets.clear();
        }

        this.pendingVelocity = null;
        this.velocityApplied = false;
        this.delayStart = 0L;
    }

    private void t(String var1) {
        if (this.logging.wo() && this.delayLogging.wo()) {
            ChatUtil.d("%s", "[Delay] " + var1);
        }
    }

    private boolean h(float var1) {
        float f = Math.abs(var1 % 90.0F);
        return f < 0.01F || f > 89.99F;
    }

    private int getRandomAttackCount() {
        int i = this.minAttacks.wo().intValue();
        int j = this.maxAttacks.wo().intValue();
        if (i < 1) {
            i = 1;
        }

        if (j < 1) {
            j = 1;
        }

        if (i > j) {
            int k = i;
            i = j;
            j = k;
        }

        if (i == j) {
            this.attackCount = i;
            return i;
        }
        this.attackCount = ThreadLocalRandom.current().nextInt(i, j + 1);
        return this.attackCount;
    }

    private boolean isInLiquidOrWeb() {
        if (aEg.thePlayer == null || aEg.theWorld == null) {
            return false;
        }

        if (aEg.thePlayer.isInWater() || aEg.thePlayer.isInLava()) {
            return true;
        }

        if (aEg.thePlayer.isOnLadder()) {
            return true;
        }

        BlockPos blockpos = aEg.thePlayer.getPosition();
        return aEg.theWorld.getBlockState(blockpos).getBlock() instanceof BlockWeb;
    }

    private boolean u(String var1) {
        return this.mode.wo() != null && this.mode.wo().getName().equalsIgnoreCase(var1);
    }
}
