package com.alan.clients.script.api.wrapper.impl.packet;

import com.alan.clients.script.api.wrapper.impl.ScriptBlockPos;
import net.minecraft.network.play.server.S28PacketEffect;

public class ScriptPacketEffect extends ScriptPacket<S28PacketEffect> {
    public ScriptPacketEffect(S28PacketEffect packet) {
        super(packet);
    }

    public int getSoundType() {
        return this.wrapped.getSoundType();
    }

    public ScriptBlockPos getPosition() {
        return new ScriptBlockPos(this.wrapped.getSoundPos());
    }

    public int getSoundData() {
        return this.wrapped.getSoundData();
    }

    public boolean isSoundServerwide() {
        return this.wrapped.isSoundServerwide();
    }
}
