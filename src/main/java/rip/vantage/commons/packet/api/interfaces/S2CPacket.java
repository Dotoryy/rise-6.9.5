package rip.vantage.commons.packet.api.interfaces;

public interface S2CPacket extends VantagePacket<rip.vantage.commons.handler.api.S2CPacketHandler> {
    void handle(rip.vantage.commons.handler.api.S2CPacketHandler handler);
}
