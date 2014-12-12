package guda.push.server.test.udp;

import guda.push.connect.protocol.api.Command;
import guda.push.connect.protocol.api.Field;
import guda.push.connect.protocol.api.Struct;
import guda.push.connect.protocol.codec.CodecUtil;
import guda.push.connect.protocol.codec.tlv.TLV;
import guda.push.connect.protocol.codec.tlv.TypeConvert;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Created by foodoon on 2014/12/13.
 */
public class UdpClient {

    private byte[] buffer = new byte[1024];

    private DatagramSocket ds = null;


    public UdpClient() throws Exception {
        ds = new DatagramSocket();
    }


    public final void setSoTimeout(final int timeout) throws Exception {
        ds.setSoTimeout(timeout);
    }


    public final int getSoTimeout() throws Exception {
        return ds.getSoTimeout();
    }

    public final DatagramSocket getSocket() {
        return ds;
    }


    public final DatagramPacket send(final String host, final int port,
                                     final byte[] bytes) throws IOException {
        DatagramPacket dp = new DatagramPacket(bytes, bytes.length, InetAddress
                .getByName(host), port);
        ds.send(dp);
        return dp;
    }


    public final TLV receive(final String lhost, final int lport)
            throws Exception {
        DatagramPacket dp = new DatagramPacket(buffer, buffer.length);
        ds.receive(dp);
        byte[] d = new byte[dp.getLength()];
        System.arraycopy(dp.getData(),0,d,0,dp.getLength());
        TLV info = new TLV(d);
        return info;
    }


    public final void close() {
        try {
            ds.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public static void main(String[] args) throws Exception {
        UdpClient client = new UdpClient();
        String serverHost = "127.0.0.1";
        int serverPort = 10085;
        TLV tlv = CodecUtil.newTlv(Struct.CHAT);
        tlv.add(new TLV(Field.CMD, TypeConvert.int2byte(Command.CHAT)));
        tlv.add(new TLV(Field.CHAT_CONTENT, TypeConvert.string2byte("test content")));
        tlv.add(new TLV(Field.FROM_USER, TypeConvert.long2byte(1L)));
        tlv.add(new TLV(Field.TO_USER, TypeConvert.long2byte(1L)));
        client.send(serverHost, serverPort, tlv.toBinary());
        while(true) {
            TLV info = client.receive(serverHost, serverPort);
            System.out.println("服务端回应数据：" + info);
            Thread.sleep(1*1000);
        }
    }
}
