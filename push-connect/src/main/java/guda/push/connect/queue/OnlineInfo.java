package guda.push.connect.queue;

import guda.push.connect.udp.host.HostInfo;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by foodoon on 2014/12/12.
 */
public class OnlineInfo {

    private static ConcurrentHashMap<Long, HostInfo> online = new ConcurrentHashMap<Long, HostInfo>(1000);

    private static OpenBitSet onlineSet = new OpenBitSet(1000);

    public static void online(Long userId, String host, int port) {
        HostInfo hostInfo = new HostInfo();
        hostInfo.setHost(host);
        hostInfo.setPort(port);
        online.put(userId, hostInfo);
        onlineSet.set(userId);
    }

    public static HostInfo findOnlineInfo(Long userId) {
        if(onlineSet.fastGet(userId)) {
            return online.get(userId);
        }
        return null;
    }


    public static void offline(Long userId) {
        online.remove(userId);
        onlineSet.clear(userId);
    }

}
