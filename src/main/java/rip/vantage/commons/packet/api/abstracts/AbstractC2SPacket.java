package rip.vantage.commons.packet.api.abstracts;

public abstract class AbstractC2SPacket implements rip.vantage.commons.packet.api.interfaces.C2SPacket {
    private final byte id;

    public AbstractC2SPacket(byte var1) {
        this.id = var1;
    }

    public byte getId() {
        return this.id;
    }
}
