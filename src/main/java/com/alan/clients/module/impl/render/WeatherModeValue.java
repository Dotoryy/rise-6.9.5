package com.alan.clients.module.impl.render;

import com.alan.clients.module.Module;
import com.alan.clients.module.impl.render.Ambience;
import com.alan.clients.value.impl.ModeValue;
import com.alan.clients.value.impl.SubMode;

public class WeatherModeValue extends ModeValue {
    public WeatherModeValue(Ambience ambience, String var2, Module module) {
        super(var2, module);
        this.add(new SubMode("Unchanged"));
        this.add(new SubMode("Clear"));
        this.add(new SubMode("Rain"));
        this.add(new SubMode("Heavy Snow"));
        this.add(new SubMode("Light Snow"));
        this.add(new SubMode("Nether Particles"));
        this.setDefault("Unchanged");
    }
}
