package com.alan.clients.module.impl.ghost;

import com.alan.clients.component.impl.player.SlotComponent;
import com.alan.clients.module.Module;
import com.alan.clients.module.api.Category;
import com.alan.clients.module.api.ModuleInfo;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.value.impl.NumberValue;
import net.minecraft.item.ItemBlock;

@ModuleInfo(aliases = "module.ghost.fastplace.name", description = "module.ghost.fastplace.description", category = Category.GHOST)
public class FastPlace extends Module {
    private final NumberValue delay = new NumberValue("Delay", this, 0, 0, 3, 1);
    @EventLink
    public final Listener<PreMotionEvent> onPreMotionEvent = var1 -> {
        SlotComponent slotcomponent = this.d(SlotComponent.class);
        if (SlotComponent.getItemStack() != null) {
            slotcomponent = this.d(SlotComponent.class);
            if (SlotComponent.getItemStack().getItem() instanceof ItemBlock) {
                aEg.rightClickDelayTimer = Math.min(aEg.rightClickDelayTimer, this.delay.wo().intValue());
            }
        }
    };

    public FastPlace() {
    }
}
