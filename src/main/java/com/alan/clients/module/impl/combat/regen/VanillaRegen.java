package com.alan.clients.module.impl.combat.regen;

import com.alan.clients.module.impl.combat.Regen;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.value.Mode;
import com.alan.clients.value.impl.NumberValue;
import com.alan.clients.util.packet.PacketUtil;
import net.minecraft.network.play.client.C03PacketPlayer.C06PacketPlayerPosLook;

public final class VanillaRegen extends Mode<Regen> {
    private final NumberValue health = new NumberValue("Minimum Health", this, 15, 1, 20, 1);
    private final NumberValue packets = new NumberValue("Speed", this, 20, 1, 100, 1);
    @EventLink
    public final Listener<PreMotionEvent> onPreMotionEvent = var1x -> {
        if (aEg.thePlayer.getHealth() < this.health.wo().floatValue()) {
            for (int i = 0; i < this.packets.wo().intValue(); i++) {
                PacketUtil.send(
                    new C06PacketPlayerPosLook(
                        aEg.thePlayer.posX, aEg.thePlayer.posY, aEg.thePlayer.posZ, aEg.thePlayer.pl, aEg.thePlayer.rotationPitch, aEg.thePlayer.onGround
                    )
                );
            }
        }
    };

    public VanillaRegen(String var1, Regen regen) {
        super(var1, regen);
    }
}
