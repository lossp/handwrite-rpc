package nettyrpc.client.serviceImp;

import nettyrpc.client.handler.ClientCenterHandler;
import nettyrpc.client.proxy.ObjectProxy;
import nettyrpc.client.service.Client;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import nettyrpc.registry.ServiceDiscovery;

import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;

public class ClientCenter implements Client {
    private String host;
    private int port;
    private boolean isRunning;
    private String address;
    private ServiceDiscovery serviceDiscovery;

    public ClientCenter(String host, int port) {
        this.host = host;
        this.port = port;
        this.isRunning = false;
        this.address = host + ":" + port;
    }
    public ClientCenter(ServiceDiscovery serviceDiscovery) {
        this.serviceDiscovery = serviceDiscovery;
    }

    public void start() throws Exception{
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .remoteAddress(new InetSocketAddress(host, port))
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel socketChannel) throws Exception{
                            socketChannel.pipeline().addLast(new ClientCenterHandler());
                        }
                    });
            ChannelFuture future = bootstrap.connect().sync();
            future.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully().sync();
        }
    }

    public static <T> T create(Class<T> interfaceClass) {
        return (T) Proxy.newProxyInstance(
                interfaceClass.getClassLoader(),
                new Class<?>[]{interfaceClass},
                new ObjectProxy<>(interfaceClass)
        );
    }

    public void stop() {

    }

    public boolean isRunning() {
        return isRunning;
    }

    public int getPort() {
        return port;
    }
}
