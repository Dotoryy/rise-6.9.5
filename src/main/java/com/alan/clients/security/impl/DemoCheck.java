package com.alan.clients.security.impl;

import com.alan.clients.security.CrashCheck;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S2BPacketChangeGameState;

public final class DemoCheck extends CrashCheck {
    public DemoCheck() {
        super("Demo Check", "Server attempted to prevent gameplay with a demo screen");
    }

    @Override
    public boolean handle(Packet<?> packet) {
        return !(packet instanceof S2BPacketChangeGameState s2bpacketchangegamestate)
            ? false
            : s2bpacketchangegamestate.getGameState() == 5 && s2bpacketchangegamestate.func_149137_d() == 0.0F;
    }
}
