package com.alan.clients.module.impl.player.antivoid;

import com.alan.clients.module.impl.player.AntiVoid;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.value.Mode;
import com.alan.clients.value.impl.NumberValue;
import com.alan.clients.util.packet.PacketUtil;
import com.alan.clients.util.player.PlayerUtil;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;

public class PacketAntiVoid extends Mode<AntiVoid> {
    private final NumberValue distance = new NumberValue("Distance", this, 5, 0, 10, 1);
    @EventLink
    public final Listener<PreMotionEvent> onPreMotionEvent = var1x -> {
        if (aEg.thePlayer.fallDistance > this.distance.wo().floatValue() && !PlayerUtil.vh()) {
            PacketUtil.send(new C04PacketPlayerPosition());
        }
    };

    public PacketAntiVoid(String var1, AntiVoid antiVoid) {
        super(var1, antiVoid);
    }
}
