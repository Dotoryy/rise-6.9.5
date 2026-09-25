package com.alan.clients.module.impl.player.nofall;

import com.alan.clients.module.impl.player.NoFall;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.newevent.impl.other.TeleportEvent;
import com.alan.clients.value.Mode;
import com.alan.clients.component.impl.player.FallDistanceComponent;
import net.minecraft.network.play.client.C03PacketPlayer.C06PacketPlayerPosLook;

public class FlagNoFall extends Mode<NoFall> {
    @EventLink
    public final Listener<PreMotionEvent> onPreMotion = var0 -> {
        float f = FallDistanceComponent.cY;
        if (f > 3.0F) {
            f = -999.0F;
            var0.setPosY(var0.getPosY() + Math.random());
        }

        FallDistanceComponent.cY = f;
    };
    @EventLink
    public final Listener<TeleportEvent> onTeleport = var0 -> {
        FallDistanceComponent.cY = 0.0F;
        var0.setResponse(new C06PacketPlayerPosLook(var0.getPosX(), var0.getPosY(), var0.getPosZ(), var0.getYaw(), var0.getPitch(), true));
    };

    public FlagNoFall(String var1, NoFall noFall) {
        super(var1, noFall);
    }
}
