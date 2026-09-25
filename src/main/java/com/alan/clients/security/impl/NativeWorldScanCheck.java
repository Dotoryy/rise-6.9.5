package com.alan.clients.security.impl;

import com.alan.clients.security.SecurityFeature;
import com.alan.clients.util.interfaces.InstanceAccess;
import java.util.regex.Pattern;
import net.minecraft.block.Block;

public class NativeWorldScanCheck extends SecurityFeature implements InstanceAccess {
    public static int avO;
    public volatile boolean avQ;
    public static Pattern avP;

    public boolean nS() {
        return false;
    }

    public Block c(int var1, int var2, int var3) {
        return null;
    }

    @Override
    public String getReason() {
        return null;
    }

    public NativeWorldScanCheck() {
    }

    @Override
    public boolean run() {
        return false;
    }

    public boolean i(int var1, int var2) {
        return false;
    }
}
