package com.alan.clients.module.impl.combat;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;

public final class RotationSnapshot {
    private final float pl;
    private final float pitch;
    private final float yawHead;
    private final float po;
    private final float pp;
    private final float pq;
    private final float pr;

    public RotationSnapshot(float var1, float var2, float var3, float var4, float var5, float var6, float var7) {
        this.pl = var1;
        this.pitch = var2;
        this.yawHead = var3;
        this.po = var4;
        this.pp = var5;
        this.pq = var6;
        this.pr = var7;
    }

    public void restore() {
        EntityPlayerSP entityplayersp = Minecraft.getMinecraft().thePlayer;
        if (entityplayersp != null) {
            entityplayersp.pl = this.pl;
            entityplayersp.rotationPitch = this.pitch;
            entityplayersp.rotationYawHead = this.yawHead;
            entityplayersp.po = this.po;
            entityplayersp.pp = this.pp;
            entityplayersp.pq = this.pq;
            entityplayersp.pr = this.pr;
        }
    }
}
