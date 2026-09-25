package com.alan.clients.module.impl.movement.flight;

import com.alan.clients.Client;
import com.alan.clients.component.impl.player.BlinkComponent;
import com.alan.clients.component.impl.player.RotationComponent;
import com.alan.clients.component.impl.player.rotationcomponent.MovementFix;
import com.alan.clients.module.impl.combat.KillAura;
import com.alan.clients.module.impl.movement.Flight;
import com.alan.clients.module.impl.movement.LongJump;
import com.alan.clients.module.impl.player.AntiFireBall;
import com.alan.clients.module.impl.player.Scaffold;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PostMotionEvent;
import com.alan.clients.newevent.impl.motion.PostStrafeEvent;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.newevent.impl.motion.StrafeEvent;
import com.alan.clients.newevent.impl.packet.PacketReceiveEvent;
import com.alan.clients.newevent.impl.packet.PacketSendEvent;
import com.alan.clients.util.player.MoveUtil;
import com.alan.clients.util.vector.Vector2d;
import com.alan.clients.util.vector.Vector2f;
import com.alan.clients.value.Mode;
import com.alan.clients.util.chat.ChatUtil;
import com.alan.clients.util.packet.PacketUtil;
import com.alan.clients.component.impl.player.PacketlessDamageComponent;
import java.util.ArrayList;
import net.minecraft.init.Items;
import net.minecraft.item.ItemFireball;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S32PacketConfirmTransaction;
import net.minecraft.potion.Potion;
import net.minecraft.util.MathHelper;
import rip.vantage.commons.util.time.StopWatch;

public class WatchdogFlight extends Mode<Flight> {
    private int previousSlot = -1;
    private int boostTicks = -1;
    private boolean velocityReceived;
    public static boolean active;
    private boolean placedFireball;
    private int stage;
    private boolean waitingForVelocity;
    private AntiFireBall antiFireBall;
    private final ArrayList<Packet<?>> packets = new ArrayList<>();
    StopWatch stopwatch = new StopWatch();
    private boolean pendingVelocity;
    private boolean replayingPackets;
    private boolean boosting;
    int startSlot = -1;
    double speed = 0.0;
    double savedMotionY = -1.0;
    private int hurtTicks;
    @EventLink(value = 1)
    public final Listener<PacketReceiveEvent> receive = var1x -> {
        if (!this.replayingPackets) {
            if (aEg.thePlayer != null && aEg.theWorld != null) {
                if (var1x.getPacket() instanceof S12PacketEntityVelocity) {
                    if (((S12PacketEntityVelocity)var1x.getPacket()).getEntityID() != aEg.thePlayer.getEntityId()) {
                        return;
                    }

                    if (this.waitingForVelocity) {
                        this.boostTicks = 0;
                        this.velocityReceived = true;
                        this.waitingForVelocity = false;
                        active = true;
                    }
                }

                switch (var1x.getPacket()) {
                    case S12PacketEntityVelocity s12packetentityvelocity:
                        if (!var1x.isCancelled() && s12packetentityvelocity.getEntityID() == aEg.thePlayer.getEntityId() && !this.boosting) {
                            this.savedMotionY = aEg.thePlayer.motionY;
                            Vector2d vector2d = new Vector2d(aEg.thePlayer.motionX, aEg.thePlayer.motionZ);
                            aEg.thePlayer.motionX = vector2d.getX();
                            aEg.thePlayer.motionZ = vector2d.getY();
                            aEg.thePlayer.motionY = this.savedMotionY;
                            var1x.setCancelled();
                            this.pendingVelocity = true;
                            this.packets.add(s12packetentityvelocity);
                        } else if (!var1x.isCancelled() && s12packetentityvelocity.getEntityID() == aEg.thePlayer.getEntityId()) {
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
        }
    };
    @EventLink
    public final Listener<PacketSendEvent> onPacketSend = var1x -> {
        if (var1x.dq() instanceof C08PacketPlayerBlockPlacement
            && ((C08PacketPlayerBlockPlacement)var1x.dq()).getStack() != null
            && ((C08PacketPlayerBlockPlacement)var1x.dq()).getStack().getItem() instanceof ItemFireball) {
            this.waitingForVelocity = true;
        }
    };
    @EventLink
    public final Listener<StrafeEvent> onStrafeMedium = var0 -> {
        if (aEg.thePlayer.ae == 1) {
            double d0 = (
                    MathHelper.wrapAngleTo180_double(
                            Math.toDegrees(MoveUtil.direction(aEg.thePlayer.pl, aEg.thePlayer.moveForward, aEg.thePlayer.moveStrafing))
                        )
                        + 360.0
                )
                % 360.0
                % 90.0
                / 90.0;
            double d1 = 1.05 + 0.549 * (1.0 - 4.0 * d0 * (1.0 - d0));
            if (!aEg.thePlayer.isPotionActive(Potion.moveSpeed)) {
                MoveUtil.strafe(d1);
            } else {
                MoveUtil.strafe(d1);
            }
        }

        if (aEg.thePlayer.ae == 20) {
            aEg.gameSettings.keyBindJump.isPressed();
        }

        if (aEg.thePlayer.tR > 3 && aEg.thePlayer.tR < 33) {
            MoveUtil.strafe();
        }

        MoveUtil.useDiagonalSpeed();
        if (aEg.thePlayer.tR == 31) {
            aEg.gameSettings.keyBindJump.isKeyDown();
        }

        if (aEg.thePlayer.tR == 32) {
            aEg.gameSettings.keyBindJump.isKeyDown();
        }

        if (aEg.thePlayer.tR == 33) {
            aEg.gameSettings.keyBindJump.isKeyDown();
        }
    };
    @EventLink
    public final Listener<PreMotionEvent> onPreMotion = var1x -> {
        if (!this.pendingVelocity && aEg.thePlayer.tR < 7) {
            aEg.thePlayer.crd = true;
        }

        if (aEg.thePlayer.tR < 2 && aEg.gameSettings.keyBindJump.isPressed()) {
            aEg.gameSettings.keyBindJump.setPressed(false);
        }

        if (this.e(LongJump.class).autoDisable.wo() && !PacketlessDamageComponent.bd() && aEg.thePlayer.onGround && this.hurtTicks >= 999) {
            ;
        }

        if (aEg.thePlayer.onGround) {
            aEg.thePlayer.tR = 0;
        }

        if ((aEg.thePlayer.ae != 0 || !aEg.thePlayer.isPotionActive(Potion.moveSpeed)) && aEg.thePlayer.ae == 0) {
        }

        if (aEg.thePlayer.ae > 4 && aEg.thePlayer.ae < 20 && this.boosting && aEg.gameSettings.keyBindJump.isKeyDown() && !this.e(KillAura.class).isEnabled()) {
            aEg.thePlayer.motionY = 0.35;
            MoveUtil.strafe();
        } else if (aEg.thePlayer.ae > 0 && aEg.thePlayer.ae < 21 && this.boosting) {
            aEg.thePlayer.motionY = 0.005;
            MoveUtil.strafe();
        } else {
            aEg.thePlayer.motionY += 0.028;
        }

        if (aEg.thePlayer.onGround && this.boosting) {
            this.e(Flight.class).toggle();
        }

        if (aEg.thePlayer.onGround && this.hurtTicks >= 999) {
            ;
        }

        if (this.hurtTicks >= 999 && this.pendingVelocity && aEg.thePlayer.tR == 0) {
            double d0 = (
                    MathHelper.wrapAngleTo180_double(
                            Math.toDegrees(MoveUtil.direction(aEg.thePlayer.pl, aEg.thePlayer.moveForward, aEg.thePlayer.moveStrafing))
                        )
                        + 360.0
                )
                % 360.0
                % 90.0
                / 90.0;
            this.speed = 1.05 + 0.35 * (1.0 - 4.0 * d0 * (1.0 - d0));
            if (!aEg.thePlayer.isPotionActive(Potion.moveSpeed)) {
                MoveUtil.strafe(this.speed);
            } else {
                MoveUtil.strafe(this.speed);
            }
        }

        if (this.hurtTicks >= 999 && this.pendingVelocity) {
            MoveUtil.strafe(this.speed);
        }

        if (aEg.thePlayer.tR == 1) {
            aEg.thePlayer.isPotionActive(Potion.moveSpeed);
        }

        aEg.thePlayer.isPotionActive(Potion.moveSpeed);
        MoveUtil.speed();
        if (this.stage == 0) {
            RotationComponent.d(false);
            RotationComponent.setRotations(new Vector2f(aEg.thePlayer.pl - 180.0F, 89.0F), 10.0, MovementFix.OFF);
            int i = this.getFireballSlot();
            if (i != -1 && i != aEg.thePlayer.inventory.currentItem) {
                this.previousSlot = aEg.thePlayer.inventory.currentItem;
                aEg.thePlayer.inventory.currentItem = i;
            }
        }

        if (this.stage == 1) {
            if (!this.placedFireball) {
                PacketUtil.send(new C08PacketPlayerBlockPlacement(aEg.thePlayer.getHeldItem()));
                this.placedFireball = true;
                if (this.antiFireBall != null && this.antiFireBall.isEnabled()) {
                    this.antiFireBall.cooldownMs = 1500;
                    this.antiFireBall.cooldownStopWatch.aX();
                }
            }
        } else if (this.stage == 2 && this.previousSlot != -1) {
            aEg.thePlayer.inventory.currentItem = this.previousSlot;
            this.previousSlot = -1;
        }

        if (this.boostTicks > 1) {
            this.toggle();
        } else {
            if (this.velocityReceived) {
                active = true;
                this.boostTicks++;
            }

            if (this.stage < 3) {
                this.stage++;
            }

            if (this.velocityReceived) {
                if (this.boostTicks > 1) {
                    active = this.velocityReceived = false;
                    this.boostTicks = 0;
                    return;
                }

                active = true;
                this.boostTicks++;
            }

            MoveUtil.useDiagonalSpeed();
        }
    };
    @EventLink(value = 4)
    public final Listener<PostStrafeEvent> onStrafe = var1x -> {
        if (aEg.thePlayer.hurtTime > 0) {
            this.hurtTicks = 999;
        }

        if (this.pendingVelocity && aEg.thePlayer.tR == 7) {
            aEg.thePlayer.ae = 0;
            this.boosting = true;
            this.pendingVelocity = false;
            this.replayingPackets = true;
            new Vector2d(aEg.thePlayer.motionX, aEg.thePlayer.motionZ);
            BlinkComponent.dispatch();
            MoveUtil.strafe();
            this.packets.forEach(PacketUtil::receive);
            aEg.thePlayer.motionY = 0.005;
            MoveUtil.strafe(1.59F);
            this.packets.clear();
            this.replayingPackets = false;
        }
    };
    @EventLink
    public final Listener<PostMotionEvent> onPostMotion = var1x -> {};

    public WatchdogFlight(String var1, Flight flight) {
        super(var1, flight);
    }

    @Override
    public void onEnable() {
        aEg.thePlayer.crd = true;
        if (this.e(Scaffold.class).isEnabled()) {
            this.e(Scaffold.class).toggle();
        }

        this.antiFireBall = Client.a.g().c(AntiFireBall.class);
        this.hurtTicks = 0;
        if (this.getFireballSlot() == -1) {
            ChatUtil.b("Could not find Fireball");
            this.toggle();
        } else {
            aEg.thePlayer.motionX *= -1.0;
            aEg.thePlayer.motionZ *= -1.0;
            active = true;
            this.stage = 0;
            this.pendingVelocity = false;
            this.replayingPackets = false;
            this.startSlot = aEg.thePlayer.inventory.currentItem;
            this.boosting = false;
            this.boostTicks = 0;
        }
    }

    private int getThrowableSlot() {
        for (int i = 0; i < 9; i++) {
            ItemStack itemstack = aEg.thePlayer.inventory.getStackInSlot(i);
            if (itemstack != null && itemstack.getItem() == Items.bow) {
                return i;
            }
        }

        for (int j = 0; j < 9; j++) {
            ItemStack itemstack1 = aEg.thePlayer.inventory.getStackInSlot(j);
            if (itemstack1 != null && itemstack1.getItem() == Items.fishing_rod) {
                return j;
            }
        }

        for (int k = 0; k < 9; k++) {
            ItemStack itemstack2 = aEg.thePlayer.inventory.getStackInSlot(k);
            if (itemstack2 != null && itemstack2.getItem() == Items.egg || itemstack2 != null && itemstack2.getItem() == Items.snowball) {
                return k;
            }
        }

        return -1;
    }

    @Override
    public void onDisable() {
        BlinkComponent.dispatch();
        if (aEg.thePlayer.onGround) {
            MoveUtil.stop();
        }

        if (this.startSlot != -1) {
            aEg.thePlayer.inventory.currentItem = this.startSlot;
        }

        this.packets.forEach(PacketUtil::receive);
        this.packets.clear();
        if (this.previousSlot != -1) {
            aEg.thePlayer.inventory.currentItem = this.previousSlot;
        }

        this.boostTicks = this.previousSlot = -1;
        this.velocityReceived = active = this.placedFireball = false;
    }

    private int getFireballSlot() {
        int i = -1;

        for (int j = 0; j < 9; j++) {
            ItemStack itemstack = aEg.thePlayer.inventory.getStackInSlot(j);
            if (itemstack != null && itemstack.getItem() == Items.fire_charge) {
                i = j;
                break;
            }
        }

        return i;
    }

    private void strafeBoost() {
        MoveUtil.strafe(1.768F);
    }
}
