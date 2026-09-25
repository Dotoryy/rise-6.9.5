package com.alan.clients.script.api.wrapper.impl.packet;

import net.minecraft.network.play.server.S2BPacketChangeGameState;

public class ScriptPacketChangeGameState extends ScriptPacket<S2BPacketChangeGameState> {
    public ScriptPacketChangeGameState(S2BPacketChangeGameState packet) {
        super(packet);
    }

    public int getGameState() {
        return this.wrapped.getGameState();
    }

    public float getValue() {
        return this.wrapped.func_149137_d();
    }
}
