package com.alan.clients.script.api.wrapper.impl.packet;

import java.lang.reflect.Field;
import net.minecraft.network.play.client.C19PacketResourcePackStatus;

public class ScriptPacketResourcePackStatus extends ScriptPacket<C19PacketResourcePackStatus> {
    public ScriptPacketResourcePackStatus(C19PacketResourcePackStatus packet) {
        super(packet);
    }

    public String getHash() {
        try {
            Field field = this.vanillaField("hash");
            field.setAccessible(true);
            return (String)field.get(this.wrapped);
        } catch (Exception exception) {
            return "";
        }
    }

    public String getStatus() {
        try {
            Field field = this.vanillaField("status");
            field.setAccessible(true);
            Object object = field.get(this.wrapped);
            return object != null ? object.toString() : "";
        } catch (Exception exception) {
            return "";
        }
    }
}
