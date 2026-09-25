package com.alan.clients.component.impl.player;

import com.alan.clients.component.Component;
import com.alan.clients.component.impl.player.RotationComponent;
import com.alan.clients.component.impl.player.rotationcomponent.MovementFix;
import com.alan.clients.module.impl.movement.Speed;
import com.alan.clients.module.impl.player.Scaffold;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.newevent.impl.motion.StrafeEvent;
import com.alan.clients.util.player.MoveUtil;
import com.alan.clients.util.vector.Vector2f;
import com.alan.clients.component.impl.player.BadPacketsComponent;
import net.minecraft.potion.Potion;

public class WatchdogJumpComponent extends Component {
    private boolean gC = false;
    private boolean gD = false;
    @EventLink
    public final Listener<PreMotionEvent> onPreMotion = var1 -> {
        if (this.e(Scaffold.class).sprint.wo().getName().equals("Watchdog Jump") || this.e(Scaffold.class).sprint.wo().getName().equals("Watchdog Fast")) {
            if (this.gD
                && !aEg.gameSettings.keyBindJump.isKeyDown()
                && this.e(Scaffold.class).isEnabled()
                && !this.e(Speed.class).isEnabled()
                && MoveUtil.isMoving()) {
                if (aEg.thePlayer.isPotionActive(Potion.moveSpeed)) {
                    MoveUtil.strafe(-0.1);
                } else {
                    MoveUtil.strafe(-0.1);
                }

                aEg.thePlayer.crd = true;
                RotationComponent.d(false);
                RotationComponent.setRotations(
                    new Vector2f((float)(aEg.thePlayer.pl - 99.99999999999999 + (Math.random() - 0.5) * 3.0), 86.0F), 10.0, MovementFix.OFF
                );
                this.e(Speed.class).isEnabled();
                this.gD = false;
                this.gC = true;
            } else if (!aEg.gameSettings.keyBindJump.isKeyDown() && this.gC && !this.e(Speed.class).isEnabled() && MoveUtil.isMoving()) {
                if (this.e(Speed.class).isEnabled()) {
                    if (aEg.thePlayer.isPotionActive(Potion.moveSpeed)) {
                        MoveUtil.strafe(-0.03);
                    } else {
                        MoveUtil.strafe(-0.03);
                    }
                }

                if (!this.e(Speed.class).isEnabled() && !BadPacketsComponent.bad(false, false, false, true, false)) {
                    this.e(Scaffold.class).placeOffset = this.e(Scaffold.class).placeOffset.v(0.0, -1.0, 0.0);
                }

                aEg.thePlayer.crd = true;
                this.gC = false;
            }

            if (!this.e(Scaffold.class).isEnabled() || aEg.thePlayer.Zl < 2) {
                this.gD = false;
            }

            if (aEg.gameSettings.keyBindJump.isKeyDown() && this.e(Scaffold.class).isEnabled()) {
                this.gD = true;
            }
        }
    };
    @EventLink
    public final Listener<StrafeEvent> onStrafe = var1 -> {
        if (this.gD
            && !aEg.gameSettings.keyBindJump.isKeyDown()
            && this.e(Scaffold.class).isEnabled()
            && !this.e(Speed.class).isEnabled()
            && MoveUtil.isMoving()) {
            aEg.Az();
        }
    };

    public WatchdogJumpComponent() {
    }
}
