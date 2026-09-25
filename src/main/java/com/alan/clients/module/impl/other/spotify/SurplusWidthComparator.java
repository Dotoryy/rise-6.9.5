package com.alan.clients.module.impl.other.spotify;

import com.alan.clients.module.impl.other.Spotify;
import java.util.Comparator;
import java.util.List;

public class SurplusWidthComparator implements Comparator<Integer> {
    final int size;
    final int[] widths;
    final int[] minWidths;
    final List tokenKinds;

    public SurplusWidthComparator(Spotify spotify, int var2, int[] var3, int[] var4, List var5) {
        this.size = var2;
        this.widths = var3;
        this.minWidths = var4;
        this.tokenKinds = var5;
    }

    public int compareSurplus(Integer var1, Integer var2) {
        if (var1 == this.size - 1) {
            return 1;
        }

        if (var2 == this.size - 1) {
            return -1;
        }

        int i = this.widths[var1] - this.minWidths[var1];
        int j = this.widths[var2] - this.minWidths[var2];
        return i != j ? Integer.compare(j, i) : Integer.compare((Integer)this.tokenKinds.get(var1), (Integer)this.tokenKinds.get(var2));
    }

    public int compare(Integer var1, Integer var2) {
        return this.compareSurplus(var1, var2);
    }
}
