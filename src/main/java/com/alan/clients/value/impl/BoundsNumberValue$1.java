package com.alan.clients.value.impl;

class BoundsNumberValue$1 extends Number {
    final long aQV;
    final BoundsNumberValue aQW;

    BoundsNumberValue$1(BoundsNumberValue boundsNumberValue, long var2) {
        this.aQW = boundsNumberValue;
        this.aQV = var2;
    }

    @Override
    public int intValue() {
        return Math.round((float)this.aQV);
    }

    @Override
    public long longValue() {
        return this.aQV;
    }

    @Override
    public float floatValue() {
        return (float)this.aQV;
    }

    @Override
    public double doubleValue() {
        return this.aQV;
    }
}
