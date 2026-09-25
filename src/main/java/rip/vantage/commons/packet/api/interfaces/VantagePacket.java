package rip.vantage.commons.packet.api.interfaces;

public interface VantagePacket<T extends rip.vantage.commons.handler.api.PacketHandler> {
    void handle(T var1);

    String aJk();
}
