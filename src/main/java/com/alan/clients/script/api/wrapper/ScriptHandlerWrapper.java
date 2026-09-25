package com.alan.clients.script.api.wrapper;

import com.alan.clients.script.util.ScriptHandler;
import org.openjdk.nashorn.api.scripting.JSObject;

public abstract class ScriptHandlerWrapper<T> extends ScriptWrapper<T> {
    private final ScriptHandler handler = new ScriptHandler();

    public ScriptHandlerWrapper(T var1) {
        super(var1);
    }

    public void handle(String var1, JSObject jsObject) {
        this.handler.handle(var1, jsObject);
    }

    public void unhandle(String var1) {
        this.handler.unhandle(var1);
    }

    public void call(String var1, Object... var2) {
        this.handler.call(var1, var2);
    }
}
