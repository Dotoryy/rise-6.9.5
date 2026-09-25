package com.alan.clients.module.impl.combat;

import com.alan.clients.module.Module;
import com.alan.clients.module.api.Category;
import com.alan.clients.module.api.ModuleInfo;
import com.alan.clients.module.impl.combat.velocity.AACVelocity;
import com.alan.clients.module.impl.combat.velocity.BounceVelocity;
import com.alan.clients.module.impl.combat.velocity.BufferAbuseVelocity;
import com.alan.clients.module.impl.combat.velocity.DelayVelocity;
import com.alan.clients.module.impl.combat.velocity.Grim2Velocity;
import com.alan.clients.module.impl.combat.velocity.GrimReduceVelocity;
import com.alan.clients.module.impl.combat.velocity.GrimTestVelocity;
import com.alan.clients.module.impl.combat.velocity.GrimVelocity;
import com.alan.clients.module.impl.combat.velocity.GroundVelocity;
import com.alan.clients.module.impl.combat.velocity.IntaveVelocity;
import com.alan.clients.module.impl.combat.velocity.KarhuVelocity;
import com.alan.clients.module.impl.combat.velocity.MMCVelocity;
import com.alan.clients.module.impl.combat.velocity.MatrixVelocity;
import com.alan.clients.module.impl.combat.velocity.StandardVelocity;
import com.alan.clients.module.impl.combat.velocity.TickVelocity;
import com.alan.clients.module.impl.combat.velocity.UniversocraftVelocity;
import com.alan.clients.module.impl.combat.velocity.VulcanVelocity;
import com.alan.clients.module.impl.combat.velocity.WatchdogDamageBoostFlyDisablerVelocity;
import com.alan.clients.module.impl.combat.velocity.WatchdogPredictionVelocity;
import com.alan.clients.module.impl.combat.velocity.WatchdogReduceVelocity;
import com.alan.clients.module.impl.combat.velocity.WatchdogVelocity;
import com.alan.clients.value.impl.BooleanValue;
import com.alan.clients.value.impl.ModeValue;
import com.alan.clients.module.impl.combat.velocity.LegitVelocity;

@ModuleInfo(aliases = "module.combat.velocity.name", description = "module.combat.velocity.description", category = Category.COMBAT)
public final class Velocity extends Module {
    public final ModeValue mode = new ModeValue("Mode", this)
        .add(new StandardVelocity("Standard", this))
        .add(new BufferAbuseVelocity("Buffer Abuse", this))
        .add(new DelayVelocity("Delay", this))
        .add(new LegitVelocity("Legit/Jump Reset", this))
        .add(new LegitVelocity("Polar", this))
        .add(new GroundVelocity("Ground", this))
        .add(new IntaveVelocity("Intave", this))
        .add(new MatrixVelocity("Matrix", this))
        .add(new AACVelocity("AAC", this))
        .add(new VulcanVelocity("Vulcan", this))
        .add(new WatchdogDamageBoostFlyDisablerVelocity("Watchdog Damage Boost Fly Disabler", this))
        .add(new TickVelocity("Tick", this))
        .add(new BounceVelocity("Bounce", this))
        .add(new KarhuVelocity("Karhu", this))
        .add(new MMCVelocity("MMC", this))
        .add(new UniversocraftVelocity("Universocraft", this))
        .add(new GrimReduceVelocity("Grim Reduce", this))
        .add(new GrimVelocity("Grim", this))
        .add(new Grim2Velocity("Grim 2", this))
        .add(new WatchdogVelocity("Watchdog", this))
        .add(new WatchdogPredictionVelocity("Watchdog Prediction", this))
        .add(new WatchdogReduceVelocity("Watchdog Reduce", this))
        .add(new GrimTestVelocity("Grim Test", this))
        .setDefault("Standard");
    public final BooleanValue onSwing = new BooleanValue("On Swing", this, false);

    public Velocity() {
    }
}
