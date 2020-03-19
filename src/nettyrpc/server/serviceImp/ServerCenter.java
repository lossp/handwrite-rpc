package nettyrpc.server.serviceImp;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import nettyrpc.registry.ServiceRegistry;
import nettyrpc.server.handler.ServerCenterHandler;
import nettyrpc.server.service.RpcService;
import nettyrpc.server.service.Server;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

public class ServerCenter implements Server, ApplicationContextAware {

    private static ServiceRegistry serviceRegistry;

    private Map<String, Object> handlerMap = new HashMap<>();

    private static boolean isRunning = false;

    private static int port;


    // 此方法目的获取Beans，不是单单设置applicationContext，更重要是获取Spring boot运行的上下文, 由于server, client registry都是作为
    // bean方式注入，此时作为client的bean需要调用Rpc服务来运行，即Bean依赖于Spring boot容器，因此需要设置相应ApplicationContext
    // 此目的是获取Bean与对应的interface的对应关系，即Map<interface, Bean>
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, Object> serviceBeanMap = applicationContext.getBeansWithAnnotation(RpcService.class);
        for (Object serviceBean:serviceBeanMap.values()) {
            String interfaceName = serviceBean.getClass().getAnnotation(RpcService.class).getValue().getName();
            handlerMap.put(interfaceName, serviceBean);
        }
    }


    public ServerCenter(int port, ServiceRegistry serviceRegistry) {
        this.serviceRegistry = serviceRegistry;
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

}
