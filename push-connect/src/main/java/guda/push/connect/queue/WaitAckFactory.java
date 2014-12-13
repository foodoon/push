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

    static final ConcurrentHashMap<Long, TLV> waitMap = new ConcurrentHashMap<Long, TLV>(
            MAX_SIZE);
    static final BlockingQueue<TLV> waitList = new ArrayBlockingQueue<TLV>(MAX_SIZE);

    public static  void add(Long seq,TLV tlv){
        waitList.add(tlv);
        waitMap.put(seq,tlv);
    }

    public static  void remove(Long seq) {
        TLV tlv = waitMap.get(seq);
        if(tlv == null){
            return ;
        }
        waitList.remove(tlv);
    }

    public static  TLV take() {
        try {
            return waitList.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
