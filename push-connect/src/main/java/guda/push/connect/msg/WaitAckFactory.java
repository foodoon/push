package guda.push.connect.msg;

import guda.push.connect.protocol.codec.tlv.TLV;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Created by foodoon on 2014/12/12.
 */
public class WaitAckFactory {

    private static final int MAX_SIZE = 5000;

    static final Map<Long, TLV> waitMap = new HashMap<Long, TLV>(
            MAX_SIZE);
    static final BlockingQueue<TLV> waitList = new ArrayBlockingQueue<TLV>(MAX_SIZE);

    public static synchronized void add(Long seq,TLV tlv){
        waitList.add(tlv);
        waitMap.put(seq,tlv);
    }

    public static synchronized void remove(Long seq) {
        TLV tlv = waitMap.get(seq);
        if(tlv == null){
            return ;
        }
        waitList.remove(tlv);
    }

    public static synchronized TLV take() {
        try {
            return waitList.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
