package com.alan.clients.module.impl.other;

import com.alan.clients.module.Module;
import com.alan.clients.module.api.Category;
import com.alan.clients.module.api.ModuleInfo;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import net.minecraft.client.settings.GameSettings;

@ModuleInfo(aliases = "module.other.antiafk.name", description = "module.other.antiafk.description", category = Category.MOVEMENT)
public final class AntiAFK extends Module {
    private int lastInput;
    @EventLink
    public final Listener<PreMotionEvent> onPreMotionEvent = var1 -> {
        GameSettings gamesettings = aEg.gameSettings;
        if (gamesettings.keyBindJump.isPressed()
            || gamesettings.keyBindRight.isPressed()
            || gamesettings.keyBindForward.isPressed()
            || gamesettings.keyBindLeft.isPressed()
            || gamesettings.keyBindBack.isPressed()) {
            this.lastInput = 0;
        }

        this.lastInput++;
        if (this.lastInput >= 200) {
            if (aEg.thePlayer.ticksExisted % 5 == 0) {
                aEg.gameSettings.keyBindRight.setPressed(false);
                aEg.gameSettings.keyBindLeft.setPressed(false);
                aEg.gameSettings.keyBindJump.setPressed(false);
            }

            if (aEg.thePlayer.ticksExisted % 20 == 0) {
                if (aEg.thePlayer.ticksExisted % 40 == 0) {
                    aEg.gameSettings.keyBindRight.setPressed(true);
                } else {
                    aEg.gameSettings.keyBindLeft.setPressed(true);
                }
            }

            if (aEg.thePlayer.ticksExisted % 100 == 0) {
                aEg.gameSettings.keyBindJump.setPressed(true);
            }
        }
    };

    public AntiAFK() {
    }
}
