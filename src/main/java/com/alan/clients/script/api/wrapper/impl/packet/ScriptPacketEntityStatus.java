package com.alan.clients.script.api.wrapper.impl.packet;

import java.lang.reflect.Field;
import net.minecraft.network.play.server.ab;

public class ScriptPacketEntityStatus extends ScriptPacket<ab> {
    public ScriptPacketEntityStatus(ab var1) {
        super(var1);
    }

    public int getEntityId() {
        try {
            Field field = this.wrapped.getClass().getDeclaredField("entityId");
            field.setAccessible(true);
            return field.getInt(this.wrapped);
        } catch (Exception exception) {
            return -1;
        }
    }

    public byte getOpCode() {
        return this.wrapped.getOpCode();
    }
}
