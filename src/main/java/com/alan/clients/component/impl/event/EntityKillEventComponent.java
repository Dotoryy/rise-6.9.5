package com.alan.clients.component.impl.event;

import com.alan.clients.Client;
import com.alan.clients.component.Component;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.newevent.impl.other.AttackEvent;
import com.alan.clients.newevent.impl.other.KillEvent;
import com.alan.clients.newevent.impl.other.WorldChangeEvent;
import com.alan.clients.util.interfaces.ExecutorAccess;
import net.minecraft.entity.Entity;

public class EntityKillEventComponent extends Component implements ExecutorAccess {
    Entity target = null;
    @EventLink(value = 1)
    public final Listener<PreMotionEvent> onPreMotionEvent = var1 -> {
        if (this.target != null && !aEg.theWorld.loadedEntityList.contains(this.target)) {
            Client.a.e().d(new KillEvent(this.target));
            this.target = null;
        }
    };
    @EventLink(value = 1)
    public final Listener<AttackEvent> onAttackEvent = var1 -> this.target = var1.getLiving();
    @EventLink(value = 1)
    public final Listener<WorldChangeEvent> onWorldChange = var1 -> this.target = null;

    public EntityKillEventComponent() {
    }
}
