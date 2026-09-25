package com.alan.clients.module.impl.other.clientspoofer;

import com.alan.clients.module.impl.other.ClientSpoofer;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.packet.PacketSendEvent;
import com.alan.clients.value.Mode;
import com.alan.clients.value.impl.StringValue;
import com.alan.clients.util.packet.PacketUtil;
import com.alan.clients.util.tuples.Triple;
import com.alan.clients.module.impl.other.clientspoofer.LabyModPayloadList;
import io.netty.buffer.Unpooled;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.C17PacketCustomPayload;

public class LabyModClientSpoofer extends Mode<ClientSpoofer> {
    public final StringValue spoofedVersionLatestWouldBePreferred = new StringValue("Spoofed Version, Latest would be preferred", this, "4.2.31");
    @EventLink
    public final Listener<PacketSendEvent> onPacketSend = var1x -> {
        if (var1x.dq() instanceof C17PacketCustomPayload) {
            LabyModPayloadList ss = new LabyModPayloadList(this);
            var1x.setCancelled();

            for (Triple ajt : ss) {
                PacketBuffer packetbuffer = new PacketBuffer(Unpooled.buffer());
                if ((Boolean)ajt.getThird()) {
                    packetbuffer.writeString((String)ajt.getSecond());
                } else {
                    packetbuffer.writeBytes(((String)ajt.getSecond()).getBytes());
                }

                PacketUtil.sendNoEvent(new C17PacketCustomPayload((String)ajt.getFirst(), packetbuffer));
            }
        }
    };

    public LabyModClientSpoofer(String var1, ClientSpoofer clientSpoofer) {
        super(var1, clientSpoofer);
    }
}
