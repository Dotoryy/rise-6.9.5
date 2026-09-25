package com.alan.clients.module.impl.movement.flight;

import com.alan.clients.module.impl.movement.Flight;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.newevent.impl.motion.StrafeEvent;
import com.alan.clients.util.player.MoveUtil;
import com.alan.clients.value.Mode;
import com.alan.clients.component.impl.render.NotificationComponent;
import net.minecraft.util.Vec3;

public class ZoneCraftFlight extends Mode<Flight> {
    public Vec3 position = new Vec3(0.0, 0.0, 0.0);
    @EventLink
    public final Listener<PreMotionEvent> onPreMotionEvent = var1x -> {
        var1x.setPosX(this.position.xCoord);
        var1x.setPosY(this.position.yCoord);
        var1x.setPosZ(this.position.zCoord);
        var1x.setOnGround(true);
    };
    @EventLink
    public final Listener<StrafeEvent> onStrafe = var0 -> {
        var0.setSpeed(3.0);
        aEg.thePlayer.motionY = 0.0;
    };

    public ZoneCraftFlight(String var1, Flight flight) {
        super(var1, flight);
    }

    @Override
    public void onEnable() {
        if (!aEg.thePlayer.onGround) {
            this.toggle();
        }

        NotificationComponent.e("Flight", "This feature is only enabled for developers atm");
        this.position = new Vec3(aEg.thePlayer.posX, aEg.thePlayer.posY, aEg.thePlayer.posZ);
    }

    @Override
    public void onDisable() {
        MoveUtil.stop();
    }
}
