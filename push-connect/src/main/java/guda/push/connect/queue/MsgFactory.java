package guda.push.connect.queue;

import guda.push.connect.protocol.codec.tlv.TLV;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Created by foodoon on 2014/12/12.
 */
public class MsgFactory {

    private static final int MAX_SIZE = 5000;

    static final BlockingQueue<TLV> bizQueue = new ArrayBlockingQueue<TLV>(
            MAX_SIZE);

    static final BlockingQueue<TLV> routeUdpQueue = new ArrayBlockingQueue<TLV>(
            MAX_SIZE);

    static final BlockingQueue<TLV> routeTcpQueue = new ArrayBlockingQueue<TLV>(
            MAX_SIZE);



    public static void addBiz(TLV tlv) {
        bizQueue.add(tlv);
    }

    public static void addUdpRoute(TLV tlv) {
        routeUdpQueue.add(tlv);
    }

    public static void addTcpRoute(TLV tlv) {
        routeTcpQueue.add(tlv);
    }

    public static TLV takeBiz() throws InterruptedException {
        return bizQueue.take();
    }

    public static TLV takeTcpRoute() throws InterruptedException {
        return routeTcpQueue.take();
    }

    public static TLV takeUdpRoute() throws InterruptedException {
        return routeUdpQueue.take();
    }


}
