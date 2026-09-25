package com.alan.clients.newevent.impl.other;

import com.alan.clients.newevent.CancellableEvent;
import lombok.Generated;
import net.minecraft.network.play.client.C03PacketPlayer;

public final class TeleportEvent extends CancellableEvent {
    private C03PacketPlayer response;
    private double posX;
    private double posY;
    private double posZ;
    private float yaw;
    private float pitch;

    @Generated
    public C03PacketPlayer getResponse() {
        return this.response;
    }

    @Generated
    public double getPosX() {
        return this.posX;
    }

    @Generated
    public double getPosY() {
        return this.posY;
    }

    @Generated
    public double getPosZ() {
        return this.posZ;
    }

    @Generated
    public float getYaw() {
        return this.yaw;
    }

    @Generated
    public float getPitch() {
        return this.pitch;
    }

    @Generated
    public void setResponse(C03PacketPlayer response) {
        this.response = response;
    }

    @Generated
    public void setPosX(double var1) {
        this.posX = var1;
    }

    @Generated
    public void setPosY(double var1) {
        this.posY = var1;
    }

    @Generated
    public void setPosZ(double var1) {
        this.posZ = var1;
    }

    @Generated
    public void setYaw(float var1) {
        this.yaw = var1;
    }

    @Generated
    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    @Generated
    public TeleportEvent(C03PacketPlayer response, double var2, double var4, double var6, float var8, float var9) {
        this.response = response;
        this.posX = var2;
        this.posY = var4;
        this.posZ = var6;
        this.yaw = var8;
        this.pitch = var9;
    }
}
