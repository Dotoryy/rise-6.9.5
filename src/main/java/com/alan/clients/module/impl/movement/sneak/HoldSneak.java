package com.alan.clients.module.impl.movement.sneak;

import com.alan.clients.module.impl.movement.Sneak;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.value.Mode;
import org.lwjgl.input.Keyboard;

public class HoldSneak extends Mode<Sneak> {
    @EventLink
    public final Listener<PreMotionEvent> onPreMotionEvent = var0 -> aEg.gameSettings.keyBindSneak.setPressed(true);

    public HoldSneak(String var1, Sneak sneak) {
        super(var1, sneak);
    }

    @Override
    public void onDisable() {
        aEg.gameSettings.keyBindSneak.setPressed(Keyboard.isKeyDown(aEg.gameSettings.keyBindSneak.getKeyCode()));
    }
}
