package com.alan.clients.ui.menu.impl.account.impl;

import com.alan.clients.util.account.localts.LocaltsProduct;
import java.util.Comparator;

public class AddLocaltsScreen$1 implements Comparator<LocaltsProduct> {
    AddLocaltsScreen$1() {
    }

    public int compare(LocaltsProduct var1, LocaltsProduct var2) {
        int i = AddLocaltsScreen.productPriority(var1) - AddLocaltsScreen.productPriority(var2);
        return i != 0 ? i : var1.aFD.compareToIgnoreCase(var2.aFD);
    }
}
