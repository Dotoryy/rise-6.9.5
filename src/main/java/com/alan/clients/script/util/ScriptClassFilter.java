package com.alan.clients.script.util;

import org.openjdk.nashorn.api.scripting.ClassFilter;

public final class ScriptClassFilter implements ClassFilter {
    public ScriptClassFilter() {
    }

    @Override
    public boolean exposeToScripts(String var1) {
        return var1.startsWith("com.alan.clients.script.api");
    }
}
