package com.alan.clients.module.impl.movement.longjump;

import com.alan.clients.module.impl.movement.LongJump;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.value.Mode;

public final class Vulcan2LongJump extends Mode<LongJump> {
    private int ticks;
    @EventLink
    public final Listener<PreMotionEvent> onPreMotion = var1x -> {
        this.ticks++;
        if (this.ticks == 1) {
            aEg.thePlayer.motionY = 0.0;
            aEg.thePlayer.onGround = true;
            aEg.thePlayer.setPosition(aEg.thePlayer.posX, aEg.thePlayer.posY + 9.9, aEg.thePlayer.posZ);
        }

        if (this.ticks > 0 && this.ticks <= 3) {
            aEg.thePlayer.motionY = 0.0;
            aEg.thePlayer.onGround = true;
        }

        if (this.ticks > 3 && this.ticks % 2 == 0 & !aEg.thePlayer.onGround) {
            aEg.thePlayer.motionY = -0.155;
        } else if (this.ticks % 2 != 0 || aEg.thePlayer.onGround) {
            aEg.thePlayer.motionY = -0.098;
        }
    };

    public Vulcan2LongJump(String var1, LongJump longJump) {
        super(var1, longJump);
    }

    @Override
    public void onEnable() {
        this.ticks = 0;
    }
}
