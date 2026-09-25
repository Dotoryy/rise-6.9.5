package com.alan.clients.util.web;

import rip.vantage.commons.packet.impl.client.community.C2SPacketTelemetry;
import rip.vantage.network.core.VantageNetwork;

public final class CommunityChat {
    private CommunityChat() {
    }

    public static void A(String var0, String var1) {
        try {
            VantageNetwork aInstance = VantageNetwork.aKB();
            if (aInstance.aKK() == null || aInstance.bX() == null || aInstance.bX().trim().isEmpty()) {
                return;
            }

            aInstance.aKK().sendMessage(new C2SPacketTelemetry(aInstance.bX(), var0, var1).aJk());
        } catch (Exception exception) {
        }
    }
}
