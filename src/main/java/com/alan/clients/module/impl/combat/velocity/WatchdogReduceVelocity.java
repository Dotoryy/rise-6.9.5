package com.alan.clients.module.impl.combat.velocity;

import com.alan.clients.module.impl.combat.Velocity;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.newevent.impl.packet.PacketReceiveEvent;
import com.alan.clients.newevent.impl.packet.PacketSendEvent;
import com.alan.clients.value.Mode;
import com.alan.clients.value.impl.BooleanValue;
import com.alan.clients.util.packet.PacketUtil;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity.Action;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S32PacketConfirmTransaction;
import net.minecraft.network.play.server.z;

public class WatchdogReduceVelocity extends Mode<Velocity> {
    private final BooleanValue cancelOnGround = new BooleanValue("Cancel On Ground", this, true);
    private final BooleanValue cancelOnAttack = new BooleanValue("Cancel On Attack", this, true);
    private final BooleanValue cancelExplosions = new BooleanValue("Cancel Explosions", this, true);
    private final List<Packet<?>> heldPackets = new ArrayList<>();
    private boolean flushing = false;
    @EventLink
    public final Listener<PacketReceiveEvent> onPacketReceive = var1x -> {
        if (!this.flushing && aEg.thePlayer != null) {
            Packet packet = var1x.getPacket();
            if (packet instanceof S12PacketEntityVelocity s12packetentityvelocity) {
                if (s12packetentityvelocity.getEntityID() == aEg.thePlayer.getEntityId()) {
                    var1x.setCancelled();
                    synchronized (this.heldPackets) {
                        this.heldPackets.add(packet);
                    }
                }
            } else if (packet instanceof S32PacketConfirmTransaction) {
                synchronized (this.heldPackets) {
                    if (!this.heldPackets.isEmpty()) {
                        var1x.setCancelled();
                        this.heldPackets.add(packet);
                    }
                }
            } else if (packet instanceof z) {
                synchronized (this.heldPackets) {
                    if (!this.heldPackets.isEmpty()) {
                        var1x.setCancelled();
                        this.heldPackets.add(packet);
                    }
                }
            }
        }
    };
    @EventLink
    public final Listener<PacketSendEvent> onPacketSend = var1x -> {
        if (this.cancelOnAttack.wo()) {
            Packet packet = var1x.dq();
            if (packet instanceof C02PacketUseEntity && ((C02PacketUseEntity)packet).getAction() == Action.ATTACK) {
                this.v("attack");
            }
        }
    };
    @EventLink
    public final Listener<PreMotionEvent> onPreMotion = var1x -> {
        if (this.cancelOnGround.wo()) {
            if (aEg.thePlayer.onGround && !this.heldPackets.isEmpty()) {
                this.v("ground");
            }
        }
    };

    public WatchdogReduceVelocity(String var1, Velocity velocity) {
        super(var1, velocity);
    }

    private void v(String param1) {
        List<Packet<?>> list;
        synchronized (this.heldPackets) {
            if (this.heldPackets.isEmpty()) {
                return;
            }

            list = new ArrayList<>(this.heldPackets);
            this.heldPackets.clear();
        }

        this.flushing = true;

        try {
            for (Packet<?> packet : list) {
                PacketUtil.receive(packet);
            }
        } finally {
            this.flushing = false;
        }
    }

    @Override
    public void onDisable() {
        this.v("disable");
    }
}
