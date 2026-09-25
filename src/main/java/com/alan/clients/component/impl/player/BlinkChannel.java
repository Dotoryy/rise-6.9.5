package com.alan.clients.component.impl.player;

import com.alan.clients.component.impl.player.BlinkComponent;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import net.minecraft.network.Packet;
import net.minecraft.util.Tuple;

public final class BlinkChannel {
    private final String ej;
    public BlinkTicket ek;
    long dQ;
    public long el;
    private boolean em;
    private boolean af;
    private boolean en;
    private boolean eo;
    private boolean ep;
    private boolean eq;
    private boolean er;
    private final Deque<Integer> es = new ArrayDeque<>();
    private int et;

    public BlinkChannel(String var1) {
        this.ej = var1;
    }

    public void a(long var1, boolean var3, boolean var4, boolean var5, boolean var6, boolean var7, boolean var8, boolean var9) {
        this.dQ = var1;
        this.em = var3;
        this.af = var4;
        this.en = var5;
        this.eo = var6;
        this.ep = var7;
        this.eq = var8;
        this.er = var9;
        this.el = System.currentTimeMillis();
        this.bo();
    }

    public void bo() {
        if (this.ek == null) {
            this.ek = new BlinkTicket(this);
        }

        this.el = System.currentTimeMillis();
    }

    public BlinkTicket bp() {
        BlinkTicket bn = this.ek;
        if (bn != null) {
            bn.dj = false;
            this.ek = null;
        }

        this.bs();
        return bn;
    }

    public boolean d(Packet<?> packet) {
        return a(BlinkComponent.dV, this.em, packet)
            || a(BlinkComponent.dW, this.af, packet)
            || a(BlinkComponent.dX, this.en, packet)
            || a(BlinkComponent.dY, this.eo, packet)
            || a(BlinkComponent.dZ, this.ep, packet)
            || a(BlinkComponent.ea, this.eq, packet)
            || a(BlinkComponent.eb, this.er, packet);
    }

    public boolean bd() {
        return this.ek != null && this.ek.dj;
    }

    public void bq() {
        this.et++;
    }

    public void br() {
        if (this.et > 0) {
            this.es.add(this.et);
            this.et = 0;
        }
    }

    public int g(int var1) {
        int i = 0;

        for (int j = 0; j < var1; j++) {
            Integer integer = this.es.poll();
            if (integer == null) {
                break;
            }

            i += integer;
        }

        return i;
    }

    private void bs() {
        this.es.clear();
        this.et = 0;
    }

    private static boolean a(Tuple<Class[], Boolean> var0, boolean var1, Packet<?> packet) {
        return var1 && Arrays.<Class>stream((Class[])var0.getFirst()).anyMatch(var1x -> var1x == packet.getClass());
    }
}
