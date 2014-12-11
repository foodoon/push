package guda.push.connect.protocol.codec;

import com.alibaba.fastjson.JSONObject;
import guda.push.connect.protocol.PushRequest;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.EmptyByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

import java.nio.charset.CharsetDecoder;

/**
 * Created by well on 2014/12/11.
 */
public class PushDecode extends LengthFieldBasedFrameDecoder {


    public PushDecode(){
        super(Integer.MAX_VALUE, 0, 4, -4, 0);
    }


    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        if (in.readableBytes() == 4) {
            in.markReaderIndex();
            int i = in.readInt();
            if(i==4) {
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

        }
    }

    private PushRequest decode(ByteBuf buff)  {
        // 设置标识
        buff.markReaderIndex();

        int length = buff.readInt();
        PushRequest request = new PushRequest();
        byte[] dataBytes = new byte[length];
        buff.readBytes(dataBytes, 0, length);
        JSONObject.parseObject(dataBytes,0,length);

        return p;
    }
}
