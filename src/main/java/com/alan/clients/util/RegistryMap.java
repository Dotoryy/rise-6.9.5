package com.alan.clients.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map.Entry;

public class RegistryMap<K, V> extends HashMap<K, V> implements Serializable {
    private final ArrayList<V> aEh = new ArrayList<>();

    public RegistryMap() {
    }

    public void g(V var1) {
        this.aEh.add(var1);
    }

    public ArrayList<V> rP() {
        ArrayList arraylist = new ArrayList<>(super.values());
        arraylist.addAll(this.aEh);
        return arraylist;
    }

    public void h(V var1) {
        for (Entry entry : this.entrySet()) {
            if (entry.getValue().equals(var1)) {
                this.remove(entry.getKey());
                break;
            }
        }

        this.aEh.remove(var1);
    }

    @Override
    public Collection values() {
        return this.rP();
    }
}
