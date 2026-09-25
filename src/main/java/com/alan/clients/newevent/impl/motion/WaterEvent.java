package com.alan.clients.newevent.impl.motion;

import com.alan.clients.newevent.Event;
import com.alan.clients.script.api.wrapper.impl.event.ScriptEvent;
import com.alan.clients.script.api.wrapper.impl.event.impl.ScriptWaterEvent;
import lombok.Generated;

public class WaterEvent implements Event {
    private boolean water;

    @Override
    public ScriptEvent<? extends Event> getScriptEvent() {
        return new ScriptWaterEvent(this);
    }

    @Generated
    public boolean isWater() {
        return this.water;
    }

    @Generated
    public void setWater(boolean water) {
        this.water = water;
    }

    @Generated
    public WaterEvent(boolean water) {
        this.water = water;
    }
}
