package com.alan.clients.script.api.wrapper.impl.packet;

import java.lang.reflect.Field;
import net.minecraft.network.play.server.S27PacketExplosion;

public class ScriptPacketExplosion extends ScriptPacket<S27PacketExplosion> {
    public ScriptPacketExplosion(S27PacketExplosion packet) {
        super(packet);
    }

    public double getX() {
        return this.wrapped.getX();
    }

    public double getY() {
        return this.wrapped.getY();
    }

    public double getZ() {
        return this.wrapped.getZ();
    }

    public float getStrength() {
        return this.wrapped.getStrength();
    }

    public float getPlayerMotionX() {
        return this.wrapped.func_149149_c();
    }

    public float getPlayerMotionY() {
        return this.wrapped.func_149144_d();
    }

    public float getPlayerMotionZ() {
        return this.wrapped.func_149147_e();
    }

    public void setPlayerMotionX(float var1) {
        try {
            Field field = this.vanillaField("field_149153_g");
            field.setAccessible(true);
            field.setFloat(this.wrapped, var1);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void setPlayerMotionY(float var1) {
        try {
            Field field = this.vanillaField("field_149152_f");
            field.setAccessible(true);
            field.setFloat(this.wrapped, var1);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void setPlayerMotionZ(float var1) {
        try {
            Field field = this.vanillaField("field_149159_h");
            field.setAccessible(true);
            field.setFloat(this.wrapped, var1);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
