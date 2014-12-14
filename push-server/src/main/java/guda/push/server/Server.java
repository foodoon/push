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
        Thread bizThread = new Thread(new BizThread());
        bizThread.setDaemon(true);
        bizThread.start();



        Thread udpRouterThread = new Thread(new UdpRouterThread());
        udpRouterThread.setDaemon(true);
        udpRouterThread.start();
        Thread t = new Thread( new UdpServer(10085));
        t.setDaemon(true);
        t.start();

        try {
            Thread.sleep(3*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

}
