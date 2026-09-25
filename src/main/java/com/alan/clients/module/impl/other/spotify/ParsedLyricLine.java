package com.alan.clients.module.impl.other.spotify;

import java.util.List;

public class ParsedLyricLine {
    public final String text;
    public final List<LyricWord> words;

    public ParsedLyricLine(String var1, List<LyricWord> var2) {
        this.text = var1;
        this.words = var2;
    }
}
