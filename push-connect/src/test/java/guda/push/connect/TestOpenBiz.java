package guda.push.connect;

import guda.push.connect.queue.OpenBitSet;

/**
 * Created by foodoon on 2014/12/13.
 */
public class TestOpenBiz {

    public static void main(String args[]){
        OpenBitSet openBitSet = new OpenBitSet();
        openBitSet.set(1000L);
        System.out.println(openBitSet.fastGet(1000L));
        System.out.println(openBitSet.fastGet(1001L));
    }
}
