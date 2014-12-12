package guda.push.server.process;

import guda.push.connect.msg.MsgFactory;
import guda.push.connect.protocol.api.Field;
import guda.push.connect.protocol.codec.CodecUtil;
import guda.push.connect.protocol.codec.tlv.TLV;
import guda.push.server.biz.Biz;
import guda.push.server.biz.BizFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by foodoon on 2014/12/13.
 */
public class BizThread implements  Runnable{

    private Logger log = LoggerFactory.getLogger(BizThread.class);

    @Override
    public void run() {
        while(true) {
            try {
                TLV tlv = MsgFactory.takeUdpRoute();
                if(tlv == null){
                    Thread.sleep(1*1000);
                }
                int command = CodecUtil.findTagInt(tlv, Field.CMD);
                Biz biz = BizFactory.getBiz().get(command);
                if(biz == null){
                    log.error("can not find biz process:command:" + command +"," + tlv.toString());
                }
                biz.service(tlv);

            } catch (Exception e) {

            }


        }
    }
}