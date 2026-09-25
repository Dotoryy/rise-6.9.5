package com.alan.clients.module.impl.render;

import com.alan.clients.module.Module;
import com.alan.clients.module.impl.render.Interface;
import com.alan.clients.value.impl.ModeValue;
import com.alan.clients.value.impl.SubMode;

public class InformationTypeModeValue extends ModeValue {
    public InformationTypeModeValue(Interface var1, String var2, Module module) {
        super(var2, module);
        this.add(new SubMode("Rise"));
        this.add(new SubMode("Traditional"));
        this.setDefault("Rise");
    }
}
