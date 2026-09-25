package com.alan.clients.module.impl.movement.speed;

import com.alan.clients.Client;
import com.alan.clients.module.impl.combat.Velocity;
import com.alan.clients.module.impl.movement.InventoryMove;
import com.alan.clients.module.impl.movement.Speed;
import com.alan.clients.module.impl.movement.inventorymove.bypass.WatchdogBypass;
import com.alan.clients.module.impl.player.Scaffold;
import com.alan.clients.module.impl.player.scaffold.tower.WatchdogTower;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.input.KeyboardInputEvent;
import com.alan.clients.newevent.impl.input.MoveInputEvent;
import com.alan.clients.newevent.impl.motion.JumpEvent;
import com.alan.clients.newevent.impl.motion.PostStrafeEvent;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.newevent.impl.motion.StrafeEvent;
import com.alan.clients.newevent.impl.other.BlockAABBEvent;
import com.alan.clients.newevent.impl.other.MoveEvent;
import com.alan.clients.newevent.impl.packet.PacketReceiveEvent;
import com.alan.clients.util.player.MoveUtil;
import com.alan.clients.value.Mode;
import com.alan.clients.value.impl.BooleanValue;
import com.alan.clients.value.impl.ModeValue;
import com.alan.clients.value.impl.SubMode;
import com.alan.clients.util.chat.ChatUtil;
import com.alan.clients.util.math.MathUtil;
import com.alan.clients.util.player.PlayerUtil;
import com.alan.clients.component.impl.player.BadPacketsComponent;
import com.alan.clients.component.impl.player.FallDistanceComponent;
import java.util.Objects;
import java.util.Random;
import net.minecraft.block.BlockIce;
import net.minecraft.block.BlockPackedIce;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.init.Blocks;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.potion.Potion;
import net.minecraft.util.MathHelper;

public class WatchdogSpeed extends Mode<Speed> {
    private final ModeValue mode = new ModeValue("Type", this)
        .add(new SubMode("Strafe"))
        .add(new SubMode("Low Strafe"))
        .add(new SubMode("NCP Strafe"))
        .setDefault("Low Strafe");
    public final BooleanValue airStrafe = new BooleanValue("Air Strafe", this, true);
    public final BooleanValue alwaysGlideStrafe = new BooleanValue("Always Glide Strafe", this, true);
    public final BooleanValue frictionOverride = new BooleanValue("Friction Override", this, true);
    public final BooleanValue frictionFullDisabler = new BooleanValue("Friction Full Disabler", this, true);
    public final BooleanValue damageBoost = new BooleanValue("Damage Boost", this, false);
    public final BooleanValue damageStrafeHypixelFlyDisabler = new BooleanValue("Damage Strafe (Hypixel Fly Disabler)", this, false);
    public final BooleanValue uHCMode = new BooleanValue("UHC Mode", this, false);
    public final BooleanValue alternateMotion = new BooleanValue("Alternate Motion", this, false);
    public static boolean damageBoostEnabled = false;
    boolean onIce = false;
    public static boolean damageStrafeEnabled = false;
    boolean onUnevenGround = false;
    boolean scaffoldActive = false;
    boolean appliedGlideBoost = false;
    boolean liftBoosted = false;
    private double motionDelta;
    private int collisionExpireTick;
    private int sG;
    private boolean suppressBoost;
    private int RD;
    private boolean verticallyBlocked;
    public static boolean pendingToggle;
    private boolean El;
    private boolean recentlyCollided;
    private static float strafeAngle = 0.0F;
    private static final float MAX_ANGLE_CHANGE = 8.0F;
    @EventLink
    public final Listener<MoveInputEvent> onMoveInput = var0 -> var0.setJump(false);
    @EventLink
    public final Listener<PacketReceiveEvent> onPacketReceive = var1x -> {
        if (!this.e(Velocity.class).isEnabled() && this.mode.wo().getName() == "Low Strafe") {
            Packet packet = var1x.getPacket();
            if (packet instanceof S12PacketEntityVelocity
                && ((S12PacketEntityVelocity)packet).getEntityID() == aEg.thePlayer.getEntityId()
                && (aEg.thePlayer.tR == 1 || aEg.thePlayer.tR == 2 || aEg.thePlayer.tR == 3)) {
                var1x.setCancelled();
            }
        }
    };
    @EventLink(value = 3)
    public final Listener<PreMotionEvent> onPreMotion = var1x -> {
        damageBoostEnabled = this.damageBoost.wo();
        damageStrafeEnabled = this.damageStrafeHypixelFlyDisabler.wo();
        if (MoveUtil.isMoving()) {
            if (aEg.thePlayer.ae < 10 && this.damageStrafeHypixelFlyDisabler.wo()) {
                MoveUtil.strafe();
            } else if (aEg.thePlayer.ae == 1) {
                MoveUtil.strafe();
            }
        } else if (aEg.thePlayer.ae == 1) {
            aEg.thePlayer.motionX *= -1.0;
            aEg.thePlayer.motionZ *= -1.0;
        }

        if (aEg.thePlayer.isInWater() && aEg.thePlayer.isInWeb && aEg.thePlayer.isInLava()) {
            this.suppressBoost = true;
        } else {
            if (this.e(Scaffold.class).isEnabled()) {
                this.scaffoldActive = true;
            }

            if (aEg.thePlayer.isCollidedHorizontally || aEg.thePlayer.Zl < 2) {
                this.recentlyCollided = true;
                this.collisionExpireTick = aEg.thePlayer.ticksExisted + 9;
            }

            if (!aEg.thePlayer.isCollidedHorizontally && aEg.thePlayer.ticksExisted > this.collisionExpireTick) {
                this.recentlyCollided = false;
                this.verticallyBlocked = false;
            }

            if (PlayerUtil.p(0.0, -1.0, 0.0) == Blocks.packed_ice || PlayerUtil.p(0.0, -1.0, 0.0) == Blocks.ice) {
                this.onIce = true;
            } else if (aEg.thePlayer.tR > 1) {
                this.onIce = false;
            }

            if (aEg.thePlayer.onGround) {
                this.appliedGlideBoost = false;
            }

            if (PlayerUtil.p(0.0, aEg.thePlayer.motionY, 0.0) != Blocks.air) {
                this.suppressBoost = false;
            }

            if (aEg.thePlayer.isCollidedVertically && !aEg.thePlayer.onGround && this.mode.wo().getName() == "Low Strafe" && PlayerUtil.b(2.0, true)) {
                this.suppressBoost = true;
            }

            double d0 = var1x.getPosY();
            if (Math.abs(d0 - Math.round(d0)) > 0.0325 && aEg.thePlayer.onGround) {
                this.onUnevenGround = true;
            } else if (aEg.thePlayer.onGround) {
                this.onUnevenGround = false;
            }

            if (aEg.thePlayer.onGround) {
                ;
            }

            if (aEg.thePlayer.isInWeb) {
                aEg.thePlayer.motionY = -0.0784000015258789;
                MoveUtil.strafe(MoveUtil.getAllowedHorizontalDistance() * 3.1);
            }

            if (aEg.thePlayer.isInWeb
                && aEg.thePlayer.isPotionActive(Potion.moveSpeed)
                && aEg.thePlayer.getActivePotionEffect(Potion.moveSpeed).amplifier + 1 >= 1
                && !aEg.thePlayer.onGround) {
                MoveUtil.strafe(MoveUtil.getAllowedHorizontalDistance() * 3.8);
            }

            if (aEg.thePlayer.isInWeb
                && aEg.thePlayer.isPotionActive(Potion.moveSpeed)
                && aEg.thePlayer.getActivePotionEffect(Potion.moveSpeed).amplifier + 1 >= 2
                && !aEg.thePlayer.onGround) {
                MoveUtil.strafe(MoveUtil.getAllowedHorizontalDistance() * 4.0);
            }

            PlayerUtil.b(0.0, true);
            double d1;
            int i = (d1 = FallDistanceComponent.cY - 1.4) == 0.0 ? 0 : (d1 < 0.0 ? -1 : 1);
            if (aEg.thePlayer.onGround && !(Math.abs(d0 - Math.round(d0)) > 0.03)) {
                ;
            }

            if (aEg.thePlayer.tR == 5 && !this.scaffoldActive) {
                this.scaffoldActive = false;
            }
        }
    };
    @EventLink
    public final Listener<MoveEvent> onMove = var0 -> {
        double d0;
        int i = (d0 = aEg.thePlayer.motionY - 0.0) == 0.0 ? 0 : (d0 < 0.0 ? -1 : 1);
    };
    @EventLink(value = 3)
    public final Listener<StrafeEvent> onStrafe = var1x -> {
        if (aEg.thePlayer.cqL > 1 && this.e(Scaffold.class).isEnabled() && aEg.thePlayer.ticksExisted % 3 == 1) {
            MoveUtil.stop();
        } else {
            if (aEg.thePlayer.ae < 2 && this.damageStrafeHypixelFlyDisabler.wo() && this.mode.wo().getName() != "Strafe") {
                this.suppressBoost = true;
            }

            if (aEg.thePlayer.ae < 20 && this.damageStrafeHypixelFlyDisabler.wo()) {
                MoveUtil.partialStrafePercent(100.0);
            }

            if (aEg.thePlayer.isInWater() && aEg.thePlayer.isInWeb && aEg.thePlayer.isInLava()) {
                this.suppressBoost = true;
            } else {
                if (this.e(Scaffold.class).isEnabled() && aEg.thePlayer.isPotionActive(Potion.moveSpeed) && aEg.gameSettings.keyBindJump.isKeyDown()) {
                    this.recentlyCollided = true;
                    this.collisionExpireTick = aEg.thePlayer.ticksExisted + 8;
                }

                double d0 = MathHelper.wrapAngleTo180_double(Math.toDegrees(MoveUtil.direction()));
                double d1 = MathHelper.wrapAngleTo180_double(Math.toDegrees(Math.atan2(aEg.thePlayer.motionZ, aEg.thePlayer.motionX)) - 90.0);
                if ((!(aEg.currentScreen instanceof GuiChest) || this.mode.wo().getName() == "Strafe" || !this.e(InventoryMove.class).isEnabled())
                    && !WatchdogBypass.inventoryClicking) {
                    if (aEg.currentScreen instanceof GuiChest && this.e(InventoryMove.class).isEnabled() || WatchdogBypass.inventoryClicking) {
                        return;
                    }
                } else {
                    pendingToggle = true;
                }

                label1264: {
                    label1224: {
                        {
                            String s = this.mode.wo().getName();
                            switch (s) {
                                case "Strafe":
                                    if (aEg.thePlayer.onGround && this.suppressBoost) {
                                        aEg.thePlayer.motionY = 0.42;
                                    }

                                    aEg.thePlayer.bjQ = true;
                                    if (aEg.thePlayer.onGround && !aEg.thePlayer.isPotionActive(Potion.moveSpeed) && MoveUtil.isMoving() && !this.recentlyCollided) {
                                        MoveUtil.strafe(MoveUtil.getAllowedHorizontalDistance());
                                        aEg.thePlayer.jump();
                                    } else if (aEg.thePlayer.onGround && MoveUtil.isMoving() && !this.recentlyCollided) {
                                        MoveUtil.strafe(MoveUtil.getAllowedHorizontalDistance());
                                        aEg.thePlayer.jump();
                                    } else if (aEg.thePlayer.onGround && MoveUtil.isMoving()) {
                                        MoveUtil.strafe(MoveUtil.vd());
                                        aEg.thePlayer.jump();
                                    }

                                    if (aEg.thePlayer.tR == 1 && aEg.thePlayer.crz - aEg.thePlayer.lastTickPosY > -0.43 && !this.suppressBoost && !this.recentlyCollided) {
                                        if (aEg.thePlayer.isPotionActive(Potion.moveSpeed)
                                            && aEg.thePlayer.getActivePotionEffect(Potion.moveSpeed).amplifier + 1 >= 2) {
                                            MoveUtil.strafe(0.48);
                                        } else if (aEg.thePlayer.isPotionActive(Potion.moveSpeed)
                                            && aEg.thePlayer.getActivePotionEffect(Potion.moveSpeed).amplifier + 1 == 1) {
                                            MoveUtil.strafe(0.4);
                                        } else {
                                            MoveUtil.strafe(0.33);
                                        }

                                        MoveUtil.strafe();
                                    }

                                    if (aEg.thePlayer.tR == 2 && this.airStrafe.wo() && !this.suppressBoost) {
                                        double d2 = aEg.thePlayer.motionX;
                                        double d3 = aEg.thePlayer.motionZ;
                                        aEg.thePlayer.motionZ = (aEg.thePlayer.motionZ * 1.0 + d3 * 2.0) / 3.0;
                                        aEg.thePlayer.motionX = (aEg.thePlayer.motionX * 1.0 + d2 * 2.0) / 3.0;
                                        double d4 = Math.sqrt(aEg.thePlayer.motionX * aEg.thePlayer.motionX + aEg.thePlayer.motionZ * aEg.thePlayer.motionZ);
                                        if (d4 < MoveUtil.getAllowedHorizontalDistance() - 0.05 || aEg.thePlayer.motionX == 0.0 || aEg.thePlayer.motionZ == 0.0
                                            )
                                         {
                                            ;
                                        }
                                    }

                                    if (aEg.thePlayer.tR == 3) {
                                        aEg.thePlayer.isPotionActive(Potion.moveSpeed);
                                    }

                                    if (!this.recentlyCollided
                                        && (
                                            aEg.thePlayer.tR == 2
                                                || aEg.thePlayer.tR == 3
                                                || aEg.thePlayer.tR == 4
                                                || aEg.thePlayer.tR == 5
                                                || aEg.thePlayer.tR == 6
                                                || aEg.thePlayer.tR == 7
                                                || aEg.thePlayer.tR == 8
                                        )
                                        && this.airStrafe.wo()) {
                                        ;
                                    }

                                    if (aEg.thePlayer.tR == 8 && PlayerUtil.p(0.0, aEg.thePlayer.motionY * 4.7, 0.0) != Blocks.air) {
                                        if (aEg.thePlayer.isPotionActive(Potion.moveSpeed)) {
                                            aEg.thePlayer.motionZ *= 1.001;
                                            aEg.thePlayer.motionX *= 1.001;
                                        } else {
                                            aEg.thePlayer.motionZ *= 1.001;
                                            aEg.thePlayer.motionX *= 1.001;
                                        }
                                    }

                                    double d5 = MathHelper.wrapAngleTo180_double(Math.toDegrees(MoveUtil.direction()));
                                    double d6 = MathHelper.wrapAngleTo180_double(
                                        Math.toDegrees(Math.atan2(aEg.thePlayer.motionZ, aEg.thePlayer.motionX)) - 90.0
                                    );
                                    double d7 = Math.abs(d5 - d6);
                                    double d8 = Math.hypot(
                                        aEg.thePlayer.motionX - (aEg.thePlayer.lastTickPosX - aEg.thePlayer.cry),
                                        aEg.thePlayer.motionZ - (aEg.thePlayer.lastTickPosZ - aEg.thePlayer.crA)
                                    );
                                    boolean flag1;
                                    if (!(d7 < 2.0) && !(MathUtil.n(d5, d6) > 90.0) && !pendingToggle && !(d8 < 0.0125)) {
                                        flag1 = true;
                                    } else {
                                        flag1 = false;
                                    }

                                    if (aEg.thePlayer.tR == 9 && PlayerUtil.p(0.0, aEg.thePlayer.motionY * 3.5, 0.0) != Blocks.air) {
                                        if (!this.suppressBoost) {
                                            if (aEg.thePlayer.isPotionActive(Potion.moveSpeed)) {
                                                aEg.thePlayer.motionZ *= 1.003;
                                                aEg.thePlayer.motionX *= 1.003;
                                            } else if (!this.e(Scaffold.class).isEnabled()) {
                                                aEg.thePlayer.motionZ *= 1.003;
                                                aEg.thePlayer.motionX *= 1.003;
                                            }

                                            if (this.alwaysGlideStrafe.wo() || flag1) {
                                                aEg.thePlayer.motionY += 0.075;
                                                MoveUtil.strafe();
                                            }
                                        }

                                        if (this.alwaysGlideStrafe.wo()
                                            && !this.recentlyCollided
                                            && (
                                                Math.sqrt(aEg.thePlayer.motionX * aEg.thePlayer.motionX + aEg.thePlayer.motionZ * aEg.thePlayer.motionZ)
                                                        < MoveUtil.getAllowedHorizontalDistance() - 0.02
                                                    || aEg.thePlayer.motionX == 0.0
                                                    || aEg.thePlayer.motionZ == 0.0
                                            )
                                            && !this.suppressBoost) {
                                            ;
                                        }
                                    }

                                    if (aEg.thePlayer.tR == 10 && PlayerUtil.p(0.0, aEg.thePlayer.motionY * 2.0, 0.0) != Blocks.air) {
                                        if (!this.suppressBoost && !this.recentlyCollided) {
                                            if (aEg.thePlayer.isPotionActive(Potion.moveSpeed)) {
                                                aEg.thePlayer.motionZ *= 1.005;
                                                aEg.thePlayer.motionX *= 1.005;
                                            } else {
                                                aEg.thePlayer.motionZ *= 1.005;
                                                aEg.thePlayer.motionX *= 1.005;
                                            }
                                        }

                                        if (this.alwaysGlideStrafe.wo() && !this.recentlyCollided) {
                                            if ((
                                                    Math.sqrt(aEg.thePlayer.motionX * aEg.thePlayer.motionX + aEg.thePlayer.motionZ * aEg.thePlayer.motionZ)
                                                            < MoveUtil.getAllowedHorizontalDistance()
                                                        || aEg.thePlayer.motionX == 0.0
                                                        || aEg.thePlayer.motionZ == 0.0
                                                )
                                                && !this.suppressBoost
                                                && !this.recentlyCollided) {
                                                MoveUtil.strafe(MoveUtil.getAllowedHorizontalDistance());
                                            }

                                            MoveUtil.strafe();
                                        }
                                    }

                                    if (PlayerUtil.p(0.0, aEg.thePlayer.motionY, 0.0) != Blocks.air) {
                                        MoveUtil.strafe();
                                        if (aEg.thePlayer.tR == 11) {
                                            if (aEg.thePlayer.isPotionActive(Potion.moveSpeed)
                                                && aEg.thePlayer.getActivePotionEffect(Potion.moveSpeed).amplifier + 1 >= 2) {
                                                MoveUtil.strafe(0.45);
                                            } else if (aEg.thePlayer.isPotionActive(Potion.moveSpeed)
                                                && aEg.thePlayer.getActivePotionEffect(Potion.moveSpeed).amplifier + 1 == 1) {
                                                MoveUtil.strafe(0.375);
                                            } else {
                                                MoveUtil.strafe(0.31);
                                            }
                                        }
                                    }

                                    double d20 = MoveUtil.vd() / 2.29;
                                    this.motionDelta = Math.hypot(
                                        aEg.thePlayer.motionX - (aEg.thePlayer.lastTickPosX - aEg.thePlayer.cry),
                                        aEg.thePlayer.motionZ - (aEg.thePlayer.lastTickPosZ - aEg.thePlayer.crA)
                                    );
                                    if (this.motionDelta < 0.0125 && this.frictionOverride.wo()) {
                                        MoveUtil.strafe();
                                    }

                                    if (this.motionDelta > 0.0125 && this.motionDelta < MoveUtil.vd()) {
                                        this.frictionOverride.wo();
                                    }

                                    MoveUtil.useDiagonalSpeed();
                                    if (MoveUtil.speed() < 0.125) {
                                        MoveUtil.strafe(0.125);
                                    }
                                    break label1264;
                                case "Low Strafe":
                                    break label1224;
                                case "NCP Strafe":
                                    break;
                                default:
                                    break label1264;
                            }
                        }

                        if (this.damageStrafeHypixelFlyDisabler.wo()) {
                            ;
                        }

                        if (aEg.thePlayer.onGround && MoveUtil.isMoving()) {
                            MoveUtil.strafe(MoveUtil.getAllowedHorizontalDistance());
                            aEg.thePlayer.jump();
                        }

                        double d21 = aEg.thePlayer.posY % 1.0;
                        if (aEg.thePlayer.tR == 1 && !this.suppressBoost) {
                            if (aEg.thePlayer.isPotionActive(Potion.moveSpeed) && aEg.thePlayer.getActivePotionEffect(Potion.moveSpeed).amplifier + 1 >= 2) {
                                MoveUtil.strafe(0.48);
                            } else if (aEg.thePlayer.isPotionActive(Potion.moveSpeed)
                                && aEg.thePlayer.getActivePotionEffect(Potion.moveSpeed).amplifier + 1 == 1) {
                                MoveUtil.strafe(0.4);
                            } else {
                                MoveUtil.strafe(0.334);
                            }

                            aEg.thePlayer.motionY += 0.03F;
                            MoveUtil.preventDiagonalSpeed();
                        }

                        if (aEg.thePlayer.tR == 2 && !this.suppressBoost) {
                            double d17 = aEg.thePlayer.motionX;
                            double d18 = aEg.thePlayer.motionZ;
                            MoveUtil.strafe();
                            aEg.thePlayer.motionZ = (aEg.thePlayer.motionZ * 1.0 + d18 * 3.0) / 4.0;
                            aEg.thePlayer.motionX = (aEg.thePlayer.motionX * 1.0 + d17 * 3.0) / 4.0;
                            MoveUtil.useDiagonalSpeed();
                            aEg.thePlayer.motionY -= 0.005F;
                        }

                        if (aEg.thePlayer.tR == 3 && !this.suppressBoost) {
                            MoveUtil.useDiagonalSpeed();
                            aEg.thePlayer.motionY -= 0.04F;
                        }

                        if (aEg.thePlayer.tR == 4 && (aEg.thePlayer.ae > 1 || this.damageBoost.wo()) && !this.suppressBoost) {
                            MoveUtil.useDiagonalSpeed();
                            aEg.thePlayer.motionY -= 0.05F;
                        }

                        if (aEg.thePlayer.tR == 5 && !this.suppressBoost) {
                            MoveUtil.useDiagonalSpeed();
                        }

                        if (aEg.thePlayer.tR == 6 && !this.suppressBoost && aEg.thePlayer.ae > 10 && this.damageStrafeHypixelFlyDisabler.wo()) {
                            MoveUtil.useDiagonalSpeed();
                            aEg.thePlayer.motionZ *= 1.01;
                            aEg.thePlayer.motionX *= 1.01;
                        }

                        if (aEg.thePlayer.tR == 7 && !this.suppressBoost && aEg.thePlayer.ae > 10 && this.damageStrafeHypixelFlyDisabler.wo()) {
                            MoveUtil.useDiagonalSpeed();
                            if (!aEg.thePlayer.isPotionActive(Potion.moveSpeed)) {
                                aEg.thePlayer.motionZ *= 1.01;
                                aEg.thePlayer.motionX *= 1.01;
                            } else {
                                aEg.thePlayer.motionZ *= 1.008;
                                aEg.thePlayer.motionX *= 1.008;
                            }
                        }

                        if (aEg.thePlayer.tR == 8 && PlayerUtil.p(0.0, aEg.thePlayer.motionY * 3.0, 0.0) != Blocks.air) {
                            MoveUtil.useDiagonalSpeed();
                            if (this.alwaysGlideStrafe.wo()) {
                                aEg.thePlayer.motionY += 0.075F;
                                MoveUtil.strafe();
                            }
                        }

                        if (aEg.thePlayer.tR == 9 && PlayerUtil.p(0.0, aEg.thePlayer.motionY * 3.0, 0.0) != Blocks.air) {
                            MoveUtil.useDiagonalSpeed();
                            if (this.alwaysGlideStrafe.wo()) {
                                MoveUtil.strafe();
                            }
                        }

                        if (PlayerUtil.p(0.0, aEg.thePlayer.motionY, 0.0) != Blocks.air) {
                            if (aEg.thePlayer.isPotionActive(Potion.moveSpeed) && aEg.thePlayer.getActivePotionEffect(Potion.moveSpeed).amplifier + 1 >= 2) {
                                MoveUtil.strafe(0.45);
                            } else if (aEg.thePlayer.isPotionActive(Potion.moveSpeed)
                                && aEg.thePlayer.getActivePotionEffect(Potion.moveSpeed).amplifier + 1 == 1) {
                                MoveUtil.strafe(0.375);
                            } else {
                                MoveUtil.strafe(0.31);
                            }
                        }

                        if (this.e(Scaffold.class).isEnabled() && aEg.gameSettings.keyBindJump.isKeyDown()) {
                            if (aEg.thePlayer.onGround && pendingToggle) {
                                this.getParent().toggle();
                                pendingToggle = false;
                            }
                        } else if ((this.suppressBoost || aEg.thePlayer.tR > 4 || aEg.thePlayer.onGround) && pendingToggle) {
                            this.getParent().toggle();
                            pendingToggle = false;
                        }

                        if (MoveUtil.speed() <= 0.125) {
                            MoveUtil.strafe(0.125);
                        }

                        double d19 = Math.hypot(
                            aEg.thePlayer.motionX - (aEg.thePlayer.lastTickPosX - aEg.thePlayer.cry),
                            aEg.thePlayer.motionZ - (aEg.thePlayer.lastTickPosZ - aEg.thePlayer.crA)
                        );
                        if (d19 <= 0.0125) {
                            MoveUtil.strafe();
                        }
                        break label1264;
                    }

                    MoveUtil.useDiagonalSpeed();
                    PlayerUtil.b(0.5, true);
                    if (this.damageStrafeHypixelFlyDisabler.wo()) {
                        ;
                    }

                    if (aEg.thePlayer.isPotionActive(Potion.moveSpeed) && aEg.thePlayer.onGround) {
                        if (this.e(Scaffold.class).isEnabled() && aEg.gameSettings.keyBindJump.isKeyDown()
                            || this.recentlyCollided
                            || !(MoveUtil.speed() < MoveUtil.getAllowedHorizontalDistance()) && aEg.thePlayer.ae <= 5 && this.damageBoost.wo()) {
                            if (!this.e(Scaffold.class).isEnabled() || this.recentlyCollided || !(MoveUtil.speed() < 0.29) && aEg.thePlayer.ae <= 5 && this.damageBoost.wo()) {
                                if (MoveUtil.speed() < 0.29 && aEg.thePlayer.ae < 5 && this.damageBoost.wo() && !this.recentlyCollided) {
                                    aEg.thePlayer.jump();
                                    if (aEg.thePlayer.ae > 1) {
                                        MoveUtil.strafe();
                                    }
                                } else {
                                    MoveUtil.strafe(0.29);
                                    aEg.thePlayer.jump();
                                }
                            } else {
                                MoveUtil.strafe(0.29);
                                aEg.thePlayer.jump();
                            }
                        } else {
                            MoveUtil.strafe(this.alwaysGlideStrafe.wo() ? MoveUtil.getAllowedHorizontalDistance() : MoveUtil.getAllowedHorizontalDistance());
                            aEg.thePlayer.jump();
                        }
                    } else if (aEg.thePlayer.onGround) {
                        if (this.recentlyCollided || !(MoveUtil.speed() < MoveUtil.getAllowedHorizontalDistance()) && aEg.thePlayer.ae <= 5 && this.damageBoost.wo()) {
                            if (this.e(Scaffold.class).isEnabled()) {
                                MoveUtil.strafe(0.23);
                                aEg.thePlayer.jump();
                            } else if (!(MoveUtil.speed() < MoveUtil.vd()) && aEg.thePlayer.ae <= 5 && this.damageBoost.wo()) {
                                aEg.thePlayer.jump();
                                if (aEg.thePlayer.ae > 1) {
                                    MoveUtil.strafe();
                                }
                            } else {
                                MoveUtil.strafe(MoveUtil.vd());
                                aEg.thePlayer.jump();
                            }
                        } else {
                            MoveUtil.strafe(MoveUtil.getAllowedHorizontalDistance());
                            aEg.thePlayer.jump();
                        }
                    }

                    if (aEg.thePlayer.tR == 1 && !this.suppressBoost) {
                        this.liftBoosted = true;
                        aEg.thePlayer.motionY += 0.057F;
                        if (!aEg.thePlayer.isPotionActive(Potion.moveSpeed)
                            || this.e(Scaffold.class).isEnabled() && aEg.gameSettings.keyBindJump.isKeyDown()
                            || aEg.thePlayer.getActivePotionEffect(Potion.moveSpeed).amplifier + 1 < 2
                            || this.suppressBoost
                            || this.recentlyCollided
                            || !(MoveUtil.speed() < 0.48) && aEg.thePlayer.ae <= 5 && this.damageBoost.wo()) {
                            if (!aEg.thePlayer.isPotionActive(Potion.moveSpeed)
                                || aEg.thePlayer.getActivePotionEffect(Potion.moveSpeed).amplifier + 1 < 2
                                || !(MoveUtil.speed() < 0.4) && aEg.thePlayer.ae <= 5 && this.damageBoost.wo()) {
                                if (this.recentlyCollided
                                    || !aEg.thePlayer.isPotionActive(Potion.moveSpeed)
                                    || aEg.thePlayer.getActivePotionEffect(Potion.moveSpeed).amplifier + 1 != 1
                                    || !(MoveUtil.speed() < 0.41) && aEg.thePlayer.ae <= 5 && this.damageBoost.wo()) {
                                    if (!this.recentlyCollided
                                        || !aEg.thePlayer.isPotionActive(Potion.moveSpeed)
                                        || aEg.thePlayer.getActivePotionEffect(Potion.moveSpeed).amplifier + 1 != 1
                                        || !(MoveUtil.speed() < 0.41) && aEg.thePlayer.ae <= 5 && this.damageBoost.wo()) {
                                        if (!(MoveUtil.speed() < 0.33) && aEg.thePlayer.ae <= 3 && this.damageBoost.wo()) {
                                            MoveUtil.strafe();
                                        } else {
                                            MoveUtil.strafe(0.3325);
                                        }
                                    } else {
                                        MoveUtil.strafe(0.35);
                                    }
                                } else {
                                    MoveUtil.strafe(0.41);
                                }
                            } else {
                                MoveUtil.strafe(0.4);
                            }
                        } else {
                            MoveUtil.strafe(0.4875);
                        }
                    }

                    if (aEg.thePlayer.tR == 2 && this.airStrafe.wo()) {
                        double d9 = aEg.thePlayer.motionX;
                        double d10 = aEg.thePlayer.motionZ;
                        aEg.thePlayer.motionZ = (aEg.thePlayer.motionZ * 1.0 + d10 * 2.0) / 3.0;
                        aEg.thePlayer.motionX = (aEg.thePlayer.motionX * 1.0 + d9 * 2.0) / 3.0;
                        if (aEg.thePlayer.ae < 3) {
                            ;
                        }
                    }

                    if (aEg.thePlayer.tR == 3 && !this.suppressBoost) {
                        aEg.thePlayer.motionY -= 0.1309F;
                    }

                    if (aEg.thePlayer.tR == 4 && !this.suppressBoost) {
                        aEg.thePlayer.motionY -= 0.2;
                    }

                    if ((aEg.thePlayer.tR <= 1 || aEg.thePlayer.tR >= 6 || !aEg.thePlayer.isPotionActive(Potion.moveSpeed) || !this.airStrafe.wo())
                        && aEg.thePlayer.tR > 1
                        && aEg.thePlayer.tR < 6
                        && this.airStrafe.wo()) {
                    }

                    double d11 = MathHelper.wrapAngleTo180_double(Math.toDegrees(MoveUtil.direction()));
                    double d12 = MathHelper.wrapAngleTo180_double(Math.toDegrees(Math.atan2(aEg.thePlayer.motionZ, aEg.thePlayer.motionX)) - 90.0);
                    double d13 = Math.abs(d11 - d12);
                    double d14 = Math.hypot(
                        aEg.thePlayer.motionX - (aEg.thePlayer.lastTickPosX - aEg.thePlayer.cry),
                        aEg.thePlayer.motionZ - (aEg.thePlayer.lastTickPosZ - aEg.thePlayer.crA)
                    );
                    boolean flag2;
                    if (!(d13 < 5.0) && !(MathUtil.n(d11, d12) > 90.0) && !pendingToggle && !(d14 < 0.0125)) {
                        flag2 = true;
                    } else {
                        flag2 = false;
                    }

                    if (d13 < 7.0) {
                        this.alwaysGlideStrafe.wo();
                    }

                    if (aEg.thePlayer.tR == 6
                        && !this.suppressBoost
                        && PlayerUtil.ae(aEg.thePlayer.motionY * 3.0)
                        && (this.alwaysGlideStrafe.wo() || flag2)
                        && (MoveUtil.speed() < MoveUtil.getAllowedHorizontalDistance() * 0.994 || !this.damageBoost.wo() || aEg.thePlayer.ae > 5)
                        && (!this.suppressBoost || !this.damageStrafeHypixelFlyDisabler.wo())
                        && aEg.thePlayer.ae > 1) {
                        aEg.thePlayer.motionY += 0.075;
                        MoveUtil.strafe(MoveUtil.speed());
                        double d15 = Math.sqrt(aEg.thePlayer.motionX * aEg.thePlayer.motionX + aEg.thePlayer.motionZ * aEg.thePlayer.motionZ);
                        if ((d15 < MoveUtil.getAllowedHorizontalDistance() || aEg.thePlayer.motionX == 0.0 || aEg.thePlayer.motionZ == 0.0)
                            && !this.suppressBoost
                            && !this.recentlyCollided
                            && aEg.thePlayer.isPotionActive(Potion.moveSpeed)
                            && !this.e(Scaffold.class).isEnabled()) {
                            MoveUtil.strafe(MoveUtil.getAllowedHorizontalDistance() * 0.994);
                        } else if (!this.suppressBoost
                            && !this.e(Scaffold.class).isEnabled()
                            && (d15 < MoveUtil.getAllowedHorizontalDistance() || aEg.thePlayer.motionX == 0.0 || aEg.thePlayer.motionZ == 0.0)) {
                            MoveUtil.strafe(MoveUtil.getAllowedHorizontalDistance() - 0.05);
                        }
                    }

                    if (aEg.thePlayer.tR < 7
                        && PlayerUtil.p(0.0, aEg.thePlayer.motionY, 0.0) != Blocks.air
                        && aEg.thePlayer.isPotionActive(Potion.moveSpeed)
                        && !this.onUnevenGround) {
                        this.verticallyBlocked = true;
                        this.collisionExpireTick = aEg.thePlayer.ticksExisted + 9;
                        this.recentlyCollided = true;
                    }

                    if (aEg.thePlayer.tR == 7
                        && !this.suppressBoost
                        && PlayerUtil.ae(aEg.thePlayer.motionY * 2.0)
                        && !this.e(Scaffold.class).isEnabled()
                        && (MoveUtil.speed() < MoveUtil.getAllowedHorizontalDistance() * 1.1 || !this.damageBoost.wo() || aEg.thePlayer.ae > 5)
                        && aEg.thePlayer.ae > 1) {
                        MoveUtil.strafe(this.alwaysGlideStrafe.wo() ? MoveUtil.speed() : MoveUtil.getAllowedHorizontalDistance() * 1.1);
                    }

                    if (!PlayerUtil.ae(aEg.thePlayer.motionY)
                        || aEg.thePlayer.ae <= 1
                        || this.suppressBoost
                        || this.uHCMode.wo()
                        || (aEg.thePlayer.tR <= 6 || !this.alwaysGlideStrafe.wo()) && (aEg.thePlayer.tR <= 6 || this.alwaysGlideStrafe.wo())
                        || this.appliedGlideBoost
                        || !(MoveUtil.speed() < MoveUtil.getAllowedHorizontalDistance() * 1.095) && this.damageBoost.wo() && aEg.thePlayer.ae <= 5) {
                        if ((!PlayerUtil.ae(aEg.thePlayer.motionY) || this.suppressBoost || aEg.thePlayer.ae <= 1 || this.uHCMode.wo() || aEg.thePlayer.tR <= 6 || !this.alwaysGlideStrafe.wo())
                            && (
                                aEg.thePlayer.tR <= 6
                                    || this.alwaysGlideStrafe.wo()
                                    || this.appliedGlideBoost
                                    || !(MoveUtil.speed() < MoveUtil.getAllowedHorizontalDistance()) && this.damageBoost.wo() && aEg.thePlayer.ae <= 5
                            )) {
                            if (!PlayerUtil.ae(aEg.thePlayer.motionY)
                                || aEg.thePlayer.tR <= 5
                                || this.appliedGlideBoost
                                || aEg.thePlayer.ae <= 1
                                || !(MoveUtil.speed() < MoveUtil.getAllowedHorizontalDistance()) && this.damageBoost.wo() && aEg.thePlayer.ae <= 5) {
                                if (PlayerUtil.ae(aEg.thePlayer.motionY) && aEg.thePlayer.tR > 5 && !this.appliedGlideBoost && aEg.thePlayer.ae > 1 && this.damageBoost.wo()) {
                                    MoveUtil.strafe();
                                }
                            } else {
                                MoveUtil.strafe(MoveUtil.getAllowedHorizontalDistance());
                                this.appliedGlideBoost = true;
                            }
                        } else {
                            MoveUtil.strafe(MoveUtil.getAllowedHorizontalDistance());
                            this.appliedGlideBoost = true;
                        }
                    } else {
                        if (this.alwaysGlideStrafe.wo()) {
                            if (aEg.thePlayer.isPotionActive(Potion.moveSpeed) && aEg.thePlayer.getActivePotionEffect(Potion.moveSpeed).amplifier + 1 >= 2) {
                                MoveUtil.strafe(MoveUtil.getAllowedHorizontalDistance() * 1.093);
                            } else {
                                MoveUtil.strafe(MoveUtil.getAllowedHorizontalDistance() * 1.095);
                            }
                        } else if (aEg.thePlayer.isPotionActive(Potion.moveSpeed) && aEg.thePlayer.getActivePotionEffect(Potion.moveSpeed).amplifier + 1 >= 2) {
                            MoveUtil.strafe(MoveUtil.getAllowedHorizontalDistance() * 1.093);
                        } else {
                            MoveUtil.strafe(MoveUtil.getAllowedHorizontalDistance() * 1.095);
                        }

                        this.appliedGlideBoost = true;
                    }

                    if (this.e(Scaffold.class).isEnabled() && aEg.gameSettings.keyBindJump.isKeyDown()) {
                        if (aEg.thePlayer.onGround && pendingToggle) {
                            this.getParent().toggle();
                            pendingToggle = false;
                        }
                    } else if ((this.suppressBoost || aEg.thePlayer.tR > 3 || aEg.thePlayer.onGround) && pendingToggle) {
                        this.getParent().toggle();
                        pendingToggle = false;
                    }

                    double d16 = Math.hypot(
                        aEg.thePlayer.motionX - (aEg.thePlayer.lastTickPosX - aEg.thePlayer.cry),
                        aEg.thePlayer.motionZ - (aEg.thePlayer.lastTickPosZ - aEg.thePlayer.crA)
                    );
                    if (d16 < 0.0125) {
                        MoveUtil.strafe();
                    }

                    if (!aEg.thePlayer.onGround && aEg.thePlayer.tR != 11) {
                        ;
                    }

                    if (MoveUtil.speed() < 0.1245 && this.frictionOverride.wo()) {
                        MoveUtil.strafe(0.1245);
                    }

                    if (aEg.thePlayer.tR > 1 && !aEg.thePlayer.isCollidedHorizontally) {
                        MoveUtil.moveFlying(9.0E-4);
                    }
                }

                if (MoveUtil.speed() < 0.45
                    || aEg.thePlayer.isPotionActive(Potion.moveSpeed)
                        && aEg.thePlayer.getActivePotionEffect(Potion.moveSpeed).amplifier + 1 == 1
                        && MoveUtil.speed() < 0.55
                    || aEg.thePlayer.isPotionActive(Potion.moveSpeed)
                        && aEg.thePlayer.getActivePotionEffect(Potion.moveSpeed).amplifier + 1 >= 2
                        && MoveUtil.speed() < 0.55) {
                    if (this.onIce && aEg.thePlayer.onGround && !this.suppressBoost) {
                        aEg.thePlayer.motionX *= 1.45;
                        aEg.thePlayer.motionZ *= 1.45;
                    }

                    if (this.onIce
                        && (PlayerUtil.p(0.0, aEg.thePlayer.motionY, 0.0) == Blocks.ice || PlayerUtil.p(0.0, aEg.thePlayer.motionY, 0.0) == Blocks.packed_ice)
                        && !this.suppressBoost
                        && !this.suppressBoost) {
                        aEg.thePlayer.motionX *= 1.01;
                        aEg.thePlayer.motionZ *= 1.01;
                    }

                    if (this.onIce && aEg.thePlayer.tR == 1 && !this.suppressBoost) {
                        aEg.thePlayer.motionX *= 1.15;
                        aEg.thePlayer.motionZ *= 1.15;
                    }

                    if (this.onIce && aEg.thePlayer.tR > 1 && !this.suppressBoost && this.mode.wo().getName() == "Low Strafe") {
                        aEg.thePlayer.motionX *= 1.015;
                        aEg.thePlayer.motionZ *= 1.015;
                    }
                }

                if (MathUtil.n(d0, d1) < 5.0
                    && Objects.equals(this.mode.wo().getName(), "Low Strafe")
                    && this.frictionFullDisabler.wo()
                    && (!this.verticallyBlocked || aEg.thePlayer.tR > 2 && aEg.thePlayer.tR < 6)
                    && (!aEg.gameSettings.keyBindJump.isKeyDown() || !this.e(Scaffold.class).isEnabled())
                    && aEg.thePlayer.Zl > 10
                    && !this.suppressBoost
                    && aEg.thePlayer.ae > 1
                    && MoveUtil.speed() < MoveUtil.vd() - 0.0323
                    && aEg.thePlayer.tR < 18) {
                    ChatUtil.c(aEg.thePlayer.tR);
                    if (aEg.thePlayer.isPotionActive(Potion.moveSpeed)) {
                        var1x.setSpeed(MoveUtil.vd() - 0.003);
                    } else {
                        var1x.setSpeed(MoveUtil.vd() - 0.001);
                    }
                }
            }
        }
    };
    @EventLink
    public final Listener<JumpEvent> onJump = var1x -> {
        if (aEg.thePlayer.isInLava() || aEg.thePlayer.isInWeb) {
            var1x.setCancelled();
        }

        if (!this.suppressBoost && this.mode.wo().getName() == "Low Strafe" && this.alternateMotion.wo()) {
            var1x.setJumpMotion(0.42001F);
        } else if (this.mode.wo().getName() == "Low Strafe" && this.alternateMotion.wo()) {
            var1x.setJumpMotion(0.42001F);
        }
    };
    @EventLink
    public final Listener<PostStrafeEvent> onPostStrafe = var1x -> {
        if (aEg.thePlayer.ae > 1 || !this.damageBoost.wo() && aEg.thePlayer.tR < 12) {
            double d0 = MathHelper.wrapAngleTo180_double(Math.toDegrees(MoveUtil.direction()));
            double d1 = MathHelper.wrapAngleTo180_double(Math.toDegrees(Math.atan2(aEg.thePlayer.motionZ, aEg.thePlayer.motionX)) - 90.0);
            if (this.airStrafe.wo() && this.mode.wo().getName() == "Strafe") {
                if (MathUtil.n(d0, d1) > 90.0) {
                    if (aEg.thePlayer.isPotionActive(Potion.moveSpeed)) {
                        MoveUtil.a(MoveUtil.speed(), (float)d1 - 180.0F);
                    } else {
                        MoveUtil.a(MoveUtil.speed(), (float)d1 - 180.0F);
                    }
                } else if (this.damageStrafeHypixelFlyDisabler.wo() && aEg.thePlayer.ae > 20) {
                    double d2 = aEg.thePlayer.motionX;
                    double d3 = aEg.thePlayer.motionZ;
                    MoveUtil.strafe();
                    aEg.thePlayer.motionZ = (aEg.thePlayer.motionZ * 3.0 + d3 * 1.0) / 4.0;
                    aEg.thePlayer.motionX = (aEg.thePlayer.motionX * 3.0 + d2 * 1.0) / 4.0;
                }
            } else if (this.airStrafe.wo()) {
                if (MathUtil.n(d0, d1) > 90.0) {
                    MoveUtil.a(MoveUtil.speed(), (float)d1 - 180.0F);
                } else {
                    if (this.damageStrafeHypixelFlyDisabler.wo() && aEg.thePlayer.ae > 20) {
                        double d4 = aEg.thePlayer.motionX;
                        double d5 = aEg.thePlayer.motionZ;
                        MoveUtil.strafe();
                        aEg.thePlayer.motionZ = (aEg.thePlayer.motionZ * 3.0 + d5 * 1.0) / 4.0;
                        aEg.thePlayer.motionX = (aEg.thePlayer.motionX * 3.0 + d4 * 1.0) / 4.0;
                    }

                    double d6 = aEg.thePlayer.motionX;
                    double d7 = aEg.thePlayer.motionZ;
                }
            }
        }
    };
    @EventLink
    public final Listener<BlockAABBEvent> onBlockAABB = var0 -> {
        if (!(var0.getBlock() instanceof BlockIce) && !(var0.getBlock() instanceof BlockPackedIce) && aEg.thePlayer.cqL > 1) {
        }
    };
    @EventLink
    public final Listener<KeyboardInputEvent> onKeyboardInput = var1x -> {
        if (this.mode.wo().getName() != "Strafe" && var1x.getKeyCode() == this.getParent().getKey() && !pendingToggle) {
            var1x.setCancelled();
            pendingToggle = true;
        }
    };

    public WatchdogSpeed(String var1, Speed speed) {
        super(var1, speed);
    }

    @Override
    public void onEnable() {
        pendingToggle = false;
        if (!BadPacketsComponent.bad(true, true, false, true, true) && !this.e(Scaffold.class).isEnabled()) {
            Random random = new Random();
            random.nextFloat();
            random.nextFloat();
        }

        this.onUnevenGround = false;
        this.liftBoosted = false;
        Client.a.g().c(Scaffold.class).isEnabled();
        this.appliedGlideBoost = false;
        if (this.e(Scaffold.class).sameY.wo().getName().equals("On") && Client.a.g().c(Scaffold.class).isEnabled()) {
            MoveUtil.stop();
        }

        pendingToggle = false;
        if (aEg.thePlayer.tR > 2) {
            this.suppressBoost = true;
        }

        this.recentlyCollided = false;
    }

    @Override
    public void onDisable() {
        WatchdogTower.jumpStage = 0;
        WatchdogTower.moveTicks = 0;
        this.onIce = false;
        ChatUtil.c(aEg.thePlayer.tR + ": " + aEg.thePlayer.ae);
        this.liftBoosted = false;
        if (Client.a.g().c(Scaffold.class).isEnabled()) {
            aEg.thePlayer.motionX *= 0.85;
            aEg.thePlayer.motionZ *= 0.85;
        }

        if (this.e(Scaffold.class).isEnabled() && aEg.gameSettings.keyBindJump.isKeyDown() && !aEg.thePlayer.isPotionActive(Potion.moveSpeed)) {
            MoveUtil.strafe(0.355);
        }

        this.suppressBoost = false;
        aEg.thePlayer.bjQ = false;
    }

    public static void simulateStrafe(MoveEvent moveEvent, double var1, float var3, float var4, float var5) {
        if (var3 != 0.0F || var4 != 0.0F) {
            float f = var5;
            boolean flag = var3 < 0.0F;
            float f1 = 90.0F * (var3 > 0.0F ? 0.5F : (flag ? -0.5F : 1.0F));
            if (flag) {
                f += 180.0F;
            }

            if (var4 > 0.0F) {
                f -= f1;
            } else if (var4 < 0.0F) {
                f += f1;
            }

            float f2 = (f + 360.0F) % 360.0F;
            float f3 = f2 - strafeAngle;
            float f4 = (f3 + 180.0F) % 360.0F - 180.0F;
            if (Math.abs(f4) < 8.0F) {
                strafeAngle = f2;
            } else {
                strafeAngle = strafeAngle + Math.signum(f4) * 8.0F;
            }

            strafeAngle = (strafeAngle + 360.0F) % 360.0F;
            double d0 = StrictMath.cos(Math.toRadians(strafeAngle + 90.0));
            double d1 = StrictMath.cos(Math.toRadians(strafeAngle));
            moveEvent.setPosX(d0 * var1);
            moveEvent.setPosZ(d1 * var1);
        }
    }
}
