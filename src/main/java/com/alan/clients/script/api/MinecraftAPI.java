package com.alan.clients.script.api;

import net.minecraft.client.Minecraft;

public class MinecraftAPI extends API {
    public MinecraftAPI() {
    }

    public int getDisplayWidth() {
        return MC.displayWidth;
    }

    public int getDisplayHeight() {
        return MC.displayHeight;
    }

    public float getTimerSpeed() {
        return MC.getTimer().dzD;
    }

    public void setTimerSpeed(float timerSpeed) {
        MC.getTimer().dzD = timerSpeed;
    }

    public float getPartialTicks() {
        return MC.timer.dzE;
    }

    public float getRenderPartialTicks() {
        return MC.timer.bWm;
    }

    public int getFPS() {
        return Minecraft.getDebugFPS();
    }
}
