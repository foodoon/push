package guda.push.connect.queue;

import guda.push.connect.protocol.codec.tlv.TLV;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by foodoon on 2014/12/12.
 */
public class WaitAckFactory {

    private static final int MAX_SIZE = 5000;

    static final ConcurrentHashMap<Long, AckTLV> waitMap = new ConcurrentHashMap<Long, AckTLV>(
            MAX_SIZE);
    static final BlockingQueue<AckTLV> waitList = new ArrayBlockingQueue<AckTLV>(MAX_SIZE);


    public static  void add(Long seq,TLV tlv){
        AckTLV ackTLV = new AckTLV(tlv);
        waitList.add(ackTLV);
        waitMap.put(seq,ackTLV);
    }

    public static  void remove(Long seq) {
        AckTLV tlv = waitMap.get(seq);
        if(tlv == null){
            return ;
        }
        waitList.remove(tlv);
    }

    public static  AckTLV poll() {
        try {
            return waitList.poll();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
