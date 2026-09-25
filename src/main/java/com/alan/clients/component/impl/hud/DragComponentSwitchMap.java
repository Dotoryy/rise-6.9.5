package com.alan.clients.component.impl.hud;

import com.alan.clients.component.impl.hud.SnapAxis;

public class DragComponentSwitchMap {
    public static final int[] cc = new int[SnapAxis.values().length];

    static {
        try {
            cc[SnapAxis.VERTICAL.ordinal()] = 1;
        } catch (NoSuchFieldError nosuchfielderror1) {
        }

        try {
            cc[SnapAxis.HORIZONTAL.ordinal()] = 2;
        } catch (NoSuchFieldError nosuchfielderror) {
        }
    }
}
