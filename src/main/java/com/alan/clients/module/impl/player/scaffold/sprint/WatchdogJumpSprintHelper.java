package com.alan.clients.module.impl.player.scaffold.sprint;

import com.alan.clients.Client;
import com.alan.clients.module.impl.exploit.Disabler;
import com.alan.clients.module.impl.player.Scaffold;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.JumpEvent;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.util.interfaces.InstanceAccess;
import com.alan.clients.util.packet.PacketUtil;
import com.alan.clients.util.player.MoveUtil;
import net.minecraft.network.play.client.C03PacketPlayer;

public final class WatchdogJumpSprintHelper implements InstanceAccess {
    private Scaffold scaffold;
    private static final double Y_OFFSET = 0.001;
    private boolean active;
    @EventLink(value = 3)
    public final Listener<PreMotionEvent> onPreMotion = var1x -> {
        if (!this.shouldRun()) {
            this.unregister();
        } else {
            if (!var1x.isOnGround() && !aEg.thePlayer.onGround) {
                if (!this.scaffold.isEnabled()) {
                    this.unregister();
                }
            } else {
                if (!this.e(Disabler.class).watchdogFly.wo() || !this.e(Disabler.class).isEnabled()) {
                    if (aEg.thePlayer.ticksExisted % 3 == 0 && aEg.thePlayer.cqL > 2 && !aEg.gameSettings.keyBindJump.isKeyDown()) {
                        if (this.e(Scaffold.class).isEnabled() && !aEg.gameSettings.cgI.isKeyDown() && !aEg.gameSettings.keyBindSneak.isKeyDown()) {
                            var1x.setPosY(var1x.getPosY() + 0.001);
                        }
                    } else if (this.e(Scaffold.class).isEnabled()) {
                    }
                }

                this.active = true;
            }

            if (this.e(Scaffold.class).isEnabled() && aEg.gameSettings.keyBindSneak.isPressed() && aEg.thePlayer.ticksExisted % 3 == 1) {
                aEg.gameSettings.keyBindSneak.setPressed(false);
            }

            if (this.e(Scaffold.class).isEnabled() && aEg.gameSettings.keyBindSneak.isPressed()) {
                MoveUtil.stop();
                aEg.timer.dzD = 0.5F;
                PacketUtil.send(new C03PacketPlayer(aEg.thePlayer.onGround));
            }
        }
    };
    @EventLink(value = 4)
    public final Listener<JumpEvent> onJump = var1x -> {
        if (aEg.thePlayer.ticksExisted % 3 == 1 && aEg.thePlayer.cqL > 2 && this.e(Scaffold.class).isEnabled()) {
            var1x.setCancelled();
        }
    };

    public WatchdogJumpSprintHelper(Scaffold scaffold) {
        this.scaffold = scaffold;
    }

    private void unregister() {
        Client.a.e().c(this);
        this.active = false;
    }

    private boolean shouldRun() {
        return this.scaffold.isEnabled() || this.active;
    }

    public void onEnable() {
        Client.a.e().b(this);
    }
}
