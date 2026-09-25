package com.alan.clients.script.api.wrapper;

import com.alan.clients.script.api.API;
import lombok.Generated;

public abstract class ScriptWrapper<T> extends API {
    protected T wrapped;

    @Generated
    public ScriptWrapper(T wrapped) {
        this.wrapped = wrapped;
    }
}
