package com.alan.clients.script.api.wrapper.impl.packet;

import net.minecraft.network.play.server.S3BPacketScoreboardObjective;

public class ScriptPacketScoreboardObjective extends ScriptPacket<S3BPacketScoreboardObjective> {
    public ScriptPacketScoreboardObjective(S3BPacketScoreboardObjective packet) {
        super(packet);
    }

    public String getObjectiveName() {
        return this.wrapped.func_149339_c();
    }

    public String getObjectiveValue() {
        return this.wrapped.func_149337_d();
    }

    public int getAction() {
        return this.wrapped.func_149338_e();
    }
}
