package com.alan.clients.util.packet;

import com.alan.clients.newevent.impl.packet.PacketReceiveEvent;
import com.alan.clients.script.api.NetworkAPI;
import com.alan.clients.util.interfaces.InstanceAccess;
import java.util.Arrays;
import lombok.Generated;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.Packet;

public final class PacketUtil implements InstanceAccess {
    private static NetHandlerPlayClient uK() {
        return aEg != null ? aEg.getNetHandler() : null;
    }

    public static void send(Packet<?> packet) {
        NetHandlerPlayClient nethandlerplayclient = uK();
        if (nethandlerplayclient != null && packet != null) {
            nethandlerplayclient.addToSendQueue(packet);
        }
    }

    public static void sendNoEvent(Packet<?> packet) {
        NetHandlerPlayClient nethandlerplayclient = uK();
        if (nethandlerplayclient != null && packet != null) {
            nethandlerplayclient.u(packet);
        }
    }

    public static void queue(Packet<?> packet) {
        if (packet == null) {
            System.out.println("Packet is null");
        } else {
            NetHandlerPlayClient nethandlerplayclient = uK();
            if (nethandlerplayclient != null) {
                if (isClientPacket(packet)) {
                    nethandlerplayclient.addToSendQueue(packet);
                } else {
                    nethandlerplayclient.v(packet);
                }
            }
        }
    }

    public static void queueNoEvent(Packet<?> packet) {
        NetHandlerPlayClient nethandlerplayclient = uK();
        if (nethandlerplayclient != null && packet != null) {
            if (isClientPacket(packet)) {
                nethandlerplayclient.u(packet);
            } else {
                nethandlerplayclient.w(packet);
            }
        }
    }

    public static void receive(Packet<?> packet) {
        NetHandlerPlayClient nethandlerplayclient = uK();
        if (nethandlerplayclient != null && packet != null) {
            nethandlerplayclient.v(packet);
        }
    }

    public static void receiveNoEvent(Packet<?> packet) {
        NetHandlerPlayClient nethandlerplayclient = uK();
        if (nethandlerplayclient != null && packet != null) {
            nethandlerplayclient.w(packet);
        }
    }

    public static boolean isServerPacket(Packet<?> packet) {
        return !isClientPacket(packet);
    }

    public static boolean isClientPacket(Packet<?> packet) {
        return Arrays.stream(NetworkAPI.serverbound).anyMatch(var1 -> var1 == packet.getClass());
    }

    public static void j(PacketReceiveEvent event) {
    }

    @Generated
    private PacketUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
