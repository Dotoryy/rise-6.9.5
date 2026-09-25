package com.alan.clients.script.api.wrapper.impl.packet;

import java.lang.reflect.Field;
import net.minecraft.network.play.server.S34PacketMaps;

public class ScriptPacketMaps extends ScriptPacket<S34PacketMaps> {
    public ScriptPacketMaps(S34PacketMaps packet) {
        super(packet);
    }

    public int getMapId() {
        return this.wrapped.getMapId();
    }

    public byte getMapScale() {
        try {
            Field field = this.vanillaField("mapScale");
            field.setAccessible(true);
            return field.getByte(this.wrapped);
        } catch (Exception exception) {
            return 0;
        }
    }
}
