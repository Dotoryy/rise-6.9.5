package com.alan.clients.newevent.impl.packet;

import com.alan.clients.newevent.CancellableEvent;
import com.alan.clients.newevent.Event;
import com.alan.clients.script.api.wrapper.impl.event.ScriptEvent;
import com.alan.clients.script.api.wrapper.impl.event.impl.ScriptPacketSendEvent;
import lombok.Generated;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;

public final class PacketSendEvent extends CancellableEvent {
    private Packet<?> packet;
    private NetworkManager jS;

    @Override
    public ScriptEvent<? extends Event> getScriptEvent() {
        return new ScriptPacketSendEvent(this);
    }

    @Generated
    public Packet<?> dq() {
        return this.packet;
    }

    @Generated
    public NetworkManager getNetworkManager() {
        return this.jS;
    }

    @Generated
    public void setPacket(Packet<?> packet) {
        this.packet = packet;
    }

    @Generated
    public void setNetworkManager(NetworkManager networkManager) {
        this.jS = networkManager;
    }

    @Generated
    public PacketSendEvent(Packet<?> packet, NetworkManager networkManager) {
        this.packet = packet;
        this.jS = networkManager;
    }
}
