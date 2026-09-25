package com.alan.clients.module.impl.player.scaffold.tower;

import com.alan.clients.module.impl.player.Scaffold;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.newevent.impl.motion.StrafeEvent;
import com.alan.clients.value.Mode;
import com.alan.clients.util.player.PlayerUtil;
import net.minecraft.potion.Potion;

public class VerusTower extends Mode<Scaffold> {
    @EventLink
    public final Listener<PreMotionEvent> onPreMotion = var0 -> {
        if (aEg.gameSettings.keyBindJump.isKeyDown() && PlayerUtil.ay(1)) {
            aEg.thePlayer.motionY = 0.8F;
        }
    };
    @EventLink
    public final Listener<StrafeEvent> onStrafe = var0 -> {
        if (aEg.thePlayer.motionY < 0.77) {
            aEg.thePlayer.isPotionActive(Potion.moveSpeed);
        }
    };

    public VerusTower(String var1, Scaffold scaffold) {
        super(var1, scaffold);
    }
}
