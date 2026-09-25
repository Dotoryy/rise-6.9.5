package com.alan.clients.module.impl.movement;

import com.alan.clients.module.Module;
import com.alan.clients.module.api.Category;
import com.alan.clients.module.api.ModuleInfo;
import com.alan.clients.module.impl.movement.teleport.MiniBloxTeleport;
import com.alan.clients.module.impl.movement.teleport.WatchdogBedWarsTeleport;
import com.alan.clients.value.impl.ModeValue;

@ModuleInfo(aliases = "module.movement.teleport.name", description = "module.movement.teleport.description", category = Category.MOVEMENT)
public class Teleport extends Module {
    private final ModeValue mode = new ModeValue("Mode", this)
        .add(new MiniBloxTeleport("MiniBlox", this))
        .add(new WatchdogBedWarsTeleport("Watchdog BedWars", this))
        .setDefault("Vanilla");

    public Teleport() {
    }
}
