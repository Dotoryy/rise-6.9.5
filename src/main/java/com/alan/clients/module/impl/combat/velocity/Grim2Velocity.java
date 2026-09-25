package com.alan.clients.module.impl.combat.velocity;

import com.alan.clients.module.impl.combat.Velocity;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.packet.PacketReceiveEvent;
import com.alan.clients.newevent.impl.packet.PacketSendEvent;
import com.alan.clients.value.Mode;
import com.alan.clients.value.impl.BooleanValue;
import com.alan.clients.value.impl.NumberValue;
import com.alan.clients.util.chat.ChatUtil;
import java.util.Random;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer.C05PacketPlayerLook;
import net.minecraft.network.play.client.C03PacketPlayer.C06PacketPlayerPosLook;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.play.server.S12PacketEntityVelocity;

public final class Grim2Velocity extends Mode<Velocity> {
    private final BooleanValue fullRotationFix = new BooleanValue("Full Rotation Fix", this, true);
    private final BooleanValue fakeS08 = new BooleanValue("Fake S08", this, true);
    private final BooleanValue cancelVelocity = new BooleanValue("Cancel Velocity", this, true);
    private final NumberValue up = new NumberValue("Rotation Noise", this, 0.001, 0.0, 0.1, 0.001);
    private final BooleanValue debugLog = new BooleanValue("Debug Log", this, false);
    private final Random random = new Random();
    @EventLink
    public final Listener<PacketSendEvent> onPacketSend = var1x -> {
        if (aEg.thePlayer != null && aEg.theWorld != null) {
            if (this.fullRotationFix.wo() && var1x.dq() instanceof C03PacketPlayer c03packetplayer && c03packetplayer.afG()) {
                float f = c03packetplayer.getYaw();
                float f1 = c03packetplayer.getPitch();
                if (this.h(f) || this.h(f1)) {
                    float f2 = this.up.wo().floatValue();
                    float f3 = f + (this.random.nextBoolean() ? 1 : -1) * f2;
                    float f4 = f1 + (this.random.nextBoolean() ? 1 : -1) * f2;
                    C03PacketPlayer c03packetplayer1;
                    if (c03packetplayer.isMoving()) {
                        c03packetplayer1 = new C06PacketPlayerPosLook(
                            c03packetplayer.afD(), c03packetplayer.afE(), c03packetplayer.afF(), f3, f4, c03packetplayer.isOnGround()
                        );
                    } else {
                        c03packetplayer1 = new C05PacketPlayerLook(f3, f4, c03packetplayer.isOnGround());
                    }

                    var1x.setPacket(c03packetplayer1);
                    this.debug("Full rotation bypass");
                }
            }
        }
    };
    @EventLink
    public final Listener<PacketReceiveEvent> onPacketReceive = var1x -> {
        if (aEg.thePlayer != null && aEg.theWorld != null) {
            Packet packet = var1x.getPacket();
            if (this.fakeS08.wo() && packet instanceof S08PacketPlayerPosLook s08packetplayerposlook) {
                aEg.thePlayer.setPosition(s08packetplayerposlook.getX(), s08packetplayerposlook.getY(), s08packetplayerposlook.getZ());
                aEg.thePlayer.motionX = 0.0;
                aEg.thePlayer.motionY = 0.0;
                aEg.thePlayer.motionZ = 0.0;
                var1x.setCancelled(true);
                this.debug("Fake S08");
            }

            if (this.cancelVelocity.wo()
                && packet instanceof S12PacketEntityVelocity s12packetentityvelocity
                && s12packetentityvelocity.getEntityID() == aEg.thePlayer.getEntityId()) {
                var1x.setCancelled(true);
                this.debug("Velocity cancelled");
            }
        }
    };

    public Grim2Velocity(String var1, Velocity velocity) {
        super(var1, velocity);
    }

    private boolean h(float var1) {
        float f = Math.abs(var1 % 90.0F);
        return f < 0.01F || f > 89.99F;
    }

    private void debug(String var1) {
        if (this.debugLog.wo()) {
            ChatUtil.b("§8[§cGrimVelocity2§8] §7" + var1);
        }
    }
}
