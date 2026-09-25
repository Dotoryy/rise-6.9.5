package com.alan.clients.script.util;

import com.alan.clients.module.api.Category;
import com.alan.clients.module.api.ModuleInfo;
import java.lang.annotation.Annotation;

public final class ScriptModuleInfo implements ModuleInfo {
    private final String name;
    private final String description;

    public ScriptModuleInfo(String name, String var2) {
        this.name = name;
        this.description = var2;
    }

    @Override
    public String[] aliases() {
        return new String[]{this.name};
    }

    @Override
    public String description() {
        return this.description;
    }

    @Override
    public Category category() {
        return Category.SCRIPT;
    }

    @Override
    public int keyBind() {
        return 0;
    }

    @Override
    public boolean autoEnabled() {
        return false;
    }

    @Override
    public boolean allowDisable() {
        return true;
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return ScriptModuleInfo.class;
    }
}
