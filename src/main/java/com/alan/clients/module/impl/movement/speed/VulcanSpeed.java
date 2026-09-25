package com.alan.clients.module.impl.movement.speed;

import com.alan.clients.component.impl.player.SlotComponent;
import com.alan.clients.module.impl.combat.Velocity;
import com.alan.clients.module.impl.movement.Speed;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.input.MoveInputEvent;
import com.alan.clients.newevent.impl.motion.JumpEvent;
import com.alan.clients.newevent.impl.motion.PostStrafeEvent;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.newevent.impl.motion.StrafeEvent;
import com.alan.clients.newevent.impl.other.StepEvent;
import com.alan.clients.newevent.impl.other.TickEvent;
import com.alan.clients.util.player.MoveUtil;
import com.alan.clients.value.Mode;
import com.alan.clients.value.impl.ModeValue;
import com.alan.clients.value.impl.NumberValue;
import com.alan.clients.value.impl.SubMode;
import com.alan.clients.util.chat.ChatUtil;
import com.alan.clients.util.packet.PacketUtil;
import com.alan.clients.util.player.PlayerUtil;
import com.alan.clients.util.player.SlotUtil;
import com.alan.clients.component.impl.player.BadPacketsComponent;
import net.minecraft.block.BlockAir;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.potion.Potion;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public final class VulcanSpeed extends Mode<Speed> {
    private final NumberValue speed = new NumberValue("Speed", this, 2, 1, 10, 1);
    ModeValue mode = new ModeValue("Mode", this)
        .add(new SubMode("BHop"))
        .add(new SubMode("Lowhop"))
        .add(new SubMode("Funny"))
        .add(new SubMode("Use Disabler"))
        .add(new SubMode("Ground"))
        .add(new SubMode("Yport"))
        .setDefault("Yport");
    private int nextPlaceTick;
    private int stage;
    private double yOffset;
    private int jumps = 0;
    private double lastPosZ = 0.0;
    private double lastPosX = 0.0;
    private boolean wasSprinting = false;
    @EventLink
    public final Listener<JumpEvent> onJump = var1x -> this.jumps++;
    @EventLink
    public final Listener<StepEvent> onStep = var1x -> {
        switch (this.mode.wo().getName()) {
            case "BHop":
                MoveUtil.strafe(0.22);
        }
    };
    @EventLink
    public final Listener<PreMotionEvent> onPreMotion = var1x -> {
        if (aEg.thePlayer.ae < 2) {
            aEg.thePlayer.motionX *= 1.4;
            aEg.thePlayer.motionZ *= 1.4;
            if (MoveUtil.isMoving()) {
                MoveUtil.strafe();
            } else {
                aEg.thePlayer.motionX *= -1.0;
                aEg.thePlayer.motionZ *= -1.0;
            }

            if (aEg.thePlayer.ae == 2) {
                aEg.thePlayer.motionX *= 1.3;
                aEg.thePlayer.motionZ *= 1.3;
            }
        }

        if (this.mode.wo().getName().equals("BHop")) {
            if (aEg.thePlayer.tR == 0) {
                MoveUtil.strafe();
            }

            if (aEg.thePlayer.tR == 1) {
                MoveUtil.strafe();
            }

            if (aEg.thePlayer.tR == 2) {
                MoveUtil.strafe();
            }

            if (aEg.thePlayer.tR == 4) {
                MoveUtil.strafe();
            }

            if (aEg.thePlayer.tR == 5) {
                aEg.thePlayer.onGround = true;
                MoveUtil.strafe();
                var1x.setOnGround(true);
            }
        }

        if (this.mode.wo().getName().equals("Ground")) {
            if (aEg.thePlayer.isJumping) {
                aEg.thePlayer.motionX *= 0.75;
                aEg.thePlayer.motionZ *= 0.75;
            }

            if (aEg.thePlayer.onGround && MoveUtil.isMoving()) {
                double d0 = Math.hypot(aEg.thePlayer.motionX, aEg.thePlayer.motionZ);
                boolean flag = (aEg.thePlayer.isPotionActive(Potion.moveSpeed) ? aEg.thePlayer.getActivePotionEffect(Potion.moveSpeed).amplifier + 1 : 0) >= 2;
                double d1;
                switch (this.stage) {
                    case 1:
                        this.yOffset = 0.58F;
                        d1 = flag ? d0 + 0.2 : 0.487;
                        var1x.setOnGround(true);
                        aEg.thePlayer.onGround = true;
                        break;
                    case 2:
                        d1 = flag ? d0 * 0.71 : 0.197;
                        this.yOffset -= 0.0784F;
                        var1x.setOnGround(false);
                        aEg.thePlayer.onGround = true;
                        break;
                    default:
                        this.stage = 0;
                        d1 = d0 / (flag ? 0.64 : 0.58);
                        var1x.setOnGround(true);
                        aEg.thePlayer.onGround = true;
                }

                MoveUtil.strafe(d1);
                this.stage++;
                var1x.setPosY(var1x.getPosY() + this.yOffset);
                MoveUtil.preventDiagonalSpeed();
            } else {
                MoveUtil.strafe();
                this.stage = 0;
            }
        }
    };
    @EventLink
    public final Listener<PostStrafeEvent> onPostStrafe = var1x -> {
        if (this.mode.wo().getName().equals("Use Disabler")) {
            if (aEg.thePlayer.onGround) {
                aEg.thePlayer.motionY = 0.42F;
            }

            int i = aEg.thePlayer.isPotionActive(Potion.moveSpeed) ? aEg.thePlayer.getActivePotionEffect(Potion.moveSpeed).amplifier + 1 : 0;
            boolean flag = aEg.thePlayer.tR == 11;
            boolean flag1 = aEg.thePlayer.onGround;
            switch (i) {
                case 0:
                    MoveUtil.strafe(flag ? 0.3225 : (flag1 ? 0.6505 : 0.36));
                    break;
                case 1:
                    MoveUtil.strafe(flag ? 0.3975 : (flag1 ? 0.7255 : 0.4275));
                    break;
                default:
                    MoveUtil.strafe(flag ? 0.4725 : (flag1 ? 0.8005 : 0.495));
            }
        }
    };
    @EventLink
    public final Listener<MoveInputEvent> onMove = var1x -> {
        if (this.mode.wo().getName().equals("BHop")) {
            var1x.setJump(false);
        }
    };
    @EventLink
    public final Listener<StrafeEvent> onStrafe = var1x -> {
        Math.sqrt(aEg.thePlayer.motionX * aEg.thePlayer.motionX + aEg.thePlayer.motionZ * aEg.thePlayer.motionZ);
        if (MoveUtil.isMoving()) {
            label343: {
                label344: {
                    {
                        String s = this.mode.wo().getName();
                        switch (s) {
                            case "Yport":
                                MoveUtil.strafe();
                                double d1;
                                int k = (d1 = MoveUtil.speed() - 0.22) == 0.0 ? 0 : (d1 < 0.0 ? -1 : 1);
                                if (aEg.thePlayer.tR == 1) {
                                    aEg.thePlayer.motionY = -0.15;
                                }

                                if (aEg.thePlayer.tR == 2) {
                                    aEg.thePlayer.motionY = -0.3;
                                }

                                double d2;
                                k = (
                                            d2 = Math.hypot(
                                                    aEg.thePlayer.motionX - (aEg.thePlayer.lastTickPosX - aEg.thePlayer.cry),
                                                    aEg.thePlayer.motionZ - (aEg.thePlayer.lastTickPosZ - aEg.thePlayer.crA)
                                                )
                                                - 0.022
                                        )
                                        == 0.0
                                    ? 0
                                    : (d2 < 0.0 ? -1 : 1);
                                if (aEg.thePlayer.hurtTime > 0 || aEg.thePlayer.ae < 40) {
                                    this.e(Velocity.class).mode.wo().getName();
                                }

                                if (aEg.thePlayer.onGround) {
                                    aEg.thePlayer.jump();
                                    if ((!aEg.thePlayer.isPotionActive(Potion.moveSpeed) || aEg.thePlayer.ae <= 11) && aEg.thePlayer.ae > 11) {
                                    }
                                }

                                if (aEg.thePlayer.tR == 1) {
                                    if (aEg.thePlayer.isPotionActive(Potion.moveSpeed) && aEg.thePlayer.ae > 11) {
                                        MoveUtil.strafe(0.05 * (1 + aEg.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier()) + 0.308);
                                    } else if (aEg.thePlayer.ae > 11) {
                                        MoveUtil.strafe(0.3035);
                                    } else {
                                        MoveUtil.strafe();
                                    }
                                }

                                if (aEg.thePlayer.tR == 2 && (!aEg.thePlayer.isPotionActive(Potion.moveSpeed) || aEg.thePlayer.ae <= 11)) {
                                    if (aEg.thePlayer.ae > 11) {
                                        MoveUtil.strafe();
                                    } else {
                                        MoveUtil.strafe();
                                    }
                                }

                                MoveUtil.preventDiagonalSpeed();
                                return;
                            case "Lowhop":
                                break label344;
                            case "BHop":
                                break;
                            case "Funny":
                                break label343;
                            default:
                                return;
                        }
                    }

                    if (MoveUtil.speed() < 0.22) {
                        MoveUtil.strafe(0.22);
                    }

                    if (aEg.thePlayer.hurtTime > 0 || aEg.thePlayer.crG < 50) {
                        this.e(Velocity.class).mode.wo().getName();
                    }

                    if (Math.hypot(
                            aEg.thePlayer.motionX - (aEg.thePlayer.lastTickPosX - aEg.thePlayer.cry),
                            aEg.thePlayer.motionZ - (aEg.thePlayer.lastTickPosZ - aEg.thePlayer.crA)
                        )
                        < 0.022) {
                        MoveUtil.strafe();
                    }

                    if (MoveUtil.speed() < 0.22) {
                        MoveUtil.strafe(0.22);
                    }

                    switch (aEg.thePlayer.tR) {
                        case 0:
                            if (!aEg.thePlayer.isPotionActive(Potion.moveSpeed) && aEg.thePlayer.ae > 30) {
                                aEg.thePlayer.motionX *= 0.985;
                                aEg.thePlayer.motionZ *= 0.985;
                            }

                            if (!(PlayerUtil.p(0.0, aEg.thePlayer.motionY, 0.0) instanceof BlockAir)) {
                                aEg.thePlayer.jump();
                            }

                            if (MoveUtil.speed() < 487.0 && aEg.thePlayer.ae > 10) {
                                MoveUtil.strafe(0.487);
                                break;
                            }

                            MoveUtil.strafe();
                            break;
                        case 1:
                            if (MoveUtil.speed() < 0.3355 && aEg.thePlayer.ae > 10) {
                                MoveUtil.strafe(0.3355);
                            } else {
                                MoveUtil.strafe();
                            }

                            if (!aEg.thePlayer.isPotionActive(Potion.moveSpeed)
                                || aEg.thePlayer.getActivePotionEffect(Potion.moveSpeed).amplifier + 1 < 2
                                || !(MoveUtil.speed() < 0.06 * (1 + aEg.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier()) + 0.487)) {
                                if (aEg.thePlayer.isPotionActive(Potion.moveSpeed)
                                    && aEg.thePlayer.getActivePotionEffect(Potion.moveSpeed).amplifier + 1 == 1
                                    && MoveUtil.speed() < 0.06 * (1 + aEg.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier()) + 0.41) {
                                    MoveUtil.strafe();
                                } else {
                                    MoveUtil.strafe();
                                    if (MoveUtil.speed() < 0.3355) {
                                    }
                                }
                            }
                        case 2:
                        case 3:
                        case 4:
                        case 6:
                        case 7:
                        case 8:
                        case 11:
                        default:
                            break;
                        case 5:
                            if (aEg.thePlayer.ae > 10) {
                                MoveUtil.strafe(0.27);
                            }

                            if (!(PlayerUtil.p(0.0, 1.0, 0.0) instanceof BlockAir)) {
                                aEg.thePlayer.motionY = 0.003;
                            }
                            break;
                        case 9:
                            if (!(PlayerUtil.p(0.0, aEg.thePlayer.motionY, 0.0) instanceof BlockAir)) {
                                MoveUtil.strafe();
                            }

                            if ((
                                    !aEg.thePlayer.isPotionActive(Potion.moveSpeed)
                                        || aEg.thePlayer.getActivePotionEffect(Potion.moveSpeed).amplifier + 1 < 2
                                        || !(MoveUtil.speed() < 0.06 * (1 + aEg.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier()) + 0.46)
                                )
                                && (
                                    !aEg.thePlayer.isPotionActive(Potion.moveSpeed)
                                        || aEg.thePlayer.getActivePotionEffect(Potion.moveSpeed).amplifier + 1 != 1
                                        || !(MoveUtil.speed() < 0.06 * (1 + aEg.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier()) + 0.385)
                                )) {
                                if (MoveUtil.speed() < 0.299) {
                                    MoveUtil.strafe(0.299);
                                } else {
                                    MoveUtil.strafe();
                                }
                            }

                            aEg.thePlayer.motionY = -0.8;
                            break;
                        case 10:
                            aEg.thePlayer.motionY = -10.0;
                    }

                    this.correctSpeed(aEg.thePlayer.posZ, aEg.thePlayer.posX, aEg.thePlayer.isSprinting(), aEg.thePlayer.cse, aEg.thePlayer.tR);
                    MoveUtil.useDiagonalSpeed();
                    return;
                }

                if ((aEg.thePlayer.hurtTime > 0 || aEg.thePlayer.ae < 40) && this.e(Velocity.class).mode.wo().getName() == "Vulcan") {
                    MoveUtil.strafe();
                }

                if (MoveUtil.speed() < 0.22) {
                    MoveUtil.strafe(0.22);
                }

                double d0 = MoveUtil.vd() / 2.29;
                if (Math.hypot(
                        aEg.thePlayer.motionX - (aEg.thePlayer.lastTickPosX - aEg.thePlayer.cry),
                        aEg.thePlayer.motionZ - (aEg.thePlayer.lastTickPosZ - aEg.thePlayer.crA)
                    )
                    < 0.022) {
                    MoveUtil.strafe();
                }

                switch (aEg.thePlayer.tR) {
                    case 0:
                        aEg.thePlayer.jump();
                        if (aEg.thePlayer.isPotionActive(Potion.moveSpeed) && aEg.thePlayer.ae > 11) {
                            MoveUtil.strafe(0.06 * (1 + aEg.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier()) + 0.485);
                        } else if (aEg.thePlayer.ae > 11) {
                            MoveUtil.strafe(0.485);
                        } else {
                            MoveUtil.strafe();
                        }
                        break;
                    case 1:
                        MoveUtil.strafe();
                        break;
                    case 2:
                        if (this.jumps % 4 != 1 && !aEg.thePlayer.isCollidedVertically) {
                            aEg.thePlayer.motionY = MoveUtil.predictedMotion(aEg.thePlayer.motionY, 2);
                        }
                    case 3:
                    case 6:
                    case 7:
                    case 9:
                    default:
                        break;
                    case 4:
                        if (this.jumps % 4 == 1 || aEg.thePlayer.isCollidedVertically) {
                            aEg.thePlayer.motionY = MoveUtil.predictedMotion(aEg.thePlayer.motionY, 4);
                        }
                        break;
                    case 5:
                        if (this.jumps % 4 == 1) {
                            MoveUtil.strafe();
                        }
                        break;
                    case 8:
                        MoveUtil.strafe();
                }

                if (!(PlayerUtil.p(0.0, aEg.thePlayer.motionY, 0.0) instanceof BlockAir)) {
                    MoveUtil.strafe();
                }

                this.correctSpeed(aEg.thePlayer.posZ, aEg.thePlayer.posX, aEg.thePlayer.isSprinting(), aEg.thePlayer.cse, aEg.thePlayer.tR);
                return;
            }

            int i = SlotUtil.vx();
            if (i == -1) {
                ChatUtil.b("This speed requires a block to be in your HotBar.");
            } else {
                if (!BadPacketsComponent.bad(false, true, false, false, false)) {
                    SlotComponent slotcomponent = this.d(SlotComponent.class);
                    SlotComponent.setSlot(i);
                }

                int j = aEg.thePlayer.isPotionActive(Potion.moveSpeed) ? aEg.thePlayer.getActivePotionEffect(Potion.moveSpeed).amplifier + 1 : 0;
                if (!BadPacketsComponent.bad(false, true, false, false, false) && this.nextPlaceTick < aEg.thePlayer.ticksExisted) {
                    this.nextPlaceTick = aEg.thePlayer.ticksExisted + 2;
                    BlockPos blockpos = new BlockPos(aEg.thePlayer).down();
                    int l = EnumFacing.UP.getIndex();
                    SlotComponent slotcomponent1 = this.d(SlotComponent.class);
                    PacketUtil.send(new C08PacketPlayerBlockPlacement(blockpos, l, SlotComponent.getItemStack(), 0.0F, 1.0F, 0.0F));
                }

                switch (aEg.thePlayer.tR) {
                    case 0:
                        switch (j) {
                            case 0:
                                MoveUtil.strafe(0.55);
                                break;
                            case 1:
                                MoveUtil.strafe(0.54);
                                break;
                            default:
                                MoveUtil.strafe(0.6699999999999999);
                        }

                        aEg.thePlayer.motionY = 0.2F;
                        break;
                    case 1:
                        aEg.thePlayer.motionY = -0.0784000015258789;
                        switch (j) {
                            case 0:
                                MoveUtil.strafe(0.43);
                                return;
                            case 1:
                                MoveUtil.strafe(0.49);
                                return;
                            default:
                                MoveUtil.strafe(0.5700000000000001);
                                return;
                        }
                    case 2:
                        switch (j) {
                            case 0:
                                MoveUtil.strafe(0.37);
                                break;
                            case 1:
                                MoveUtil.strafe(0.44000000000000006);
                                break;
                            default:
                                MoveUtil.strafe(0.47000000000000003);
                        }
                }
            }
        }
    };
    @EventLink
    public final Listener<TickEvent> onTick = var1x -> {
        switch (this.mode.wo().getName()) {
            case "Yport":
                if (aEg.thePlayer.tR == 0) {
                    MoveUtil.strafe();
                }

                if (aEg.thePlayer.tR == 2) {
                    MoveUtil.strafe();
                }

                if (aEg.thePlayer.tR == 3) {
                    MoveUtil.strafe();
                }

                this.correctSpeed(aEg.thePlayer.posZ, aEg.thePlayer.posX, aEg.thePlayer.isSprinting(), aEg.thePlayer.cse, aEg.thePlayer.tR);
        }
    };

    public VulcanSpeed(String var1, Speed var2) {
        super(var1, var2);
    }

    @Override
    public void onDisable() {
        if (this.mode.wo().getName().equals("Ground")) {
            MoveUtil.strafe(MoveUtil.getAllowedHorizontalDistance() * 0.6 - 0.02);
        }
    }

    public void correctSpeed(double var1, double var3, boolean var5, int var6, int var7) {
        double d0 = (aEg.thePlayer.lastTickPosX - aEg.thePlayer.cry) * 0.91F;
        double d1 = (aEg.thePlayer.lastTickPosX - aEg.thePlayer.cry) * 0.91F;
        double d2 = 1000000.0;
        double d3 = var1 * d2;
        double d4 = var3 * d2;
        double d5 = d3 - d0;
        double d6 = d4 - d1;
        double d7 = Math.hypot(d5, d6);
        double d8 = d7 / (this.wasSprinting ? 1.3 : 1.3);
        double d9 = d8 - (!var5 && var6 >= 2 ? 0.026 : 0.026);
        int i = aEg.thePlayer.ticksExisted % 2;
        boolean flag = d9 > 0.0075 && Math.hypot(d3, d4) > 0.25 && var7 > 2;
        if (!flag) {
            MoveUtil.strafe();
        }

        this.lastPosZ = d3;
        this.lastPosX = d4;
        this.wasSprinting = var5;
    }
}
