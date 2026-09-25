package com.alan.clients.module.impl.movement.speed;

import com.alan.clients.component.impl.player.RotationComponent;
import com.alan.clients.component.impl.player.rotationcomponent.MovementFix;
import com.alan.clients.module.impl.combat.KillAura;
import com.alan.clients.module.impl.movement.Speed;
import com.alan.clients.module.impl.player.Scaffold;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreUpdateEvent;
import com.alan.clients.newevent.impl.motion.StrafeEvent;
import com.alan.clients.newevent.impl.packet.PacketSendEvent;
import com.alan.clients.util.player.MoveUtil;
import com.alan.clients.util.vector.Vector2f;
import com.alan.clients.value.Mode;
import com.alan.clients.value.impl.BooleanValue;
import com.alan.clients.value.impl.ModeValue;
import com.alan.clients.value.impl.SubMode;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import de.florianmichael.vialoadingbase.ViaLoadingBase;

public class LegitSpeed extends Mode<Speed> {
    private final ModeValue rotationExploit = new ModeValue("Rotation Exploit Mode", this)
        .add(new SubMode("Off"))
        .add(new SubMode("Rotate (Fully Legit)"))
        .add(new SubMode("Speed Equivalent (Almost legit, Very hard to flag)"))
        .setDefault("Speed Equivalent (Almost legit, Very hard to flag)");
    private final BooleanValue cpuSpeedUpExploit = new BooleanValue("CPU SpeedUp Exploit", this, true);
    private final BooleanValue noJumpDelay = new BooleanValue("No Jump Delay", this, true);
    @EventLink(value = 1)
    public final Listener<PreUpdateEvent> preUpdate = var1x -> {
        label45: {
            {
                String s = this.rotationExploit.wo().getName();
                switch (s) {
                    case "Rotate (Fully Legit)":
                        if ((!KillAura.attacking || !this.e(KillAura.class).isEnabled())
                            && !aEg.thePlayer.onGround
                            && !aEg.gameSettings.keyBindRight.isKeyDown()
                            && !aEg.gameSettings.keyBindLeft.isKeyDown()
                            && aEg.thePlayer.Zl > 5) {
                            RotationComponent.setRotations(new Vector2f(aEg.thePlayer.pl + 45.0F, aEg.thePlayer.rotationPitch), 10.0, MovementFix.NORMAL);
                        }
                        break label45;
                    case "Speed Equivalent (Almost legit, Very hard to flag)":
                        break;
                    default:
                        break label45;
                }
            }

            MoveUtil.useDiagonalSpeed();
        }

        if (this.noJumpDelay.wo()) {
            aEg.thePlayer.jumpTicks = 0;
        }

        if (this.cpuSpeedUpExploit.wo()) {
            aEg.timer.dzD = 1.004F;
        }
    };
    @EventLink
    Listener<PacketSendEvent> onPacketSend = var0 -> {
        boolean flag = aEg.thePlayer.isSprinting();
        if (aEg.thePlayer.isSprinting()) {
            ;
        }
    };
    @EventLink(value = 4)
    public final Listener<StrafeEvent> strafe = var1x -> {
        if (aEg.thePlayer.isCollidedHorizontally) {
            this.e(Scaffold.class).isEnabled();
        }

        if (!ViaLoadingBase.getInstance().getTargetVersion().newerThanOrEqualTo(ProtocolVersion.v1_21_2)
            && aEg.thePlayer.ae > 2
            && aEg.thePlayer.Zl > 2
            && ViaLoadingBase.getInstance().getTargetVersion().newerThanOrEqualTo(ProtocolVersion.v1_19_4)) {
            MoveUtil.moveFlying(1.0E-5);
        }

        ViaLoadingBase.getInstance().getTargetVersion().equalTo(ProtocolVersion.v1_8);
        if (aEg.thePlayer.onGround && MoveUtil.isMoving() && aEg.gameSettings.keyBindJump.isKeyDown() && !aEg.thePlayer.inWater) {
            aEg.thePlayer.jump();
        }
    };

    public LegitSpeed(String var1, Speed speed) {
        super(var1, speed);
    }
}
