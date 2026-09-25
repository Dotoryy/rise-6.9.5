package com.alan.clients.component.impl.viamcp;

import com.alan.clients.component.Component;
import com.alan.clients.module.impl.combat.KillAura;
import com.alan.clients.module.impl.movement.LongJump;
import com.alan.clients.module.impl.movement.TerrainSpeed;
import com.alan.clients.module.impl.player.Breaker;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreUpdateEvent;
import com.alan.clients.newevent.impl.packet.PacketSendEvent;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import de.florianmichael.vialoadingbase.ViaLoadingBase;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging.Action;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C0EPacketClickWindow;
import net.minecraft.network.play.client.C16PacketClientStatus.EnumState;
import net.minecraft.network.play.client.C16PacketClientStatus;

public final class FlyingPacketFixComponent extends Component {
    public static double il = 0.0;
    private boolean im;
    @EventLink(value = 0)
    public final Listener<PacketSendEvent> onPacketSend = var1 -> {
        TerrainSpeed terrainspeed = this.e(TerrainSpeed.class);
        if (aEg.thePlayer.ticksExisted < 2) {
            il = 0.0;
        }

        if ((
                ViaLoadingBase.getInstance().getTargetVersion().newerThan(ProtocolVersion.v1_8)
                    || "Bloxd".equals(terrainspeed.mode.wo().getName()) && terrainspeed.isEnabled() && !this.e(LongJump.class).isEnabled()
            )
            && var1.dq() instanceof C03PacketPlayer c03packetplayer) {
            if ("Bloxd".equals(terrainspeed.mode.wo().getName())
                && terrainspeed.isEnabled()
                && !this.e(LongJump.class).isEnabled()
                && !c03packetplayer.isMoving()
                && aEg.thePlayer.Zl > 2) {
                il++;
                var1.setCancelled();
            }

            il = il - (aEg.timer.dzD - 1.6);
            if (!c03packetplayer.isMoving() && !c03packetplayer.isRotating() && c03packetplayer.aO == this.im) {
                var1.setCancelled();
            }

            this.im = c03packetplayer.aO;
        }

        if (ViaLoadingBase.getInstance().getTargetVersion().newerThan(ProtocolVersion.v1_8)) {
            Packet packet = var1.dq();
            if (packet instanceof C07PacketPlayerDigging c07packetplayerdigging
                && c07packetplayerdigging.getStatus() != Action.ABORT_DESTROY_BLOCK
                && c07packetplayerdigging.getStatus() != Action.START_DESTROY_BLOCK
                && c07packetplayerdigging.getStatus() != Action.STOP_DESTROY_BLOCK
                && c07packetplayerdigging.getStatus() != Action.RELEASE_USE_ITEM) {
            }

            if (Breaker.targetBlock != null) {
                this.e(Breaker.class).attackWhileBreaking.wo();
            }

            if (!(packet instanceof C0EPacketClickWindow)
                && (!(packet instanceof C16PacketClientStatus) || ((C16PacketClientStatus)packet).getStatus() != EnumState.OPEN_INVENTORY_ACHIEVEMENT)) {
                boolean flag = packet instanceof net.minecraft.network.play.client.q;
            }

            if (packet instanceof C02PacketUseEntity c02packetuseentity
                && c02packetuseentity.getAction() == net.minecraft.network.play.client.C02PacketUseEntity.Action.ATTACK
                && (!(il < 10.0) || this.e(KillAura.class).isEnabled())
                && il < 1.0) {
                this.e(KillAura.class).isEnabled();
            }
        }
    };
    @EventLink
    public final Listener<PreUpdateEvent> onPreUpdate = var1 -> {
        if (!this.e(KillAura.class).isEnabled()) {
            double d0;
            int i = (d0 = il - 10.0) == 0.0 ? 0 : (d0 < 0.0 ? -1 : 1);
        } else if (il < 1.0) {
        }
    };

    public FlyingPacketFixComponent() {
    }
}
