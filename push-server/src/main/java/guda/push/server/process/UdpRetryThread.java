package guda.push.server.process;

import guda.push.connect.queue.WaitAckFactory;
import guda.push.connect.protocol.api.Field;
import guda.push.connect.protocol.codec.CodecUtil;
import guda.push.connect.protocol.codec.tlv.TLV;
import guda.push.connect.protocol.codec.tlv.TypeConvert;
import guda.push.connect.udp.host.HostInfo;
import guda.push.connect.queue.OnlineInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Created by foodoon on 2014/12/12.
 */
public class UdpRetryThread implements Runnable{
    private Logger log = LoggerFactory.getLogger(UdpRetryThread.class);
    @Override
    public void run() {
        while(true) {
            try {
                TLV tlv = WaitAckFactory.take();
                if (tlv == null) {
                    try {
                        Thread.sleep(1 * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                //reset online info
                long toUser = CodecUtil.findTagLong(tlv, Field.TO_USER);
                HostInfo onlineInfo = OnlineInfo.findOnlineInfo(toUser);
                if (onlineInfo == null) {
                    return;
                }
                TLV tagPort = CodecUtil.findTag(tlv, Field.TO_PORT);
                TLV tagHost = CodecUtil.findTag(tlv, Field.TO_HOST);
                tagHost.setValue(TypeConvert.string2byte(onlineInfo.getHost()));
                tagPort.setValue(TypeConvert.int2byte(onlineInfo.getPort()));
                //
                DatagramSocket sendSocket = new DatagramSocket();
                byte[] bytes = tlv.toBinary();

                InetAddress inetAddress = InetAddress.getByName(onlineInfo.getHost());
                DatagramPacket sendPacket = new DatagramPacket(bytes, bytes.length, inetAddress,
                        onlineInfo.getPort());
                sendSocket.send(sendPacket);
                sendSocket.close();
                long seq = CodecUtil.findTagLong(tlv, Field.SEQ);
                WaitAckFactory.add(seq, tlv);
            } catch (Exception e) {
                log.error("", e);
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}
