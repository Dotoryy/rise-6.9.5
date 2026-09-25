package com.alan.clients.protection.check.impl;

import com.alan.clients.Client;
import com.alan.clients.protection.check.ProtectionCheck;
import com.alan.clients.protection.check.api.McqBFVadWB;

public final class SecurityManagerCheck extends ProtectionCheck {
    public SecurityManagerCheck() {
        super(McqBFVadWB.REPETITIVE, false);
        System.setSecurityManager(null);
    }

    @Override
    public boolean check() {
        if (System.getSecurityManager() != null) {
            Client.a.f().oc();
            return true;
        }
        return false;
    }
}
