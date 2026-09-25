package com.alan.clients.newevent.impl.other;

import com.alan.clients.newevent.Event;
import com.alan.clients.script.api.wrapper.impl.event.ScriptEvent;
import com.alan.clients.script.api.wrapper.impl.event.impl.ScriptKillEvent;
import lombok.Generated;
import net.minecraft.entity.Entity;

public final class KillEvent implements Event {
    Entity entity;

    @Override
    public ScriptEvent<? extends Event> getScriptEvent() {
        return new ScriptKillEvent(this);
    }

    @Generated
    public Entity getEntity() {
        return this.entity;
    }

    @Generated
    public KillEvent(Entity entity) {
        this.entity = entity;
    }
}
