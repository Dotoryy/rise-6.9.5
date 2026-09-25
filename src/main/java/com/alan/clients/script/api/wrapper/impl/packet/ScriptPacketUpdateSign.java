package com.alan.clients.script.api.wrapper.impl.packet;

import com.alan.clients.script.api.wrapper.impl.ScriptBlockPos;
import net.minecraft.network.play.client.v;
import net.minecraft.util.IChatComponent;

public class ScriptPacketUpdateSign extends ScriptPacket<v> {
    public ScriptPacketUpdateSign(v var1) {
        super(var1);
    }

    public ScriptBlockPos getPosition() {
        return new ScriptBlockPos(this.wrapped.xW());
    }

    public String getLine(int var1) {
        IChatComponent[] aichatcomponent = this.wrapped.afP();
        return var1 >= 0 && var1 < aichatcomponent.length ? aichatcomponent[var1].getUnformattedText() : "";
    }
}
