package com.alan.clients.module.impl.movement;

import com.alan.clients.module.Module;
import com.alan.clients.module.api.Category;
import com.alan.clients.module.api.ModuleInfo;
import com.alan.clients.module.impl.movement.speed.BlocksMCSpeed;
import com.alan.clients.module.impl.movement.speed.Grim2Speed;
import com.alan.clients.module.impl.movement.speed.GrimSpeed;
import com.alan.clients.module.impl.movement.speed.KoksCraftSpeed;
import com.alan.clients.module.impl.movement.speed.LegitSpeed;
import com.alan.clients.module.impl.movement.speed.MatrixSpeed;
import com.alan.clients.module.impl.movement.speed.MineMenClubSpeed;
import com.alan.clients.module.impl.movement.speed.MiniBloxSpeed;
import com.alan.clients.module.impl.movement.speed.NCPSpeed;
import com.alan.clients.module.impl.movement.speed.OldNCPYPortSpeed;
import com.alan.clients.module.impl.movement.speed.PolarSpeed;
import com.alan.clients.module.impl.movement.speed.StrafeSpeed;
import com.alan.clients.module.impl.movement.speed.TatakoSpeed;
import com.alan.clients.module.impl.movement.speed.VanillaSpeed;
import com.alan.clients.module.impl.movement.speed.VerusSpeed;
import com.alan.clients.module.impl.movement.speed.VulcanSpeed;
import com.alan.clients.module.impl.movement.speed.Watchdog6TickSpeed;
import com.alan.clients.module.impl.movement.speed.WatchdogPredictionSpeed;
import com.alan.clients.module.impl.movement.speed.WatchdogSpeed;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.other.TeleportEvent;
import com.alan.clients.util.player.MoveUtil;
import com.alan.clients.value.impl.BooleanValue;
import com.alan.clients.value.impl.ModeValue;
import lombok.Generated;

@ModuleInfo(aliases = "module.movement.speed.name", description = "module.movement.speed.description", category = Category.MOVEMENT)
public class Speed extends Module {
    public final ModeValue mode = new ModeValue("Mode", this)
        .add(new VanillaSpeed("Vanilla", this))
        .add(new StrafeSpeed("Strafe", this))
        .add(new WatchdogPredictionSpeed("Watchdog Prediction", this))
        .add(new VulcanSpeed("Vulcan", this))
        .add(new WatchdogSpeed("Watchdog", this))
        .add(new NCPSpeed("NCP", this))
        .add(new MiniBloxSpeed("MiniBlox", this))
        .add(new VerusSpeed("Verus", this))
        .add(new MatrixSpeed("Matrix", this))
        .add(new BlocksMCSpeed("BlocksMC", this))
        .add(new MineMenClubSpeed("MineMenClub", this))
        .add(new Watchdog6TickSpeed("Watchdog 6 Tick", this))
        .add(new KoksCraftSpeed("KoksCraft", this))
        .add(new LegitSpeed("Legit", this))
        .add(new PolarSpeed("Polar", this))
        .add(new TatakoSpeed("Tatako", this))
        .add(new GrimSpeed("Grim", this))
        .add(new Grim2Speed("Grim 2", this))
        .add(new OldNCPYPortSpeed("Old NCP Y-port", this))
        .setDefault("Vanilla");
    private final BooleanValue disableOnTeleport = new BooleanValue("Disable on Teleport", this, false);
    private final BooleanValue stopOnDisable = new BooleanValue("Stop on Disable", this, false);
    @EventLink
    public final Listener<TeleportEvent> onTeleport = var1 -> {
        if (this.disableOnTeleport.wo()) {
            this.toggle();
        }
    };

    public Speed() {
    }

    @Override
    public void onDisable() {
        aEg.timer.dzD = 1.0F;
        if (this.stopOnDisable.wo()) {
            MoveUtil.stop();
        }
    }

    @Generated
    public ModeValue getMode() {
        return this.mode;
    }
}
