package guda.push.connect.protocol.codec;

import guda.push.connect.protocol.PushRequest;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * Created by well on 2014/12/11.
 */
public class PushEncode extends MessageToByteEncoder<PushRequest> {

    @Override
    protected void encode(ChannelHandlerContext ctx, PushRequest msg, ByteBuf out) throws Exception {

    }
}
