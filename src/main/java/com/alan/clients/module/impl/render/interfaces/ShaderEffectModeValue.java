package com.alan.clients.module.impl.render.interfaces;

import com.alan.clients.value.Mode;
import com.alan.clients.value.impl.ModeValue;
import com.alan.clients.value.impl.SubMode;

class ShaderEffectModeValue extends ModeValue {
    ShaderEffectModeValue(ModernInterface modernInterface, String var2, Mode mode) {
        super(var2, mode);
        this.add(new SubMode("Glow"));
        this.add(new SubMode("Shadow"));
        this.setDefault("Shadow");
    }
}
