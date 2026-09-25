package com.alan.clients.module.impl.render.appleskin;

public class FoodValues {
    public final int hunger;
    public final float saturationModifier;

    public FoodValues(int hunger, float saturationModifier) {
        this.hunger = hunger;
        this.saturationModifier = saturationModifier;
    }

    public float getSaturationIncrement() {
        return this.hunger * this.saturationModifier * 2.0F;
    }

    @Override
    public boolean equals(Object var1) {
        if (this == var1) {
            return true;
        }
        return !(var1 instanceof FoodValues yc) ? false : this.hunger == yc.hunger && Float.compare(yc.saturationModifier, this.saturationModifier) == 0;
    }

    @Override
    public int hashCode() {
        int i = this.hunger;
        return 31 * i + (this.saturationModifier != 0.0F ? Float.floatToIntBits(this.saturationModifier) : 0);
    }
}
