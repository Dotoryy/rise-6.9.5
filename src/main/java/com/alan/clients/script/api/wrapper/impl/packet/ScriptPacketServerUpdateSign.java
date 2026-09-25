package com.alan.clients.script.api.wrapper.impl.packet;

import com.alan.clients.script.api.wrapper.impl.ScriptBlockPos;
import net.minecraft.network.play.server.bd;
import net.minecraft.util.IChatComponent;

public class ScriptPacketServerUpdateSign extends ScriptPacket<bd> {
    public ScriptPacketServerUpdateSign(bd var1) {
        super(var1);
    }

    public ScriptBlockPos getPosition() {
        return new ScriptBlockPos(this.wrapped.zM());
    }

    public String getLine(int var1) {
        IChatComponent[] aichatcomponent = this.wrapped.afP();
        return var1 >= 0 && var1 < aichatcomponent.length ? aichatcomponent[var1].getUnformattedText() : "";
    }
}
