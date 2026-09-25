package com.alan.clients.module.impl.ghost;

import com.alan.clients.component.impl.player.RotationComponent;
import com.alan.clients.module.Module;
import com.alan.clients.module.api.Category;
import com.alan.clients.module.api.ModuleInfo;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.render.MouseOverEvent;
import com.alan.clients.value.impl.BooleanValue;
import com.alan.clients.value.impl.NumberValue;
import com.alan.clients.util.RayCastUtil;
import com.alan.clients.newevent.impl.input.RightClickEvent;

@ModuleInfo(aliases = "module.ghost.hitbox.name", description = "module.ghost.hitbox.description", category = Category.GHOST)
public class HitBox extends Module {
    public final NumberValue expand = new NumberValue("Expand Amount", this, 0, 0, 6, 0.01);
    private final BooleanValue effectRange = new BooleanValue("Effect range", this, true);
    @EventLink
    public final Listener<MouseOverEvent> onMouseOver = var1 -> {
        var1.setExpand(this.expand.wo().floatValue());
        if (!this.effectRange.wo()) {
            var1.setRange(var1.dA() - this.expand.wo().doubleValue());
        }
    };
    @EventLink
    public final Listener<RightClickEvent> onGetMouseOver = var0 -> aEg.objectMouseOver = RayCastUtil.c(RotationComponent.fk, 4.5);

    public HitBox() {
    }
}
