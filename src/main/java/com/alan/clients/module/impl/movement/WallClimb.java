package com.alan.clients.module.impl.movement;

import com.alan.clients.module.Module;
import com.alan.clients.module.api.Category;
import com.alan.clients.module.api.ModuleInfo;
import com.alan.clients.module.impl.movement.wallclimb.GrimWallClimb;
import com.alan.clients.module.impl.movement.wallclimb.MineMenClubWallClimb;
import com.alan.clients.module.impl.movement.wallclimb.VerusWallClimb;
import com.alan.clients.module.impl.movement.wallclimb.VulcanWallClimb;
import com.alan.clients.module.impl.movement.wallclimb.WatchdogWallClimb;
import com.alan.clients.value.impl.ModeValue;

@ModuleInfo(aliases = {"module.movement.wallclimb.name", "Spider"}, description = "module.movement.wallclimb.description", category = Category.MOVEMENT)
public class WallClimb extends Module {
    private final ModeValue mode = new ModeValue("Mode", this)
        .add(new VulcanWallClimb("Vulcan", this))
        .add(new VerusWallClimb("Verus", this))
        .add(new GrimWallClimb("Grim", this))
        .add(new WatchdogWallClimb("Watchdog", this))
        .add(new MineMenClubWallClimb("MineMenClub", this))
        .setDefault("Vulcan");

    public WallClimb() {
    }
}
