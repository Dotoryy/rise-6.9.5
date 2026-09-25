package com.alan.clients.newevent.impl.render;

import com.alan.clients.newevent.Event;
import net.minecraft.entity.EntityLivingBase;

public final class RenderLayersEvent implements Event {
    private final EntityLivingBase ko;
    private Runnable kp;
    private Runnable kq;

    public RenderLayersEvent(EntityLivingBase living, Runnable runnable, Runnable var3) {
        this.ko = living;
        this.kp = runnable;
        this.kq = var3;
    }

    public EntityLivingBase dH() {
        return this.ko;
    }

    public Runnable dI() {
        return this.kp;
    }

    public Runnable dJ() {
        return this.kq;
    }

    public void a(Runnable runnable) {
        this.kp = runnable;
    }

    public void b(Runnable runnable) {
        this.kq = runnable;
    }
}
