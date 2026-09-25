package com.alan.clients.module.impl.movement.longjump;

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
import java.util.Objects;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S32PacketConfirmTransaction;

public class WatchdogWater118LongJump extends Mode<LongJump>
{
    public int ticks;
    @EventLink
    public Listener<JumpEvent> onJump;
    public int previousSlot;
    @EventLink
    public Listener<StrafeEvent> onStrafe;
    @EventLink
    public Listener<MoveInputEvent> onMoveInput;
    public boolean replaying;
    public int airTicks;
    public boolean boosted;
    @EventLink(value = 4)
    public Listener<PreUpdateEvent> onPreUpdate;
    public boolean launched;
    @EventLink(value = 2)
    public Listener<PacketReceiveEvent> onPacketReceive;
    public ArrayList<Packet<?>> heldPackets;
    @EventLink(value = 3)
    public Listener<PreMotionEvent> onPreMotion;
    public boolean active;
    public int stage;
    @EventLink
    public Listener<KeyboardInputEvent> onKeyboardInput;
    public int velocityCount;
    public boolean keyConsumed;
    public double startY;
    public double velocityY;


    @Override
    public void onDisable() {
        this.heldPackets.forEach(PacketUtil::receive);
        this.heldPackets.clear();
        this.boosted = false;
        this.velocityCount = 0;
        MoveUtil.stop();
    }

    public WatchdogWater118LongJump(final String s, final LongJump longJump) {
        super(s, longJump);
        this.onPreMotion = (preMotionEvent -> {
            if (!WatchdogWater118LongJump.aEg.thePlayer.inWater) {
                ++this.airTicks;
            }
            else {
                MoveUtil.strafe();
                this.airTicks = 0;
            }
            if (WatchdogWater118LongJump.aEg.thePlayer.ae == 0 && this.boosted) {
                final EntityPlayerSP thePlayer = WatchdogWater118LongJump.aEg.thePlayer;
                thePlayer.motionX *= 4.1;
                final EntityPlayerSP thePlayer2 = WatchdogWater118LongJump.aEg.thePlayer;
                thePlayer2.motionZ *= 4.1;
                MoveUtil.strafe();
            }
            else if (WatchdogWater118LongJump.aEg.thePlayer.ae == 0) {
                MoveUtil.strafe();
            }
            preMotionEvent.setSprinting(true);
            if (!WatchdogWater118LongJump.aEg.thePlayer.inWater) {
                final EntityPlayerSP thePlayer3 = WatchdogWater118LongJump.aEg.thePlayer;
                thePlayer3.motionY += 0.0281;
            }
            final boolean inWater = WatchdogWater118LongJump.aEg.thePlayer.inWater;
            switch (WatchdogWater118LongJump.aEg.thePlayer.ae) {
                case 1: {}
                case 7: {
                    WatchdogWater118LongJump.aEg.thePlayer.motionY = 0.03;
                    break;
                }
                case 8: {
                    WatchdogWater118LongJump.aEg.thePlayer.motionY = 0.03;
                }
                case 9: {}
                case 11: {
                    final EntityPlayerSP thePlayer4 = WatchdogWater118LongJump.aEg.thePlayer;
                    thePlayer4.motionY += 0.01;
                    break;
                }
            }
            return;
        });
        this.heldPackets = new ArrayList<Packet<?>>();
        this.previousSlot = -1;
        this.velocityY = -1.0;
        this.onStrafe = (p0 -> {
            if (WatchdogWater118LongJump.aEg.thePlayer.inWater) {
                MoveUtil.stop();
                WatchdogWater118LongJump.aEg.timer.dzD = 0.33f;
                PacketUtil.send(new C03PacketPlayer(true));
                PacketUtil.send(new C03PacketPlayer.C04PacketPlayerPosition(WatchdogWater118LongJump.aEg.thePlayer.posX, WatchdogWater118LongJump.aEg.thePlayer.posY + 0.068, WatchdogWater118LongJump.aEg.thePlayer.posZ, true));
            }
            final boolean b = MoveUtil.speed() < 0.205;
            return;
        });
        this.onPacketReceive = (packetReceiveEvent -> {
            if (this.replaying) {
                return;
            }
            else {
                final Packet packet = packetReceiveEvent.getPacket();
                Objects.requireNonNull(packet);
                switch (packet) {
                    case S12PacketEntityVelocity s12PacketEntityVelocity: {
                        if (!packetReceiveEvent.isCancelled() && s12PacketEntityVelocity.getEntityID() == WatchdogWater118LongJump.aEg.thePlayer.getEntityId()) {
                            new Vector2d(WatchdogWater118LongJump.aEg.thePlayer.motionX, WatchdogWater118LongJump.aEg.thePlayer.motionZ);
                            this.velocityY = s12PacketEntityVelocity.getMotionZ() / 8000.0;
                            this.velocityY = s12PacketEntityVelocity.getMotionY() / 8000.0;
                            packetReceiveEvent.setCancelled();
                            this.active = true;
                            this.heldPackets.add(s12PacketEntityVelocity);
                            break;
                        }
                        else {
                            break;
                        }
                    }
                    case S32PacketConfirmTransaction s32PacketConfirmTransaction: {
                        if (this.active) {
                            this.heldPackets.add(s32PacketConfirmTransaction);
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
            }
        });
        this.onMoveInput = (moveInputEvent -> {
            if (WatchdogWater118LongJump.aEg.thePlayer.inWater) {
                moveInputEvent.setJump(true);
            }
            return;
        });
        this.onJump = (p0 -> {});
        this.onKeyboardInput = (keyboardInputEvent -> {
            if (keyboardInputEvent.getKeyCode() == this.getParent().getKey() && !this.keyConsumed) {
                keyboardInputEvent.setCancelled();
                this.keyConsumed = true;
            }
            return;
        });
        this.onPreUpdate = (p0 -> {
            if (this.active && !WatchdogWater118LongJump.aEg.thePlayer.inWater) {
                WatchdogWater118LongJump.aEg.thePlayer.ae = 0;
                this.launched = true;
                this.active = false;
                this.replaying = true;
                new Vector2d(WatchdogWater118LongJump.aEg.thePlayer.motionX, WatchdogWater118LongJump.aEg.thePlayer.motionZ);
                this.heldPackets.forEach(PacketUtil::receive);
                if (this.velocityY > 0.41) {
                    ++this.velocityCount;
                }
                if (this.velocityCount == 2) {
                    this.boosted = true;
                }
                this.heldPackets.clear();
                if (!this.boosted) {
                    final EntityPlayerSP thePlayer5 = WatchdogWater118LongJump.aEg.thePlayer;
                    thePlayer5.motionX *= 1.3;
                    final EntityPlayerSP thePlayer6 = WatchdogWater118LongJump.aEg.thePlayer;
                    thePlayer6.motionZ *= 1.3;
                }
                else {
                    final EntityPlayerSP thePlayer7 = WatchdogWater118LongJump.aEg.thePlayer;
                    thePlayer7.motionX *= 1.1;
                    final EntityPlayerSP thePlayer8 = WatchdogWater118LongJump.aEg.thePlayer;
                    thePlayer8.motionZ *= 1.1;
                }
                this.replaying = false;
            }
        });
    }

    static {
    }

    @Override
    public void onEnable() {
        this.heldPackets.forEach(PacketUtil::receive);
        this.heldPackets.clear();
        if (WatchdogWater118LongJump.aEg.thePlayer.onGround) {
            MoveUtil.stop();
        }
        FallDistanceComponent.cY = 0.0f;
    }

}
