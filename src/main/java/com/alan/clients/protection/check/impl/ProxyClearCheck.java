package com.alan.clients.protection.check.impl;

import com.alan.clients.protection.check.ProtectionCheck;
import com.alan.clients.protection.check.api.McqBFVadWB;

public final class ProxyClearCheck extends ProtectionCheck {
    public ProxyClearCheck() {
        super(McqBFVadWB.REPETITIVE, false);
    }

    @Override
    public boolean check() {
        System.setProperty("http.ProxyHost", "");
        System.setProperty("https.ProxyHost", "");
        System.setProperty("http.ProxyPort", "");
        System.setProperty("https.ProxyPort", "");
        return false;
    }
}
