package com.alan.clients.module.impl.movement.jesus;

import com.alan.clients.component.impl.player.BlinkComponent;
import com.alan.clients.module.impl.movement.Flight;
import com.alan.clients.module.impl.movement.Jesus;
import com.alan.clients.module.impl.movement.LongJump;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.input.KeyboardInputEvent;
import com.alan.clients.newevent.impl.input.MoveInputEvent;
import com.alan.clients.newevent.impl.motion.JumpEvent;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.newevent.impl.motion.PreUpdateEvent;
import com.alan.clients.newevent.impl.motion.StrafeEvent;
import com.alan.clients.newevent.impl.packet.PacketReceiveEvent;
import com.alan.clients.util.player.MoveUtil;
import com.alan.clients.util.vector.Vector2d;
import com.alan.clients.value.Mode;
import com.alan.clients.util.packet.PacketUtil;
import com.alan.clients.component.impl.player.FallDistanceComponent;
import java.util.ArrayList;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S32PacketConfirmTransaction;

public class WatchdogDolphin118Jesus extends Mode<Jesus> {
    public static int outOfWaterTicks = 0;
    private boolean boostReady;
    private boolean keyCancelled;
    private int notInWaterTicks;
    private int velocityCount;
    @EventLink(value = 3)
    public final Listener<PreMotionEvent> onPreMotion = var1x -> {
        if (!this.e(LongJump.class).isEnabled() && !this.e(Flight.class).isEnabled() && outOfWaterTicks <= 30) {
            if (!aEg.thePlayer.inWater) {
                this.notInWaterTicks++;
            } else {
                MoveUtil.strafe();
                this.notInWaterTicks = 0;
            }

            if (!aEg.thePlayer.inWater && aEg.thePlayer.onGround) {
                outOfWaterTicks = 31;
            }

            if (aEg.thePlayer.ae == 0 && this.boostReady) {
                MoveUtil.strafe();
            } else if (aEg.thePlayer.ae == 0) {
                MoveUtil.strafe();
            }

            var1x.setSprinting(true);
            if (aEg.thePlayer.inWater) {
                outOfWaterTicks = 0;
            } else {
                outOfWaterTicks++;
                if (aEg.thePlayer.ae > 1 && outOfWaterTicks < 30) {
                    aEg.thePlayer.motionY += 0.0281;
                }
            }

            switch (aEg.thePlayer.ae) {
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                case 8:
                case 9:
                case 10:
                case 11:
                case 12:
                case 13:
                case 14:
                case 15:
                case 16:
                case 17:
                case 18:
                case 19:
                case 20:
            }
        }
    };
    private final ArrayList<Packet<?>> packets = new ArrayList<>();
    private boolean pendingVelocity;
    private boolean replayingPackets;
    private boolean boosting;
    int startSlot = -1;
    double startY;
    double velocityY = -1.0;
    private int hurtTicks;
    private int boostTicks;
    @EventLink
    public final Listener<StrafeEvent> onStrafe = var0 -> {
        if (aEg.thePlayer.inWater) {
            aEg.timer.dzD = 0.5F;
            PacketUtil.send(new C03PacketPlayer(true));
        }

        if (aEg.thePlayer.inWater) {
            BlinkComponent.a(100, true, true, false, false, false);
            var0.setSpeed(0.2);
        }

        double d0;
        int i = (d0 = MoveUtil.speed() - 0.205) == 0.0 ? 0 : (d0 < 0.0 ? -1 : 1);
    };
    @EventLink(value = 2)
    public final Listener<PacketReceiveEvent> onPacketReceive = var1x -> {
        if (!this.replayingPackets) {
            switch (var1x.getPacket()) {
                case S12PacketEntityVelocity s12packetentityvelocity:
                    if (!var1x.isCancelled()
                        && s12packetentityvelocity.getEntityID() == aEg.thePlayer.getEntityId()
                        && (outOfWaterTicks < 30 || aEg.thePlayer.inWater)
                        && (s12packetentityvelocity.getMotionY() / 8000.0 > 0.4 || s12packetentityvelocity.getMotionY() / 8000.0 < 0.1)) {
                        new Vector2d(aEg.thePlayer.motionX, aEg.thePlayer.motionZ);
                        this.velocityY = s12packetentityvelocity.getMotionZ() / 8000.0;
                        this.velocityY = s12packetentityvelocity.getMotionY() / 8000.0;
                        var1x.setCancelled();
                        this.pendingVelocity = true;
                        this.packets.add(s12packetentityvelocity);
                        if (this.velocityY > 0.2) {
                            this.velocityCount++;
                        }

                        if (this.velocityCount == 2) {
                            this.boostReady = true;
                        }
                    } else {
                        var1x.setCancelled();
                    }
                    break;
                case S32PacketConfirmTransaction s32packetconfirmtransaction:
                    if (this.pendingVelocity) {
                        this.packets.add(s32packetconfirmtransaction);
                        var1x.setCancelled();
                    }
                    break;
                default:
            }
        }
    };
    @EventLink
    public final Listener<MoveInputEvent> onMoveInput = var0 -> {
        if (aEg.thePlayer.inWater) {
            var0.setJump(true);
        }
    };
    @EventLink
    public final Listener<JumpEvent> onJump = var0 -> {};
    @EventLink
    public final Listener<KeyboardInputEvent> onKeyboardInput = var1x -> {
        if (var1x.getKeyCode() == this.getParent().getKey() && !this.keyCancelled) {
            var1x.setCancelled();
            this.keyCancelled = true;
        }
    };
    @EventLink(value = 4)
    public final Listener<PreUpdateEvent> onPreUpdate = var1x -> {
        if (this.pendingVelocity && (!aEg.thePlayer.inWater || this.boostReady) && outOfWaterTicks < 30) {
            aEg.thePlayer.ae = 1;
            this.boosting = true;
            this.pendingVelocity = false;
            this.replayingPackets = true;
            new Vector2d(aEg.thePlayer.motionX, aEg.thePlayer.motionZ);
            this.packets.forEach(PacketUtil::receive);
            this.boostReady = false;
            if (aEg.thePlayer.Zl > 20 && this.velocityY > 0.4) {
                aEg.thePlayer.motionX *= 1.23;
                aEg.thePlayer.motionZ *= 1.23;
            }

            this.packets.clear();
            this.replayingPackets = false;
        }
    };

    public WatchdogDolphin118Jesus(String var1, Jesus jesus) {
        super(var1, jesus);
    }

    @Override
    public void onEnable() {
        this.packets.forEach(PacketUtil::receive);
        this.packets.clear();
        FallDistanceComponent.cY = 0.0F;
    }

    @Override
    public void onDisable() {
        this.packets.forEach(PacketUtil::receive);
        this.packets.clear();
        this.boostReady = false;
        this.velocityCount = 0;
    }
}
