package com.alan.clients.script.api.wrapper.impl.packet;

import com.alan.clients.script.api.wrapper.impl.ScriptBlockPos;
import net.minecraft.network.play.server.ap;

public class ScriptPacketBlockBreakAnim extends ScriptPacket<ap> {
    public ScriptPacketBlockBreakAnim(ap var1) {
        super(var1);
    }

    public int getBreakerId() {
        return this.wrapped.getBreakerId();
    }

    public ScriptBlockPos getPosition() {
        return new ScriptBlockPos(this.wrapped.xW());
    }

    public int getProgress() {
        return this.wrapped.getProgress();
    }
}
