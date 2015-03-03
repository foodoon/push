package guda.push.server;

import guda.push.connect.udp.UdpSelfServer;
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

        UdpSelfServer udpSelfServer = new UdpSelfServer(10085);
        Thread t = new Thread(udpSelfServer );
        t.start();
        Thread bizThread = new Thread(new BizThread());
        bizThread.setDaemon(true);
        bizThread.start();

        new UdpRetryThread(udpSelfServer.getServerDatagramSocket());

        Thread udpRouterThread = new Thread(new UdpRouterThread(udpSelfServer.getServerDatagramSocket()));
        udpRouterThread.setDaemon(true);
        udpRouterThread.start();

        //t.setDaemon(true);




    }

}
