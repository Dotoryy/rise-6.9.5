package com.alan.clients.component.impl.render;

import com.alan.clients.component.Component;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.render.Render2DEvent;
import com.alan.clients.util.interfaces.ExecutorAccess;
import com.alan.clients.util.render.particle.Particle;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ParticleComponent extends Component implements ExecutorAccess {
    public static ConcurrentLinkedQueue<Particle> hu = new ConcurrentLinkedQueue<>();
    public static int hv;
    public static int hw;
    @EventLink(value = 4)
    public final Listener<Render2DEvent> onRender2DEvent = var1 -> hu.isEmpty();

    public ParticleComponent() {
    }

    public static void ci() {
        hu.forEach(Particle::ci);
        hw = aEg.ingameGUI.bnh;
    }

    public static void cj() {
        hv = aEg.ingameGUI.bnh;
        hu.forEach(var0 -> {
            var0.cj();
            if (var0.hj.getElapsedTime() > 3000L) {
                hu.remove(var0);
            }
        });
        if (!hu.isEmpty()) {
            aMR.execute(() -> hu.forEach(Particle::ju));
        }
    }

    public static void a(Particle particle) {
        hu.add(particle);
    }
}
