package guda.push.connect.udp.handle;

import guda.push.connect.msg.MsgFactory;
import guda.push.connect.protocol.api.Field;
import guda.push.connect.protocol.codec.CodecUtil;
import guda.push.connect.protocol.codec.tlv.TLV;
import guda.push.connect.protocol.codec.tlv.TypeConvert;
import guda.push.connect.udp.host.OnlineInfo;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;

/**
 * Created by well on 2014/12/11.
 */
public class UdpHandler extends SimpleChannelInboundHandler<DatagramPacket> {

    private Logger log = LoggerFactory.getLogger(UdpHandler.class);

    @Override
    public void channelRead0(ChannelHandlerContext ctx, DatagramPacket packet) throws Exception {

        ByteBuf content = packet.content();
        //from
        InetSocketAddress sender = packet.sender();
        TLV tlv = new TLV(content.array());
        if (log.isInfoEnabled()) {
            log.info("udp recvive:" + tlv.toString());
        }
        long userId = CodecUtil.findTagLong(tlv, Field.FROM_USER);
        OnlineInfo.online(userId,sender.getHostName(),sender.getPort());
        tlv.add(new TLV(Field.FROM_HOST, TypeConvert.string2byte(sender.getHostName())));
        tlv.add(new TLV(Field.FROM_PORT, TypeConvert.int2byte(sender.getPort())));
        MsgFactory.addBiz(tlv);
        ack(CodecUtil.newACK(tlv));

    }

    private void ack(TLV tlv) {
        try {
            DatagramSocket sendSocket = new DatagramSocket();
            byte[] bytes = tlv.toBinary();
            String host = CodecUtil.findTagString(tlv, Field.TO_HOST);
            InetAddress inetAddress = InetAddress.getByName(host);
            int port = CodecUtil.findTagInt(tlv, Field.TO_PORT);
            java.net.DatagramPacket sendPacket = new java.net.DatagramPacket(bytes, bytes.length, inetAddress,
                    port);
            sendSocket.send(sendPacket);
            sendSocket.close();
        } catch (Exception e) {
            log.error("", e);
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        //ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("", cause);
    }
}
