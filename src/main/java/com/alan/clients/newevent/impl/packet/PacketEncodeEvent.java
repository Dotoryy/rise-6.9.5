package com.alan.clients.newevent.impl.packet;

import com.alan.clients.newevent.CancellableEvent;
import io.netty.buffer.ByteBuf;
import lombok.Generated;

public final class PacketEncodeEvent extends CancellableEvent {
    private ByteBuf jT;
    private int id;

    @Generated
    public ByteBuf getByteBuf() {
        return this.jT;
    }

    @Generated
    public int getId() {
        return this.id;
    }

    @Generated
    public void setByteBuf(ByteBuf byteBuf) {
        this.jT = byteBuf;
    }

    @Generated
    public void setId(int var1) {
        this.id = var1;
    }

    @Generated
    public PacketEncodeEvent(ByteBuf byteBuf, int var2) {
        this.jT = byteBuf;
        this.id = var2;
    }
}
