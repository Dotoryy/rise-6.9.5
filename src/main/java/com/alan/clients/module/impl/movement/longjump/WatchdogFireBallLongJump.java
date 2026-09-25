package com.alan.clients.module.impl.movement.longjump;

import com.alan.clients.component.impl.player.RotationComponent;
import com.alan.clients.component.impl.player.rotationcomponent.MovementFix;
import com.alan.clients.module.impl.movement.LongJump;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.input.MoveInputEvent;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.newevent.impl.motion.PreUpdateEvent;
import com.alan.clients.newevent.impl.packet.PacketReceiveEvent;
import com.alan.clients.newevent.impl.packet.PacketSendEvent;
import com.alan.clients.util.player.MoveUtil;
import com.alan.clients.util.vector.Vector2f;
import com.alan.clients.value.Mode;
import com.alan.clients.value.impl.BooleanValue;
import com.alan.clients.util.chat.ChatUtil;
import com.alan.clients.util.packet.PacketUtil;
import java.util.ArrayList;
import net.minecraft.init.Items;
import net.minecraft.item.ItemFireball;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S32PacketConfirmTransaction;
import rip.vantage.commons.util.time.StopWatch;

public class WatchdogFireBallLongJump extends Mode<LongJump> {
    public final BooleanValue boost = new BooleanValue("Boost", this, true);
    private int previousSlot = -1;
    private int velocityTicks = -1;
    private boolean receivedVelocity;
    public static boolean boosting;
    private boolean sentPlacement;
    private int stage;
    private boolean sentFireball;
    private boolean Ms;
    StopWatch stopWatch = new StopWatch();
    public static boolean active = false;
    public static boolean replaying;
    private int tR;
    private final ArrayList<Packet<?>> heldPackets = new ArrayList<>();
    private float velocityYaw;
    private double velocityX;
    private double velocityZ;
    private boolean jump;
    @EventLink
    public final Listener<PacketSendEvent> onPacketSend = var1x -> {
        if (var1x.dq() instanceof C08PacketPlayerBlockPlacement
            && ((C08PacketPlayerBlockPlacement)var1x.dq()).getStack() != null
            && ((C08PacketPlayerBlockPlacement)var1x.dq()).getStack().getItem() instanceof ItemFireball) {
            this.sentFireball = true;
        }
    };
    @EventLink(value = 2)
    public final Listener<PacketReceiveEvent> onPacketReceive = var1x -> {
        Packet packet = var1x.getPacket();
        if (!replaying) {
            if (packet instanceof S12PacketEntityVelocity) {
                if (((S12PacketEntityVelocity)var1x.getPacket()).getEntityID() != aEg.thePlayer.getEntityId()) {
                    return;
                }

                if (this.sentFireball) {
                    this.velocityTicks = 0;
                    this.receivedVelocity = true;
                    this.sentFireball = false;
                    boosting = true;
                }
            }

            switch (var1x.getPacket()) {
                case S12PacketEntityVelocity s12packetentityvelocity:
                    if (s12packetentityvelocity.getEntityID() == aEg.thePlayer.getEntityId() && !var1x.isCancelled()) {
                        this.velocityTicks = 0;
                        this.receivedVelocity = true;
                        this.sentFireball = false;
                        boosting = true;
                        this.heldPackets.add(s12packetentityvelocity);
                        var1x.setCancelled();
                        active = true;
                        double d0 = s12packetentityvelocity.getMotionX() / 8000.0;
                        double d1 = s12packetentityvelocity.getMotionZ() / 8000.0;
                        this.velocityYaw = (float)Math.toDegrees(Math.atan2(d1, d0));
                        if (this.velocityYaw < -180.0F) {
                            this.velocityYaw += 360.0F;
                        }

                        if (this.velocityYaw > 180.0F) {
                            this.velocityYaw -= 360.0F;
                        }

                        this.velocityX = d0;
                        this.velocityZ = d1;
                    }
                    break;
                case S32PacketConfirmTransaction s32packetconfirmtransaction:
                    if (active) {
                        var1x.setCancelled();
                        this.heldPackets.add(s32packetconfirmtransaction);
                    }
                    break;
                case net.minecraft.network.play.server.a a:
                    if (active) {
                        var1x.setCancelled();
                        this.heldPackets.add(a);
                    }
                    break;
                default:
                    if (active) {
                    }
            }
        }
    };
    @EventLink
    public final Listener<PreMotionEvent> onPreMotion = var1x -> {
        if (this.stage == 0) {
            RotationComponent.d(false);
            RotationComponent.setRotations(new Vector2f(aEg.thePlayer.pl, 89.0F), 10.0, MovementFix.NORMAL);
            int i = this.findFireballSlot();
            if (i != -1 && i != aEg.thePlayer.inventory.currentItem) {
                this.previousSlot = aEg.thePlayer.inventory.currentItem;
                if (aEg.thePlayer.cqL > 1) {
                    aEg.thePlayer.inventory.currentItem = i;
                }
            }
        }

        if (this.stage == 1) {
            if (!this.sentPlacement) {
                PacketUtil.send(new C08PacketPlayerBlockPlacement(aEg.thePlayer.getHeldItem()));
                this.sentPlacement = true;
            }
        } else if (this.stage == 2 && this.previousSlot != -1) {
            aEg.thePlayer.inventory.currentItem = this.previousSlot;
            this.previousSlot = -1;
        }

        if (this.velocityTicks > 1) {
            this.toggle();
        } else {
            if (this.receivedVelocity) {
                boosting = true;
                this.velocityTicks++;
            }

            if (this.stage < 3) {
                this.stage++;
            }

            if (this.receivedVelocity) {
                if (this.velocityTicks > 1) {
                    boosting = this.receivedVelocity = false;
                    this.velocityTicks = 0;
                    return;
                }

                boosting = true;
                this.velocityTicks++;
            }
        }
    };
    @EventLink(value = 2)
    public final Listener<PreUpdateEvent> onPreUpdate = var1x -> {
        if (active && aEg.thePlayer.tR == 13) {
            replaying = true;
            active = false;
            this.heldPackets.forEach(PacketUtil::receive);
            this.heldPackets.clear();
            replaying = false;
            this.tR = 0;
        }

        if (!aEg.thePlayer.onGround) {
            RotationComponent.setRotations(new Vector2f(aEg.thePlayer.pl + 45.0F, aEg.thePlayer.rotationPitch), 10.0, MovementFix.NORMAL);
        }
    };
    @EventLink
    public final Listener<MoveInputEvent> onMoveInput = var1x -> {
        if (this.jump) {
            this.jump = false;
        }

        if (active || replaying) {
            this.jump = false;
        }

        if (!active) {
            ;
        }
    };

    public WatchdogFireBallLongJump(String var1, LongJump longJump) {
        super(var1, longJump);
    }

    @Override
    public void onDisable() {
        this.heldPackets.clear();
        replaying = false;
        active = false;
        if (aEg.thePlayer.onGround) {
            MoveUtil.stop();
        }

        if (this.previousSlot != -1) {
            aEg.thePlayer.inventory.currentItem = this.previousSlot;
        }

        this.velocityTicks = this.previousSlot = -1;
        this.receivedVelocity = boosting = this.sentPlacement = false;
        this.stage = 0;
    }

    @Override
    public void onEnable() {
        this.heldPackets.clear();
        replaying = false;
        active = false;
        if (this.findFireballSlot() == -1) {
            ChatUtil.b("Could not find Fireball");
            this.toggle();
        } else {
            boosting = true;
            this.stage = 0;
        }
    }

    private int findFireballSlot() {
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
}
