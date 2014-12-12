package guda.push.connect.tcp.handle;

import guda.push.connect.msg.MsgFactory;
import guda.push.connect.protocol.codec.tlv.TLV;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by foodoon on 2014/12/12.
 */
public class TcpHandler extends ChannelInboundHandlerAdapter {

    private Logger log = LoggerFactory.getLogger(TcpHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if(msg instanceof TLV) {
            MsgFactory.addBiz((TLV)msg);
            return ;
        }
        log.error("receive unkown msg!!");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("",cause);
    }
}
