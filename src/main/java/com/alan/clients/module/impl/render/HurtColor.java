package com.alan.clients.module.impl.render;

import com.alan.clients.module.Module;
import com.alan.clients.module.api.Category;
import com.alan.clients.module.api.ModuleInfo;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.render.HurtRenderEvent;
import com.alan.clients.value.impl.BooleanValue;

@ModuleInfo(aliases = "module.render.hurtcolor.name", description = "module.render.hurtcolor.description", category = Category.RENDER)
public final class HurtColor extends Module {
    private final BooleanValue oldDamage = new BooleanValue("1.7 Damage Animation", this, true);
    @EventLink
    public final Listener<HurtRenderEvent> onHurtRender = var1 -> var1.setOldDamage(this.oldDamage.wo());

    public HurtColor() {
    }
}
