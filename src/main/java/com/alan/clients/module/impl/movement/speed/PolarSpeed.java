package com.alan.clients.module.impl.movement.speed;

import com.alan.clients.component.impl.player.RotationComponent;
import com.alan.clients.module.impl.movement.Speed;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.JumpEvent;
import com.alan.clients.newevent.impl.motion.PreUpdateEvent;
import com.alan.clients.newevent.impl.motion.StrafeEvent;
import com.alan.clients.util.player.MoveUtil;
import com.alan.clients.value.Mode;
import com.alan.clients.util.chat.ChatUtil;

public class PolarSpeed extends Mode<Speed> {
    private int jumps = 0;
    @EventLink(value = 4)
    public final Listener<PreUpdateEvent> onPreUpdate = var0 -> {
        aEg.timer.dzD = 1.009F;
        if (!aEg.thePlayer.onGround && aEg.gameSettings.keyBindForward.isKeyDown()) {
            RotationComponent.bd();
        }
    };
    @EventLink(value = 4)
    public final Listener<StrafeEvent> onStrafe = var1x -> {
        if (aEg.thePlayer.tR == 5 && this.jumps % 2 != 0) {
            aEg.thePlayer.motionY -= 0.03;
        }

        if (aEg.thePlayer.onGround) {
            aEg.thePlayer.jump();
        }

        if (this.jumps % 2 != 0) {
            ChatUtil.c(aEg.thePlayer.tR);
        }

        MoveUtil.moveFlying(0.002);
    };
    @EventLink
    public final Listener<JumpEvent> onJump = var1x -> this.jumps++;

    public PolarSpeed(String var1, Speed speed) {
        super(var1, speed);
    }
}
