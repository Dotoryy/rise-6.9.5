package com.alan.clients.anticheat.check;

import com.alan.clients.anticheat.alert.AlertManager;
import com.alan.clients.anticheat.check.api.CheckInfo;
import com.alan.clients.anticheat.data.PlayerData;
import com.alan.clients.util.interfaces.ExecutorAccess;
import lombok.Generated;
import net.minecraft.network.Packet;

public abstract class Check implements ExecutorAccess {
    public final PlayerData data;
    AlertManager O = new AlertManager();
    private final CheckInfo checkInfo;
    protected double T;
    protected double U;

    public Check(PlayerData playerData) {
        this.data = playerData;
        if (this.getClass().isAnnotationPresent(CheckInfo.class)) {
            this.checkInfo = this.getClass().getAnnotation(CheckInfo.class);
        } else {
            this.checkInfo = null;
            throw new RuntimeException("CheckInfo annotation not found in " + this.getClass().getSimpleName());
        }
    }

    public abstract void handle(Packet<?> packet);

    public final void J() {
        this.T++;
        this.O.sendAlert(this);
    }

    public final double K() {
        return this.U = Math.min(10000.0, this.U + 1.0);
    }

    public final double increaseBufferBy(double var1) {
        return this.U = Math.min(10000.0, this.U + var1);
    }

    public final double L() {
        return this.U = Math.max(0.0, this.U - 1.0);
    }

    public final double decreaseBufferBy(double var1) {
        return this.U = Math.max(0.0, this.U - var1);
    }

    public final void M() {
        this.U = 0.0;
    }

    public final void setBuffer(double var1) {
        this.U = var1;
    }

    public final void multiplyBuffer(double var1) {
        this.U *= var1;
    }

    @Generated
    public PlayerData getData() {
        return this.data;
    }

    @Generated
    public AlertManager I() {
        return this.O;
    }

    @Generated
    public CheckInfo getCheckInfo() {
        return this.checkInfo;
    }

    @Generated
    public double P() {
        return this.T;
    }

    @Generated
    public double Q() {
        return this.U;
    }
}
