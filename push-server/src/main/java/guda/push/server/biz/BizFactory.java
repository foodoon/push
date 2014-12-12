package guda.push.server.biz;

import guda.push.connect.protocol.api.Command;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by foodoon on 2014/12/13.
 */
public class BizFactory {


    private static final Map<Integer,Biz> biz = new HashMap<Integer,Biz>();

    public static Map<Integer, Biz> getBiz() {
        return biz;
    }

    static{
        biz.put(Command.ACK,new AckBiz());
        biz.put(Command.CHAT,new ChatBiz());
    }
}
