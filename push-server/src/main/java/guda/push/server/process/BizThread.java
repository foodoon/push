package guda.push.server.process;

import guda.push.connect.queue.MsgFactory;
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
    private volatile  boolean started = true;

    public void stop(){
        started = false;
    }
    @Override
    public void run() {
        while(started) {
            try {
                log.info("staart biz:");
                TLV tlv = MsgFactory.takeBiz();
                if(tlv == null){
                    log.info("take null");
                    Thread.sleep(1*1000);
                }
                int command = CodecUtil.findTagInt(tlv, Field.CMD);
                Biz biz = BizFactory.getBiz().get(command);
                if(biz == null){
                    log.error("can not find biz process:command:" + command +"," + tlv.toString());
                }
                log.info("find biz:" + command + "," + tlv.toString());
                biz.service(tlv);

            } catch (Exception e) {
                log.error("",e);
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}
