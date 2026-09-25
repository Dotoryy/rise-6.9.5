package com.alan.clients.script.api.wrapper.impl.packet;

import java.lang.reflect.Field;
import net.minecraft.network.play.client.C18PacketSpectate;

public class ScriptPacketSpectate extends ScriptPacket<C18PacketSpectate> {
    public ScriptPacketSpectate(C18PacketSpectate packet) {
        super(packet);
    }

    public String getEntityUUID() {
        try {
            Field field = this.vanillaField("id");
            field.setAccessible(true);
            Object object = field.get(this.wrapped);
            return object != null ? object.toString() : "";
        } catch (Exception exception) {
            return "";
        }
    }
}
