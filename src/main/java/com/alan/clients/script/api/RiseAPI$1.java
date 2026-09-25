package com.alan.clients.script.api;

import com.alan.clients.module.Module;
import com.alan.clients.module.api.ModuleInfo;
import com.alan.clients.script.api.wrapper.impl.ScriptModule;
import java.util.concurrent.atomic.AtomicReference;

public class RiseAPI$1 extends Module {
    final AtomicReference val$scriptModuleReference;

    RiseAPI$1(RiseAPI riseAPI, ModuleInfo var2, AtomicReference atomicReference) {
        super(var2);
        this.val$scriptModuleReference = atomicReference;
    }

    @Override
    public void onEnable() {
        ScriptModule scriptmodule = (ScriptModule)this.val$scriptModuleReference.get();
        if (scriptmodule != null) {
            scriptmodule.call("onEnable");
        }
    }

    @Override
    public void onDisable() {
        ScriptModule scriptmodule = (ScriptModule)this.val$scriptModuleReference.get();
        if (scriptmodule != null) {
            scriptmodule.call("onDisable");
        }
    }
}
