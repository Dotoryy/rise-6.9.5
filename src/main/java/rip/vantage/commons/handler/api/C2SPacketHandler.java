package rip.vantage.commons.handler.api;

import rip.vantage.commons.packet.impl.client.community.C2SPacketTelemetry;
import rip.vantage.commons.packet.impl.client.community.C2SPacketStoreDelivery;
import rip.vantage.commons.packet.impl.client.community.C2SPacketServerState;
import rip.vantage.commons.packet.impl.client.community.C2SPacketUserLookup;
import rip.vantage.commons.packet.impl.client.community.C2SPacketTitle;
import rip.vantage.commons.packet.impl.client.protection.C2SPacketDetectionReport;
import rip.vantage.commons.packet.impl.client.protection.C2SPacketJdkUnlockRequest;

public interface C2SPacketHandler extends PacketHandler {
    void handle(C2SPacketServerState var1);

    void handle(C2SPacketTelemetry packet);

    void handle(rip.vantage.commons.packet.impl.client.protection.C2SPacketConfirmServer var1);

    void handle(C2SPacketJdkUnlockRequest packet);

    void handle(rip.vantage.commons.packet.impl.client.community.C2SPacketConfigRequest packet);

    void handle(rip.vantage.commons.packet.impl.client.general.C2SPacketKeepAlive var1);

    void handle(C2SPacketDetectionReport packet);

    void handle(rip.vantage.commons.packet.impl.client.community.C2SPacketChatMessage packet);

    void handle(rip.vantage.commons.packet.impl.client.protection.C2SPacketEntityList packet);

    void handle(rip.vantage.commons.packet.impl.client.community.C2SPacketConfigListRequest packet);

    void handle(C2SPacketStoreDelivery packet);

    void handle(rip.vantage.commons.packet.impl.client.protection.C2SPacketAuthentication packet);

    void handle(C2SPacketTitle packet);

    void handle(rip.vantage.commons.packet.impl.client.protection.C2SPacketAccount packet);

    void handle(C2SPacketUserLookup packet);

    void handle(rip.vantage.commons.packet.impl.client.protection.C2SPacketConfig packet);
}
