package com.alan.clients.module.impl.render.appleskin;

import com.alan.clients.module.impl.render.appleskin.FoodValues;
import com.alan.clients.util.interfaces.InstanceAccess;
import lombok.Generated;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;

public final class FoodHelper implements InstanceAccess {
    public static FoodValues getFoodValues(ItemStack stack) {
        ItemFood itemfood = (ItemFood)stack.getItem();
        int i = itemfood != null ? itemfood.getHealAmount(stack) : 0;
        float f = itemfood != null ? itemfood.getSaturationModifier(stack) : 0.0F;
        return new FoodValues(i, f);
    }

    public static boolean isRotten(ItemStack stack) {
        if (!(stack.getItem() instanceof ItemFood)) {
            return false;
        }

        ItemFood itemfood = (ItemFood)stack.getItem();
        return itemfood.getPotionEffect(stack) != null ? Potion.potionTypes[itemfood.getPotionId()].isBadEffect() : false;
    }

    @Generated
    private FoodHelper() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
