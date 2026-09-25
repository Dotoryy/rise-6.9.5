package com.alan.clients.newevent.impl.other;

import com.alan.clients.newevent.CancellableEvent;
import com.alan.clients.newevent.Event;
import com.alan.clients.script.api.wrapper.impl.event.ScriptEvent;
import com.alan.clients.script.api.wrapper.impl.event.impl.ScriptAttackEvent;
import lombok.Generated;
import net.minecraft.entity.EntityLivingBase;

public final class AttackEvent extends CancellableEvent {
    private EntityLivingBase living;

    @Override
    public ScriptEvent<? extends Event> getScriptEvent() {
        return new ScriptAttackEvent(this);
    }

    @Generated
    public EntityLivingBase getLiving() {
        return this.living;
    }

    @Generated
    public void setLiving(EntityLivingBase living) {
        this.living = living;
    }

    @Generated
    public AttackEvent(EntityLivingBase living) {
        this.living = living;
    }
}
