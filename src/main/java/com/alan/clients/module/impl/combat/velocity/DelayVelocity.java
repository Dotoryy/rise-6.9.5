package com.alan.clients.module.impl.combat.velocity;

import com.alan.clients.module.impl.combat.Velocity;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.other.TickEvent;
import com.alan.clients.newevent.impl.packet.PacketReceiveEvent;
import com.alan.clients.value.Mode;
import com.alan.clients.value.impl.BooleanValue;
import com.alan.clients.value.impl.NumberValue;
import com.alan.clients.util.chat.ChatUtil;
import com.alan.clients.util.packet.PacketUtil;
import com.alan.clients.util.packet.QueuedPacket;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S27PacketExplosion;
import net.minecraft.network.play.server.S32PacketConfirmTransaction;

public class DelayVelocity extends Mode<Velocity> {
    private final NumberValue delayTicks = new NumberValue("Delay (ticks)", this, 5, 0, 40, 1);
    private final BooleanValue pingSpoof = new BooleanValue("Delay Explosions", this, true);
    private final Deque<QueuedPacket> velocityPackets = new ArrayDeque<>();
    private boolean to = false;
    @EventLink
    public final Listener<PacketReceiveEvent> onPacketReceiveEvent = var1x -> {
        if (!this.to && aEg.thePlayer != null) {
            Packet packet = var1x.getPacket();
            if (packet instanceof S12PacketEntityVelocity s12packetentityvelocity) {
                if (s12packetentityvelocity.getEntityID() == aEg.thePlayer.getEntityId()) {
                    var1x.setCancelled();
                    int i = this.delayTicks.wo().intValue();
                    int j = aEg.thePlayer.ticksExisted;
                    this.velocityPackets.addLast(new QueuedPacket(s12packetentityvelocity, j + i));
                }
            } else if (packet instanceof S32PacketConfirmTransaction s32packetconfirmtransaction) {
                var1x.setCancelled();
                int k = this.delayTicks.wo().intValue();
                int l = aEg.thePlayer.ticksExisted;
                this.velocityPackets.addLast(new QueuedPacket(s32packetconfirmtransaction, l + k));
            } else {
                if (this.pingSpoof.wo() && packet instanceof S27PacketExplosion) {
                    var1x.setCancelled();
                    int i1 = this.delayTicks.wo().intValue();
                    int j1 = aEg.thePlayer.ticksExisted;
                    this.velocityPackets.addLast(new QueuedPacket(packet, j1 + i1));
                }
            }
        }
    };
    @EventLink
    public final Listener<TickEvent> onTick = var1x -> {
        if (aEg.thePlayer != null && !this.velocityPackets.isEmpty()) {
            int i = aEg.thePlayer.ticksExisted;

            while (!this.velocityPackets.isEmpty() && this.velocityPackets.peekFirst().ts <= i) {
                Packet packet = this.velocityPackets.removeFirst().tr;
                this.to = true;

                try {
                    PacketUtil.receive(packet);
                } finally {
                    this.to = false;
                }
            }

            ChatUtil.c(this.velocityPackets.peekFirst().ts - i);
        }
    };

    public DelayVelocity(String var1, Velocity velocity) {
        super(var1, velocity);
    }

    @Override
    public void onDisable() {
        if (!this.velocityPackets.isEmpty()) {
            this.to = true;

            try {
                Iterator iterator = this.velocityPackets.iterator();

                while (iterator.hasNext()) {
                    PacketUtil.receive(((QueuedPacket)iterator.next()).tr);
                }
            } finally {
                this.to = false;
                this.velocityPackets.clear();
            }
        }
    }
}
