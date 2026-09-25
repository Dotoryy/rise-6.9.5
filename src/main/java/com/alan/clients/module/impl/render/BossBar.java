package com.alan.clients.module.impl.render;

import com.alan.clients.module.Module;
import com.alan.clients.module.api.Category;
import com.alan.clients.module.api.ModuleInfo;

@ModuleInfo(
    aliases = "module.render.bossbar.name",
    description = "module.render.bossbar.description",
    category = Category.RENDER,
    autoEnabled = true,
    allowDisable = true
)
public final class BossBar extends Module {
    public BossBar() {
    }
}
