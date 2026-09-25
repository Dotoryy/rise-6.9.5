package de.florianmichael.vialoadingbase.netty.handler;

import com.alan.clients.Client;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.type.types.VarIntType;
import com.viaversion.viaversion.exception.CancelCodecException;
import com.viaversion.viaversion.exception.CancelEncoderException;
import com.viaversion.viaversion.util.PipelineUtil;
import com.alan.clients.newevent.impl.packet.PacketEncodeEvent;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import java.util.List;

@Sharable
public class VLBViaEncodeHandler extends MessageToMessageEncoder<ByteBuf> {
    private final UserConnection user;
    private int lastTick = -1;
    private boolean actionPerformedThisTick = false;

    public VLBViaEncodeHandler(UserConnection user) {
        this.user = user;
    }

    protected void encode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> var3) {
        if (!this.user.checkOutgoingPacket()) {
            throw CancelEncoderException.generate(null);
        }

        if (!this.user.shouldTransformPacket()) {
            var3.add(byteBuf.retain());
        } else {
            int i = new VarIntType().readPrimitive(byteBuf);
            ByteBuf bytebuf = byteBuf.readerIndex(0);
            PacketEncodeEvent fj = new PacketEncodeEvent(bytebuf, i);
            Client.a.e().d(fj);
            if (!fj.isCancelled()) {
                ByteBuf bytebuf1 = ctx.alloc().buffer().writeBytes(bytebuf);

                try {
                    this.user.transformOutgoing(bytebuf1, CancelEncoderException::generate);
                    var3.add(bytebuf1.retain());
                } finally {
                    bytebuf1.release();
                }
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable t) throws Exception {
        if (!PipelineUtil.containsCause(t, CancelCodecException.class)) {
            super.exceptionCaught(ctx, t);
        }
    }
}
