package com.alan.clients.module.impl.ghost;

import com.alan.clients.component.impl.player.SlotComponent;
import com.alan.clients.module.Module;
import com.alan.clients.module.api.Category;
import com.alan.clients.module.api.ModuleInfo;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreUpdateEvent;
import com.alan.clients.value.impl.BooleanValue;
import net.minecraft.item.ItemBlock;

@ModuleInfo(aliases = "module.ghost.safewalk.name", description = "module.ghost.safewalk.description", category = Category.GHOST)
public class SafeWalk extends Module {
    private final BooleanValue blocksOnly = new BooleanValue("Blocks Only", this, false);
    private final BooleanValue backwardsOnly = new BooleanValue("Backwards Only", this, false);
    @EventLink
    public final Listener<PreUpdateEvent> onPreUpdate = var1 -> {
        boolean flag;
        label26: {
            label30: {
                if (aEg.thePlayer.onGround && (!aEg.gameSettings.keyBindForward.isKeyDown() || !this.backwardsOnly.wo())) {
                    SlotComponent slotcomponent1 = this.d(SlotComponent.class);
                    if (SlotComponent.getItemStack() != null) {
                        SlotComponent slotcomponent = this.d(SlotComponent.class);
                        if (SlotComponent.getItemStack().getItem() instanceof ItemBlock) {
                            break label30;
                        }
                    }

                    if (!this.blocksOnly.wo()) {
                        break label30;
                    }
                }

                flag = false;
                break label26;
            }

            flag = true;
        }

        aEg.thePlayer.crd = flag;
    };

    public SafeWalk() {
    }
}
