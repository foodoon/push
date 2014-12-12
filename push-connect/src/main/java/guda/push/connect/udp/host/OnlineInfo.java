package guda.push.connect.udp.host;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by foodoon on 2014/12/12.
 */
public class OnlineInfo {

    private static Map<Long, HostInfo> online = new HashMap<Long, HostInfo>(1000);

    public static void online(Long userId, String host, int port) {
        HostInfo hostInfo = new HostInfo();
        hostInfo.setHost(host);
        hostInfo.setPort(port);
        online.put(userId, hostInfo);
    }

    public static HostInfo findOnlineInfo(Long userId) {
        return online.get(userId);
    }


    public static void offline(Long userId) {
        online.remove(userId);
    }

}
