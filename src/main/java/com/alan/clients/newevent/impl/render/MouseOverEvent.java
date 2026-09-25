package com.alan.clients.newevent.impl.render;

import com.alan.clients.newevent.Event;
import lombok.Generated;
import net.minecraft.util.MovingObjectPosition;

public class MouseOverEvent implements Event {
    private double range;
    private float expand;
    private MovingObjectPosition kf;

    public MouseOverEvent(double range, float expand) {
        this.range = range;
        this.expand = expand;
    }

    @Generated
    public double dA() {
        return this.range;
    }

    @Generated
    public float dB() {
        return this.expand;
    }

    @Generated
    public MovingObjectPosition dC() {
        return this.kf;
    }

    @Generated
    public void setRange(double range) {
        this.range = range;
    }

    @Generated
    public void setExpand(float expand) {
        this.expand = expand;
    }

    @Generated
    public void a(MovingObjectPosition hit) {
        this.kf = hit;
    }
}
