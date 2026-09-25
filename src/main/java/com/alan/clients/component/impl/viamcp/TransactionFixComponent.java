package com.alan.clients.component.impl.viamcp;

import com.alan.clients.component.Component;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.packet.PacketSendEvent;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import de.florianmichael.vialoadingbase.ViaLoadingBase;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;

public final class TransactionFixComponent extends Component {
    @EventLink(value = 0)
    public final Listener<PacketSendEvent> onPacketSend = var0 -> {
        if (!var0.isCancelled() && ViaLoadingBase.getInstance().getTargetVersion().newerThanOrEqualTo(ProtocolVersion.v1_17)) {
            boolean flag = var0.dq() instanceof C0FPacketConfirmTransaction;
        }
    };

    public TransactionFixComponent() {
    }
}
