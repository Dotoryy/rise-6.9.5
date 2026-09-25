package com.alan.clients.module.impl.movement.longjump;

import com.alan.clients.module.impl.movement.LongJump;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.newevent.impl.motion.StrafeEvent;
import com.alan.clients.newevent.impl.packet.PacketReceiveEvent;
import com.alan.clients.newevent.impl.packet.PacketSendEvent;
import com.alan.clients.util.player.MoveUtil;
import com.alan.clients.value.Mode;
import com.alan.clients.value.impl.BooleanValue;
import com.alan.clients.util.chat.ChatUtil;
import com.alan.clients.util.packet.PacketUtil;
import net.minecraft.init.Items;
import net.minecraft.item.ItemFireball;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import rip.vantage.commons.util.time.StopWatch;

public class WatchdogFireBall2LongJump extends Mode<LongJump> {
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
    @EventLink
    public final Listener<PacketSendEvent> onPacketSend = var1x -> {
        if (var1x.dq() instanceof C08PacketPlayerBlockPlacement
            && ((C08PacketPlayerBlockPlacement)var1x.dq()).getStack() != null
            && ((C08PacketPlayerBlockPlacement)var1x.dq()).getStack().getItem() instanceof ItemFireball) {
            this.sentFireball = true;
            if (aEg.thePlayer.onGround) {
                aEg.thePlayer.jump();
            }
        }
    };
    @EventLink
    public final Listener<PacketReceiveEvent> onPacketReceive = var1x -> {
        if (aEg.thePlayer != null && aEg.theWorld != null) {
            if (var1x.getPacket() instanceof S12PacketEntityVelocity) {
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
        }
    };
    @EventLink(value = 0)
    public final Listener<PreMotionEvent> onPreMotion = var1x -> {
        if (aEg.thePlayer != null && aEg.theWorld != null) {
            if (aEg.thePlayer.hurtTime == 10) {
                aEg.thePlayer.motionY = 1.1F;
            }

            if (aEg.thePlayer.ae <= 80 && aEg.thePlayer.ae >= 1 && (aEg.thePlayer.ae % 1 == 0 || aEg.thePlayer.ae <= 15)) {
                aEg.thePlayer.motionY += 0.028F;
            }

            if (aEg.thePlayer.ae == 28) {
                this.boost.wo();
                aEg.thePlayer.motionY = 0.16F;
            }

            if (aEg.thePlayer.ae == 33) {
                aEg.thePlayer.motionY = -0.082F;
            }

            if (aEg.thePlayer.ae >= 35 && aEg.thePlayer.ae <= 50) {
                MoveUtil.strafe();
            }

            if (aEg.thePlayer.ae >= 3 && aEg.thePlayer.ae <= 50) {
                MoveUtil.strafe();
            }

            if (this.stage == 0) {
                var1x.setYaw(aEg.thePlayer.pl - 180.0F);
                var1x.setPitch(89.0F);
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
                    this.boostStrafe();
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
                    this.boostStrafe();
                }
            }
        }
    };
    @EventLink
    public final Listener<StrafeEvent> onStrafe = var0 -> {
        if (aEg.thePlayer.ae <= 70 && aEg.thePlayer.ae >= 1 && (aEg.thePlayer.ae % 1 == 0 || aEg.thePlayer.ae <= 15)) {
            aEg.thePlayer.motionX *= 1.0003;
            aEg.thePlayer.motionZ *= 1.0003;
        }

        if (aEg.thePlayer.ae == 1) {
            aEg.thePlayer.motionX *= 1.15;
            aEg.thePlayer.motionZ *= 1.15;
            aEg.thePlayer.motionY = 1.33;
        }

        if (aEg.thePlayer.ae == 15) {
            aEg.thePlayer.motionY = 0.39;
        }

        if (aEg.thePlayer.ae == 22) {
            aEg.thePlayer.motionY = 0.05;
        }

        if (aEg.thePlayer.ae == 23) {
            aEg.thePlayer.motionY = 0.005;
        }

        if (aEg.thePlayer.ae == 24) {
            aEg.thePlayer.motionY += 0.01;
            ChatUtil.c(aEg.thePlayer.motionY);
        }

        if (aEg.thePlayer.ae == 25) {
            aEg.thePlayer.motionY += 0.015;
            ChatUtil.c(aEg.thePlayer.motionY);
        }

        if (aEg.thePlayer.ae == 26) {
            aEg.thePlayer.motionY += 0.015;
            ChatUtil.c(aEg.thePlayer.motionY);
        }

        if (aEg.thePlayer.ae == 33) {
            aEg.thePlayer.motionY += 0.015;
        }

        if (aEg.thePlayer.ae == 35) {
            aEg.thePlayer.motionY += 0.015;
            ChatUtil.c(aEg.thePlayer.motionY);
        }

        if (aEg.thePlayer.ae == 38) {
            aEg.thePlayer.motionY += 0.03;
            ChatUtil.c(aEg.thePlayer.motionY);
        }

        if (aEg.thePlayer.hurtTime == 8) {
            aEg.thePlayer.motionX *= 1.02;
            aEg.thePlayer.motionZ *= 1.02;
        }

        if (aEg.thePlayer.hurtTime == 7) {
            aEg.thePlayer.motionX *= 1.0004;
            aEg.thePlayer.motionZ *= 1.0004;
        }

        if (aEg.thePlayer.hurtTime == 6) {
            aEg.thePlayer.motionX *= 1.0004;
            aEg.thePlayer.motionZ *= 1.0004;
        }

        if (aEg.thePlayer.hurtTime == 5) {
            aEg.thePlayer.motionX *= 1.0004;
            aEg.thePlayer.motionZ *= 1.0004;
        }

        if (aEg.thePlayer.hurtTime <= 4 && aEg.thePlayer.hurtTime != 0) {
            aEg.thePlayer.motionX *= 1.0004;
            aEg.thePlayer.motionZ *= 1.0004;
        }
    };

    public WatchdogFireBall2LongJump(String var1, LongJump longJump) {
        super(var1, longJump);
    }

    @Override
    public void onDisable() {
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
        if (this.findFireballSlot() == -1) {
            ChatUtil.b("Could not find Fireball");
            this.toggle();
        } else {
            boosting = true;
            this.stage = 0;
        }
    }

    private void boostStrafe() {
        MoveUtil.strafe(1.768F);
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
