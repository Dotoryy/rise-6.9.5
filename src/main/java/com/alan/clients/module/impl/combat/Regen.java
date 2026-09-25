package com.alan.clients.module.impl.combat;

import com.alan.clients.module.Module;
import com.alan.clients.module.api.Category;
import com.alan.clients.module.api.ModuleInfo;
import com.alan.clients.module.impl.combat.regen.VanillaRegen;
import com.alan.clients.module.impl.combat.regen.VerusRegenRegen;
import com.alan.clients.module.impl.combat.regen.WorldGuardRegen;
import com.alan.clients.value.impl.ModeValue;

@ModuleInfo(aliases = "module.combat.regen.name", description = "module.combat.regen.description", category = Category.COMBAT)
public final class Regen extends Module {
    private final ModeValue mode = new ModeValue("Mode", this)
        .add(new VanillaRegen("Vanilla", this))
        .add(new VerusRegenRegen("Verus Regen", this))
        .add(new WorldGuardRegen("World Guard", this))
        .setDefault("Vanilla");

    public Regen() {
    }
}
