package guda.push.connect.tcp.handle;

import guda.push.connect.protocol.codec.tlv.TLV;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.EmptyByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * Created by well on 2014/12/11.
 */
public class TcpDecoder extends LengthFieldBasedFrameDecoder {


    public TcpDecoder(){
        super(Integer.MAX_VALUE, 0, 4, -4, 0);
    }


    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        if (in.readableBytes() == 4) {
            in.markReaderIndex();
            int i = in.readInt();
            if(i==4) {
                //心跳包
                return null;
            }else{
                in.resetReaderIndex();
            }
        }
        ByteBuf buff = (ByteBuf) super.decode(ctx, in);
        if (buff == null) {
            return null;
        }

        if (in instanceof EmptyByteBuf) {
            return null;
        } else {
            return decode(buff);
        }
    }

    private TLV decode(ByteBuf buff)  {
        int length = buff.readInt();

        byte[] dataBytes = new byte[length];
        buff.readBytes(dataBytes, 0, length);
        return new TLV(dataBytes);

    }
}
