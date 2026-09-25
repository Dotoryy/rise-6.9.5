package com.alan.clients.script.api.wrapper.impl.packet;

import net.minecraft.network.play.server.bq;

public class ScriptPacketDisplayScoreboard extends ScriptPacket<bq> {
    public ScriptPacketDisplayScoreboard(bq var1) {
        super(var1);
    }

    public int getPosition() {
        return this.wrapped.func_149371_c();
    }

    public String getScoreName() {
        return this.wrapped.func_149370_d();
    }

    public void setPosition(int position) {
        this.wrapped.setPosition(position);
    }

    public void setScoreName(String scoreName) {
        this.wrapped.setScoreName(scoreName);
    }
}
