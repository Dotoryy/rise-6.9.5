package com.alan.clients.script.api.wrapper.impl.packet;

import net.minecraft.network.play.server.ag;

public class ScriptPacketSetExperience extends ScriptPacket<ag> {
    public ScriptPacketSetExperience(ag var1) {
        super(var1);
    }

    public float getExperienceBar() {
        return this.wrapped.agL();
    }

    public int getLevel() {
        return this.wrapped.getLevel();
    }

    public int getTotalExperience() {
        return this.wrapped.getTotalExperience();
    }
}
