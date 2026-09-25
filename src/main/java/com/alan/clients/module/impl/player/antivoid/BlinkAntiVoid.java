package com.alan.clients.module.impl.player.antivoid;

import com.alan.clients.component.impl.player.BlinkComponent;
import com.alan.clients.module.impl.player.AntiVoid;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PostMotionEvent;
import com.alan.clients.util.player.MoveUtil;
import com.alan.clients.util.vector.Vector2f;
import com.alan.clients.value.Mode;
import com.alan.clients.util.player.PlayerUtil;
import com.alan.clients.util.vector.Vector3d;
import com.alan.clients.component.impl.player.FallDistanceComponent;

public class BlinkAntiVoid extends Mode<AntiVoid> {
    private Vector3d position;
    private Vector3d motion;
    private Vector2f rotation;
    @EventLink
    public final Listener<PostMotionEvent> onPreUpdate = var1x -> {
        if (aEg.thePlayer.ticksExisted > 60) {
            if (this.position == null || this.motion == null || this.rotation == null || PlayerUtil.a(50.0, true)) {
                this.position = new Vector3d(aEg.thePlayer.posX, aEg.thePlayer.posY, aEg.thePlayer.posZ);
                this.motion = new Vector3d(aEg.thePlayer.motionX, aEg.thePlayer.motionY, aEg.thePlayer.motionZ);
                this.rotation = new Vector2f(aEg.thePlayer.pl, aEg.thePlayer.rotationPitch);
            } else if (FallDistanceComponent.cY > 4.0F) {
                aEg.thePlayer.setPosition(this.position.x, this.position.y, this.position.z);
                aEg.thePlayer.motionX = 0.0;
                aEg.thePlayer.motionY = MoveUtil.predictedMotion(this.motion.y);
                aEg.thePlayer.motionZ = 0.0;
                aEg.thePlayer.pl = this.rotation.x;
                aEg.thePlayer.rotationPitch = this.rotation.y;
                FallDistanceComponent.cY = 0.0F;
                BlinkComponent.disable();
                BlinkComponent.dispatch();
            }
        }
    };

    public BlinkAntiVoid(String var1, AntiVoid antiVoid) {
        super(var1, antiVoid);
    }
}
