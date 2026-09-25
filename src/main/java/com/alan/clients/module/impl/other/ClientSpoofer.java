package com.alan.clients.module.impl.other;

import com.alan.clients.module.Module;
import com.alan.clients.module.api.Category;
import com.alan.clients.module.api.ModuleInfo;
import com.alan.clients.module.impl.other.clientspoofer.LabyModClientSpoofer;
import com.alan.clients.value.impl.ModeValue;

@ModuleInfo(aliases = "module.other.clientspoofer.name", description = "module.other.clientspoofer.description", category = Category.PLAYER)
public final class ClientSpoofer extends Module {
    private final ModeValue mode = new ModeValue("Mode", this).add(new LabyModClientSpoofer("LabyMod", this)).setDefault("LabyMod");

    public ClientSpoofer() {
    }
}
