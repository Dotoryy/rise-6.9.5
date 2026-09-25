package com.alan.clients.script.api.wrapper.impl.packet;

import java.lang.reflect.Field;
import net.minecraft.network.play.client.l;

public class ScriptPacketHeldItem extends ScriptPacket<l> {
    public ScriptPacketHeldItem(l var1) {
        super(var1);
    }

    public int getSlotId() {
        return this.wrapped.getSlotId();
    }

    public void setSlotId(int slotId) {
        try {
            Field field = this.wrapped.getClass().getDeclaredField("slotId");
            field.setAccessible(true);
            field.setInt(this.wrapped, slotId);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
