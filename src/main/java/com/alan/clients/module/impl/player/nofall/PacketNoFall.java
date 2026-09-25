package com.alan.clients.module.impl.player.nofall;

import com.alan.clients.module.impl.player.NoFall;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.value.Mode;
import com.alan.clients.util.packet.PacketUtil;
import com.alan.clients.component.impl.player.FallDistanceComponent;
import net.minecraft.network.play.client.C03PacketPlayer;

public class PacketNoFall extends Mode<NoFall> {
    @EventLink
    public final Listener<PreMotionEvent> onPreMotionEvent = var0 -> {
        float f = FallDistanceComponent.cY;
        if (FallDistanceComponent.cY > 3.1 + aEg.thePlayer.motionY) {
            PacketUtil.send(new C03PacketPlayer(true));
            f = 0.0F;
        }

        FallDistanceComponent.cY = f;
    };

    public PacketNoFall(String var1, NoFall noFall) {
        super(var1, noFall);
    }
}
