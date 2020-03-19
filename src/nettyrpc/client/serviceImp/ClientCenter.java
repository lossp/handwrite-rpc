package nettyrpc.client.serviceImp;

import nettyrpc.client.handler.ClientCenterHandler;
import nettyrpc.client.service.Client;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import nettyrpc.server.service.Hello;
import nettyrpc.server.serviceImp.HelloImp;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;

public class ClientCenter implements Client {
    private final String host;
    private final int port;
    private boolean isRunning;

    public ClientCenter(String host, int port) {
        this.host = host;
        this.port = port;
        this.isRunning = false;
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
                new InvocationHandler() {
                    // TODO helloService属于硬编码，仅仅作为测试需要
                    Hello helloService = new HelloImp();
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        System.out.println("Here we are");
                        System.out.println(method.getDeclaringClass().getName());
                        // TODO helloService 是硬编码，需要通过Response获取到，这里仅仅作为方法尝试
                        return method.invoke(helloService, args);
                    }
                }
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
