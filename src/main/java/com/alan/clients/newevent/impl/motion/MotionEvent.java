package com.alan.clients.newevent.impl.motion;

import com.alan.clients.newevent.CancellableEvent;
import lombok.Generated;
import net.minecraft.util.Vec3;

public final class MotionEvent extends CancellableEvent {
    private Vec3 jw;

    @Generated
    public Vec3 cZ() {
        return this.jw;
    }

    @Generated
    public void b(Vec3 vec) {
        this.jw = vec;
    }

    @Generated
    public MotionEvent(Vec3 vec) {
        this.jw = vec;
    }
}
