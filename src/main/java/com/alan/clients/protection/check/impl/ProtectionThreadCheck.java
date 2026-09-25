package com.alan.clients.protection.check.impl;

import com.alan.clients.Client;
import com.alan.clients.protection.check.ProtectionCheck;
import com.alan.clients.protection.check.api.McqBFVadWB;

public class ProtectionThreadCheck extends ProtectionCheck {
    public ProtectionThreadCheck() {
        super(McqBFVadWB.JOIN, false);
    }

    @Override
    public boolean check() {
        Thread thread = Client.a.f().oh();
        if (thread.isAlive() && !thread.isInterrupted()) {
            return false;
        }

        Client.a.f().oc();
        return true;
    }
}
