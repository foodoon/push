package guda.push.connect.client;

import guda.push.connect.protocol.api.Command;
import guda.push.connect.protocol.api.Field;
import guda.push.connect.protocol.api.Struct;
import guda.push.connect.protocol.codec.CodecUtil;
import guda.push.connect.protocol.codec.tlv.TLV;
import guda.push.connect.protocol.codec.tlv.TypeConvert;

import java.io.IOException;
import java.net.*;

/**
 * Created by foodoon on 2014/12/15.
 */
public class UdpTestServer implements Runnable {

    public volatile boolean started = true;
    InetAddress mInetAddress;
    protected byte[] buffer = new byte[1024];
    static int localPort = 10086;
    protected static String serverHost;
    protected static int serverPort;
    protected static long userId;
    private Hearbeat hearbeat;
    DatagramSocket datagramSocket = null;


    public UdpTestServer(String server_host, int server_port, long user_id, int localPort) {
        this.serverPort = server_port;
        this.serverHost = server_host;
        this.userId = user_id;
        this.localPort = localPort;
        hearbeat = new Hearbeat();
    }

    public void stop(){
        started = false;
        if(hearbeat!=null){
            hearbeat.stop();
        }
    }

    public void startListen() {

        byte[] message = new byte[100];
        try {

            while(datagramSocket == null){
                try {
                   // InetSocketAddress  socketAddress = new InetSocketAddress("192.168.207.104", localPort);
                    datagramSocket = new DatagramSocket(localPort);
                }catch(Exception e){
                    e.printStackTrace();
                    localPort++ ;
                   Thread.sleep(300);
                }
            }
            datagramSocket.setBroadcast(true);
            datagramSocket.setReuseAddress(true);

            System.out.println("start listen on " + localPort);
            try {
                while (started) {
                    DatagramPacket datagramPacket = new DatagramPacket(message,
                            message.length);
                    System.out.println(datagramSocket.getLocalSocketAddress().toString());
                    datagramSocket.receive(datagramPacket);
                    TLV rece = new TLV(datagramPacket.getData());


                    int cmd = CodecUtil.findTagInt(rece,Field.CMD);
                    System.out.println("recv::cmd:" + cmd + ",host:" + datagramPacket.getAddress().getHostAddress()+ rece);
                    if(cmd!= Command.ACK && cmd!=Command.HEARBEAT) {
                        ack(datagramSocket,CodecUtil.newACK(rece), serverPort);
                    }

                    Thread.sleep(300);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public  void send(TLV tlv) {
        if(tlv == null){
            return;
        }
        TLV tag = CodecUtil.findTag(tlv, Field.FROM_PORT);
        if(tag == null){
            tlv.add(new TLV(Field.FROM_PORT, localPort));
        }else{
            tag.setValue(TypeConvert.int2byte(localPort));
        }
        System.out.println("UDP发送数据:" + tlv);

//        DatagramSocket s = null;
//        try {
//            s = new DatagramSocket(serverPort);
//        } catch (SocketException e) {
//            e.printStackTrace();
//        }
        InetAddress server = null;
        try {
            server = InetAddress.getByName(serverHost);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        byte[] messageByte = tlv.toBinary();
        DatagramPacket p = new DatagramPacket(messageByte, messageByte.length, server,
                serverPort);
        try {
System.out.println("send packet:host:" + serverHost + ",port:" + serverPort);
            datagramSocket.send(p);
           // datagramSocket.disconnect();



        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        startListen();
    }

    public class Hearbeat implements Runnable{
        public volatile boolean started = true;
        public Hearbeat(){
            Thread t = new Thread(this);
            t.setDaemon(true);
            t.start();
        }

        public void stop(){
            started = false;
        }

        @Override
        public void run() {
            while(started) {
                try {
                    Thread.sleep(30 * 1000);
                } catch (Exception e) {

                }
                try {
                    TLV tlv = CodecUtil.newTlv(Struct.HEARBEAT);
                    tlv.add(new TLV(Field.CMD, TypeConvert.int2byte(Command.HEARBEAT)));
                    tlv.add(new TLV(Field.FROM_USER, TypeConvert.long2byte(userId)));
                    tlv.add(new TLV(Field.FROM_PORT, TypeConvert.long2byte(localPort)));
                    send(tlv);
                }catch(Exception e){
                    e.printStackTrace();
                }


            }
        }
    }

    private static void ack(DatagramSocket serverDatagramSocket ,TLV tlv,int fromPort) {
        try {

            byte[] bytes = tlv.toBinary();
            String host = CodecUtil.findTagString(tlv, Field.TO_HOST);
            InetAddress inetAddress = InetAddress.getByName(host);
            //int port = CodecUtil.findTagInt(tlv, Field.TO_PORT);
            System.out.println("returen ack: target host:" + host + ",port:" + fromPort);
            java.net.DatagramPacket sendPacket = new java.net.DatagramPacket(bytes, bytes.length, inetAddress,
                    fromPort);
            serverDatagramSocket.send(sendPacket);

        } catch (Exception e) {
            //logger.error("", e);
            e.printStackTrace();
        }
    }


}
