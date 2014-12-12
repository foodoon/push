package guda.push.connect.protocol.codec;

import guda.push.connect.protocol.SeqHelper;
import guda.push.connect.protocol.api.Command;
import guda.push.connect.protocol.api.Field;
import guda.push.connect.protocol.api.Struct;
import guda.push.connect.protocol.codec.tlv.TLV;
import guda.push.connect.protocol.codec.tlv.Tag;
import guda.push.connect.protocol.codec.tlv.TypeConvert;

/**
 * Created by well on 2014/12/11.
 */
public class CodecUtil {


      public static TLV newACK(TLV tlv){
          long seq = findTagLong(tlv, Field.SEQ);
          TLV ack = new TLV(Struct.ACK, new TLV(Field.SEQ, TypeConvert.long2byte(seq)));
          ack.add(new TLV(Field.CMD,TypeConvert.short2byte(Command.ACK)));
          String fromHost = findTagString(tlv, Field.FROM_HOST);
          ack.add(new TLV(Field.TO_HOST,TypeConvert.string2byte(fromHost)));
          int fromPort = findTagInt(tlv, Field.FROM_PORT);
          ack.add(new TLV(Field.TO_PORT,TypeConvert.int2byte(fromPort)));
          return ack;
      }


    public static TLV newTlv(Tag struct){
        return new TLV(struct, new TLV(Field.SEQ, TypeConvert.long2byte(SeqHelper.next())));
    }


    public static int findTagInt(TLV tlv,Tag tag){
        if(tlv == null || tag == null){
            return 0;
        }
        TLV t = tlv.findTag(tag, null);
        if(t == null){
            return 0;
        }
        return t.valueAsInt();
    }

    public static TLV findTag(TLV tlv,Tag tag){
        if(tlv == null || tag == null){
            return null;
        }
        return tlv.findTag(tag, null);
    }

    public static String findTagString(TLV tlv,Tag tag){
        if(tlv == null || tag == null){
            return null;
        }
        TLV t = tlv.findTag(tag, null);
        if(t == null){
            return null;
        }
        return new String(t.valueAsByteArray());
    }

    public static long findTagLong(TLV tlv,Tag tag){
        if(tlv == null || tag == null){
            return 0;
        }
        TLV t = tlv.findTag(tag, null);
        if(t == null){
            return 0;
        }
        return t.valueAsLong();
    }

}
