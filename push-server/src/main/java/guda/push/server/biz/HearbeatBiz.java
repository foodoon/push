package guda.push.server.biz;

import guda.push.connect.protocol.api.Command;
import guda.push.connect.protocol.api.Field;
import guda.push.connect.protocol.codec.CodecUtil;
import guda.push.connect.protocol.codec.tlv.TLV;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by foodoon on 2014/12/13.
 */
public class HearbeatBiz implements Biz{


    private Logger log = LoggerFactory.getLogger(HearbeatBiz.class);
    public static final int command = Command.HEARBEAT;

    @Override
    public void service(TLV tlv) {
        if(log.isDebugEnabled()){
            long userId = CodecUtil.findTagLong(tlv, Field.FROM_USER);
            String host = CodecUtil.findTagString(tlv, Field.FROM_HOST);
            int port = CodecUtil.findTagInt(tlv, Field.FROM_PORT);
            log.debug("recvive hearbeat:USER:" + userId + ",host:" + host + ",port:" + port);
        }
    }
}
