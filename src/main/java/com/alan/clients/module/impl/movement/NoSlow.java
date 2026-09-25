package com.alan.clients.module.impl.movement;

import com.alan.clients.module.Module;
import com.alan.clients.module.api.Category;
import com.alan.clients.module.api.ModuleInfo;
import com.alan.clients.module.impl.movement.noslow.Grim19NoSlow;
import com.alan.clients.module.impl.movement.noslow.Grim30NoSlow;
import com.alan.clients.module.impl.movement.noslow.GrimNoSlow;
import com.alan.clients.module.impl.movement.noslow.IntaveNoSlow;
import com.alan.clients.module.impl.movement.noslow.LegitNoSlow;
import com.alan.clients.module.impl.movement.noslow.MatrixNoSlow;
import com.alan.clients.module.impl.movement.noslow.NCPNoSlow;
import com.alan.clients.module.impl.movement.noslow.NewNCPNoSlow;
import com.alan.clients.module.impl.movement.noslow.PredictionNoSlow;
import com.alan.clients.module.impl.movement.noslow.VanillaNoSlow;
import com.alan.clients.module.impl.movement.noslow.VariableNoSlow;
import com.alan.clients.module.impl.movement.noslow.WatchdogNoSlow;
import com.alan.clients.module.impl.movement.noslow.WatchdogPredictionNoSlow;
import com.alan.clients.value.impl.BooleanValue;
import com.alan.clients.value.impl.ModeValue;

@ModuleInfo(aliases = "module.movement.noslow.name", description = "module.movement.noslow.description", category = Category.MOVEMENT)
public class NoSlow extends Module {
    private final ModeValue mode = new ModeValue("Mode", this)
        .add(new VanillaNoSlow("Vanilla", this))
        .add(new NCPNoSlow("NCP", this))
        .add(new NewNCPNoSlow("New NCP", this))
        .add(new IntaveNoSlow("Intave", this))
        .add(new LegitNoSlow("Legit", this))
        .add(new WatchdogPredictionNoSlow("Watchdog Prediction", this))
        .add(new VariableNoSlow("Variable", this))
        .add(new PredictionNoSlow("Prediction", this))
        .add(new WatchdogNoSlow("Watchdog", this))
        .add(new Grim19NoSlow("Grim 1.9", this))
        .add(new GrimNoSlow("Grim", this))
        .add(new Grim30NoSlow("Grim 3.0", this))
        .add(new MatrixNoSlow("Matrix", this))
        .setDefault("Vanilla");
    public final BooleanValue food = new BooleanValue("Food", this, false);
    public final BooleanValue potion = new BooleanValue("Potion", this, false);
    public final BooleanValue sword = new BooleanValue("Sword", this, false);
    public final BooleanValue bow = new BooleanValue("Bow", this, false);

    public NoSlow() {
    }
}
