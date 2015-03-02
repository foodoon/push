package guda.push.connect.client;

/**
 * Created by foodoon on 2015/1/21.
 */
public class ClientTest {

    public static void main(String[] args) throws Exception {
        Thread server = new Thread(new UdpServer("192.168.7.250",10086,1L,10085));
        server.start();

        UdpClientImpl impl = new UdpClientImpl("192.168.7.250",10085,1L);
        Thread t = new Thread(impl);
        t.start();

    }


}
