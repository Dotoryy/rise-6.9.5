package com.alan.clients.module.impl.movement;

import com.alan.clients.module.Module;
import com.alan.clients.module.api.Category;
import com.alan.clients.module.api.ModuleInfo;
import com.alan.clients.module.impl.movement.terrainspeed.BloxdTerrainSpeed;
import com.alan.clients.module.impl.movement.terrainspeed.WatchdogTerrainSpeed;
import com.alan.clients.value.impl.ModeValue;

@ModuleInfo(aliases = "module.movement.terrainspeed.name", description = "module.movement.terrainspeed.description", category = Category.MOVEMENT)
public class TerrainSpeed extends Module {
    public final ModeValue mode = new ModeValue("Mode", this)
        .add(new WatchdogTerrainSpeed("Watchdog", this))
        .add(new BloxdTerrainSpeed("Bloxd", this))
        .setDefault("Watchdog");

    public TerrainSpeed() {
    }
}
