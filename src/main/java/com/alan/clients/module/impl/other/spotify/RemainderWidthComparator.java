package com.alan.clients.module.impl.other.spotify;

import com.alan.clients.module.impl.other.Spotify;
import java.util.Comparator;
import java.util.List;

public class RemainderWidthComparator implements Comparator<Integer> {
    final int size;
    final double[] remainders;
    final List segmentKinds;

    public RemainderWidthComparator(Spotify spotify, int var2, double[] var3, List var4) {
        this.size = var2;
        this.remainders = var3;
        this.segmentKinds = var4;
    }

    public int compareIndices(Integer var1, Integer var2) {
        if (var1 == this.size - 1) {
            return -1;
        }

        if (var2 == this.size - 1) {
            return 1;
        }

        int i = Double.compare(this.remainders[var2], this.remainders[var1]);
        return i != 0 ? i : -Integer.compare((Integer)this.segmentKinds.get(var1), (Integer)this.segmentKinds.get(var2));
    }

    public int compare(Integer var1, Integer var2) {
        return this.compareIndices(var1, var2);
    }
}
