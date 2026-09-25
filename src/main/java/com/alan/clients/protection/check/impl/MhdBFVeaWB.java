package com.alan.clients.protection.check.impl;

import com.alan.clients.Client;
import com.alan.clients.protection.check.ProtectionCheck;
import com.alan.clients.protection.check.api.McqBFVadWB;
import com.alan.clients.util.OSUtil;
import com.alan.clients.util.OS;
import java.io.File;
import java.nio.file.Files;
import java.util.Iterator;
import java.util.Locale;

public final class MhdBFVeaWB extends ProtectionCheck {
    private final File hostsFile = new File(OSUtil.rV() == OS.WINDOWS ? System.getenv("WinDir") + "\\System32\\drivers\\etc\\hosts" : "/etc/hosts");

    public MhdBFVeaWB() {
        super(McqBFVadWB.INITIALIZE, false);
    }

    @Override
    public boolean check() {
        new Thread(() -> {
            try {
                while (true) {
                    if (!this.hostsFile.exists() || !this.hostsFile.canRead() || !this.hostsFile.isFile()) {
                        Client.a.f().oc();
                    }

                    Iterator iterator = Files.readAllLines(this.hostsFile.toPath()).iterator();

                    while (iterator.hasNext()) {
                        String s = ((String)iterator.next()).toLowerCase(Locale.ENGLISH).trim();
                        if (s.contains("riseclient.com") || s.contains("vantage")) {
                            Client.a.f().oc();
                        }
                    }

                    Thread.sleep(5000L);
                }
            } catch (Throwable throwable) {
                Client.a.f().oc();
            }
        }).start();
        return false;
    }
}
