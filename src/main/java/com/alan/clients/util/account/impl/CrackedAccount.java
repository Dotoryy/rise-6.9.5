package com.alan.clients.util.account.impl;

import com.alan.clients.util.account.AltAccount;
import com.alan.clients.util.account.AltType;
import java.util.UUID;

public class CrackedAccount extends AltAccount {
    public CrackedAccount(String var1) {
        super(AltType.CRACKED, var1, bl(var1), "accessToken");
    }

    private static String bl(String var0) {
        return UUID.nameUUIDFromBytes(("OfflinePlayer:" + var0).getBytes()).toString().replace("-", "");
    }
}
