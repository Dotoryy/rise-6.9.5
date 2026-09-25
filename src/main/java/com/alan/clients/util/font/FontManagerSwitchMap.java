package com.alan.clients.util.font;

import com.alan.clients.util.font.FontWeight;

class FontManagerSwitchMap {
    static final int[] kC = new int[FontWeight.values().length];

    static {
        try {
            kC[FontWeight.LIGHT.ordinal()] = 1;
        } catch (NoSuchFieldError nosuchfielderror4) {
        }

        try {
            kC[FontWeight.MEDIUM.ordinal()] = 2;
        } catch (NoSuchFieldError nosuchfielderror3) {
        }

        try {
            kC[FontWeight.BOLD.ordinal()] = 3;
        } catch (NoSuchFieldError nosuchfielderror2) {
        }

        try {
            kC[FontWeight.REGULAR.ordinal()] = 4;
        } catch (NoSuchFieldError nosuchfielderror1) {
        }

        try {
            kC[FontWeight.NONE.ordinal()] = 5;
        } catch (NoSuchFieldError nosuchfielderror) {
        }
    }
}
