package com.alan.clients.value.impl;

import com.alan.clients.module.Module;
import com.alan.clients.ui.click.standard.components.value.ValueComponent;
import com.alan.clients.value.Mode;
import com.alan.clients.value.Value;
import com.alan.clients.ui.click.standard.components.value.impl.SupplierValueComponent;
import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

public class SupplierValue extends Value<Supplier<Double>> {
    public SupplierValue(String var1, Module module) {
        super(var1, module, null);
    }

    public SupplierValue(String var1, Mode<?> mode) {
        super(var1, mode, null);
    }

    public SupplierValue(String var1, Module module, BooleanSupplier booleanSupplier) {
        super(var1, module, null, booleanSupplier);
    }

    public SupplierValue(String var1, Mode<?> mode, BooleanSupplier booleanSupplier) {
        super(var1, mode, null, booleanSupplier);
    }

    @Override
    public List<Value<?>> getSubValues() {
        return null;
    }

    public SupplierValueComponent wD() {
        return new SupplierValueComponent(this);
    }

    @Override
    public ValueComponent wl() {
        return this.wD();
    }
}
