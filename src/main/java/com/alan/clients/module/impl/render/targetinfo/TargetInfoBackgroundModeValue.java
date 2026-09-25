package com.alan.clients.module.impl.render.targetinfo;

import com.alan.clients.module.impl.render.targetinfo.ModernTargetInfo;
import com.alan.clients.value.Mode;
import com.alan.clients.value.impl.ModeValue;
import com.alan.clients.value.impl.SubMode;

public class TargetInfoBackgroundModeValue extends ModeValue {
    public TargetInfoBackgroundModeValue(ModernTargetInfo modernTargetInfo, String var2, Mode mode) {
        super(var2, mode);
        this.add(new SubMode("Glass"));
        this.add(new SubMode("Tint"));
        this.add(new SubMode("Solid"));
        this.setDefault("Glass");
    }
}
