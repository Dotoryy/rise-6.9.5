package com.alan.clients.protection.check;

import com.alan.clients.protection.check.api.McqBFVadWB;
import lombok.Generated;

public abstract class ProtectionCheck {
    private final McqBFVadWB trigger;
    private final boolean exemptDev;

    public abstract boolean check() throws java.lang.IllegalAccessException, java.lang.reflect.InvocationTargetException;

    @Generated
    public McqBFVadWB getTrigger() {
        return this.trigger;
    }

    @Generated
    public boolean isExemptDev() {
        return this.exemptDev;
    }

    @Generated
    public ProtectionCheck(McqBFVadWB mcqBFVadWB, boolean exemptDev) {
        this.trigger = mcqBFVadWB;
        this.exemptDev = exemptDev;
    }
}
