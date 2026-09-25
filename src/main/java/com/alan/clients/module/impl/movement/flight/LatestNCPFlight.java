package com.alan.clients.module.impl.movement.flight;

import com.alan.clients.module.impl.movement.Flight;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.newevent.impl.motion.StrafeEvent;
import com.alan.clients.newevent.impl.other.TeleportEvent;
import com.alan.clients.util.player.MoveUtil;
import com.alan.clients.value.Mode;
import com.alan.clients.value.impl.ModeValue;
import com.alan.clients.value.impl.SubMode;
import com.alan.clients.util.chat.ChatUtil;
import com.alan.clients.util.packet.PacketUtil;
import com.alan.clients.util.player.PlayerUtil;
import net.minecraft.network.play.client.C03PacketPlayer.C06PacketPlayerPosLook;
import net.minecraft.util.AxisAlignedBB;

public class LatestNCPFlight extends Mode<Flight> {
    private final ModeValue mode = new ModeValue("NCP Mode", this).add(new SubMode("Normal")).add(new SubMode("Clip")).setDefault("Normal");
    private double moveSpeed;
    private boolean started;
    private boolean notUnder;
    private boolean clipped;
    private boolean teleport;
    @EventLink
    public final Listener<TeleportEvent> onTeleport = var1x -> {
        if (this.teleport) {
            var1x.setCancelled();
            this.teleport = false;
            ChatUtil.b("Teleported");
        }
    };
    @EventLink
    public final Listener<StrafeEvent> onStrafe = var1x -> {
        if (this.mode.wo().getName().equals("Clip")) {
            AxisAlignedBB axisalignedbb = aEg.thePlayer.getEntityBoundingBox().offset(0.0, 1.0, 0.0);
            if (!aEg.theWorld.getCollidingBoundingBoxes(aEg.thePlayer, axisalignedbb).isEmpty() && !this.started) {
                this.notUnder = true;
                if (this.clipped) {
                    return;
                }

                this.clipped = true;
                PacketUtil.send(
                    new C06PacketPlayerPosLook(aEg.thePlayer.posX, aEg.thePlayer.posY, aEg.thePlayer.posZ, aEg.thePlayer.pl, aEg.thePlayer.rotationPitch, false)
                );
                PacketUtil.send(
                    new C06PacketPlayerPosLook(
                        aEg.thePlayer.posX, aEg.thePlayer.posY - 0.1, aEg.thePlayer.posZ, aEg.thePlayer.pl, aEg.thePlayer.rotationPitch, false
                    )
                );
                PacketUtil.send(
                    new C06PacketPlayerPosLook(aEg.thePlayer.posX, aEg.thePlayer.posY, aEg.thePlayer.posZ, aEg.thePlayer.pl, aEg.thePlayer.rotationPitch, false)
                );
                this.teleport = true;
            } else {
                switch (aEg.thePlayer.tR) {
                    case 0:
                        if (this.notUnder && this.clipped) {
                            this.started = true;
                            var1x.setSpeed(10.0);
                            aEg.thePlayer.motionY = 0.42F;
                            this.notUnder = false;
                        }
                        break;
                    case 1:
                        if (this.started) {
                            var1x.setSpeed(9.6);
                        }
                }
            }

            MoveUtil.strafe();
            aEg.timer.dzD = 0.4F;
        }
    };
    @EventLink
    private final Listener<PreMotionEvent> preMotionEventListener = var1x -> {
        if (this.mode.wo().getName().equals("Normal")) {
            AxisAlignedBB axisalignedbb = aEg.thePlayer.getEntityBoundingBox().offset(0.0, 1.0, 0.0);
            if (this.started) {
                aEg.thePlayer.motionY += 0.025;
                MoveUtil.strafe(this.moveSpeed *= 0.935F);
                if (aEg.thePlayer.motionY < -0.5 && !PlayerUtil.vh()) {
                    this.toggle();
                }
            }

            if (aEg.theWorld.getCollidingBoundingBoxes(aEg.thePlayer, axisalignedbb).isEmpty() && !this.started) {
                this.started = true;
                aEg.thePlayer.jump();
                MoveUtil.strafe(this.moveSpeed = 9.0);
            }
        }
    };

    public LatestNCPFlight(String var1, Flight flight) {
        super(var1, flight);
    }

    @Override
    public void onDisable() {
        MoveUtil.stop();
    }

    @Override
    public void onEnable() {
        ChatUtil.b("Start the fly under the block and walk forward");
        this.moveSpeed = 0.0;
        this.notUnder = false;
        this.started = false;
        this.clipped = false;
        this.teleport = false;
    }
}
