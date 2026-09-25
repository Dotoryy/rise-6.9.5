package com.alan.clients.module.impl.movement.speed;

import com.alan.clients.module.impl.combat.KillAura;
import com.alan.clients.module.impl.movement.Speed;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PostStrafeEvent;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.newevent.impl.motion.StrafeEvent;
import com.alan.clients.util.player.MoveUtil;
import com.alan.clients.value.Mode;
import com.alan.clients.value.impl.NumberValue;
import com.alan.clients.util.vector.Vector3d;

public class WatchdogPredictionSpeed extends Mode<Speed> {
    private final NumberValue speed = new NumberValue("Speed", this, 1, 1, 10, 1);
    private double tU;
    private final double PI = 0.03;
    private final double PJ = 0.053299998353843775;
    private final double PK = 1.0;
    private int tY;
    private boolean boostPending;
    private Vector3d savedMotion;
    @EventLink
    public final Listener<PreMotionEvent> onPreMotion = var0 -> {};
    @EventLink
    public final Listener<StrafeEvent> onStrafe = var0 -> {};
    @EventLink
    public final Listener<PostStrafeEvent> onPostStrafe = var1x -> {
        if (aEg.thePlayer.onGround && MoveUtil.isMoving() && !aEg.thePlayer.inWater) {
            aEg.thePlayer.jump();
        }

        if (aEg.thePlayer.tR == 5 && aEg.thePlayer.ae > 7) {
            this.savedMotion = new Vector3d(aEg.thePlayer.motionX, aEg.thePlayer.motionY, aEg.thePlayer.motionZ);
            aEg.thePlayer.motionY = 0.0;
            MoveUtil.stop();
        } else if (this.savedMotion != null && aEg.thePlayer.ae > 7 && aEg.thePlayer.tR == 6) {
            if (Math.abs(this.savedMotion.getX()) < 0.005) {
                this.savedMotion.setX(0.0);
            }

            if (Math.abs(this.savedMotion.getY()) < 0.005) {
                this.savedMotion.setY(0.0);
            }

            if (Math.abs(this.savedMotion.getZ()) < 0.005) {
                this.savedMotion.setZ(0.0);
            }

            double d0 = aEg.thePlayer.tR <= 1 ? 0.5460000157356262 : 1.0;
            aEg.thePlayer.motionX = this.savedMotion.getX() * d0;
            aEg.thePlayer.motionY = this.savedMotion.getY() - 0.02 * d0;
            aEg.thePlayer.motionZ = this.savedMotion.getZ() * d0;
            if (KillAura.blocking) {
                MoveUtil.moveFlying(0.1);
            } else {
                MoveUtil.moveFlying(0.1);
            }

            this.savedMotion = null;
            this.boostPending = true;
        } else if (this.boostPending && aEg.thePlayer.ae > 7 && !KillAura.blocking && aEg.thePlayer.tR == 7) {
            double d1 = aEg.thePlayer.tR <= 1 ? 0.5460000157356262 : 1.0;
            MoveUtil.moveFlying(0.06);
            this.boostPending = false;
        }

        if (aEg.thePlayer.inWater) {
            aEg.gameSettings.keyBindJump.setPressed(true);
            if ((aEg.thePlayer.tR - 1) % 3 == 0 && aEg.thePlayer.ae > 1) {
                this.savedMotion = new Vector3d(aEg.thePlayer.motionX, aEg.thePlayer.motionY, aEg.thePlayer.motionZ);
                aEg.thePlayer.motionY = 0.0;
                MoveUtil.stop();
            } else if (this.savedMotion != null && aEg.thePlayer.ae > 2) {
                if (Math.abs(this.savedMotion.getX()) < 0.005) {
                    this.savedMotion.setX(0.0);
                }

                if (Math.abs(this.savedMotion.getY()) < 0.005) {
                    this.savedMotion.setY(0.0);
                }

                if (Math.abs(this.savedMotion.getZ()) < 0.005) {
                    this.savedMotion.setZ(0.0);
                }

                double d2 = aEg.thePlayer.tR <= 1 ? 0.5460000157356262 : 1.0;
                double d3 = 1.0;
                aEg.thePlayer.motionX = this.savedMotion.getX() * d2;
                aEg.thePlayer.motionY = (this.savedMotion.getY() - 1.0E-14) * d3;
                aEg.thePlayer.motionZ = this.savedMotion.getZ() * d2;
                if (!KillAura.blocking) {
                    MoveUtil.moveFlying(0.087);
                } else {
                    MoveUtil.moveFlying(0.063);
                }

                this.savedMotion = null;
                this.boostPending = true;
            } else if (this.boostPending && aEg.thePlayer.ae > 2) {
                this.boostPending = false;
                if (!KillAura.blocking) {
                    MoveUtil.moveFlying(0.086);
                } else {
                    MoveUtil.moveFlying(0.063);
                }
            }
        }
    };

    public WatchdogPredictionSpeed(String var1, Speed var2) {
        super(var1, var2);
    }

    @Override
    public void onDisable() {
        this.tU = 0.0;
        if (aEg.gameSettings.keyBindJump.isKeyDown()) {
            aEg.gameSettings.keyBindJump.setPressed(false);
        }

        aEg.timer.dzD = 1.0F;
        if (this.savedMotion != null) {
            aEg.thePlayer.motionX = this.savedMotion.getX() * 0.91F;
            aEg.thePlayer.motionY = this.savedMotion.getY();
            aEg.thePlayer.motionZ = this.savedMotion.getZ() * 0.91F;
            this.savedMotion = null;
        }
    }

    @Override
    public void onEnable() {
        this.tU = 0.0;
        this.tY = 0;
    }
}
