package com.alan.clients.module.impl.movement;

import com.alan.clients.module.Module;
import com.alan.clients.module.api.Category;
import com.alan.clients.module.api.ModuleInfo;
import com.alan.clients.module.impl.movement.inventorymove.GrimInventoryMove;
import com.alan.clients.module.impl.movement.inventorymove.bypass.BufferAbuseBypass;
import com.alan.clients.module.impl.movement.inventorymove.bypass.CancelBypass;
import com.alan.clients.module.impl.movement.inventorymove.bypass.Grim2Bypass;
import com.alan.clients.module.impl.movement.inventorymove.bypass.NormalBypass;
import com.alan.clients.module.impl.movement.inventorymove.bypass.WatchdogBypass;
import com.alan.clients.value.impl.ModeValue;

@ModuleInfo(aliases = "module.movement.inventorymove.name", description = "module.movement.inventorymove.description", category = Category.MOVEMENT)
public class InventoryMove extends Module {
    private final GrimInventoryMove grimBypass = new GrimInventoryMove("Grim", this);
    private final ModeValue bypassMode = new ModeValue("Bypass Mode", this)
        .add(new NormalBypass("Normal", this))
        .add(new BufferAbuseBypass("Buffer Abuse", this))
        .add(new CancelBypass("Cancel", this))
        .add(this.grimBypass)
        .add(new Grim2Bypass("Grim 2", this))
        .add(new WatchdogBypass("Watchdog", this))
        .setDefault("Normal");

    public InventoryMove() {
    }

    public boolean isGrimBypass() {
        return this.bypassMode.wo() != null && this.bypassMode.wo().getName().equalsIgnoreCase("Grim");
    }

    public int getExtraSprintTicks() {
        return this.grimBypass.getManagerExtraSprintTicks();
    }
}
