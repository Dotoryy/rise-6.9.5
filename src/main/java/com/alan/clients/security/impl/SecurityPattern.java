package com.alan.clients.security.impl;

import java.util.regex.Pattern;

final class SecurityPattern {
    final String awF;
    final Pattern awG;

    SecurityPattern(String var1, String var2) {
        this.awF = var1;
        this.awG = Pattern.compile(var2, 66);
    }
}
