package guda.push.server.biz;

import guda.push.connect.queue.WaitAckFactory;
import guda.push.connect.protocol.api.Command;
import guda.push.connect.protocol.api.Field;
import guda.push.connect.protocol.codec.CodecUtil;
import guda.push.connect.protocol.codec.tlv.TLV;

/**
 * Created by foodoon on 2014/12/13.
 */
public class AckBiz implements Biz{

    public static final int command = Command.ACK;

    @Override
    public void service(TLV request) {
        long seq = CodecUtil.findTagLong(request, Field.SEQ);
        WaitAckFactory.remove(seq);
    }
}
