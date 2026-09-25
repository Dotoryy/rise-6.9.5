package com.alan.clients.module.impl.movement.teleport;

import com.alan.clients.module.impl.movement.Teleport;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.value.Mode;
import com.alan.clients.util.chat.ChatUtil;
import net.minecraft.util.Vec3;

public final class WatchdogBedWarsTeleport extends Mode<Teleport> {
    public Vec3 startPosition = new Vec3(0.0, 0.0, 0.0);
    @EventLink
    public final Listener<PreMotionEvent> onPreMotion = var1x -> {
        var1x.setPosY(this.startPosition.yCoord);
        var1x.setPosX(this.startPosition.xCoord);
        var1x.setPosZ(this.startPosition.zCoord);
    };

    public WatchdogBedWarsTeleport(String var1, Teleport teleport) {
        super(var1, teleport);
    }

    @Override
    public void onEnable() {
        this.startPosition = new Vec3(aEg.thePlayer.posX, aEg.thePlayer.posY, aEg.thePlayer.posZ);
        ChatUtil.b("Die -> Fly to where you want to teleport -> Toggle");
    }
}
