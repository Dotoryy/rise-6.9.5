package com.alan.clients.module.impl.ghost.wtap;

import com.alan.clients.module.impl.ghost.WTap;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.newevent.impl.other.AttackEvent;
import com.alan.clients.value.Mode;

public class LegitWTap extends Mode<WTap> {
    private boolean unsprint;
    private boolean wTap;
    @EventLink
    public final Listener<AttackEvent> onAttack = var1x -> {
        this.wTap = Math.random() * 100.0 < this.getParent().chance.wo().doubleValue() && var1x.getLiving().hurtTime >= 6;
        if (this.wTap && !this.unsprint) {
            if (aEg.thePlayer.isSprinting() || aEg.gameSettings.cgG.isKeyDown()) {
                aEg.gameSettings.cgG.setPressed(true);
                this.unsprint = true;
            }
        }
    };
    @EventLink
    public final Listener<PreMotionEvent> onPreMotionEvent = var1x -> {
        if (this.wTap) {
            if (this.unsprint && Math.random() * 100.0 < this.getParent().chance.wo().doubleValue()) {
                aEg.gameSettings.cgG.setPressed(false);
                this.unsprint = false;
            }
        }
    };

    public LegitWTap(String var1, WTap wTap) {
        super(var1, wTap);
    }
}
