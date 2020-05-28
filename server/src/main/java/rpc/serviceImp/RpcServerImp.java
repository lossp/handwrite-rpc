package rpc.serviceImp;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import rpc.handlers.ServerHandlerInitializer;
import rpc.service.RpcServer;
import rpc.service.RpcService;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

public class RpcServerImp implements RpcServer, InitializingBean, ApplicationContextAware {
    private Map<String, Object> handlerMap = new HashMap<>();

    private final EventLoopGroup boss = new NioEventLoopGroup();
    private final EventLoopGroup worker = new NioEventLoopGroup();

    private String address;
    private int port;


    public RpcServerImp(String address, int port) {
        this.address = address;
        this.port = port;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        start();
    }

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        Map<String, Object> beanMap = context.getBeansWithAnnotation(RpcService.class);
        if (!beanMap.isEmpty()) {
            for (Object bean:beanMap.values()) {
                String interfaceName = bean.getClass().getAnnotation(RpcService.class).getValue().getName();
                System.out.println("Loading the interface: " + interfaceName);
                handlerMap.put(interfaceName, bean);
            }
        }
    }

    @Override
    public void start() throws Exception {
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(boss, worker)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(port))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel socketChannel) {
                            socketChannel.pipeline().addLast(new ServerHandlerInitializer());
                        }
                    });
            ChannelFuture future = bootstrap.bind().sync();
            System.out.println(RpcServerImp.class.getName() + " is started and listening on " + future.channel().localAddress());
            future.channel().closeFuture().sync();
        } finally {
            System.out.println("Netty server start up error");
        }
    }

    @Override
    public void close() {
        if (boss != null) {
            boss.shutdownGracefully();
        }
        if (worker != null) {
            worker.shutdownGracefully();
        }
    }

}
