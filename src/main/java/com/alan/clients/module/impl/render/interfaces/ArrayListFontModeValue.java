package com.alan.clients.module.impl.render.interfaces;

import com.alan.clients.value.Mode;
import com.alan.clients.value.impl.ModeValue;
import com.alan.clients.value.impl.SubMode;

class ArrayListFontModeValue extends ModeValue {
    ArrayListFontModeValue(ModernInterface modernInterface, String var2, Mode mode) {
        super(var2, mode);
        this.add(new SubMode("Apple UI"));
        this.add(new SubMode("Minecraft"));
        this.add(new SubMode("Custom"));
        this.setDefault("Apple UI");
    }
}
