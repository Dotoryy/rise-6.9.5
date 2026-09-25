package com.alan.clients.component.impl.player;

import com.alan.clients.component.Component;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;

public final class FallDistanceComponent extends Component {
    public static float cY;
    @EventLink(value = 0)
    public final Listener<PreMotionEvent> onPreMotionEvent = var0 -> {
        double d0 = aEg.thePlayer.lastTickPosY - aEg.thePlayer.posY;
        if (d0 > 0.0) {
            cY = (float)(cY + d0);
        }

        if (var0.isOnGround()) {
            cY = 0.0F;
        }
    };

    public FallDistanceComponent() {
    }
}
