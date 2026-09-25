package de.florianmichael.vialoadingbase.netty;

import com.viaversion.viaversion.api.connection.UserConnection;
import de.florianmichael.vialoadingbase.netty.event.CompressionReorderEvent;
import de.florianmichael.vialoadingbase.netty.handler.VLBViaDecodeHandler;
import de.florianmichael.vialoadingbase.netty.handler.VLBViaEncodeHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public abstract class VLBPipeline extends ChannelInboundHandlerAdapter {
    public static final String VIA_DECODER_HANDLER_NAME = "via-decoder";
    public static final String VIA_ENCODER_HANDLER_NAME = "via-encoder";
    private final UserConnection user;

    public VLBPipeline(UserConnection user) {
        this.user = user;
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        super.handlerAdded(ctx);
        ctx.pipeline().addBefore(this.getDecoderHandlerName(), "via-decoder", this.createVLBViaDecodeHandler());
        ctx.pipeline().addBefore(this.getEncoderHandlerName(), "via-encoder", this.createVLBViaEncodeHandler());
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object var2) throws Exception {
        super.userEventTriggered(ctx, var2);
        if (var2 instanceof CompressionReorderEvent) {
            int i = ctx.pipeline().names().indexOf(this.getDecompressionHandlerName());
            if (i == -1) {
                return;
            }

            if (i > ctx.pipeline().names().indexOf("via-decoder")) {
                ChannelHandler channelhandler = ctx.pipeline().get("via-decoder");
                ChannelHandler channelhandler1 = ctx.pipeline().get("via-encoder");
                ctx.pipeline().remove(channelhandler);
                ctx.pipeline().remove(channelhandler1);
                ctx.pipeline().addAfter(this.getDecompressionHandlerName(), "via-decoder", channelhandler);
                ctx.pipeline().addAfter(this.getCompressionHandlerName(), "via-encoder", channelhandler1);
            }
        }
    }

    public VLBViaDecodeHandler createVLBViaDecodeHandler() {
        return new VLBViaDecodeHandler(this.user);
    }

    public VLBViaEncodeHandler createVLBViaEncodeHandler() {
        return new VLBViaEncodeHandler(this.user);
    }

    public abstract String getDecoderHandlerName();

    public abstract String getEncoderHandlerName();

    public abstract String getDecompressionHandlerName();

    public abstract String getCompressionHandlerName();

    public UserConnection getUser() {
        return this.user;
    }
}
