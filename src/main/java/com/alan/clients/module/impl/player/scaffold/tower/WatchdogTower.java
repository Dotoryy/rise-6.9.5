package com.alan.clients.module.impl.player.scaffold.tower;

import com.alan.clients.Client;
import com.alan.clients.component.impl.player.RotationComponent;
import com.alan.clients.component.impl.player.SlotComponent;
import com.alan.clients.component.impl.player.rotationcomponent.MovementFix;
import com.alan.clients.module.impl.exploit.Disabler;
import com.alan.clients.module.impl.movement.Speed;
import com.alan.clients.module.impl.player.Scaffold;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.input.KeyboardInputEvent;
import com.alan.clients.newevent.impl.input.MoveInputEvent;
import com.alan.clients.newevent.impl.motion.JumpEvent;
import com.alan.clients.newevent.impl.motion.PostStrafeEvent;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.newevent.impl.motion.StrafeEvent;
import com.alan.clients.newevent.impl.other.TickEvent;
import com.alan.clients.newevent.impl.packet.PacketSendEvent;
import com.alan.clients.util.player.MoveUtil;
import com.alan.clients.util.vector.Vector2f;
import com.alan.clients.value.Mode;
import com.alan.clients.util.player.PlayerUtil;
import com.alan.clients.util.player.SlotUtil;
import com.alan.clients.util.rotation.RotationUtil;
import com.alan.clients.util.vector.Vector3d;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.potion.Potion;
import net.minecraft.util.MathHelper;

public class WatchdogTower extends Mode<Scaffold> {
    public static int moveTicks;
    int dm;
    public static int jumpStage;
    private boolean placingUp;
    private boolean pendingToggle;
    private int upwardPlacements = 0;
    private int akn = 0;
    float savedYaw;
    boolean startedInAir = true;
    boolean aligning = false;
    private int alignStep;
    public static float towerYaw;
    double targetZ = Double.NaN;
    double targetX = Double.NaN;
    private int lastPlaceY;
    private boolean jumpKeyHeld = false;
    @EventLink
    public final Listener<JumpEvent> onJump = var0 -> {};
    @EventLink(value = 1)
    public Listener<PreMotionEvent> onPreMotion = var1x -> {
        double d0 = Math.toRadians(aEg.thePlayer.pl);
        double d27 = -Math.sin(d0);
        if (Math.cos(d0) * 1.0 < 0.0) {
            boolean flag = true;
        } else {
            boolean flag1 = false;
        }

        if (aEg.thePlayer.ticksExisted % 3 != 0 || !aEg.thePlayer.onGround || aEg.thePlayer.cqL <= 2) {
            if (!aEg.gameSettings.keyBindJump.isKeyDown() && MoveUtil.isMoving()) {
                towerYaw = aEg.thePlayer.pl;
                jumpStage = 0;
                moveTicks = 0;
            } else if (!aEg.gameSettings.keyBindJump.isKeyDown()) {
                jumpStage = 100;
                return;
            }

            if (MoveUtil.isMoving()) {
                moveTicks++;
            } else if (moveTicks > 20) {
                moveTicks--;
            }

            if (aEg.gameSettings.keyBindJump.isKeyDown()) {
                jumpStage++;
            }

            if (moveTicks >= 23) {
                moveTicks = 1;
                towerYaw = aEg.thePlayer.pl;
                jumpStage = 99;
            }

            if (aEg.thePlayer.onGround) {
                jumpStage = 0;
                this.alignStep = 0;
                towerYaw = aEg.thePlayer.pl;
            } else if (jumpStage == 100) {
                MoveUtil.strafe(0.0);
            }

            if (!MoveUtil.isMoving()) {
                double d1 = aEg.thePlayer.pl;
                double d2 = Math.toRadians(d1);
                d27 = -Math.sin(d2);
                boolean flag2 = Math.cos(d2) * 1.0 < 0.0;
                this.getParent().extraPlacements = 1;
                if (!this.aligning) {
                    if (flag2) {
                        this.targetX = Math.floor(aEg.thePlayer.posX) + 0.999999999999;
                        this.targetZ = Double.NaN;
                    } else {
                        this.targetZ = Math.floor(aEg.thePlayer.posZ) + 0.999999999999;
                        this.targetX = Double.NaN;
                    }

                    this.aligning = true;
                }

                this.alignStep++;
                if (Math.abs(this.lastPlaceY - aEg.thePlayer.posY) >= 1.0) {
                    if (this.alignStep == 1) {
                        MoveUtil.stop();
                        if (!this.isEnclosed()) {
                            if (!Double.isNaN(this.targetX)) {
                                double d3 = aEg.thePlayer.posX + (this.targetX - aEg.thePlayer.posX) / 3.0;
                                double d4 = aEg.thePlayer.posY;
                                double d5 = aEg.thePlayer.posZ;
                                if (this.canFitAt(d3, d4, d5)) {
                                    aEg.thePlayer.setPosition(d3, d4, d5);
                                }
                            } else if (!Double.isNaN(this.targetZ)) {
                                double d6 = aEg.thePlayer.posX;
                                double d7 = aEg.thePlayer.posY;
                                double d8 = aEg.thePlayer.posZ + (this.targetZ - aEg.thePlayer.posZ) / 3.0;
                                if (this.canFitAt(d6, d7, d8)) {
                                    aEg.thePlayer.setPosition(d6, d7, d8);
                                }
                            }
                        }
                    } else if (this.alignStep == 2) {
                        MoveUtil.stop();
                        if (!this.isEnclosed()) {
                            if (!Double.isNaN(this.targetX)) {
                                double d9 = aEg.thePlayer.posX + 2.0 * (this.targetX - aEg.thePlayer.posX) / 3.0;
                                double d10 = aEg.thePlayer.posY;
                                double d11 = aEg.thePlayer.posZ;
                                if (this.canFitAt(d9, d10, d11)) {
                                    aEg.thePlayer.setPosition(d9, d10, d11);
                                }
                            } else if (!Double.isNaN(this.targetZ)) {
                                double d12 = aEg.thePlayer.posX;
                                double d13 = aEg.thePlayer.posY;
                                double d14 = aEg.thePlayer.posZ + 2.0 * (this.targetZ - aEg.thePlayer.posZ) / 3.0;
                                if (this.canFitAt(d12, d13, d14)) {
                                    aEg.thePlayer.setPosition(d12, d13, d14);
                                }
                            }

                            this.updateRotation();
                        } else {
                            this.updateRotation();
                        }
                    } else if (this.alignStep == 3) {
                        MoveUtil.stop();
                        if (!this.isEnclosed()) {
                            if (!Double.isNaN(this.targetX)) {
                                double d15 = this.targetX;
                                double d16 = aEg.thePlayer.posY;
                                double d17 = aEg.thePlayer.posZ;
                                if (this.canFitAt(d15, d16, d17)) {
                                    aEg.thePlayer.setPosition(d15, d16, d17);
                                    this.updateRotation();
                                }
                            } else if (!Double.isNaN(this.targetZ)) {
                                double d21 = aEg.thePlayer.posX;
                                double d22 = aEg.thePlayer.posY;
                                double d23 = this.targetZ;
                                if (this.canFitAt(d21, d22, d23)) {
                                    aEg.thePlayer.setPosition(d21, d22, d23);
                                }
                            }

                            double d18 = aEg.thePlayer.posX;
                            double d19 = aEg.thePlayer.posY;
                            double d20 = this.targetZ;
                            this.canFitAt(d18, d19, d20);
                            this.updateRotation();
                            this.alignStep = 0;
                            this.aligning = false;
                        } else {
                            this.updateRotation();
                        }
                    }
                } else {
                    this.updateRotation();
                    this.alignStep = 0;
                    this.aligning = false;
                }
            } else {
                this.aligning = false;
            }

            if (this.pendingToggle && !MoveUtil.isMoving()) {
                aEg.gameSettings.keyBindJump.setPressed(true);
            }

            if (aEg.thePlayer.Zl < 1) {
                this.aligning = false;
            }

            if (aEg.gameSettings.keyBindJump.isKeyDown()) {
                this.jumpKeyHeld = true;
            }

            moveTicks = 0;
            if (aEg.thePlayer.motionY < 0.3 && this.pendingToggle && aEg.thePlayer.motionY > 0.17) {
                this.getParent().toggle();
                if (aEg.thePlayer.onGround) {
                    aEg.thePlayer.jump();
                }

                this.pendingToggle = false;
            }

            if (this.jumpKeyHeld && !aEg.gameSettings.keyBindJump.isKeyDown()) {
                if (aEg.thePlayer.motionY < 0.3 && this.pendingToggle) {
                    double d24;
                    int i = (d24 = aEg.thePlayer.motionY - 0.17) == 0.0 ? 0 : (d24 < 0.0 ? -1 : 1);
                }

                this.jumpKeyHeld = false;
            }

            if (!aEg.gameSettings.keyBindJump.isPressed()) {
                aEg.gameSettings.keyBindJump.isKeyDown();
            }

            float f = jumpStage == 1 ? 90.0F : 0.0F;
            if (MathHelper.wrapAngleTo180_float(aEg.thePlayer.pl - towerYaw) < f) {
                towerYaw = aEg.thePlayer.pl;
            } else if (MathHelper.wrapAngleTo180_float(aEg.thePlayer.pl - towerYaw) < 0.0F) {
                towerYaw -= f;
            } else if (MathHelper.wrapAngleTo180_float(aEg.thePlayer.pl - towerYaw) > 0.0F) {
                towerYaw += f;
            }

            if (moveTicks < 20 && !Client.a.g().c(Speed.class).isEnabled()) {
                aEg.thePlayer.isPotionActive(Potion.moveSpeed);
                if (aEg.gameSettings.keyBindJump.isKeyDown()) {
                    aEg.thePlayer.isPotionActive(Potion.moveSpeed);
                    switch (jumpStage) {
                        case 0:
                            MoveUtil.strafe();
                            if (aEg.thePlayer.isPotionActive(Potion.moveSpeed) && aEg.thePlayer.getActivePotionEffect(Potion.moveSpeed).amplifier + 1 >= 2) {
                                aEg.thePlayer.motionZ *= 1.045;
                                aEg.thePlayer.motionX *= 1.045;
                            } else if (aEg.thePlayer.isPotionActive(Potion.moveSpeed)) {
                                aEg.thePlayer.motionZ *= 1.035;
                                aEg.thePlayer.motionX *= 1.035;
                            }

                            Client.a.g().c(Scaffold.class).sameY.wo().getName().equals("Off");
                            double d26;
                            int l = (d26 = MoveUtil.speed() - 0.24) == 0.0 ? 0 : (d26 < 0.0 ? -1 : 1);
                            aEg.thePlayer.motionY = 0.42;
                            break;
                        case 1:
                            MoveUtil.strafe();
                            double d25;
                            int j = (d25 = MoveUtil.speed() - 0.24) == 0.0 ? 0 : (d25 < 0.0 ? -1 : 1);
                            if (aEg.thePlayer.isPotionActive(Potion.moveSpeed)) {
                                int k = aEg.thePlayer.getActivePotionEffect(Potion.moveSpeed).amplifier + 1;
                            }

                            aEg.thePlayer.motionY = 0.33;
                            if (aEg.thePlayer.isPotionActive(Potion.moveSpeed) && aEg.thePlayer.getActivePotionEffect(Potion.moveSpeed).amplifier + 1 >= 2) {
                                aEg.thePlayer.motionZ *= 1.015;
                                aEg.thePlayer.motionX *= 1.015;
                            } else if (aEg.thePlayer.isPotionActive(Potion.moveSpeed)) {
                                aEg.thePlayer.motionZ *= 1.005;
                                aEg.thePlayer.motionX *= 1.005;
                            }

                            if (Client.a.g().c(Scaffold.class).sameY.wo().getName().equals("Off")) {
                            }
                            break;
                        case 2:
                            aEg.thePlayer.isPotionActive(Potion.moveSpeed);
                            if (this.e(Disabler.class).watchdogFly.wo() && this.e(Disabler.class).isEnabled()) {
                                MoveUtil.strafe();
                            }

                            aEg.thePlayer.motionY = 1.0 - aEg.thePlayer.posY % 1.0;
                    }
                }
            } else if (!aEg.thePlayer.onGround) {
            }

            if (aEg.thePlayer.motionY > 0.0) {
                aEg.gameSettings.keyBindJump.isKeyDown();
            }

            if (moveTicks != 20 && moveTicks != 21) {
                if (moveTicks > 20) {
                }
            } else {
                this.savedYaw = towerYaw;
            }

            if (moveTicks == 22 && !Client.a.g().c(Speed.class).isEnabled()) {
                if (aEg.thePlayer.cqL == 0) {
                    aEg.thePlayer.isPotionActive(Potion.moveSpeed);
                }

                if (aEg.thePlayer.motionY < 0.2 && aEg.thePlayer.motionY > 0.1) {
                    aEg.thePlayer.motionY -= 0.42;
                } else {
                    aEg.thePlayer.motionY = -0.0784000015258789;
                }

                aEg.thePlayer.isPotionActive(Potion.moveSpeed);
            }

            if (jumpStage == 2) {
                jumpStage = -1;
            }

            this.getParent().extraPlacements = 1;
        }
    };
    @EventLink
    public final Listener<PostStrafeEvent> onPostStrafe = var0 -> {};
    @EventLink
    public final Listener<MoveInputEvent> onMoveInput = var1x -> {
        if (MoveUtil.isMoving() && aEg.gameSettings.keyBindJump.isKeyDown() && !this.e(Speed.class).isEnabled()) {
            var1x.setForward(var1x.getForward() * 5.0F);
        }
    };
    @EventLink
    public final Listener<StrafeEvent> onStrafe = var0 -> {
        if (aEg.gameSettings.keyBindJump.isKeyDown() && (aEg.gameSettings.keyBindRight.isKeyDown() || aEg.gameSettings.keyBindLeft.isKeyDown())) {
            MoveUtil.strafe(0.25);
        }

        if (MoveUtil.speed() < 0.19 && aEg.gameSettings.keyBindJump.isKeyDown() && aEg.thePlayer.Zl > 9) {
            MoveUtil.strafe(0.19);
        }

        if (moveTicks >= 16) {
            aEg.thePlayer.isPotionActive(Potion.moveSpeed);
        }
    };
    @EventLink
    public final Listener<KeyboardInputEvent> onKeyboardInput = var1x -> {
        if (var1x.getKeyCode() == this.getParent().getKey() && !this.pendingToggle && aEg.gameSettings.keyBindJump.isKeyDown() && !Client.a.g().c(Speed.class).isEnabled()) {
            var1x.setCancelled();
            this.pendingToggle = true;
        }
    };
    @EventLink
    public final Listener<TickEvent> onTick = var0 -> {};
    @EventLink
    public final Listener<PacketSendEvent> onPacketSend = var1x -> {
        double d0 = Math.toRadians(aEg.thePlayer.pl);
        double d1 = -Math.sin(d0);
        if (Math.cos(d0) * 1.0 < 0.0) {
            boolean flag = true;
        } else {
            boolean flag1 = false;
        }

        if (var1x.dq() instanceof C08PacketPlayerBlockPlacement c08packetplayerblockplacement
            && c08packetplayerblockplacement.getPlacedBlockDirection() == 1
            && (c08packetplayerblockplacement.getStack() == null || c08packetplayerblockplacement.getStack().getItem() != Item.getItemFromBlock(Blocks.ice))) {
            this.placingUp = true;
            this.upwardPlacements++;
        } else if (var1x.dq() instanceof C08PacketPlayerBlockPlacement c08packetplayerblockplacement1
            && (c08packetplayerblockplacement1.getStack() == null || c08packetplayerblockplacement1.getStack().getItem() != Item.getItemFromBlock(Blocks.ice))) {
            this.upwardPlacements = 0;
            this.placingUp = false;
        }

        if (this.placingUp && !this.e(Speed.class).isEnabled() && this.upwardPlacements >= 2) {
            ;
        }
    };
    @EventLink
    public final Listener<JumpEvent> onJumpMedium = var1x -> {
        if (!this.e(Speed.class).isEnabled() && aEg.gameSettings.keyBindJump.isKeyDown() && !aEg.thePlayer.isPotionActive(Potion.moveSpeed)) {
            MoveUtil.strafe(0.15);
        }

        if (this.pendingToggle) {
            MoveUtil.strafe(0.0);
        }
    };

    public WatchdogTower(String var1, Scaffold scaffold) {
        super(var1, scaffold);
    }

    @Override
    public void onEnable() {
        this.placingUp = false;
        SlotComponent slotcomponent = this.d(SlotComponent.class);
        SlotComponent.setSlot(SlotUtil.vx());
        this.akn = 0;
        this.pendingToggle = false;
        this.targetZ = Double.NaN;
        moveTicks = 0;
        this.alignStep = 0;
        towerYaw = aEg.thePlayer.pl;
        this.aligning = false;
        if (aEg.thePlayer.onGround) {
            jumpStage = 0;
        } else {
            jumpStage = 100;
        }

        if (Client.a.g().c(Scaffold.class).sameY.wo().getName().equals("Off") && !aEg.thePlayer.onGround) {
            this.startedInAir = true;
        }

        this.jumpKeyHeld = false;
    }

    @Override
    public void onDisable() {
        this.aligning = false;
        this.startedInAir = false;
        this.pendingToggle = false;
        towerYaw = aEg.thePlayer.pl;
        jumpStage = 100;
        this.alignStep = 0;
        if (aEg.gameSettings.keyBindJump.isKeyDown()) {
            MoveUtil.strafe(0.23);
        }
    }

    public void updateRotation() {
        this.lastPlaceY = (int)Math.floor(aEg.thePlayer.posY);
        double d0 = aEg.thePlayer.pl;
        double radians = Math.toRadians(d0);
        double d2 = -Math.sin(radians);
        boolean flag = Math.cos(radians) * 1.0 < 0.0;
        Vector3d aka;
        if (flag) {
            RotationComponent.d(false);
            RotationComponent.setRotations(new Vector2f((float)(aEg.thePlayer.pl - 164.0F + (Math.random() - 0.5) * 3.0), 86.0F), 10.0, MovementFix.OFF);
            aka = new Vector3d(1.0, 0.0, 0.0);
        } else {
            aka = new Vector3d(0.0, 0.0, 1.0);
        }

        this.getParent().placeOffset = aka;
        RotationUtil.d(aka);
        if (!MoveUtil.isMoving() && !flag) {
            RotationComponent.d(false);
            float f = (float)(d0 + 164.0 + (Math.random() - 0.5) * 3.0);
            float f1 = 86.0F;
            RotationComponent.d(false);
            RotationComponent.setRotations(new Vector2f(f, f1), 10.0, MovementFix.OFF);
        }
    }

    private boolean isEnclosed() {
        int i = (int)Math.floor(aEg.thePlayer.posX);
        int j = (int)Math.floor(aEg.thePlayer.posY);
        int k = (int)Math.floor(aEg.thePlayer.posZ);
        Block[] ablock = new Block[]{PlayerUtil.o(i + 1, j, k), PlayerUtil.o(i - 1, j, k), PlayerUtil.o(i, j, k + 1), PlayerUtil.o(i, j, k - 1)};
        int l = ablock.length;

        for (int i1 = 0; i1 < l; i1++) {
            if (ablock[i1] instanceof BlockAir) {
                return false;
            }
        }

        return true;
    }

    private boolean canFitAt(double var1, double var3, double var5) {
        float f = aEg.thePlayer.width / 2.0F;
        double d0 = var1 - f;
        double d1 = var3;
        double d2 = var5 - f;
        double d3 = var1 + f;
        double d4 = var3 + aEg.thePlayer.height;
        double d5 = var5 + f;

        for (int i = MathHelper.floor_double(d0); i <= MathHelper.floor_double(d3); i++) {
            for (int j = MathHelper.floor_double(d1); j <= MathHelper.floor_double(d4); j++) {
                for (int k = MathHelper.floor_double(d2); k <= MathHelper.floor_double(d5); k++) {
                    if (!(PlayerUtil.o(i, j, k) instanceof BlockAir)) {
                        return false;
                    }
                }
            }
        }

        return true;
    }
}
