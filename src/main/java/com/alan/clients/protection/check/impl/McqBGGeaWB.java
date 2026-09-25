package com.alan.clients.protection.check.impl;

import com.sun.tools.attach.VirtualMachine;
import com.alan.clients.protection.check.ProtectionCheck;
import com.alan.clients.protection.check.api.McqBFVadWB;
import java.util.Arrays;

public final class McqBGGeaWB extends ProtectionCheck {
    private static final String[] HARAM = new String[]{"dump", "packetlog", "logger", "recaf", "jbyte", "bytecode", "decompile", "log"};

    public McqBGGeaWB() {
        super(McqBFVadWB.JOIN, false);
    }

    @Override
    public boolean check() {
        return VirtualMachine.list().stream().anyMatch(var0 -> {
            String s = var0.displayName().toLowerCase().trim();
            return Arrays.stream(HARAM).anyMatch(s::contains);
        });
    }
}
