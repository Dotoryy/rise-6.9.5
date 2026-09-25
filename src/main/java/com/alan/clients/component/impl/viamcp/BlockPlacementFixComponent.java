package com.alan.clients.component.impl.viamcp;

import com.alan.clients.component.Component;
import com.alan.clients.component.impl.player.RotationComponent;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.packet.PacketSendEvent;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import de.florianmichael.vialoadingbase.ViaLoadingBase;
import com.alan.clients.util.packet.PacketUtil;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C07PacketPlayerDigging.Action;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C0EPacketClickWindow;
import net.minecraft.network.play.client.m;

public final class BlockPlacementFixComponent extends Component {
    @EventLink(value = 0)
    public final Listener<PacketSendEvent> onPacketSend = var0 -> {
        if (ViaLoadingBase.getInstance().getTargetVersion().newerThanOrEqualTo(ProtocolVersion.v1_13)) {
            UserConnection userconnection = Via.getManager().getConnectionManager().getConnections().iterator().next();
            Packet packet = var0.dq();
            if (packet instanceof C07PacketPlayerDigging c07packetplayerdigging
                && (c07packetplayerdigging.getStatus() == Action.DROP_ITEM || c07packetplayerdigging.getStatus() == Action.DROP_ALL_ITEMS)) {
                PacketUtil.send(new m());
            }

            if (packet instanceof C0EPacketClickWindow c0epacketclickwindow && c0epacketclickwindow.getMode() == 4) {
                PacketUtil.send(new m());
            }

            if (ViaLoadingBase.getInstance().getTargetVersion().newerThanOrEqualTo(ProtocolVersion.v1_17)
                && ViaLoadingBase.getInstance().getTargetVersion().olderThan(ProtocolVersion.v1_21)
                && packet instanceof C08PacketPlayerBlockPlacement c08packetplayerblockplacement
                && c08packetplayerblockplacement.getPlacedBlockDirection() == 255) {
                RotationComponent.bH();
            }
        }
    };

    public BlockPlacementFixComponent() {
    }
}
