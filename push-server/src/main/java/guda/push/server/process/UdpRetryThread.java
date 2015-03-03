package guda.push.server.process;

import guda.push.connect.queue.AckTLV;
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
    private volatile  boolean started = true;

    private DatagramSocket serverDatagramSocket;

    public UdpRetryThread(DatagramSocket ds){
        serverDatagramSocket = ds;
        Thread udpRetryThread = new Thread(this);
        udpRetryThread.setDaemon(true);
        udpRetryThread.start();
    }
    public void stop(){
        started = false;
    }
    @Override
    public void run() {
        while(started) {
            if(log.isDebugEnabled()){
                log.debug("start retry:");
            }
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                AckTLV ackTLV = WaitAckFactory.poll();
                if (ackTLV == null) {
                    return;
                }
                //reset online info
                if(!ackTLV.needRetry()){
                    long seq = CodecUtil.findTagLong(ackTLV.getTlv(),Field.SEQ);
                    WaitAckFactory.add(seq,ackTLV.getTlv());
                    continue;
                }
                long toUser = CodecUtil.findTagLong(ackTLV.getTlv(), Field.TO_USER);
                HostInfo onlineInfo = OnlineInfo.findOnlineInfo(toUser);
                if (onlineInfo == null) {
                    long seq = CodecUtil.findTagLong(ackTLV.getTlv(),Field.SEQ);
                    WaitAckFactory.add(seq,ackTLV.getTlv());
                    continue;
                }

                if(log.isDebugEnabled()){
                    log.debug("retry:" + ackTLV.getTlv() + ",to host:" + onlineInfo.getHost() + ",port:" + onlineInfo.getPort());
                }
                TLV tagPort = CodecUtil.findTag(ackTLV.getTlv(), Field.TO_PORT);
                TLV tagHost = CodecUtil.findTag(ackTLV.getTlv(), Field.TO_HOST);
                tagHost.setValue(TypeConvert.string2byte(onlineInfo.getHost()));
                tagPort.setValue(TypeConvert.int2byte(onlineInfo.getPort()));
                //

                byte[] bytes = ackTLV.getTlv().toBinary();

                InetAddress inetAddress = InetAddress.getByName(onlineInfo.getHost());
                DatagramPacket sendPacket = new DatagramPacket(bytes, bytes.length, inetAddress,
                        onlineInfo.getPort());
                serverDatagramSocket.send(sendPacket);
                long seq = CodecUtil.findTagLong(ackTLV.getTlv(),Field.SEQ);
                WaitAckFactory.add(seq,ackTLV.getTlv());
            } catch (Exception e) {
                log.error("", e);
            }


        }
    }
}
