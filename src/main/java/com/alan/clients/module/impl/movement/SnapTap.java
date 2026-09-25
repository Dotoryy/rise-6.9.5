package com.alan.clients.module.impl.movement;

import com.alan.clients.module.Module;
import com.alan.clients.module.api.Category;
import com.alan.clients.module.api.ModuleInfo;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.input.MoveInputEvent;

@ModuleInfo(
    aliases = {"module.movement.snaptap.name", "Null Move", "SOCD", "Null Binds"},
    description = "module.movement.snaptap.description",
    category = Category.MOVEMENT
)
public class SnapTap extends Module {
    private boolean lastForward;
    private boolean lastBack;
    private boolean lastLeft;
    private boolean lastRight;
    private long forwardTime;
    private long backTime;
    private long leftTime;
    private long rightTime;
    @EventLink
    public final Listener<MoveInputEvent> onMoveInput = var1 -> {
        boolean flag = aEg.gameSettings.keyBindForward.isKeyDown();
        boolean flag1 = aEg.gameSettings.keyBindBack.isKeyDown();
        boolean flag2 = aEg.gameSettings.keyBindLeft.isKeyDown();
        boolean flag3 = aEg.gameSettings.keyBindRight.isKeyDown();
        long i = System.currentTimeMillis();
        if (flag && !this.lastForward) {
            this.forwardTime = i;
        }

        if (flag1 && !this.lastBack) {
            this.backTime = i;
        }

        if (flag2 && !this.lastLeft) {
            this.leftTime = i;
        }

        if (flag3 && !this.lastRight) {
            this.rightTime = i;
        }

        if (flag && flag1) {
            if (this.forwardTime != this.backTime) {
                var1.setForward(this.forwardTime > this.backTime ? 1.0F : -1.0F);
            } else {
                var1.setForward(0.0F);
            }
        } else if (flag) {
            var1.setForward(1.0F);
        } else if (flag1) {
            var1.setForward(-1.0F);
        }

        if (flag2 && flag3) {
            if (this.leftTime != this.rightTime) {
                var1.setStrafe(this.leftTime > this.rightTime ? 1.0F : -1.0F);
            } else {
                var1.setStrafe(0.0F);
            }
        } else if (flag2) {
            var1.setStrafe(1.0F);
        } else if (flag3) {
            var1.setStrafe(-1.0F);
        }

        this.lastForward = flag;
        this.lastBack = flag1;
        this.lastLeft = flag2;
        this.lastRight = flag3;
    };

    public SnapTap() {
    }

    @Override
    public void onDisable() {
        this.lastForward = false;
        this.lastBack = false;
        this.lastLeft = false;
        this.lastRight = false;
        this.forwardTime = 0L;
        this.backTime = 0L;
        this.leftTime = 0L;
        this.rightTime = 0L;
    }
}
