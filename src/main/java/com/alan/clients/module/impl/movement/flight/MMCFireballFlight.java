package com.alan.clients.module.impl.movement.flight;

import com.alan.clients.component.impl.player.BlinkComponent;
import com.alan.clients.component.impl.player.RotationComponent;
import com.alan.clients.component.impl.player.SlotComponent;
import com.alan.clients.component.impl.player.rotationcomponent.MovementFix;
import com.alan.clients.module.impl.movement.Flight;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.newevent.impl.motion.PreUpdateEvent;
import com.alan.clients.newevent.impl.motion.StrafeEvent;
import com.alan.clients.util.player.MoveUtil;
import com.alan.clients.util.vector.Vector2f;
import com.alan.clients.value.Mode;
import com.alan.clients.util.packet.PacketUtil;
import com.alan.clients.util.player.SlotUtil;
import net.minecraft.init.Items;
import net.minecraft.network.play.client.C03PacketPlayer.C06PacketPlayerPosLook;

public class MMCFireballFlight extends Mode<Flight> {
    private int ticks;
    private int desyncTicks;
    @EventLink
    public final Listener<PreUpdateEvent> onPreUpdate = var1x -> {
        int i = SlotUtil.findItem(Items.fire_charge);
        if (this.ticks > 10) {
            BlinkComponent.a(30000, true, true, false, false, false);
        }

        if (aEg.thePlayer.cqL == 1) {
            MoveUtil.stop();
        }

        if (i != -1) {
            this.ticks++;
            if (this.ticks == 4) {
                PacketUtil.send(
                    new C06PacketPlayerPosLook(
                        aEg.thePlayer.posX, aEg.thePlayer.posY - 8.0, aEg.thePlayer.posZ, aEg.thePlayer.pl, aEg.thePlayer.rotationPitch, false
                    )
                );
            }

            SlotComponent slotcomponent = this.d(SlotComponent.class);
            SlotComponent.setSlot(i);
            if (this.ticks == 2) {
                aEg.Az();
            }

            if (this.ticks < 100) {
                RotationComponent.setRotations(new Vector2f(aEg.thePlayer.pl, 90.0F), 10.0, MovementFix.OFF);
            }
        }
    };
    @EventLink
    public final Listener<PreMotionEvent> onPreMotion = var1x -> {
        aEg.thePlayer.motionY = 0.0F + (aEg.gameSettings.keyBindJump.isKeyDown() ? 0.42F : 0.0F) - (aEg.gameSettings.keyBindSneak.isKeyDown() ? 0.42F : 0.0F);
        if (aEg.thePlayer.getDistance(aEg.thePlayer.lastReportedPosX, aEg.thePlayer.lastReportedPosY, aEg.thePlayer.lastReportedPosZ) <= 8.58F) {
            if (this.ticks > 4) {
                var1x.setCancelled();
            }
        } else {
            this.desyncTicks++;
            if (this.desyncTicks >= 8) {
                MoveUtil.stop();
                this.getParent().toggle();
            }
        }
    };
    @EventLink
    public final Listener<StrafeEvent> onStrafe = var1x -> {
        if (this.ticks > 4) {
            var1x.setSpeed(2.0);
        }
    };

    public MMCFireballFlight(String var1, Flight flight) {
        super(var1, flight);
    }

    @Override
    public void onEnable() {
        this.ticks = 0;
        this.desyncTicks = 0;
    }

    @Override
    public void onDisable() {
        MoveUtil.stop();
    }
}
