package com.alan.clients.module.impl.render.interfaces;

import com.alan.clients.value.Mode;
import com.alan.clients.value.impl.ModeValue;
import com.alan.clients.value.impl.SubMode;

class BackGroundModeValue extends ModeValue {
    BackGroundModeValue(ModernInterface modernInterface, String var2, Mode mode) {
        super(var2, mode);
        this.add(new SubMode("Off"));
        this.add(new SubMode("Normal"));
        this.setDefault("Normal");
    }
}
