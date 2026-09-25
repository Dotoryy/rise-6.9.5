package com.alan.clients.module.impl.movement;

import com.alan.clients.module.Module;
import com.alan.clients.module.api.Category;
import com.alan.clients.module.api.ModuleInfo;
import com.alan.clients.module.impl.movement.step.JumpStep;
import com.alan.clients.module.impl.movement.step.MatrixStep;
import com.alan.clients.module.impl.movement.step.NCPPacketlessStep;
import com.alan.clients.module.impl.movement.step.NCPStep;
import com.alan.clients.module.impl.movement.step.NewNCPStep;
import com.alan.clients.module.impl.movement.step.VanillaStep;
import com.alan.clients.module.impl.movement.step.VulcanStep;
import com.alan.clients.module.impl.movement.step.WatchdogStep;
import com.alan.clients.value.impl.ModeValue;

@ModuleInfo(aliases = "module.movement.step.name", description = "module.movement.step.description", category = Category.MOVEMENT)
public class Step extends Module {
    private final ModeValue mode = new ModeValue("Mode", this)
        .add(new VanillaStep("Vanilla", this))
        .add(new NCPStep("NCP", this))
        .add(new NewNCPStep("New NCP", this))
        .add(new NCPPacketlessStep("NCP Packetless", this))
        .add(new VulcanStep("Vulcan", this))
        .add(new MatrixStep("Matrix", this))
        .add(new JumpStep("Jump", this))
        .add(new WatchdogStep("Watchdog", this))
        .setDefault("Vanilla");

    public Step() {
    }
}
