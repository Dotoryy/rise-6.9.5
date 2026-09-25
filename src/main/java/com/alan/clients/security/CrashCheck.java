package com.alan.clients.security;

import com.alan.clients.util.interfaces.InstanceAccess;
import net.minecraft.network.Packet;

public abstract class CrashCheck implements InstanceAccess {
    private final String axb;
    private final String axc;

    public CrashCheck(String var1, String var2) {
        this.axb = var1;
        this.axc = var2;
    }

    public abstract boolean handle(Packet<?> packet);
}
