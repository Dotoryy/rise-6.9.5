package com.alan.clients.component.impl.player;

import com.alan.clients.component.Component;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PostStrafeEvent;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.util.player.MoveUtil;
import lombok.Generated;

public final class PacketlessDamageComponent extends Component {
    private static boolean active;
    private static float timer;
    private static int jumps;
    @EventLink
    public final Listener<PreMotionEvent> onPreMotionEvent = var0 -> {
        if (active) {
            if (jumps < 4) {
                aEg.timer.dzD = timer;
                if (aEg.thePlayer.onGround) {
                    aEg.thePlayer.jump();
                    jumps++;
                }

                var0.setOnGround(false);
            } else if (aEg.thePlayer.tR >= 11) {
                aEg.timer.dzD = 1.0F;
                active = false;
                timer = 1.0F;
                jumps = 0;
            }
        }
    };
    @EventLink
    public final Listener<PostStrafeEvent> onPostStrafe = var0 -> {
        if (active) {
            MoveUtil.stop();
        }
    };

    public PacketlessDamageComponent() {
    }

    public static void setActive(float var0) {
        active = true;
        timer = var0;
        jumps = 0;
    }

    @Generated
    public static boolean bd() {
        return active;
    }

    @Generated
    public static int be() {
        return jumps;
    }
}
