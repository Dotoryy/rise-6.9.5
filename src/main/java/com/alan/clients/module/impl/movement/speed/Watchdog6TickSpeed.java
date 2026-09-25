package com.alan.clients.module.impl.movement.speed;

import com.alan.clients.module.impl.movement.Speed;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.input.MoveInputEvent;
import com.alan.clients.newevent.impl.motion.JumpEvent;
import com.alan.clients.newevent.impl.motion.PostStrafeEvent;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.newevent.impl.motion.PreUpdateEvent;
import com.alan.clients.newevent.impl.motion.StrafeEvent;
import com.alan.clients.newevent.impl.other.MoveEvent;
import com.alan.clients.newevent.impl.packet.PacketReceiveEvent;
import com.alan.clients.util.player.MoveUtil;
import com.alan.clients.value.Mode;
import com.alan.clients.util.chat.ChatUtil;
import com.alan.clients.util.math.MathUtil;
import com.alan.clients.util.player.PlayerUtil;
import net.minecraft.block.BlockStairs;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.potion.Potion;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

public class Watchdog6TickSpeed extends Mode<Speed> {
    private static float Qa = 0.0F;
    private static float Qb = 42.5F;
    private Entity Rd = null;
    private int PD = 0;
    private boolean onStairs = false;
    public static boolean LW;
    public static boolean LX;
    public static int tickCounter;
    double enableY;
    boolean LY;
    public static int LZ;
    public static int Ma;
    private double startPosX;
    private double startPosY;
    private double startPosZ;
    private double startYaw;
    private double startPitch;
    public static double Mi;
    @EventLink
    public final Listener<PacketReceiveEvent> onPacketReceive = var0 -> {
        switch (var0.getPacket()) {
            default:
                break;
        }
    };
    @EventLink
    public final Listener<MoveInputEvent> onMoveInput = var0 -> {
        if (tickCounter < 23 && tickCounter > 7) {
            var0.setJump(false);
        }
    };
    @EventLink
    public final Listener<MoveEvent> onMove = var0 -> {
        if (tickCounter < 23 && tickCounter > 7) {
            var0.setPosZ(0.0);
            var0.setPosX(0.0);
        }
    };
    @EventLink
    public final Listener<StrafeEvent> onStrafe = var1x -> {
        if (aEg.thePlayer.onGround) {
            this.onStairs = false;
        }

        AxisAlignedBB axisalignedbb = aEg.thePlayer.getEntityBoundingBox();
        WorldClient worldclient = aEg.theWorld;

        for (double d0 = axisalignedbb.minX; d0 < axisalignedbb.maxX; d0 += 0.3) {
            for (double d1 = axisalignedbb.minZ; d1 < axisalignedbb.maxZ; d1 += 0.3) {
                BlockPos blockpos = new BlockPos(d0, axisalignedbb.minY - 0.05, d1);
                if (worldclient.getBlockState(blockpos).getBlock() instanceof BlockStairs) {
                    this.onStairs = true;
                    break;
                }
            }

            if (this.onStairs) {
                break;
            }
        }

        double d2 = MathHelper.wrapAngleTo180_double(Math.toDegrees(MoveUtil.direction()));
        double d3 = MathHelper.wrapAngleTo180_double(Math.toDegrees(Math.atan2(aEg.thePlayer.motionZ, aEg.thePlayer.motionX)) - 90.0);
        if (aEg.thePlayer.hurtTime == 9 && tickCounter < 23) {
            tickCounter = 23;
        }

        if (tickCounter > 24 && !this.onStairs && aEg.thePlayer.csk > 5) {
            if (aEg.thePlayer.tR == 0) {
                if (tickCounter <= 48 && (aEg.thePlayer.isPotionActive(Potion.moveSpeed) || tickCounter <= 66)) {
                    MoveUtil.strafe(MoveUtil.getAllowedHorizontalDistance() - 0.01);
                } else {
                    MoveUtil.strafe(MoveUtil.getAllowedHorizontalDistance());
                }

                if (aEg.thePlayer.onGround) {
                    aEg.thePlayer.jump();
                }
            }

            if (aEg.thePlayer.tR == 1) {
                if (aEg.thePlayer.isPotionActive(Potion.moveSpeed) && aEg.thePlayer.getActivePotionEffect(Potion.moveSpeed).amplifier + 1 >= 2) {
                    MoveUtil.strafe(0.48);
                } else if (aEg.thePlayer.isPotionActive(Potion.moveSpeed) && aEg.thePlayer.getActivePotionEffect(Potion.moveSpeed).amplifier + 1 >= 2) {
                    MoveUtil.strafe(0.4);
                } else if (aEg.thePlayer.isPotionActive(Potion.moveSpeed) && aEg.thePlayer.getActivePotionEffect(Potion.moveSpeed).amplifier + 1 == 1) {
                    MoveUtil.strafe(0.405);
                } else {
                    MoveUtil.strafe(0.33);
                }

                aEg.thePlayer.motionY = 0.31;
            }

            if (aEg.thePlayer.tR == 2) {
                if (aEg.thePlayer.isPotionActive(Potion.moveSpeed)) {
                    aEg.thePlayer.motionX *= 1.005;
                    aEg.thePlayer.motionZ *= 1.005;
                }

                aEg.thePlayer.motionY -= 0.11;
            }

            if (aEg.thePlayer.tR == 3) {
                aEg.thePlayer.motionY -= 0.22;
            }

            if (aEg.thePlayer.tR == 4) {
                if (aEg.thePlayer.isPotionActive(Potion.moveSpeed)) {
                    if (tickCounter > 66) {
                        aEg.thePlayer.motionX *= 1.01;
                        aEg.thePlayer.motionZ *= 1.01;
                    }
                } else {
                    aEg.thePlayer.motionX *= 1.005;
                    aEg.thePlayer.motionZ *= 1.005;
                }
            }

            if (aEg.thePlayer.tR == 5 && PlayerUtil.ae(aEg.thePlayer.motionY * 2.0)) {
                aEg.thePlayer.motionY += 0.085;
                if (aEg.thePlayer.isPotionActive(Potion.moveSpeed)) {
                    if (tickCounter > 66) {
                        MoveUtil.strafe(MoveUtil.getAllowedHorizontalDistance() * 1.069);
                    }
                } else {
                    MoveUtil.strafe();
                    if (tickCounter > 154) {
                    }
                }
            }

            if (PlayerUtil.p(0.0, aEg.thePlayer.motionY, 0.0) != Blocks.air) {
                if (aEg.thePlayer.isPotionActive(Potion.moveSpeed)) {
                    if (tickCounter > 66) {
                        MoveUtil.strafe(MoveUtil.getAllowedHorizontalDistance() * 1.059);
                    }
                } else {
                    MoveUtil.strafe();
                }
            }

            PlayerUtil.vk();
            aEg.thePlayer.stepHeight = 0.2F;
            if (MoveUtil.speed() < 0.125) {
                MoveUtil.strafe(0.125);
            }

            if (MathUtil.n(d2, d3) > 40.0 && aEg.thePlayer.isPotionActive(Potion.moveSpeed)) {
                aEg.thePlayer.motionX *= 0.74;
                aEg.thePlayer.motionZ *= 0.74;
            }
        }

        if (tickCounter < 8 || this.onStairs || aEg.thePlayer.csk < 6) {
            if (aEg.thePlayer.onGround) {
                aEg.thePlayer.jump();
            }

            if (aEg.thePlayer.onGround && !aEg.thePlayer.isPotionActive(Potion.moveSpeed)) {
                MoveUtil.strafe(MoveUtil.getAllowedHorizontalDistance() - 0.01);
            } else if (aEg.thePlayer.onGround) {
                MoveUtil.strafe(0.26);
            }

            if (MoveUtil.speed() < 0.125) {
                MoveUtil.strafe(0.125);
            }

            if (aEg.thePlayer.tR == 1) {
                MoveUtil.strafe();
                aEg.thePlayer.motionY += 0.057F;
            }

            if (aEg.thePlayer.tR == 3) {
                aEg.thePlayer.motionY -= 0.1309F;
            }

            if (aEg.thePlayer.tR == 4) {
                aEg.thePlayer.motionY -= 0.2;
            }
        }

        if (tickCounter == 24) {
            MoveUtil.strafe(0.125);
        }
    };
    @EventLink
    public final Listener<PreUpdateEvent> onPreUpdate = var0 -> {
        if (tickCounter > 44) {
            ;
        }
    };
    @EventLink
    public final Listener<PreMotionEvent> onPreMotion = var0 -> {
        aEg.thePlayer.getEntityBoundingBox();
        tickCounter++;
        if (tickCounter < 23) {
            if (aEg.thePlayer.onGround && tickCounter > 3) {
                var0.setPosY(var0.getPosY() + (tickCounter % 2 != 0 ? 0.296875 : 0.001));
                var0.setOnGround(false);
            }
        } else if (aEg.thePlayer.onGround) {
            var0.setPosY(var0.getPosY() + 0.001);
        }
    };
    @EventLink
    public final Listener<JumpEvent> onJump = var1x -> {
        if (tickCounter >= 23 && tickCounter >= 8 && !this.onStairs && aEg.thePlayer.csk >= 6) {
            var1x.setJumpMotion(0.4F);
        } else {
            var1x.setJumpMotion(0.42F);
        }
    };
    @EventLink
    public final Listener<PostStrafeEvent> onPostStrafe = var0 -> {
        double d0 = MathHelper.wrapAngleTo180_double(Math.toDegrees(MoveUtil.direction()));
        double d1 = MathHelper.wrapAngleTo180_double(Math.toDegrees(Math.atan2(aEg.thePlayer.motionZ, aEg.thePlayer.motionX)) - 90.0);
        if (MathUtil.n(d0, d1) > 90.0) {
            if (aEg.thePlayer.isPotionActive(Potion.moveSpeed)) {
                MoveUtil.a(MoveUtil.speed(), (float)d1 - 180.0F);
            } else {
                MoveUtil.a(MoveUtil.speed(), (float)d1 - 180.0F);
            }
        }
    };

    public Watchdog6TickSpeed(String var1, Speed speed) {
        super(var1, speed);
    }

    @Override
    public void onEnable() {
        this.startYaw = aEg.thePlayer.pl;
        this.startPitch = aEg.thePlayer.rotationPitch;
        tickCounter = 0;
        aEg.thePlayer.pl = (float)this.startYaw;
        if (aEg.thePlayer.onGround) {
            aEg.thePlayer.jump();
        } else {
            ChatUtil.b("start on the ground");
            this.getParent().setEnabled(false);
        }

        this.startPosX = aEg.thePlayer.posX;
        this.startPosY = aEg.thePlayer.posY;
        this.startPosZ = aEg.thePlayer.posZ;
        this.enableY = aEg.thePlayer.posY;
        LX = true;
        LW = false;
    }

    @Override
    public void onDisable() {
        double d0 = aEg.thePlayer.posX - this.startPosX;
        double d1 = aEg.thePlayer.posY - this.startPosY;
        double d2 = aEg.thePlayer.posZ - this.startPosZ;
        double d3 = Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
        LW = false;
        this.LY = false;
        ChatUtil.b(aEg.thePlayer.ae);
        ChatUtil.c("Distance traveled: " + d3);
        if (aEg.thePlayer.onGround) {
            MoveUtil.stop();
        }

        aEg.thePlayer.stepHeight = 0.6F;
    }

    private boolean wouldCollide(Vec3 vec, Vec3 var2) {
        EntityPlayerSP entityplayersp = Minecraft.getMinecraft().thePlayer;
        WorldClient worldclient = Minecraft.getMinecraft().theWorld;
        AxisAlignedBB axisalignedbb = entityplayersp.getEntityBoundingBox().offset(var2.xCoord, var2.yCoord, var2.zCoord);
        return !worldclient.getCollidingBoundingBoxes(entityplayersp, axisalignedbb).isEmpty();
    }
}
