package com.alan.clients.packet;

import com.alan.clients.packet.CustomPacket;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.PacketBuffer;

public final class IntPayloadPacket extends CustomPacket {
    private int aNt;

    public IntPayloadPacket(int var1) {
        super(0, EnumConnectionState.PLAY);
        this.aNt = var1;
    }

    @Override
    public void readPacketData(PacketBuffer packetBuffer) {
        this.aNt = packetBuffer.readInt();
    }

    @Override
    public void writePacketData(PacketBuffer packetBuffer) {
        packetBuffer.writeInt(this.aNt);
    }

    @Override
    public int uL() {
        return 29;
    }
}
