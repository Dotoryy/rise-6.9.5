package com.alan.clients.module.impl.combat.regen;

import com.alan.clients.module.impl.combat.Regen;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.value.Mode;
import com.alan.clients.value.impl.NumberValue;
import com.alan.clients.util.packet.PacketUtil;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;

public final class VerusRegenRegen extends Mode<Regen> {
    private final NumberValue health = new NumberValue("Minimum Health", this, 15, 1, 20, 1);
    @EventLink
    public final Listener<PreMotionEvent> onPreMotion = var1x -> {
        if (aEg.thePlayer.getHealth() < this.health.wo().floatValue()) {
            for (int i = 0; i < 30; i++) {
                if (aEg.thePlayer.onGround) {
                    PacketUtil.sendNoEvent(new C04PacketPlayerPosition(aEg.thePlayer.posX, aEg.thePlayer.posY, aEg.thePlayer.posZ, true));
                }
            }
        }
    };

    public VerusRegenRegen(String var1, Regen regen) {
        super(var1, regen);
    }
}
