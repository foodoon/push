package guda.push.connect.protocol;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by foodoon on 2014/12/12.
 */
public class SeqHelper {

    private static AtomicInteger atomicInteger = new AtomicInteger(0);

    public static long next(){
        long base = System.currentTimeMillis();
        return (base<<20)| atomicInteger.getAndIncrement();
    }



}
