package guda.push.connect.client;

import guda.push.connect.protocol.api.Command;
import guda.push.connect.protocol.api.Field;
import guda.push.connect.protocol.api.Struct;
import guda.push.connect.protocol.codec.CodecUtil;
import guda.push.connect.protocol.codec.tlv.TLV;
import guda.push.connect.protocol.codec.tlv.TypeConvert;

/**
 * Created by foodoon on 2015/1/21.
 */
public class ClientTest {

    public static void main(String[] args) throws Exception {
        //192.168.207.104
        UdpServer udpServer = new UdpServer("192.168.207.104",10085,1L,10086);
        Thread server = new Thread(udpServer);
        server.start();

        while(true){
            try {
                Thread.sleep(15 * 1000);
            } catch (Exception e) {

            }
            try {
                TLV tlv = CodecUtil.newTlv(Struct.CHAT);
                tlv.add(new TLV(Field.CMD, TypeConvert.int2byte(Command.CHAT)));
                tlv.add(new TLV(Field.FROM_USER, TypeConvert.long2byte(1)));
                tlv.add(new TLV(Field.FROM_PORT, TypeConvert.long2byte(10086)));
                tlv.add(new TLV(Field.TO_USER, TypeConvert.long2byte(1)));
                tlv.add(new TLV(Field.CHAT_CONTENT, TypeConvert.long2byte(2222)));
                udpServer.send(tlv);
            }catch(Exception e){
                e.printStackTrace();
            }
        }

//        UdpClientImpl impl = new UdpClientImpl("192.168.1.110",10086,1L);
//        Thread t = new Thread(impl);
//        t.start();

    }


}
