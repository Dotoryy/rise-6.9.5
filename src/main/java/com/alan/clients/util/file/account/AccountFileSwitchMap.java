package com.alan.clients.util.file.account;

import com.alan.clients.util.account.AltType;

class AccountFileSwitchMap {
    static final int[] aHG = new int[AltType.values().length];

    static {
        try {
            aHG[AltType.CRACKED.ordinal()] = 1;
        } catch (NoSuchFieldError nosuchfielderror2) {
        }

        try {
            aHG[AltType.MICROSOFT.ordinal()] = 2;
        } catch (NoSuchFieldError nosuchfielderror1) {
        }

        try {
            aHG[AltType.RAVE.ordinal()] = 3;
        } catch (NoSuchFieldError nosuchfielderror) {
        }
    }
}
