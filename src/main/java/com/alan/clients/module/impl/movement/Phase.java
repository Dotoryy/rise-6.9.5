package com.alan.clients.module.impl.movement;

import com.alan.clients.module.Module;
import com.alan.clients.module.api.Category;
import com.alan.clients.module.api.ModuleInfo;
import com.alan.clients.module.impl.movement.phase.BlocksMCPhase;
import com.alan.clients.module.impl.movement.phase.GrimPhase;
import com.alan.clients.module.impl.movement.phase.NormalPhase;
import com.alan.clients.module.impl.movement.phase.VulcanPhase;
import com.alan.clients.module.impl.movement.phase.Watchdog1171FullBlockPhase;
import com.alan.clients.module.impl.movement.phase.WatchdogBlinkPhase;
import com.alan.clients.module.impl.movement.phase.WatchdogPredictionFullBlockPhase;
import com.alan.clients.module.impl.movement.phase.WatchdogSemiBlockPhase;
import com.alan.clients.value.impl.ModeValue;

@ModuleInfo(aliases = "module.movement.phase.name", description = "module.movement.phase.description", category = Category.MOVEMENT)
public class Phase extends Module {
    private final ModeValue mode = new ModeValue("Mode", this)
        .add(new NormalPhase("Normal", this))
        .add(new WatchdogSemiBlockPhase("Watchdog Semi Block", this))
        .add(new WatchdogPredictionFullBlockPhase("Watchdog Prediction Full Block", this))
        .add(new Watchdog1171FullBlockPhase("Watchdog 1.17.1 Full Block", this))
        .add(new WatchdogBlinkPhase("Watchdog Blink", this))
        .add(new BlocksMCPhase("BlocksMC", this))
        .add(new VulcanPhase("Vulcan", this))
        .add(new GrimPhase("Grim", this))
        .setDefault("Normal");

    public Phase() {
    }
}
