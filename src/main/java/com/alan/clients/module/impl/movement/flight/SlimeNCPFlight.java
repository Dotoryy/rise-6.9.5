package com.alan.clients.module.impl.movement.flight;

import com.alan.clients.module.impl.combat.KillAura;
import com.alan.clients.module.impl.movement.Flight;
import com.alan.clients.module.impl.movement.Speed;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PostStrafeEvent;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.newevent.impl.motion.StrafeEvent;
import com.alan.clients.util.player.MoveUtil;
import com.alan.clients.value.Mode;
import com.alan.clients.util.vector.Vector3d;

public class SlimeNCPFlight extends Mode<Flight> {
    private double tU;
    private final double HU = 0.03;
    private final double HV = 0.053299998353843775;
    private final double HW = 1.0;
    private int tY;
    private boolean started;
    private Vector3d savedMotion;
    @EventLink
    public final Listener<PreMotionEvent> onPreMotion = var0 -> {};
    @EventLink
    public final Listener<StrafeEvent> onStrafe = var0 -> {
        int i = (aEg.thePlayer.tR - 1) % 3;
    };
    @EventLink
    public final Listener<PostStrafeEvent> onPostStrafe = var1x -> {
        if (aEg.thePlayer.onGround) {
            aEg.thePlayer.jump();
        } else if ((aEg.thePlayer.tR - 1) % 3 == 0) {
            this.savedMotion = new Vector3d(aEg.thePlayer.motionX, aEg.thePlayer.motionY, aEg.thePlayer.motionZ);
            aEg.thePlayer.motionY = 0.0;
            MoveUtil.stop();
        } else if (this.savedMotion != null) {
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
            aEg.thePlayer.motionY = this.savedMotion.getY() * d0;
            aEg.thePlayer.motionZ = this.savedMotion.getZ() * d0;
            this.savedMotion = null;
            this.started = true;
        } else if (this.started) {
            this.started = false;
            if (!KillAura.blocking) {
            }
        }
    };

    public SlimeNCPFlight(String var1, Flight flight) {
        super(var1, flight);
    }

    @Override
    public void onDisable() {
        this.tU = 0.0;
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
        if (this.e(Speed.class).isEnabled()) {
            this.e(Speed.class).setEnabled(false);
        }

        this.tU = 0.0;
        this.tY = 0;
    }
}
