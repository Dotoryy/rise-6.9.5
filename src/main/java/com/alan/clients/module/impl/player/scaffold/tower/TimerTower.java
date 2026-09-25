package com.alan.clients.module.impl.player.scaffold.tower;

import com.alan.clients.module.impl.player.Scaffold;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.input.KeyboardInputEvent;
import com.alan.clients.newevent.impl.motion.JumpEvent;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.newevent.impl.motion.PreUpdateEvent;
import com.alan.clients.util.player.MoveUtil;
import com.alan.clients.value.Mode;

public class TimerTower extends Mode<Scaffold> {
    private boolean offsetApplied;
    @EventLink(value = 3)
    private final Listener<PreUpdateEvent> onPreUpdate = var1x -> {
        if (aEg.thePlayer.onGround && !aEg.gameSettings.keyBindJump.isKeyDown()) {
            aEg.timer.dzD = 1.0029F;
            MoveUtil.preventDiagonalSpeed();
            aEg.thePlayer.motionZ *= 0.998;
            aEg.thePlayer.motionX *= 0.998;
        }

        if (aEg.gameSettings.keyBindJump.isPressed() && aEg.thePlayer.onGround) {
            MoveUtil.stop();
            this.offsetApplied = false;
        }
    };
    @EventLink(value = 3)
    private final Listener<PreMotionEvent> onPreMotion = var1x -> {
        if (aEg.thePlayer.onGround) {
            var1x.setPosY(var1x.getPosY() + 1.0E-13);
        }

        if (aEg.thePlayer.onGround && !this.offsetApplied && !aEg.gameSettings.keyBindJump.isKeyDown()) {
            MoveUtil.stop();
            var1x.setPosY(var1x.getPosY() + 1.0E-13);
            this.offsetApplied = true;
        }
    };
    @EventLink
    public final Listener<JumpEvent> onJump = var0 -> var0.setJumpMotion(0.42F);
    @EventLink(value = 4)
    public final Listener<KeyboardInputEvent> onKeyboardInput = var0 -> {
        if (var0.getGuiScreen() == null && !var0.isCancelled()) {
            if (var0.getKeyCode() == 57) {
                var0.setCancelled();
            }
        }
    };

    public TimerTower(String var1, Scaffold scaffold) {
        super(var1, scaffold);
    }

    @Override
    public void onEnable() {
        this.offsetApplied = true;
        if (!aEg.gameSettings.keyBindJump.isKeyDown()) {
            MoveUtil.stop();
        }

        if (aEg.thePlayer.cqL > 10 && !aEg.gameSettings.keyBindJump.isKeyDown()) {
            aEg.thePlayer.motionY = 0.42;
        }
    }

    @Override
    public void onDisable() {
        MoveUtil.stop();
    }
}
