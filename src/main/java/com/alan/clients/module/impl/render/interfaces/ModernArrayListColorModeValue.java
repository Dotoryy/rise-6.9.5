package com.alan.clients.module.impl.render.interfaces;

import com.alan.clients.value.Mode;
import com.alan.clients.value.impl.ModeValue;
import com.alan.clients.value.impl.SubMode;

class ModernArrayListColorModeValue extends ModeValue {
    ModernArrayListColorModeValue(ModernInterface modernInterface, String var2, Mode mode) {
        super(var2, mode);
        this.add(new SubMode("Static"));
        this.add(new SubMode("Fade"));
        this.add(new SubMode("Breathe"));
        this.setDefault("Fade");
    }
}
