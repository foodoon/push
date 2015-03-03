package guda.push.server.process;

import guda.push.connect.queue.MsgFactory;
import guda.push.connect.queue.WaitAckFactory;
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

    private volatile  boolean started = true;
    private DatagramSocket serverDatagramSocket;

    public UdpRouterThread(DatagramSocket ds){
        serverDatagramSocket = ds;
    }

    public void stop(){
        started = false;
    }
    @Override
    public void run() {
        while(started) {
            try {
                TLV tlv = MsgFactory.takeUdpRoute();

                byte[] bytes = tlv.toBinary();
                String host = CodecUtil.findTagString(tlv, Field.TO_HOST);
                InetAddress inetAddress = InetAddress.getByName(host);
                int port = CodecUtil.findTagInt(tlv, Field.TO_PORT);
                DatagramPacket sendPacket = new DatagramPacket(bytes, bytes.length, inetAddress,
                        port);
                serverDatagramSocket.send(sendPacket);

                long seq = CodecUtil.findTagLong(tlv, Field.SEQ);
                if(log.isDebugEnabled()){
                    log.debug("send packet to host:" + host + ",port" + port + ",add wait ack seq:" + seq + ",d:"+ tlv);
                }
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
