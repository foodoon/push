package guda.push.server.process;

import guda.push.connect.msg.MsgFactory;
import guda.push.connect.msg.WaitAckFactory;
import guda.push.connect.protocol.api.Field;
import guda.push.connect.protocol.codec.CodecUtil;
import guda.push.connect.protocol.codec.tlv.TLV;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Created by foodoon on 2014/12/12.
 */
public class UdpRouterThread implements Runnable {


    private Logger log = LoggerFactory.getLogger(UdpRouterThread.class);

    @Override
    public void run() {

        try {
            TLV tlv = MsgFactory.takeUdpRoute();
            DatagramSocket sendSocket = new DatagramSocket();
            byte[] bytes = tlv.toBinary();
            String host = CodecUtil.findTagString(tlv, Field.TO_HOST);
            InetAddress inetAddress = InetAddress.getByName(host);
            int port = CodecUtil.findTagInt(tlv, Field.TO_PORT);
            DatagramPacket sendPacket = new DatagramPacket(bytes, bytes.length, inetAddress,
                    port);
            sendSocket.send(sendPacket);
            sendSocket.close();
            long seq = CodecUtil.findTagLong(tlv,Field.SEQ);
            WaitAckFactory.add(seq,tlv);
        } catch (Exception e) {
            log.error("", e);
        }

    }
}