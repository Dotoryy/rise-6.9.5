package com.alan.clients.util.profiling;

import lombok.Generated;

public class Profiler {
    private long aPl;
    private long aPm;
    private final String aPn;

    public Profiler(String var1) {
        this.rt();
        this.aPn = var1;
    }

    public void rt() {
        this.aPl = System.nanoTime();
    }

    public void vz() {
        this.aPm = this.aPm + (System.nanoTime() - this.aPl);
    }

    public void aX() {
        this.aPm = 0L;
    }

    @Generated
    public long vA() {
        return this.aPm;
    }

    @Generated
    public String getName() {
        return this.aPn;
    }
}
