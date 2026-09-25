package com.alan.clients.component.impl.player;

import com.alan.clients.component.impl.player.BlinkComponent;

public final class BlinkTicket {
    private final long eh = BlinkComponent.dU.getAndIncrement();
    private final BlinkChannel ei;
    public boolean dj = true;

    BlinkTicket(BlinkChannel var1) {
        this.ei = var1;
    }

    boolean a(long var1, long var3) {
        return var1 + this.ei.dQ < var3;
    }

    boolean bd() {
        return this.dj && this.ei.ek == this;
    }

    @Override
    public boolean equals(Object var1) {
        return var1 instanceof BlinkTicket && ((BlinkTicket)var1).eh == this.eh;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(this.eh);
    }
}
