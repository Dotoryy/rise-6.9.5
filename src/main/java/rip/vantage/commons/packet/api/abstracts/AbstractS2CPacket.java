package rip.vantage.commons.packet.api.abstracts;

import rip.vantage.commons.packet.api.interfaces.S2CPacket;

public abstract class AbstractS2CPacket implements S2CPacket {
    private final byte id;

    public AbstractS2CPacket(byte var1) {
        this.id = var1;
    }

    public byte getId() {
        return this.id;
    }
}
