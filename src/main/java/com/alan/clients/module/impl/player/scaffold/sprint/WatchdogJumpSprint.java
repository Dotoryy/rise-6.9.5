package com.alan.clients.module.impl.player.scaffold.sprint;

import com.alan.clients.Client;
import com.alan.clients.component.impl.player.BlinkComponent;
import com.alan.clients.component.impl.player.RotationComponent;
import com.alan.clients.component.impl.player.SlotComponent;
import com.alan.clients.component.impl.player.rotationcomponent.MovementFix;
import com.alan.clients.module.impl.movement.Flight;
import com.alan.clients.module.impl.movement.LongJump;
import com.alan.clients.module.impl.movement.Speed;
import com.alan.clients.module.impl.player.Scaffold;
import com.alan.clients.module.impl.player.scaffold.tower.WatchdogTower;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.newevent.impl.motion.PreUpdateEvent;
import com.alan.clients.newevent.impl.motion.StrafeEvent;
import com.alan.clients.newevent.impl.packet.PacketReceiveEvent;
import com.alan.clients.util.player.MoveUtil;
import com.alan.clients.util.vector.Vector2f;
import com.alan.clients.value.Mode;
import com.alan.clients.value.impl.BooleanValue;
import com.alan.clients.util.packet.PacketUtil;
import com.alan.clients.util.player.PlayerUtil;
import com.alan.clients.util.player.SlotUtil;
import com.alan.clients.component.impl.player.BadPacketsComponent;
import com.alan.clients.module.impl.player.scaffold.sprint.WatchdogJumpSprintHelper;
import java.util.Random;
import lombok.Generated;
import net.minecraft.block.BlockAir;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S23PacketBlockChange;
import net.minecraft.potion.Potion;
import net.minecraft.util.MathHelper;

public class WatchdogJumpSprint extends Mode<Scaffold> {
    private boolean HJ = false;
    private boolean wasJumping = false;
    private boolean justEnabled = false;
    private int startTick = -1;
    public static boolean boostPending = false;
    public static boolean boostNextJump = false;
    public static boolean blockChanged = false;
    public static boolean ajz = false;
    private int groundTicks = 0;
    private int blockChanges;
    public final BooleanValue placeExtraBlocks = new BooleanValue("Place Extra blocks", this, false);
    private final WatchdogJumpSprintHelper helper;
    @EventLink(value = 3)
    public final Listener<PreMotionEvent> onPreMotion = var1x -> {
        this.getParent().extraPlacements = 1;
        if (Math.abs(aEg.thePlayer.motionY) < 0.1) {
            ;
        }

        float f = MathHelper.wrapAngleTo180_float(aEg.thePlayer.pl);
        boolean flag = Math.abs(f % 90.0F) <= 10.0F || Math.abs(f % 90.0F) >= 80.0F;
        double d0 = MathHelper.wrapAngleTo180_double(Math.toDegrees(MoveUtil.direction()));
        double d1 = MathHelper.wrapAngleTo180_double(Math.toDegrees(Math.atan2(aEg.thePlayer.motionZ, aEg.thePlayer.motionX)) - 90.0);
        if (Math.abs(d0 - d1) < 5.0 && aEg.thePlayer.Zl > 7 && !aEg.gameSettings.cgI.isKeyDown() && !aEg.gameSettings.keyBindSneak.isKeyDown()) {
            if (aEg.thePlayer.cqL > 2
                && !aEg.gameSettings.keyBindJump.isKeyDown()
                && !Client.a.g().c(Speed.class).isEnabled()
                && !aEg.thePlayer.isPotionActive(Potion.moveSpeed)
                && aEg.thePlayer.Zl > 7) {
                MoveUtil.strafe();
                if (flag) {
                    aEg.thePlayer.motionZ *= 1.1225;
                    aEg.thePlayer.motionX *= 1.1225;
                } else {
                    aEg.thePlayer.motionZ *= 1.129;
                    aEg.thePlayer.motionX *= 1.129;
                }
            } else if (aEg.thePlayer.cqL > 2
                && !aEg.gameSettings.keyBindJump.isKeyDown()
                && !Client.a.g().c(Speed.class).isEnabled()
                && aEg.thePlayer.isPotionActive(Potion.moveSpeed)
                && aEg.thePlayer.getActivePotionEffect(Potion.moveSpeed).amplifier + 1 >= 2
                && aEg.thePlayer.Zl > 7) {
                MoveUtil.strafe();
                aEg.thePlayer.motionZ *= 1.1055;
                aEg.thePlayer.motionX *= 1.1055;
            } else if (aEg.thePlayer.cqL > 2
                && !aEg.gameSettings.keyBindJump.isKeyDown()
                && !Client.a.g().c(Speed.class).isEnabled()
                && aEg.thePlayer.isPotionActive(Potion.moveSpeed)
                && aEg.thePlayer.getActivePotionEffect(Potion.moveSpeed).amplifier + 1 == 1
                && aEg.thePlayer.Zl > 7) {
                MoveUtil.strafe();
                aEg.thePlayer.motionZ *= 1.098;
                aEg.thePlayer.motionX *= 1.098;
            }
        }

        if (!aEg.gameSettings.keyBindSneak.isKeyDown()) {
            int i = aEg.thePlayer.ticksExisted;
            if (!this.e(Flight.class).isEnabled()
                && !this.e(LongJump.class).isEnabled()
                && !BadPacketsComponent.bad(true, true, false, true, false)
                && Math.random() > 0.5
                && blockChanged
                && MoveUtil.isMoving()
                && !(aEg.thePlayer.getHeldItem().getItem() instanceof ItemSword)) {
                Random random = new Random();
                random.nextFloat();
                random.nextFloat();
                blockChanged = false;
            }

            if ((this.startTick == -1 || i - this.startTick < 4) && this.startTick == -1) {
                this.startTick = i;
            }

            if (aEg.thePlayer.onGround) {
                MoveUtil.strafe();
            }

            if (this.wasJumping && !aEg.gameSettings.keyBindJump.isKeyDown()) {
                WatchdogTower.towerYaw = aEg.thePlayer.pl;
                WatchdogTower.moveTicks = 17;
                WatchdogTower.jumpStage = 0;
                MoveUtil.stop();
                this.wasJumping = false;
                RotationComponent.setRotations(new Vector2f((float)(aEg.thePlayer.pl + (Math.random() - 0.5) * 3.0), 90.0F), 10.0, MovementFix.NORMAL);
                if (!(PlayerUtil.o(aEg.thePlayer.posX, aEg.thePlayer.aI - 2.0, aEg.thePlayer.posY) instanceof BlockAir)) {
                    this.getParent().startY = aEg.thePlayer.aI - 1.0;
                }
            }

            if (aEg.gameSettings.keyBindJump.isKeyDown()) {
                this.wasJumping = true;
            }
        }
    };
    @EventLink(value = 2)
    public final Listener<PreUpdateEvent> onPreUpdate = var1x -> {
        boolean flag = aEg.thePlayer.aI == this.getParent().startY;
        if (!this.placeExtraBlocks.wo()) {
            flag = true;
        }

        label159: {
            SlotComponent slotcomponent = this.d(SlotComponent.class);
            if (SlotComponent.getItemStack() == null
                || !(aEg.thePlayer.posY > this.getParent().startY)
                || !(aEg.thePlayer.posY + MoveUtil.predictedMotion(aEg.thePlayer.motionY, 2) < this.getParent().startY + 1.0)
                || this.justEnabled && !boostPending
                || !Client.a.g().c(Speed.class).isEnabled()
                || this.e(Scaffold.class).sameY.wo().getName().equals("Auto Jump")) {
                slotcomponent = this.d(SlotComponent.class);
                if (SlotComponent.getItemStack() == null
                    || !(aEg.thePlayer.posY > this.getParent().startY)
                    || !(aEg.thePlayer.posY + MoveUtil.predictedMotion(aEg.thePlayer.motionY, 2) < this.getParent().startY + 1.0)
                    || !this.e(Scaffold.class).sameY.wo().getName().equals("Auto Jump")) {
                    break label159;
                }
            }

            slotcomponent = this.d(SlotComponent.class);
            SlotComponent.setSlot(SlotUtil.vx());
        }

        if (Client.a.g().c(Speed.class).isEnabled()) {
            flag = false;
        }

        if (this.justEnabled) {
            flag = true;
        }

        if (aEg.thePlayer.tR > 8) {
            this.justEnabled = false;
        }

        if ((
                PlayerUtil.ad(2.0)
                    || PlayerUtil.ad(1.0)
                    || PlayerUtil.ad(0.0)
                    || PlayerUtil.ad(3.0)
                    || aEg.gameSettings.keyBindJump.isPressed()
                    || aEg.thePlayer.isPotionActive(Potion.moveSpeed)
                    || this.wasJumping
            )
            && aEg.thePlayer.cqL < 1) {
            aEg.thePlayer.crd = false;
        }

        if (aEg.gameSettings.keyBindJump.isPressed()
            && aEg.thePlayer.onGround
            && !this.wasJumping
            && (boostNextJump || this.e(Scaffold.class).sameY.wo().getName().equals("Auto Jump"))
            && !Client.a.g().c(Speed.class).isEnabled()) {
            boostNextJump = false;
            MoveUtil.strafe(MoveUtil.vd() * 0.9);
            if (this.e(Scaffold.class).sameY.wo().getName().equals("Auto Jump")) {
                MoveUtil.strafe(MoveUtil.getAllowedHorizontalDistance() - 0.001);
            }
        }

        if (aEg.thePlayer.tR == 4
            && !aEg.gameSettings.keyBindJump.isKeyDown()
            && !Client.a.g().c(Speed.class).isEnabled()
            && !flag
            && this.e(Scaffold.class).sameY.wo().getName().equals("Auto Jump")) {
            aEg.thePlayer.motionY -= 0.19;
        }

        if (aEg.thePlayer.tR == 1 && !aEg.gameSettings.keyBindJump.isKeyDown()) {
            MoveUtil.strafe();
        }

        if (PlayerUtil.p(0.0, aEg.thePlayer.motionY, 0.0) != Blocks.air && aEg.thePlayer.tR > 2) {
            MoveUtil.strafe();
        }

        if ((!flag || aEg.gameSettings.keyBindJump.isKeyDown() || aEg.thePlayer.onGround || this.e(Speed.class).isEnabled() || !this.placeExtraBlocks.wo())
            && flag
            && !aEg.gameSettings.keyBindJump.isKeyDown()
            && !aEg.thePlayer.onGround
            && this.placeExtraBlocks.wo()) {
        }

        if (flag && aEg.thePlayer.onGround && !aEg.gameSettings.keyBindJump.isKeyDown()) {
            this.groundTicks++;
        }

        if (!flag) {
            this.groundTicks = 0;
        }
    };
    @EventLink
    public final Listener<PacketReceiveEvent> onPacketReceive = var1x -> {
        if (var1x.getPacket() instanceof S23PacketBlockChange) {
            blockChanged = true;
            this.blockChanges++;
        }
    };
    @EventLink
    public final Listener<StrafeEvent> onStrafe = var1x -> {
        if (!this.e(Scaffold.class).sameY.wo().getName().equals("Auto Jump")
            && aEg.thePlayer.onGround
            && this.justEnabled
            && boostPending
            && !Client.a.g().c(Speed.class).isEnabled()
            && !aEg.gameSettings.keyBindJump.isKeyDown()) {
            boostPending = false;
        }
    };

    public WatchdogJumpSprint(String var1, Scaffold scaffold) {
        super(var1, scaffold);
        this.helper = new WatchdogJumpSprintHelper(scaffold);
    }

    @Override
    public void onEnable() {
        blockChanged = false;
        ajz = false;
        if (!this.e(Scaffold.class).sameY.wo().getName().equals("Auto Jump")) {
            if (aEg.thePlayer.cqL > 9) {
                boostPending = true;
                this.justEnabled = true;
            }
        } else {
            aEg.thePlayer.crd = false;
        }

        boostNextJump = true;
        this.wasJumping = false;
        if (!aEg.thePlayer.onGround && !aEg.gameSettings.keyBindJump.isKeyDown()) {
            aEg.thePlayer.motionX *= 0.0;
            aEg.thePlayer.motionZ *= 0.0;
        }

        this.startTick = -1;
        this.blockChanges = 0;
        boostNextJump = true;
    }

    @Override
    public void onDisable() {
        this.HJ = false;
        blockChanged = false;
        aEg.thePlayer.crd = false;
        boostPending = false;
        this.justEnabled = false;
        float f = MathHelper.wrapAngleTo180_float(aEg.thePlayer.pl);
        boolean flag = Math.abs(f % 90.0F) <= 10.0F || Math.abs(f % 90.0F) >= 80.0F;
        if (flag) {
            if (aEg.thePlayer.cqL > 2 && aEg.thePlayer.ticksExisted % 3 == 1) {
                aEg.timer.dzD = 0.5F;
                PacketUtil.send(new C03PacketPlayer(aEg.thePlayer.onGround));
            } else if (aEg.thePlayer.cqL > 2 && aEg.thePlayer.ticksExisted % 3 == 2) {
                aEg.timer.dzD = 0.33F;
                PacketUtil.send(new C03PacketPlayer(aEg.thePlayer.onGround));
                PacketUtil.send(new C03PacketPlayer(aEg.thePlayer.onGround));
            } else if (aEg.thePlayer.cqL > 2) {
                aEg.timer.dzD = 0.5F;
                PacketUtil.send(new C03PacketPlayer(aEg.thePlayer.onGround));
            }
        } else if (aEg.thePlayer.cqL > 2) {
            aEg.timer.dzD = 0.33F;
            PacketUtil.send(new C03PacketPlayer(aEg.thePlayer.onGround));
            PacketUtil.send(new C03PacketPlayer(aEg.thePlayer.onGround));
        }

        boostNextJump = false;
        BlinkComponent.dispatch();
    }

    @Generated
    public WatchdogJumpSprintHelper getHelper() {
        return this.helper;
    }
}
