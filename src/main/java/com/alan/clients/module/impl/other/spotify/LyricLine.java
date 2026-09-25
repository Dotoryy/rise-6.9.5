package com.alan.clients.module.impl.other.spotify;

import java.util.ArrayList;
import java.util.List;

public class LyricLine {
    public final int startTime;
    public final String text;
    public List<LyricWord> words;
    public final boolean wordTimed;

    public LyricLine(int var1, String var2, List<LyricWord> var3, boolean var4) {
        this.startTime = var1;
        this.text = var2;
        this.words = var3 == null ? new ArrayList<>() : var3;
        this.wordTimed = var4;
    }
}
