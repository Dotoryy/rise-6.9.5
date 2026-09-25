package com.alan.clients.module.impl.render;

import com.alan.clients.module.Module;
import com.alan.clients.module.api.Category;
import com.alan.clients.module.api.ModuleInfo;
import com.alan.clients.module.impl.render.sessionstats.ModernSessionStats;
import com.alan.clients.module.impl.render.sessionstats.RueSessionStats;
import com.alan.clients.util.vector.Vector2d;
import com.alan.clients.value.impl.DragValue;
import com.alan.clients.value.impl.ModeValue;

@ModuleInfo(aliases = "module.render.sessionstats.name", description = "module.render.sessionstats.description", category = Category.RENDER)
public final class SessionStats extends Module {
    private final DragValue position = new DragValue("", this, new Vector2d(100.0, 200.0), true);
    private final ModeValue glowMode = new ModeValue("Mode", this)
        .add(new RueSessionStats("Rue", this))
        .add(new ModernSessionStats("Modern", this))
        .setDefault("Modern");

    public SessionStats() {
    }

    public DragValue getPosition() {
        return this.position;
    }
}
