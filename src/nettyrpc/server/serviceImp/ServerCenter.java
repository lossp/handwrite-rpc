package nettyrpc.server.serviceImp;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import nettyrpc.server.handler.ServerCenterHandler;
import nettyrpc.server.service.Server;

import java.net.InetSocketAddress;
import java.util.HashMap;

public class ServerCenter implements Server {

    private static final HashMap<String, Class> serviceRegistry = new HashMap<>();

    private static boolean isRunning = false;

    private static int port;

    public ServerCenter(int port) {
        this.port = port;
    }

    public void stop() {
        isRunning = false;
    }

    public void start() throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(group)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(port))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel socketChannel) {
                            socketChannel.pipeline().addLast(new ServerCenterHandler());
                        }
                    });
            ChannelFuture future = bootstrap.bind().sync();
            System.out.println(ServerCenter.class.getName() + " started and listen on " + future.channel().localAddress());
            future.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully().sync();
        }

    }

    public boolean isRunning() {
        return isRunning;
    }

    public int getPort() {
        return port;
    }

    public void register(Class serviceInterface, Class imp) {
        serviceRegistry.put(serviceInterface.getName(), imp);
    }
}
