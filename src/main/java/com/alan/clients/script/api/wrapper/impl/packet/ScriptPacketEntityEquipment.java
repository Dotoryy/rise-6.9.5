package com.alan.clients.script.api.wrapper.impl.packet;

import net.minecraft.network.play.server.e;

public class ScriptPacketEntityEquipment extends ScriptPacket<e> {
    public ScriptPacketEntityEquipment(e var1) {
        super(var1);
    }

    public int getEntityId() {
        return this.wrapped.getEntityID();
    }

    public int getEquipmentSlot() {
        return this.wrapped.getEquipmentSlot();
    }
}
