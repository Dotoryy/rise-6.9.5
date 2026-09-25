package com.alan.clients.component.impl.hud;

public enum SnapAxis {
    VERTICAL,
    HORIZONTAL;

    private static final SnapAxis[] $VALUES = aU();

    SnapAxis() {
    }

    private static SnapAxis[] aU() {
        return new SnapAxis[]{VERTICAL, HORIZONTAL};
    }
}
