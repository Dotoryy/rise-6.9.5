package com.alan.clients.module.impl.player.scaffold.sprint;

import com.alan.clients.component.impl.player.RotationComponent;
import com.alan.clients.module.impl.movement.Speed;
import com.alan.clients.module.impl.player.Scaffold;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.StrafeEvent;
import com.alan.clients.value.Mode;
import net.minecraft.util.MathHelper;

public class MatrixSprint extends Mode<Scaffold> {
    private int time;
    private boolean ignore;
    @EventLink
    public final Listener<StrafeEvent> onStrafe = var1x -> {
        float f = MathHelper.wrapAngleTo180_float(aEg.thePlayer.pl);
        RotationComponent.d(false);
        this.e(Speed.class).setEnabled(false);
        boolean flag = Math.abs(f % 80.0F) <= 15.0F || Math.abs(f % 90.0F) >= 75.0F;
        if (!flag) {
            aEg.thePlayer.motionX *= 0.95;
            aEg.thePlayer.motionZ *= 0.95;
        }
    };

    public MatrixSprint(String var1, Scaffold scaffold) {
        super(var1, scaffold);
    }
}
