package com.alan.clients.newevent.impl.render;

import com.alan.clients.newevent.Event;
import com.alan.clients.script.api.wrapper.impl.event.ScriptEvent;
import com.alan.clients.script.api.wrapper.impl.event.impl.ScriptRender2DEvent;
import lombok.Generated;
import net.minecraft.client.gui.ScaledResolution;

public final class Render2DEvent implements Event {
    private final ScaledResolution scaledResolution;
    private final float partialTicks;

    @Override
    public ScriptEvent<? extends Event> getScriptEvent() {
        return new ScriptRender2DEvent(this);
    }

    @Generated
    public ScaledResolution getScaledResolution() {
        return this.scaledResolution;
    }

    @Generated
    public float getPartialTicks() {
        return this.partialTicks;
    }

    @Generated
    public Render2DEvent(ScaledResolution scaledResolution, float partialTicks) {
        this.scaledResolution = scaledResolution;
        this.partialTicks = partialTicks;
    }
}
