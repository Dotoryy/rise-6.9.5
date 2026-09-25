package com.alan.clients.util.rotation;

import com.alan.clients.util.type.IndexedEvictingList;

public class RotationSamples {
    public static IndexedEvictingList<RotationSample> ZI = new IndexedEvictingList<>(10);
    public static IndexedEvictingList<RotationSample> ZJ = new IndexedEvictingList<>(10);

    public RotationSamples() {
    }
}
