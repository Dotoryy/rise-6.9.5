package com.alan.clients.module.impl.render.interfaces;

import com.alan.clients.value.Mode;
import com.alan.clients.value.impl.ModeValue;
import com.alan.clients.value.impl.SubMode;

class ArrayListAlignmentModeValue extends ModeValue {
    ArrayListAlignmentModeValue(ClassicInterface var1, String var2, Mode mode) {
        super(var2, mode);
        this.add(new SubMode("Right"));
        this.add(new SubMode("Left"));
        this.setDefault("Right");
    }
}
