package com.alan.clients.anticheat.util;

import lombok.Generated;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S14PacketEntity.S16PacketEntityLook;
import net.minecraft.network.play.server.S14PacketEntity.S17PacketEntityLookMove;
import net.minecraft.network.play.server.S14PacketEntity;

public final class PacketUtil {
    public static boolean isRelMove(Packet<?> packet) {
        return packet instanceof S14PacketEntity;
    }

    public static boolean isLook(Packet<?> packet) {
        return packet instanceof S17PacketEntityLookMove || packet instanceof S16PacketEntityLook;
    }

    @Generated
    private PacketUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
