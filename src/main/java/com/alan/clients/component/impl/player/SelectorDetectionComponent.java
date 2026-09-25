package com.alan.clients.component.impl.player;

import com.alan.clients.component.Component;
import com.alan.clients.component.impl.player.SlotComponent;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreUpdateEvent;
import net.minecraft.entity.EntityList;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

public final class SelectorDetectionComponent extends Component {
    private static boolean selector;
    @EventLink(value = 4)
    public final Listener<PreUpdateEvent> onPreUpdate = var1 -> {
        SlotComponent slotcomponent = this.d(SlotComponent.class);
        if (SlotComponent.getItemStack() != null) {
            slotcomponent = this.d(SlotComponent.class);
            ItemStack itemstack = SlotComponent.getItemStack();
            selector = !trueName(itemstack).contains(itemstack.getDisplayName());
        } else {
            selector = false;
        }
    };

    public SelectorDetectionComponent() {
    }

    public static boolean selector() {
        return selector;
    }

    public static boolean c(ItemStack stack) {
        if (stack == null) {
            return false;
        }
        return stack == aEg.thePlayer.inventory.getItemStack() ? selector() : !trueName(stack).contains(stack.getDisplayName());
    }

    public static boolean a(ItemStack stack, boolean var1) {
        if (stack == null) {
            return false;
        } else if (stack == aEg.thePlayer.inventory.getItemStack()) {
            return selector();
        }
        return var1 ? !trueName(stack).contains(stack.getDisplayName()) : false;
    }

    public static boolean h(int var0) {
        return c(aEg.thePlayer.inventory.getStackInSlot(var0));
    }

    public static boolean a(int var0, boolean var1) {
        return a(aEg.thePlayer.inventory.getStackInSlot(var0), var1);
    }

    public static String trueName(ItemStack stack) {
        String s = (StatCollector.translateToLocal(stack.getUnlocalizedName() + ".name") + "").trim();
        String metadata = EntityList.getStringFromID(stack.getMetadata());
        if (metadata != null) {
            s = s + " " + StatCollector.translateToLocal("entity." + metadata + ".name");
        }

        return s;
    }
}
