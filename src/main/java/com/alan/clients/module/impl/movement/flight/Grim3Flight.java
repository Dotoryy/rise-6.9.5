package com.alan.clients.module.impl.movement.flight;

import com.alan.clients.module.impl.movement.Flight;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PostMotionEvent;
import com.alan.clients.newevent.impl.motion.PreUpdateEvent;
import com.alan.clients.value.Mode;
import com.viaversion.viarewind.protocol.v1_9to1_8.Protocol1_9To1_8;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import com.viaversion.viaversion.api.type.Types;
import com.viaversion.viaversion.protocols.v1_8to1_9.packet.ServerboundPackets1_9;
import de.florianmichael.vialoadingbase.ViaLoadingBase;

public class Grim3Flight extends Mode<Flight> {
    private int airTicks;
    @EventLink
    public final Listener<PreUpdateEvent> onPreUpdate = var1x -> {
        if (!aEg.thePlayer.onGround) {
            this.airTicks++;
        } else {
            this.airTicks = 0;
        }
    };
    @EventLink
    public final Listener<PostMotionEvent> onPostMotion = var1x -> {
        if (!aEg.thePlayer.onGround) {
            this.sendStartFallFlying();
        }
    };

    public Grim3Flight(String var1, Flight flight) {
        super(var1, flight);
    }

    @Override
    public void onEnable() {
        this.airTicks = 0;
        super.onEnable();
    }

    @Override
    public void onDisable() {
        this.airTicks = 0;
        super.onDisable();
    }

    private void sendStartFallFlying() {
        if (ViaLoadingBase.getInstance().getTargetVersion().newerThanOrEqualTo(ProtocolVersion.v1_9)) {
            UserConnection userconnection = Via.getManager().getConnectionManager().getConnections().iterator().next();
            PacketWrapper packetwrapper = PacketWrapper.create(ServerboundPackets1_9.PLAYER_COMMAND, userconnection);
            packetwrapper.write(Types.VAR_INT, aEg.thePlayer.getEntityId());
            packetwrapper.write(Types.VAR_INT, 8);
            packetwrapper.write(Types.VAR_INT, 0);
            packetwrapper.sendToServer(Protocol1_9To1_8.class);
        }
    }
}
