package com.alan.clients.module.impl.render;

import com.alan.clients.module.Module;
import com.alan.clients.module.api.Category;
import com.alan.clients.module.api.ModuleInfo;
import com.alan.clients.module.impl.render.nametags.ClassicNameTags;
import com.alan.clients.module.impl.render.nametags.ModernNameTags;
import com.alan.clients.module.impl.render.nametags.VanillaNameTags;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.impl.other.WorldChangeEvent;
import com.alan.clients.value.impl.BooleanValue;
import com.alan.clients.value.impl.ModeValue;
import com.alan.clients.util.font.Font;
import java.util.HashMap;
import java.util.Map;
import lombok.Generated;

@ModuleInfo(aliases = "module.render.nametags.name", description = "module.render.nametags.description", category = Category.RENDER)
public final class NameTags extends Module {
    public final Map<String, Integer> widthCache = new HashMap<>();
    private final ModeValue mode = new ModeValue("Mode", this)
        .add(new ModernNameTags("Modern", this))
        .add(new VanillaNameTags("Vanilla", this))
        .add(new ClassicNameTags("Classic", this))
        .setDefault("Modern");
    private final BooleanValue showTargets = new BooleanValue("Targets", this, false);
    public final BooleanValue player = new BooleanValue("Player", this, true, () -> !this.showTargets.wo());
    public final BooleanValue invisibles = new BooleanValue("Invisibles", this, false, () -> !this.showTargets.wo());
    public final BooleanValue animals = new BooleanValue("Animals", this, false, () -> !this.showTargets.wo());
    public final BooleanValue mobs = new BooleanValue("Mobs", this, false, () -> !this.showTargets.wo());
    public final BooleanValue playerTeammates = new BooleanValue("Player Teammates", this, true, () -> !this.showTargets.wo());
    public final Listener<WorldChangeEvent> onRender2D = var1 -> this.widthCache.clear();

    public NameTags() {
    }

    public float a(String var1, Font var2) {
        String s = var1 + var2.hashCode();
        if (!this.widthCache.containsKey(s)) {
            this.widthCache.put(s, var2.getStringWidth(var1));
        }

        return this.widthCache.get(s).intValue();
    }

    @Generated
    public Map<String, Integer> getWidthCache() {
        return this.widthCache;
    }
}
