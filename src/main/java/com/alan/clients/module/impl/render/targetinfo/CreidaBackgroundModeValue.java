package com.alan.clients.module.impl.render.targetinfo;

import com.alan.clients.module.impl.render.targetinfo.CreidaModernTargetInfo;
import com.alan.clients.value.Mode;
import com.alan.clients.value.impl.ModeValue;
import com.alan.clients.value.impl.SubMode;

public class CreidaBackgroundModeValue extends ModeValue {
    public CreidaBackgroundModeValue(CreidaModernTargetInfo creidaModernTargetInfo, String var2, Mode mode) {
        super(var2, mode);
        this.add(new SubMode("Glass"));
        this.add(new SubMode("Tint"));
        this.add(new SubMode("Solid"));
        this.setDefault("Glass");
    }
}
