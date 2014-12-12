package guda.push.connect.udp;

import guda.push.connect.udp.handle.UdpHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by well on 2014/12/11.
 */
public class UdpServer implements Runnable{

    private Logger log = LoggerFactory.getLogger(UdpServer.class);

    EventLoopGroup bossGroup = new NioEventLoopGroup();

    private int port = 10085;

    public UdpServer(int port) {
       this.port = port;
    }

    @Override
    public void run() {
        try {
            Bootstrap b = new Bootstrap();
            b.group(bossGroup)
                    .channel(NioDatagramChannel.class)
                    .option(ChannelOption.SO_BROADCAST, true)
                    .handler(new UdpHandler());

            b.bind(port).sync().channel().closeFuture().await();
        } catch (InterruptedException e) {
            log.error("",e);
            throw new RuntimeException(e);
        } finally {

        }
    }
}
