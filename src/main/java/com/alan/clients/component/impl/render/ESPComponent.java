package com.alan.clients.component.impl.render;

import com.alan.clients.Client;
import com.alan.clients.component.Component;
import com.alan.clients.module.impl.combat.KillAura;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreUpdateEvent;
import com.alan.clients.newevent.impl.other.WorldChangeEvent;
import com.alan.clients.newevent.impl.render.Render2DEvent;
import com.alan.clients.newevent.impl.render.Render3DEvent;
import com.alan.clients.component.impl.render.espcomponent.api.ESP;
import com.alan.clients.newevent.impl.other.UpdateModelEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ESPComponent extends Component {
    public static ConcurrentLinkedQueue<ESP> esps = new ConcurrentLinkedQueue<>();
    @EventLink(value = 4)
    public final Listener<Render2DEvent> onRender2D = var0 -> {
        if (!esps.isEmpty()) {
            esps.forEach(ESP::co);
        }
    };
    @EventLink(value = 4)
    public final Listener<UpdateModelEvent> onLimitedRender2D = var0 -> {
        if (esps != null && !esps.isEmpty()) {
            Iterator iterator = esps.iterator();

            while (iterator.hasNext()) {
                ((ESP)iterator.next()).a(var0.do_(), var0.dp());
            }
        }
    };
    @EventLink(value = 4)
    public final Listener<Render3DEvent> onRender3D = var0 -> {
        if (esps != null && !esps.isEmpty()) {
            esps.forEach(ESP::render3D);
        }
    };
    @EventLink(value = 4)
    public final Listener<PreUpdateEvent> onPreUpdate = var0 -> {
        KillAura killaura = Client.a.g().c(KillAura.class);
        List list = killaura.isEnabled() ? killaura.espTargets : new ArrayList();
        esps.removeIf(var1 -> var1.tick + 10 < aEg.thePlayer.ticksExisted || !list.contains(var1.target));
        Iterator iterator = esps.iterator();

        while (iterator.hasNext()) {
            ((ESP)iterator.next()).cq();
        }
    };
    @EventLink
    public final Listener<WorldChangeEvent> onWorldChange = var0 -> esps.clear();

    public ESPComponent() {
    }

    public static void add(ESP esp) {
        esps.removeIf(var1 -> esp.getClass() == var1.getClass() && esp.target == var1.target);
        esps.add(esp);
    }
}
