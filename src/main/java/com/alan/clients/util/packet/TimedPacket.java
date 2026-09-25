package com.alan.clients.util.packet;

import net.minecraft.network.Packet;

public class TimedPacket {
    private final Packet<?> packet;
    private final long time;

    public TimedPacket(Packet<?> packet, long time) {
        this.packet = packet;
        this.time = time;
    }

    public TimedPacket(Packet<?> packet) {
        this.packet = packet;
        this.time = System.currentTimeMillis();
    }

    public Packet<?> getPacket() {
        return this.packet;
    }

    public long getTime() {
        return this.time;
    }
}
