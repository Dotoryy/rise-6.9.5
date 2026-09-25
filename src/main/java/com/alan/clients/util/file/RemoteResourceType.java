package com.alan.clients.util.file;

import com.alan.clients.compat.NetworkToggles;
import com.alan.clients.script.ScriptManager;
import com.alan.clients.util.file.config.ConfigManager;
import java.io.File;
import lombok.Generated;

public enum RemoteResourceType {
    CONFIG("https://199.247.6.233/getconfig?id=", ConfigManager.CONFIG_DIRECTORY + File.separator, ".json"),
    SCRIPT("https://199.247.6.233/getscript?id=", ScriptManager.SCRIPT_DIRECTORY + File.separator, ".js");

    private final String aAh;
    private final String aAi;
    private final String type;
    private static final RemoteResourceType[] $VALUES = qH();

    @Generated
    RemoteResourceType(String var3, String var4, String var5) {
        this.aAh = var3;
        this.aAi = var4;
        this.type = var5;
    }

    //add code
    @Generated
    public String mY() {
        return NetworkToggles.remoteScripts() ? this.aAh : "";
    }

    @Generated
    public String qG() {
        return this.aAi;
    }

    @Generated
    public String getType() {
        return this.type;
    }

    private static RemoteResourceType[] qH() {
        return new RemoteResourceType[]{CONFIG, SCRIPT};
    }
}
