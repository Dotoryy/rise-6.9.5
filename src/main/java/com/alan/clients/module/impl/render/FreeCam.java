package com.alan.clients.module.impl.render;

import com.alan.clients.module.Module;
import com.alan.clients.module.api.Category;
import com.alan.clients.module.api.ModuleInfo;
import com.alan.clients.newevent.CancellableEvent;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.input.MoveInputEvent;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.newevent.impl.motion.StrafeEvent;
import com.alan.clients.newevent.impl.other.BlockAABBEvent;
import com.alan.clients.newevent.impl.packet.PacketSendEvent;
import com.alan.clients.util.vector.Vector2f;
import com.alan.clients.value.impl.NumberValue;
import com.alan.clients.util.vector.Vector3d;
import lombok.Generated;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.client.m;

@ModuleInfo(aliases = "module.render.freecam.name", description = "module.render.freecam.description", category = Category.RENDER)
public final class FreeCam extends Module {
    private final NumberValue speed = new NumberValue("Speed", this, 1, 0.1, 9.5, 0.1);
    private Vector3d position;
    private Vector3d delta;
    private Vector2f rotation;
    private boolean sprinting;
    @EventLink
    public final Listener<BlockAABBEvent> blockAABBEventListener = CancellableEvent::setCancelled;
    @EventLink
    public final Listener<PacketSendEvent> send = var0 -> {
        Packet packet = var0.dq();
        if (packet instanceof m
            || packet instanceof C03PacketPlayer
            || packet instanceof C02PacketUseEntity
            || packet instanceof C0BPacketEntityAction
            || packet instanceof C08PacketPlayerBlockPlacement
            || packet instanceof C07PacketPlayerDigging
            || packet instanceof C07PacketPlayerDigging) {
            var0.setCancelled();
        }
    };
    @EventLink
    public final Listener<StrafeEvent> onStrafe = var1 -> {
        float f = this.speed.wo().floatValue();
        var1.setSpeed(f);
    };
    @EventLink
    public final Listener<PreMotionEvent> onPreMotionEvent = var1 -> {
        float f = this.speed.wo().floatValue();
        aEg.thePlayer.motionY = 0.0 + (aEg.gameSettings.keyBindJump.isKeyDown() ? f : 0.0) - (aEg.gameSettings.keyBindSneak.isKeyDown() ? f : 0.0);
    };
    @EventLink
    public final Listener<MoveInputEvent> onMovementInput = var0 -> var0.setSneak(false);

    public FreeCam() {
    }

    @Override
    public void onEnable() {
        this.position = new Vector3d(aEg.thePlayer.posX, aEg.thePlayer.posY, aEg.thePlayer.posZ);
        this.delta = new Vector3d(aEg.thePlayer.motionX, aEg.thePlayer.motionY, aEg.thePlayer.motionZ);
        this.rotation = new Vector2f(aEg.thePlayer.pl, aEg.thePlayer.rotationPitch);
        this.sprinting = aEg.gameSettings.cgG.isKeyDown();
    }

    @Override
    public void onDisable() {
        aEg.thePlayer.setPosition(this.position.getX(), this.position.getY(), this.position.getZ());
        aEg.thePlayer.pl = this.rotation.getX();
        aEg.thePlayer.rotationPitch = this.rotation.getY();
        aEg.thePlayer.motionX = this.delta.getX();
        aEg.thePlayer.motionY = this.delta.getY();
        aEg.thePlayer.motionZ = this.delta.getZ();
        aEg.gameSettings.cgG.setPressed(this.sprinting);
    }

    @Generated
    public NumberValue getSpeed() {
        return this.speed;
    }

    @Generated
    public Vector3d getPosition() {
        return this.position;
    }

    @Generated
    public Vector3d getDelta() {
        return this.delta;
    }

    @Generated
    public Vector2f getRotation() {
        return this.rotation;
    }

    @Generated
    public boolean isSprinting() {
        return this.sprinting;
    }

    @Generated
    public Listener<BlockAABBEvent> getBlockAABBEventListener() {
        return this.blockAABBEventListener;
    }

    @Generated
    public Listener<PacketSendEvent> getSend() {
        return this.send;
    }

    @Generated
    public Listener<StrafeEvent> getOnStrafe() {
        return this.onStrafe;
    }

    @Generated
    public Listener<PreMotionEvent> getOnPreMotionEvent() {
        return this.onPreMotionEvent;
    }

    @Generated
    public Listener<MoveInputEvent> getOnMovementInput() {
        return this.onMovementInput;
    }
}
