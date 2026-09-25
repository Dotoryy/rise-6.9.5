package com.alan.clients.script.api.wrapper.impl.packet;

import net.minecraft.network.play.server.aa;

public class ScriptPacketEntityHeadLook extends ScriptPacket<aa> {
    public ScriptPacketEntityHeadLook(aa var1) {
        super(var1);
    }

    public int getEntityId() {
        return this.wrapped.l(MC.theWorld) != null ? this.wrapped.l(MC.theWorld).getEntityId() : -1;
    }

    public float getYaw() {
        return this.wrapped.agi() * 360.0F / 256.0F;
    }
}
