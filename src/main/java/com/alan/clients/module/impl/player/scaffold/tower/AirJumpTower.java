package com.alan.clients.module.impl.player.scaffold.tower;

import com.alan.clients.module.impl.player.Scaffold;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.value.Mode;
import com.alan.clients.util.player.PlayerUtil;

public class AirJumpTower extends Mode<Scaffold> {
    @EventLink
    public final Listener<PreMotionEvent> onPreMotionEvent = var0 -> {
        if (aEg.gameSettings.keyBindJump.isKeyDown() && aEg.thePlayer.ticksExisted % 2 == 0 && PlayerUtil.ay(2)) {
            aEg.thePlayer.motionY = 0.42F;
            var0.setOnGround(true);
        }
    };

    public AirJumpTower(String var1, Scaffold scaffold) {
        super(var1, scaffold);
    }
}
