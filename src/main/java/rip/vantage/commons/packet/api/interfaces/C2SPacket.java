package rip.vantage.commons.packet.api.interfaces;

public interface C2SPacket extends VantagePacket<rip.vantage.commons.handler.api.C2SPacketHandler> {
    void handle(rip.vantage.commons.handler.api.C2SPacketHandler handler);
}
