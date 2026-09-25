package com.alan.clients.newevent.impl.render;

import com.alan.clients.newevent.Event;
import com.alan.clients.util.vector.Vector2f;
import lombok.Generated;

public final class LookEvent implements Event {
    private Vector2f rotation;

    @Generated
    public Vector2f dy() {
        return this.rotation;
    }

    @Generated
    public void setRotation(Vector2f rotation) {
        this.rotation = rotation;
    }

    @Generated
    public LookEvent(Vector2f rotation) {
        this.rotation = rotation;
    }
}
