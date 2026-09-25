package com.alan.clients.script.api.wrapper.impl.packet;

import net.minecraft.network.play.client.u;

public class ScriptPacketEnchantItem extends ScriptPacket<u> {
    public ScriptPacketEnchantItem(u var1) {
        super(var1);
    }

    public int getWindowId() {
        return this.wrapped.getWindowId();
    }

    public int getButton() {
        return this.wrapped.getButton();
    }
}
