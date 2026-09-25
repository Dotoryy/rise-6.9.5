package com.alan.clients.module.impl.movement.longjump;

import com.alan.clients.module.impl.movement.LongJump;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.input.MoveInputEvent;
import com.alan.clients.newevent.impl.motion.StrafeEvent;
import com.alan.clients.newevent.impl.packet.PacketReceiveEvent;
import com.alan.clients.newevent.impl.render.Render3DEvent;
import com.alan.clients.util.player.MoveUtil;
import com.alan.clients.value.Mode;
import com.alan.clients.value.impl.BooleanValue;
import com.viaversion.viarewind.protocol.v1_9to1_8.Protocol1_9To1_8;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import com.viaversion.viaversion.api.type.Types;
import com.viaversion.viaversion.protocols.v1_8to1_9.packet.ServerboundPackets1_9;
import de.florianmichael.vialoadingbase.ViaLoadingBase;
import com.alan.clients.util.packet.PacketUtil;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.C03PacketPlayer.C06PacketPlayerPosLook;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;

public class Matrix2LongJump extends Mode<LongJump> {
    private final BooleanValue silent = new BooleanValue("Silent", this, false);
    private boolean started;
    private double startX;
    private double startZ;
    private double startY;
    private boolean hadViewBobbing;
    @EventLink
    public final Listener<PacketReceiveEvent> onPacketReceive = var1x -> {
        if (var1x.getPacket() instanceof S08PacketPlayerPosLook s08packetplayerposlook) {
            var1x.setCancelled(true);
            double d0 = s08packetplayerposlook.getX();
            double d1 = s08packetplayerposlook.getY();
            double d2 = s08packetplayerposlook.getZ();
            float f = aEg.thePlayer.pl;
            float f1 = aEg.thePlayer.rotationPitch;
            if (ViaLoadingBase.getInstance().getTargetVersion().newerThanOrEqualTo(ProtocolVersion.v1_9)) {
                UserConnection userconnection = Via.getManager().getConnectionManager().getConnections().iterator().next();
                PacketWrapper packetwrapper = PacketWrapper.create(ServerboundPackets1_9.ACCEPT_TELEPORTATION, userconnection);
                packetwrapper.write(Types.VAR_INT, s08packetplayerposlook.field_180058_f);
                packetwrapper.sendToServer(Protocol1_9To1_8.class);
            }

            PacketUtil.sendNoEvent(new C06PacketPlayerPosLook(d0, d1, d2, f, f1, false));
            aEg.thePlayer.setPosition(d0, d1, d2);
            aEg.thePlayer.jump();
            this.getParent().setEnabled(false);
        }
    };
    @EventLink
    public final Listener<MoveInputEvent> onMoveInput = var0 -> {
        if (!MoveUtil.isMoving()) {
            var0.setForward(1.0F);
        }
    };
    @EventLink
    public final Listener<Render3DEvent> onRender3D = var1x -> {
        if (this.silent.wo()) {
            double d3 = aEg.timer.bWm;
            double d0 = this.startX;
            double d1 = this.startY;
            double d2 = this.startZ;
            aEg.gameSettings.cfG = false;
            Entity entity = aEg.getRenderViewEntity();
            entity.posX = d0;
            entity.posY = d1;
            entity.posZ = d2;
            entity.prevPosX = d0;
            entity.prevPosY = d1;
            entity.prevPosZ = d2;
        }
    };
    @EventLink
    public final Listener<StrafeEvent> onStrafe = var1x -> {
        if (aEg.thePlayer.onGround) {
            aEg.thePlayer.jump();
        }

        this.started = true;
        if (aEg.thePlayer.ae > 1) {
            aEg.thePlayer.motionY += 0.00348;
        }

        if (this.started && aEg.thePlayer.fallDistance > 0.1) {
            aEg.thePlayer.motionY = 0.42;
            MoveUtil.strafe(1.97);
        }
    };

    public Matrix2LongJump(String var1, LongJump longJump) {
        super(var1, longJump);
    }

    @Override
    public void onDisable() {
        this.started = false;
        if (this.hadViewBobbing) {
            aEg.gameSettings.cfG = true;
        }
    }

    @Override
    public void onEnable() {
        if (aEg.gameSettings.cfG) {
            this.hadViewBobbing = true;
        } else {
            this.hadViewBobbing = false;
        }

        this.startX = aEg.thePlayer.posX;
        this.startZ = aEg.thePlayer.posZ;
        this.startY = aEg.thePlayer.posY;
    }
}
