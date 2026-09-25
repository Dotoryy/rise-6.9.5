package com.alan.clients.script.api.wrapper.impl.packet;

import com.alan.clients.script.api.wrapper.impl.ScriptBlockPos;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;

public class ScriptPacketBlockPlacement extends ScriptPacket<C08PacketPlayerBlockPlacement> {
    public ScriptPacketBlockPlacement(C08PacketPlayerBlockPlacement packet) {
        super(packet);
    }

    public ScriptBlockPos getPosition() {
        return new ScriptBlockPos(this.wrapped.getPosition());
    }

    public int getPlacedBlockDirection() {
        return this.wrapped.getPlacedBlockDirection();
    }

    public float getFacingX() {
        return this.wrapped.getFacingX();
    }

    public float getFacingY() {
        return this.wrapped.getFacingY();
    }

    public float getFacingZ() {
        return this.wrapped.getFacingZ();
    }

    public void setFacingX(float var1) {
        this.wrapped.facingX = var1;
    }

    public void setFacingY(float var1) {
        this.wrapped.facingY = var1;
    }

    public void setFacingZ(float var1) {
        this.wrapped.facingZ = var1;
    }
}
