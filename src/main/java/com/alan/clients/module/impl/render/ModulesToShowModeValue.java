package com.alan.clients.module.impl.render;

import com.alan.clients.module.Module;
import com.alan.clients.module.impl.render.Interface;
import com.alan.clients.value.impl.ModeValue;
import com.alan.clients.value.impl.SubMode;

public class ModulesToShowModeValue extends ModeValue {
    public ModulesToShowModeValue(Interface var1, String var2, Module module) {
        super(var2, module);
        this.add(new SubMode("All"));
        this.add(new SubMode("Exclude render"));
        this.add(new SubMode("Only bound"));
        this.setDefault("Exclude render");
    }
}
