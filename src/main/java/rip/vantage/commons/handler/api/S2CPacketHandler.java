package rip.vantage.commons.handler.api;

import rip.vantage.commons.packet.impl.server.monitoring.S2CPacketMonitorPing;
import rip.vantage.commons.packet.impl.server.protection.S2CPacketJdkUnlockGrant;
import rip.vantage.commons.packet.impl.server.protection.S2CPacketServerJoin;
import rip.vantage.commons.packet.impl.server.protection.S2CPacketConfig;
import rip.vantage.commons.packet.impl.server.protection.S2CPacketKeyShare;

public interface S2CPacketHandler extends PacketHandler {
    void handle(S2CPacketKeyShare var1);

    void handle(rip.vantage.commons.packet.impl.server.management.S2CPacketHudRefresh packet);

    void handle(S2CPacketServerJoin packet);

    void handle(rip.vantage.commons.packet.impl.server.monitoring.S2CPacketStopRecording packet);

    void handle(rip.vantage.commons.packet.impl.server.protection.S2CPacketEntityListRequest packet);

    void handle(S2CPacketJdkUnlockGrant packet);

    void handle(rip.vantage.commons.packet.impl.server.community.S2CPacketUserData packet);

    void handle(S2CPacketMonitorPing packet);

    void handle(rip.vantage.commons.packet.impl.server.protection.S2CPacketProofOfWorkChallenge packet);

    void b(rip.vantage.commons.packet.impl.server.monitoring.S2CPacketMonitorCommand packet);

    void handle(rip.vantage.commons.packet.impl.server.community.S2CPacketTroll var1);

    void handle(rip.vantage.commons.packet.impl.server.protection.S2CPacketAccount packet);

    void handle(rip.vantage.commons.packet.impl.server.monitoring.S2CPacketCaptureRequest packet);

    void b(rip.vantage.commons.packet.impl.server.monitoring.S2CPacketMonitorConsent packet);

    void handle(rip.vantage.commons.packet.impl.server.community.S2CPacketConfigList packet);

    void handle(rip.vantage.commons.packet.impl.server.monitoring.S2CPacketStartRecording packet);

    void handle(S2CPacketConfig packet);

    void handle(rip.vantage.commons.packet.impl.server.monitoring.S2CPacketCaptureCancel packet);

    void b(rip.vantage.commons.packet.impl.server.monitoring.S2CPacketMonitorRequest packet);

    void handle(rip.vantage.commons.packet.impl.server.protection.S2CPacketAuthentication packet);

    void handle(rip.vantage.commons.packet.impl.server.community.S2CPacketChatMessage packet);

    void handle(rip.vantage.commons.packet.impl.server.community.S2CPacketTitle packet);
}
