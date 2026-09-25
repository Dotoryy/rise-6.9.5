package com.alan.clients.security.impl;

import com.alan.clients.security.CrashCheck;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;

public final class TeleportCheck extends CrashCheck {
    public TeleportCheck() {
        super("Large Teleport", "Detects ridiculous teleports");
    }

    @Override
    public boolean handle(Packet<?> packet) {
        return !(packet instanceof S08PacketPlayerPosLook s08packetplayerposlook)
            ? false
            : Math.abs(s08packetplayerposlook.x) > 1.0E9 || Math.abs(s08packetplayerposlook.y) > 1.0E9 || Math.abs(s08packetplayerposlook.z) > 1.0E9;
    }
}
