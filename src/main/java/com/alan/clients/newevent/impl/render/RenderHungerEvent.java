package com.alan.clients.newevent.impl.render;

import com.alan.clients.newevent.Event;
import lombok.Generated;
import net.minecraft.client.gui.ScaledResolution;

public final class RenderHungerEvent implements Event {
    private final ScaledResolution scaledResolution;

    @Generated
    public ScaledResolution getScaledResolution() {
        return this.scaledResolution;
    }

    @Generated
    public RenderHungerEvent(ScaledResolution scaledResolution) {
        this.scaledResolution = scaledResolution;
    }
}
