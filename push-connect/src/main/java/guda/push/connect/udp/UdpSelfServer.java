package guda.push.connect.udp;

import guda.push.connect.protocol.api.Command;
import guda.push.connect.protocol.api.Field;
import guda.push.connect.protocol.codec.CodecUtil;
import guda.push.connect.protocol.codec.tlv.TLV;
import guda.push.connect.protocol.codec.tlv.TypeConvert;
import guda.push.connect.queue.MsgFactory;
import guda.push.connect.queue.OnlineInfo;
import org.slf4j.LoggerFactory;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Created by well on 15/3/3.
 */
public class UdpSelfServer implements Runnable{


    private static org.slf4j.Logger logger = LoggerFactory.getLogger(UdpSelfServer.class);

    private DatagramSocket serverDatagramSocket;

    private  void ack(TLV tlv,int fromPort) {
        try {

            byte[] bytes = tlv.toBinary();
            String host = CodecUtil.findTagString(tlv, Field.TO_HOST);
            InetAddress inetAddress = InetAddress.getByName(host);
            //int port = CodecUtil.findTagInt(tlv, Field.TO_PORT);
            logger.info("returen ack: target host:" + host + ",port:" + fromPort);
            java.net.DatagramPacket sendPacket = new java.net.DatagramPacket(bytes, bytes.length, inetAddress,
                    fromPort);
            serverDatagramSocket.send(sendPacket);

        } catch (Exception e) {
            logger.error("", e);
        }
    }

    private int port = 10085;

    public UdpSelfServer(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        try {
            serverDatagramSocket = new DatagramSocket(10085);

            logger.info("1. 服务端启动成功！ 服务端口：" + 10085);
            byte[] buf = new byte[1024];
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            while(true) {
                //从此套接字接收数据报包
                serverDatagramSocket.receive(packet);

                InetAddress address = packet.getAddress();

                TLV tlv = new TLV(packet.getData());

                long userId = CodecUtil.findTagLong(tlv, Field.FROM_USER);
                int fromPort = packet.getPort();
                if (logger.isInfoEnabled()) {
                    logger.info("udp recvive:" + tlv.toString()+"from ip:" + address.getHostAddress()+",from port:"+ fromPort + ",address:" + address.getAddress());
                }
                TLV tag = CodecUtil.findTag(tlv, Field.FROM_PORT);
                if(tag==null) {
                    tlv.add(new TLV(Field.FROM_PORT, TypeConvert.int2byte(fromPort)));
                }else{
                    tag.setValue(TypeConvert.int2byte(fromPort));
                }
                OnlineInfo.online(userId, address.getHostAddress(), fromPort);
                tlv.add(new TLV(Field.FROM_HOST, TypeConvert.string2byte(address.getHostAddress())));

                MsgFactory.addBiz(tlv);
                int cmd = CodecUtil.findTagInt(tlv,Field.CMD);

                if(cmd!= Command.ACK && cmd!=Command.HEARBEAT) {
                    ack(CodecUtil.newACK(tlv), fromPort);
                }
            }
        } catch (Exception e) {
            logger.error("",e);
        }

    }

    public DatagramSocket getServerDatagramSocket() {
        return serverDatagramSocket;
    }
}
