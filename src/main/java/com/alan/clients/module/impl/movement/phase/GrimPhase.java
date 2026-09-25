package com.alan.clients.module.impl.movement.phase;

import com.alan.clients.module.impl.movement.Phase;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.newevent.impl.other.TickEvent;
import com.alan.clients.newevent.impl.packet.PacketReceiveEvent;
import com.alan.clients.newevent.impl.packet.PacketSendEvent;
import com.alan.clients.value.Mode;
import com.alan.clients.value.impl.ModeValue;
import com.alan.clients.value.impl.NumberValue;
import com.alan.clients.value.impl.SubMode;
import com.alan.clients.util.packet.PacketUtil;
import com.alan.clients.util.player.PlayerUtil;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer.C06PacketPlayerPosLook;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;

public class GrimPhase extends Mode<Phase> {
    private final List<Packet<?>> packets = new ArrayList<>();
    private final ModeValue releaseMode = new ModeValue("Release Mode", this)
        .add(new SubMode("Simple"))
        .add(new SubMode("Double"))
        .add(new SubMode("Desync"))
        .add(new SubMode("None"))
        .setDefault("Simple");
    private final NumberValue semiPackets = new NumberValue("Semi Packets", this, 2, 1, 15, 1);
    private boolean phasing;
    private boolean selfDisabled;
    @EventLink
    public final Listener<PacketSendEvent> onPacketSend = var1x -> {
        if (aEg.thePlayer != null) {
            Packet packet = var1x.dq();
            if (packet instanceof C03PacketPlayer) {
                this.packets.add(packet);
                var1x.setCancelled();
            }
        }
    };
    @EventLink
    public final Listener<TickEvent> onTick = var1x -> {
        if (aEg.thePlayer != null && aEg.theWorld != null) {
            boolean flag = PlayerUtil.vk();
            if (!this.phasing && flag) {
                double d0 = aEg.thePlayer.posX;
                double d1 = aEg.thePlayer.posY;
                double d2 = aEg.thePlayer.posZ;
                float f = aEg.thePlayer.pl;
                float f1 = aEg.thePlayer.rotationPitch;
                boolean flag1 = aEg.thePlayer.onGround;

                for (int i = 0; i < this.semiPackets.wo().intValue(); i++) {
                    PacketUtil.sendNoEvent(new C06PacketPlayerPosLook(d0, d1, d2, f, f1, flag1));
                }

                this.phasing = true;
            } else {
                if (this.phasing && !flag) {
                    this.selfDisabled = true;
                    this.toggle();
                }
            }
        }
    };
    @EventLink
    public final Listener<PreMotionEvent> onPreMotion = var0 -> {};
    @EventLink
    public final Listener<PacketReceiveEvent> onPacketReceive = var0 -> {
        boolean flag = var0.getPacket() instanceof S08PacketPlayerPosLook;
    };

    public GrimPhase(String var1, Phase phase) {
        super(var1, phase);
    }

    @Override
    public void onEnable() {
        this.packets.clear();
        this.phasing = false;
        this.selfDisabled = false;
    }

    @Override
    public void onDisable() {
        if (!this.selfDisabled && this.phasing) {
            if (!this.releaseMode.wo().getName().equals("None")) {
                this.release(this.releaseMode.wo().getName());
            } else {
                PacketUtil.sendNoEvent(
                    new C06PacketPlayerPosLook(
                        aEg.thePlayer.posX, aEg.thePlayer.posY, aEg.thePlayer.posZ, aEg.thePlayer.pl, aEg.thePlayer.rotationPitch, aEg.thePlayer.onGround
                    )
                );
            }
        }

        if (aEg.thePlayer != null && !this.packets.isEmpty()) {
            this.packets.forEach(PacketUtil::sendNoEvent);
            this.packets.clear();
        }
    }

    private void release(String var1) {
        double d0;
        double d1;
        double d2;
        float f;
        float f1;
        label44: {
            {
                d0 = aEg.thePlayer.posX;
                d1 = aEg.thePlayer.posY;
                d2 = aEg.thePlayer.posZ;
                f = aEg.thePlayer.pl;
                f1 = aEg.thePlayer.rotationPitch;
                String s = var1.toLowerCase();
                switch (s) {
                    case "simple":
                        PacketUtil.sendNoEvent(new C06PacketPlayerPosLook(d0 - 5000.0, d1, d2 - 5000.0, f, f1, false));
                        PacketUtil.sendNoEvent(new C06PacketPlayerPosLook(d0, d1, d2, f, f1, aEg.thePlayer.onGround));
                        return;
                    case "double":
                        break label44;
                    case "desync":
                        break;
                    default:
                        return;
                }
            }

            PacketUtil.sendNoEvent(new C06PacketPlayerPosLook(d0, d1 + 0.0625, d2, f, f1, false));
            PacketUtil.sendNoEvent(new C06PacketPlayerPosLook(d0, d1, d2, f, f1, false));
            PacketUtil.sendNoEvent(new C06PacketPlayerPosLook(d0, d1 + 0.03125, d2, f, f1, true));
            PacketUtil.sendNoEvent(new C06PacketPlayerPosLook(d0, d1, d2, f, f1, aEg.thePlayer.onGround));
            return;
        }

        PacketUtil.sendNoEvent(new C06PacketPlayerPosLook(d0 - 5000.0, d1, d2 - 5000.0, f, f1, false));
        PacketUtil.sendNoEvent(new C06PacketPlayerPosLook(d0 + 5000.0, d1, d2 + 5000.0, f, f1, false));
        PacketUtil.sendNoEvent(new C06PacketPlayerPosLook(d0, d1, d2, f, f1, aEg.thePlayer.onGround));
    }
}
