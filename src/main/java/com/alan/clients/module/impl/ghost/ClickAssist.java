package com.alan.clients.module.impl.ghost;

import com.alan.clients.module.Module;
import com.alan.clients.module.api.Category;
import com.alan.clients.module.api.ModuleInfo;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.value.impl.NumberValue;
import com.alan.clients.component.impl.player.BadPacketsComponent;

@ModuleInfo(aliases = "module.ghost.clickassist.name", description = "module.ghost.clickassist.description", category = Category.GHOST)
public class ClickAssist extends Module {
    public final NumberValue extraLeftClicks = new NumberValue("Extra Left Clicks", this, 1, 0, 3, 1);
    public final NumberValue extraRightClicks = new NumberValue("Extra Right Clicks", this, 1, 0, 3, 1);
    public int remainingLeftClicks;
    public int remainingRightClicks;
    private boolean leftWasDown;
    private boolean rightWasDown;
    @EventLink
    public final Listener<PreMotionEvent> onPreMotionEvent = var1 -> {
        if (aEg.gameSettings.cgK.isKeyDown()) {
            if (!this.leftWasDown) {
                this.remainingLeftClicks = this.extraLeftClicks.wo().intValue();
            }

            this.leftWasDown = true;
        } else {
            this.leftWasDown = false;
        }

        if (aEg.gameSettings.cgI.isKeyDown()) {
            if (!this.rightWasDown) {
                this.remainingRightClicks = this.extraRightClicks.wo().intValue();
            }

            this.rightWasDown = true;
        } else {
            this.rightWasDown = false;
        }

        if (this.remainingLeftClicks > 0 && Math.random() > 0.2) {
            this.remainingLeftClicks--;
            if (!aEg.thePlayer.isUsingItem() && !BadPacketsComponent.aW()) {
                aEg.Ay();
            }
        } else if (this.remainingRightClicks > 0 && Math.random() > 0.2) {
            this.remainingRightClicks--;
            if (!aEg.thePlayer.isUsingItem() && !BadPacketsComponent.aW()) {
                aEg.Az();
            }
        }
    };

    public ClickAssist() {
    }
}
