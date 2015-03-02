package guda.push.server.test.udp;

import guda.push.connect.protocol.codec.tlv.TLV;

import java.io.IOException;
import java.net.*;

/**
 * Created by foodoon on 2014/12/13.
 */
public class UdpServer {

    private byte[] buffer = new byte[1024];

    private DatagramSocket ds = null;

    private DatagramPacket packet = null;

    private InetSocketAddress socketAddress = null;

    private String orgIp;


    public UdpServer(String host, int port) throws Exception {
        socketAddress = new InetSocketAddress(host, port);
        ds = new DatagramSocket(socketAddress);
        System.out.println("服务端启动!");
    }

    public final String getOrgIp() {
        return orgIp;
    }


    public final void setSoTimeout(int timeout) throws Exception {
        ds.setSoTimeout(timeout);
    }

    public final int getSoTimeout() throws Exception {
        return ds.getSoTimeout();
    }


    public final void bind(String host, int port) throws SocketException {
        socketAddress = new InetSocketAddress(host, port);
        ds = new DatagramSocket(socketAddress);
    }



    public final TLV receive() throws IOException {
        packet = new DatagramPacket(buffer, buffer.length);
        ds.receive(packet);
        orgIp = packet.getAddress().getHostAddress();
        TLV info = new TLV(packet.getData());
        System.out.println("接收信息：" + info.toString());
        return info;
    }


    public final void response(TLV info) throws IOException {
        System.out.println("客户端地址 : " + packet.getAddress().getHostAddress()
                + ",端口：" + packet.getPort());
        DatagramPacket dp = new DatagramPacket(buffer, buffer.length, packet
                .getAddress(), packet.getPort());
        dp.setData(info.toBinary());
        ds.send(dp);
    }


    public final void setLength(int bufsize) {
        packet.setLength(bufsize);
    }


    public final InetAddress getResponseAddress() {
        return packet.getAddress();
    }


    public final int getResponsePort() {
        return packet.getPort();
    }


    public final void close() {
        try {
            ds.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public static void main(String[] args) throws Exception {
        String serverHost = "127.0.0.1";
        int serverPort = 10085;
        UdpServer udpServerSocket = new UdpServer(serverHost, serverPort);
        while (true) {
            TLV receive = udpServerSocket.receive();
            udpServerSocket.response(receive);
            Thread.sleep(1*1000);

        }
    }
}
