package com.alan.clients.module.impl.movement.flight;

import com.alan.clients.module.impl.movement.Flight;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.newevent.impl.motion.StrafeEvent;
import com.alan.clients.util.player.MoveUtil;
import com.alan.clients.value.Mode;
import com.alan.clients.value.impl.BooleanValue;
import com.alan.clients.util.chat.ChatUtil;
import com.alan.clients.util.packet.PacketUtil;
import com.alan.clients.util.player.DamageUtil;
import com.alan.clients.util.player.DamageType;
import net.minecraft.network.play.client.C03PacketPlayer.C06PacketPlayerPosLook;

public class VulcanDamageFlight extends Mode<Flight> {
    private int ticks;
    private int desyncTicks;
    private boolean flying;
    public final BooleanValue selfDamageMayFlagMoreIfNotFlyWillWaitForFallDamage = new BooleanValue("Self Damage (May Flag More) if not fly will wait for fall damage", this, true);
    @EventLink
    public final Listener<PreMotionEvent> onPreMotion = var1x -> {
        if (this.selfDamageMayFlagMoreIfNotFlyWillWaitForFallDamage.wo()) {
            MoveUtil.stop();
        }

        if (aEg.thePlayer.ae == 1) {
            PacketUtil.send(
                new C06PacketPlayerPosLook(
                    aEg.thePlayer.posX, aEg.thePlayer.posY - 2.0, aEg.thePlayer.posZ, aEg.thePlayer.pl, aEg.thePlayer.rotationPitch, false
                )
            );
            this.flying = true;
        }

        if (this.flying) {
            this.ticks++;
            if (this.ticks < 10) {
                MoveUtil.stop();
            }

            aEg.thePlayer.motionY = 1.0E-10 + (aEg.gameSettings.keyBindJump.isKeyDown() ? 0.0 : 0.0) - (aEg.gameSettings.keyBindSneak.isKeyDown() ? 0.0 : 0.0);
            if (aEg.thePlayer.getDistance(aEg.thePlayer.lastReportedPosX, aEg.thePlayer.lastReportedPosY, aEg.thePlayer.lastReportedPosZ) <= 9.0) {
                var1x.setCancelled();
            } else {
                this.desyncTicks++;
                if (this.desyncTicks >= 3) {
                    MoveUtil.stop();
                    this.getParent().toggle();
                }
            }
        }
    };
    @EventLink
    public final Listener<StrafeEvent> onStrafe = var1x -> {
        if (this.ticks >= 10) {
            if (this.flying) {
                var1x.setSpeed(1.0);
            }
        }
    };

    public VulcanDamageFlight(String var1, Flight flight) {
        super(var1, flight);
    }

    @Override
    public void onEnable() {
        this.ticks = 0;
        this.desyncTicks = 0;
        this.flying = false;
        if (this.selfDamageMayFlagMoreIfNotFlyWillWaitForFallDamage.wo()) {
            DamageUtil.damagePlayer(DamageType.POSITION, 3.42F, 1, false, false);
        } else {
            ChatUtil.b("take fall damage or turn on self damage");
        }
    }

    @Override
    public void onDisable() {
        MoveUtil.stop();
    }
}
