package com.alan.clients.component.impl.player;

import com.alan.clients.component.Component;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.packet.PacketReceiveEvent;
import com.alan.clients.util.packet.EntityPositionPacket;
import com.alan.clients.util.packet.EntityPositionSnapshot;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.network.Packet;

public class EntityPositionComponent extends Component {
    private Map<Integer, EntityPositionSnapshot> gk = new HashMap<>();
    private final Map<Class<? extends Packet<?>>, EntityPositionPacket> gl = new HashMap<>();
    @EventLink
    public final Listener<PacketReceiveEvent> onPacketReceive = var1 -> {
        if (!var1.isCancelled()) {
            Packet packet = var1.getPacket();
            EntityPositionPacket cc = this.gl.get(packet.getClass());
            if (cc != null) {
                Map map = cc.a(packet, this.gk);
                if (map != null) {
                    this.gk = map;
                }
            }
        }
    };

    public EntityPositionComponent() {
    }

    @Override
    public void aT() {
        for (EntityPositionPacket cc : EntityPositionPacket.values()) {
            this.gl.put(cc.getClazz(), cc);
        }
    }

    public EntityPositionSnapshot i(int var1) {
        return this.gk.get(var1);
    }
}
