package com.alan.clients.security;

import com.alan.clients.util.interfaces.InstanceAccess;

public abstract class SecurityFeature implements InstanceAccess {
    public SecurityFeature() {
    }

    public String getReason() {
        return "";
    }

    public abstract boolean run();
}
