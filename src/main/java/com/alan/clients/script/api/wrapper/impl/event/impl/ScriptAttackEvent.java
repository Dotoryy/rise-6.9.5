package com.alan.clients.script.api.wrapper.impl.event.impl;

import com.alan.clients.newevent.impl.other.AttackEvent;
import com.alan.clients.script.api.wrapper.impl.ScriptEntityLiving;
import com.alan.clients.script.api.wrapper.impl.event.CancellableScriptEvent;

public class ScriptAttackEvent extends CancellableScriptEvent<AttackEvent> {
    public ScriptAttackEvent(AttackEvent event) {
        super(event);
    }

    public ScriptEntityLiving getTarget() {
        return new ScriptEntityLiving(this.wrapped.getLiving());
    }

    @Override
    public String getHandlerName() {
        return "onAttack";
    }
}
