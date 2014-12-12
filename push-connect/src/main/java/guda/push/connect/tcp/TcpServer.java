package guda.push.connect.tcp;

import guda.push.connect.tcp.handle.TcpDecoder;
import guda.push.connect.tcp.handle.TcpEncoder;
import guda.push.connect.tcp.handle.TcpHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by well on 2014/12/11.
 */
public class TcpServer {

    private Logger log = LoggerFactory.getLogger(TcpServer.class);

    public TcpServer(int port){
        try {
            EventLoopGroup bossGroup = new NioEventLoopGroup();
            EventLoopGroup workerGroup = new NioEventLoopGroup(1);
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024).childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline entries = ch.pipeline();
                            entries.addLast(new TcpDecoder());
                            entries.addLast(new TcpEncoder());
                            entries.addLast(new TcpHandler());
                        }
                    });
            ChannelFuture f = b.bind(port).sync();
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            log.error("",e);
            throw new RuntimeException(e);
        } finally {

        }
    }
}
