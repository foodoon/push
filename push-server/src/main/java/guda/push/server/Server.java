package guda.push.server;

import guda.push.connect.udp.UdpServer;
import guda.push.server.process.BizThread;
import guda.push.server.process.UdpRetryThread;
import guda.push.server.process.UdpRouterThread;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.concurrent.Executors;

/**
 * Created by well on 2014/12/11.
 */
public class Server {

    public static void main(String[] args) {
        UdpServer udpServer = new UdpServer(10085);
        Thread bizThread = new Thread(new BizThread());
        bizThread.setDaemon(true);
        bizThread.start();

        Thread udpRetryThread = new Thread(new UdpRetryThread());
        udpRetryThread.setDaemon(true);
        udpRetryThread.start();

        Thread udpRouterThread = new Thread(new UdpRouterThread());
        udpRouterThread.setDaemon(true);
        udpRouterThread.start();



    }

}
