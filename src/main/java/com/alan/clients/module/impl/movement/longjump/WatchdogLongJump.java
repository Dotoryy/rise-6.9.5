package com.alan.clients.module.impl.movement.longjump;

import com.alan.clients.component.impl.player.SlotComponent;
import com.alan.clients.module.impl.movement.LongJump;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PostMotionEvent;
import com.alan.clients.newevent.impl.motion.PostStrafeEvent;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.newevent.impl.motion.StrafeEvent;
import com.alan.clients.newevent.impl.packet.PacketReceiveEvent;
import com.alan.clients.util.player.MoveUtil;
import com.alan.clients.util.vector.Vector2d;
import com.alan.clients.value.Mode;
import com.alan.clients.value.impl.BooleanValue;
import com.alan.clients.util.chat.ChatUtil;
import com.alan.clients.util.packet.PacketUtil;
import com.alan.clients.util.player.PlayerUtil;
import com.alan.clients.component.impl.player.ItemDamageComponent;
import com.alan.clients.component.impl.player.PacketlessDamageComponent;
import com.alan.clients.component.impl.render.SmoothCameraComponent;
import java.util.ArrayList;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S32PacketConfirmTransaction;
import net.minecraft.potion.Potion;

public class WatchdogLongJump extends Mode<LongJump> {
    private final ArrayList<Packet<?>> heldPackets = new ArrayList<>();
    private boolean active;
    private boolean replaying;
    private boolean launched;
    int previousSlot = -1;
    double startY;
    double savedMotionY = -1.0;
    private int stage;
    private int ticks;
    private final BooleanValue smoothCamera = new BooleanValue("Smooth Camera", this, false);
    @EventLink(value = 1)
    public final Listener<PacketReceiveEvent> receive = var1x -> {
        if (!this.replaying) {
            switch (var1x.getPacket()) {
                case S12PacketEntityVelocity s12packetentityvelocity:
                    if (!var1x.isCancelled() && s12packetentityvelocity.getEntityID() == aEg.thePlayer.getEntityId() && !this.launched) {
                        this.savedMotionY = aEg.thePlayer.motionY;
                        Vector2d vector2d = new Vector2d(aEg.thePlayer.motionX, aEg.thePlayer.motionZ);
                        aEg.thePlayer.motionX = vector2d.getX();
                        aEg.thePlayer.motionZ = vector2d.getY();
                        aEg.thePlayer.motionY = this.savedMotionY;
                        var1x.setCancelled();
                        this.active = true;
                        this.heldPackets.add(s12packetentityvelocity);
                    } else if (!var1x.isCancelled() && s12packetentityvelocity.getEntityID() == aEg.thePlayer.getEntityId()) {
                        var1x.setCancelled();
                    }
                    break;
                case S32PacketConfirmTransaction s32packetconfirmtransaction:
                    if (this.active) {
                        this.heldPackets.add(s32packetconfirmtransaction);
                        var1x.setCancelled();
                    }
                    break;
                default:
            }
        }
    };
    @EventLink
    public final Listener<StrafeEvent> onStrafe = var1x -> {
        if ((aEg.thePlayer.posY > this.startY || aEg.thePlayer.tR < 14) && this.smoothCamera.wo()) {
            SmoothCameraComponent.cn();
        }

        double d0;
        int i = (
                    d0 = Math.hypot(
                            aEg.thePlayer.motionX - (aEg.thePlayer.lastTickPosX - aEg.thePlayer.cry),
                            aEg.thePlayer.motionZ - (aEg.thePlayer.lastTickPosZ - aEg.thePlayer.crA)
                        )
                        - 0.0125
                )
                == 0.0
            ? 0
            : (d0 < 0.0 ? -1 : 1);
        if (aEg.thePlayer.ae == 1) {
            ;
        }

        if (aEg.thePlayer.ae > 7) {
            ;
        }

        if (aEg.thePlayer.ae <= 120
            && aEg.thePlayer.ae >= 100
            && !aEg.thePlayer.isPotionActive(Potion.moveSpeed)
            && aEg.thePlayer.ae != 12
            && aEg.thePlayer.ae == 13) {
        }

        MoveUtil.useDiagonalSpeed();
        if (this.stage < 4) {
            ;
        }

        if (!this.launched) {
            if (aEg.thePlayer.tR == 1) {
                if (aEg.thePlayer.isPotionActive(Potion.moveSpeed) && aEg.thePlayer.getActivePotionEffect(Potion.moveSpeed).amplifier + 1 >= 2) {
                    MoveUtil.strafe(0.475);
                } else if (aEg.thePlayer.isPotionActive(Potion.moveSpeed) && aEg.thePlayer.getActivePotionEffect(Potion.moveSpeed).amplifier + 1 == 1) {
                    MoveUtil.strafe(0.395);
                } else {
                    ChatUtil.b(MoveUtil.speed());
                    MoveUtil.strafe();
                }
            }

            if (aEg.thePlayer.motionY <= 0.0) {
                aEg.thePlayer.motionY += 0.0284;
            }

            double d1;
            i = (d1 = aEg.thePlayer.motionY - 0.0) == 0.0 ? 0 : (d1 < 0.0 ? -1 : 1);
            if (aEg.thePlayer.tR == 11) {
                aEg.thePlayer.motionY += 0.0904;
            }

            if (aEg.thePlayer.tR == 12) {
                aEg.thePlayer.motionY += 0.0904;
            }

            MoveUtil.useDiagonalSpeed();
        }
    };
    @EventLink
    public final Listener<PreMotionEvent> onPreMotion = var1x -> {
        if (this.smoothCamera.wo()) {
            aEg.thePlayer.cameraYaw = 0.1F;
        }

        if (this.e(LongJump.class).autoDisable.wo() && !PacketlessDamageComponent.bd() && aEg.thePlayer.onGround && this.stage >= 999 && !this.active) {
            this.e(LongJump.class).toggle();
        }

        if (aEg.thePlayer.onGround) {
            aEg.thePlayer.tR = 0;
        }

        if (aEg.thePlayer.ae == 0 && aEg.thePlayer.isPotionActive(Potion.moveSpeed)) {
            PlayerUtil.fakeDamage();
            MoveUtil.strafe();
            aEg.thePlayer.motionX *= 2.42;
            aEg.thePlayer.motionZ *= 2.42;
        } else if (aEg.thePlayer.ae == 0) {
            PlayerUtil.fakeDamage();
            MoveUtil.strafe();
            aEg.thePlayer.motionX *= 2.42;
            aEg.thePlayer.motionZ *= 2.42;
        }

        if ((aEg.thePlayer.ae != 1 || !aEg.thePlayer.isPotionActive(Potion.moveSpeed)) && aEg.thePlayer.ae == 1) {
        }

        if (aEg.thePlayer.ae > 1 && this.launched) {
            aEg.thePlayer.motionY += 0.0284;
        }

        if (aEg.thePlayer.onGround && this.stage >= 999 && this.active) {
            MoveUtil.strafe(MoveUtil.getAllowedHorizontalDistance() - 0.01);
            aEg.thePlayer.jump();
        }

        aEg.thePlayer.isPotionActive(Potion.moveSpeed);
        MoveUtil.speed();
    };
    @EventLink(value = 4)
    public final Listener<PostStrafeEvent> onPostStrafe = var1x -> {
        if (aEg.thePlayer.hurtTime > 0) {
            this.stage = 999;
        }

        if (this.stage < 999) {
            MoveUtil.stop();
        }

        if (this.active && aEg.thePlayer.tR == 15) {
            aEg.thePlayer.ae = 0;
            this.launched = true;
            this.active = false;
            this.replaying = true;
            new Vector2d(aEg.thePlayer.motionX, aEg.thePlayer.motionZ);
            this.heldPackets.forEach(PacketUtil::receive);
            this.heldPackets.clear();
            this.replaying = false;
        }
    };
    @EventLink
    public final Listener<PostMotionEvent> onPostMotion = var1x -> {};

    public WatchdogLongJump(String var1, LongJump longJump) {
        super(var1, longJump);
    }

    @Override
    public void onEnable() {
        this.startY = aEg.thePlayer.posY;
        MoveUtil.stop();
        this.active = false;
        this.replaying = false;
        this.previousSlot = aEg.thePlayer.inventory.currentItem;
        this.launched = false;
        if (this.findProjectileSlot() == -1) {
            ChatUtil.b("you need a projectile in your hotbar for this");
        } else {
            int i = this.findProjectileSlot();
            if (i != -1) {
                SlotComponent slotcomponent = this.d(SlotComponent.class);
                SlotComponent.setSlot(i);
            } else {
                ChatUtil.b("you need a projectile in your hotbar for this");
            }

            ItemDamageComponent.damage(false);
            this.stage = 0;
            this.ticks = 0;
        }
    }

    private int findProjectileSlot() {
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
        MoveUtil.stop();
        if (this.previousSlot != -1) {
            aEg.thePlayer.inventory.currentItem = this.previousSlot;
        }

        this.heldPackets.forEach(PacketUtil::receive);
        this.heldPackets.clear();
    }
}
