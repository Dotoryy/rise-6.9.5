package com.alan.clients.module.impl.render;

import com.alan.clients.module.Module;
import com.alan.clients.module.impl.render.Interface;
import com.alan.clients.value.impl.ModeValue;
import com.alan.clients.module.impl.render.interfaces.CreidaInterface;
import com.alan.clients.module.impl.render.interfaces.ClassicInterface;
import com.alan.clients.module.impl.render.interfaces.ModernInterface;
import com.alan.clients.module.impl.render.interfaces.WurstInterface;

public class InterfaceModeValue extends ModeValue {
    public InterfaceModeValue(Interface var1, String var2, Module module) {
        super(var2, module);
        this.add(new ModernInterface("Modern", (Interface)this.wq()));
        this.add(new WurstInterface("Wurst", (Interface)this.wq()));
        this.add(new ClassicInterface("Classic", (Interface)this.wq()));
        this.add(new CreidaInterface("Creida", (Interface)this.wq()));
        this.setDefault("Modern");
    }
}
