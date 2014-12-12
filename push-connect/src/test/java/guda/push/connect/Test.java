package guda.push.connect;

import guda.push.connect.protocol.api.Field;
import guda.push.connect.protocol.api.Struct;
import guda.push.connect.protocol.codec.tlv.TLV;
import guda.push.connect.protocol.codec.tlv.TypeConvert;

/**
 * Created by foodoon on 2014/12/12.
 */
public class Test {

    public static void main(String[] args){
        TLV tlv = new TLV(Struct.CHAT, new TLV(Field.SEQ,TypeConvert.long2byte(3L)));
        tlv.add( new TLV(Field.API_VERSION, TypeConvert.int2byte(1)));
        //tlv.add( new TLV(Field.CLIENT_IP, "192.168.7.25".getBytes()));
        tlv.add( new TLV(Field.CMD, TypeConvert.int2byte(2)));
        tlv.add( new TLV(Field.USER_AGENT, "fire fox client".getBytes()));
        tlv.add( new TLV(Field.BODY, "body".getBytes()));
        byte[] bytes = null;
        long start =System.currentTimeMillis();
        for(int i=0;i<10000;++i) {
             bytes = tlv.toBinary();
        }
        System.out.println(System.currentTimeMillis()-start);
        start =System.currentTimeMillis();
        for(int i=0;i<10000;++i) {
            TLV s = new TLV(bytes);
            //new String(s.findTag(PushTag.CLIENT_IP,null).valueAsByteArray());
        }
        System.out.println(System.currentTimeMillis()-start);
//        System.out.println(bytes.length);
//        System.out.println(s.findTag(PushTag.SEQ,null).valueAsInt());
//        System.out.println(s.findTag(PushTag.API_VERSION,null).valueAsInt());
//        System.out.println(s.findTag(PushTag.CMD,null).valueAsInt());
//        System.out.println(new String(s.findTag(PushTag.CLIENT_IP,null).valueAsByteArray()));
//        System.out.println(new String(s.findTag(PushTag.USER_AGENT,null).valueAsByteArray()));
    }
}
