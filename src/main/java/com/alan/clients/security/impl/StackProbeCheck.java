package com.alan.clients.security.impl;

import com.alan.clients.security.SecurityFeature;
import rip.vantage.runtime.StackProbe;

public class StackProbeCheck extends SecurityFeature {

    @Override
    public boolean run() {
        return StackProbe.aLl();
    }


    static {
    }

    @Override
    public String getReason() {
        return "stackprobe";
    }

    public StackProbeCheck() {
    }
}
