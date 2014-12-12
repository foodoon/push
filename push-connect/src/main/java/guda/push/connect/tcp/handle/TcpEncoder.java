package guda.push.connect.tcp.handle;

import guda.push.connect.protocol.codec.tlv.TLV;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * Created by well on 2014/12/11.
 */
public class TcpEncoder extends MessageToByteEncoder<TLV> {

    @Override
    protected void encode(ChannelHandlerContext ctx, TLV tlv, ByteBuf out) throws Exception {
        if (tlv == null) {
            return;
        }
        out.writeBytes(tlv.toBinary());
    }
}
