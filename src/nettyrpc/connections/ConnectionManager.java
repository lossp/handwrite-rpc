package nettyrpc.connections;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import nettyrpc.client.handler.ClientCenterHandler;
import nettyrpc.client.handler.ClientCenterInitializer;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ConnectionManager {
    private volatile static ConnectionManager connectionManager;
    private ReentrantLock lock = new ReentrantLock();
    private Condition connected = lock.newCondition();
    private long connectTimeoutMillis = 6000;

    // 这部份不是很懂？一种算法？需要理解
    private AtomicInteger roundRobin = new AtomicInteger(0);

    private EventLoopGroup eventLoopGroup = new NioEventLoopGroup(4);
    private static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(10, 10, 600L, TimeUnit.SECONDS,
            new ArrayBlockingQueue<Runnable>(65536));

    private CopyOnWriteArrayList<ClientCenterHandler> connectedHandlers = new CopyOnWriteArrayList<>();
    private Map<InetSocketAddress, ClientCenterHandler> connectedServerNodes = new ConcurrentHashMap<>();
    private volatile boolean isRunning = true;

    public static ConnectionManager getInstance() {
        if (connectionManager == null) {
            synchronized (ConnectionManager.class) {
                // double check before creating one
                if (connectionManager == null) {
                    connectionManager = new ConnectionManager();
                }
            }
        }
        return connectionManager;
    }

    public void updateConnectServer(List<String> allServerAddresses) {
        if (allServerAddresses != null && allServerAddresses.size() > 0) {
            HashSet<InetSocketAddress> newAllServerNodeAddress = new HashSet<>();
            // 检查地址合法性
            for (int i = 0; i < allServerAddresses.size(); i++) {
                String[] params = allServerAddresses.get(i).split(":");
                if (params.length == 2) {
                    String host = params[0];
                    int port = Integer.parseInt(params[1]);
                    final InetSocketAddress socketAddress = new InetSocketAddress(host, port);
                    newAllServerNodeAddress.add(socketAddress);
                }

            }

            //添加新的服务节点
            for (final InetSocketAddress socketAddress:newAllServerNodeAddress) {
                if (!connectedServerNodes.keySet().contains(socketAddress)) {
                    connectServerNode(socketAddress);
                }
            }

            // 关闭以及取消无效服务节点
            for (int i = 0; i < connectedHandlers.size(); i++) {
                ClientCenterHandler clientCenterHandler = connectedHandlers.get(i);
                SocketAddress socketAddress = clientCenterHandler.getSocketAddress();
                if (!newAllServerNodeAddress.contains(socketAddress)) {
                    System.out.println("处理无效节点服务" + socketAddress);
                    ClientCenterHandler handler = connectedServerNodes.get(i);
                    if (handler != null) {
                        handler.close();
                    }
                    connectedServerNodes.remove(socketAddress);
                    connectedHandlers.remove(clientCenterHandler);
                }
            }
        } else {
            System.out.println("All server nodes are down");
            // TODO
        }
    }

    private void connectServerNode(final InetSocketAddress address) {
        threadPoolExecutor.submit(new Runnable() {
            @Override
            public void run() {
                Bootstrap bootstrap = new Bootstrap();
                bootstrap.group(eventLoopGroup)
                        .channel(NioSocketChannel.class)
                        .handler(new ClientCenterInitializer());
                ChannelFuture channelFuture = bootstrap.connect();
                channelFuture.addListener(new ChannelFutureListener() {
                    @Override
                    public void operationComplete(ChannelFuture channelFuture) throws Exception {
                        if (channelFuture.isSuccess()) {
                            System.out.println("Successfully connect to remote server. remote peer = " + address);
                            ClientCenterHandler handler = channelFuture.channel().pipeline().get(ClientCenterHandler.class);
                            addHandler(handler);
                        }

                    }
                });
            }
        });
    }

    private void addHandler(ClientCenterHandler clientCenterHandler) {
        connectedHandlers.add(clientCenterHandler);
        // 需要将具体的客户端地址和提供的服务联系起来
        InetSocketAddress remoteAddress = (InetSocketAddress) clientCenterHandler.getChannel().remoteAddress();
        connectedServerNodes.put(remoteAddress, clientCenterHandler);
        signalAvailableHandlers();
        // TODO why would it need to notify all the other threads?
        // TODO Notify()
    }

    private void signalAvailableHandlers() {
        lock.lock();
        try {
            connected.signalAll();
        } finally {
            lock.unlock();
        }
    }

    private boolean waitingForHandler() throws InterruptedException{
        lock.lock();
        try {
            return connected.await(this.connectTimeoutMillis, TimeUnit.MILLISECONDS);
        } finally {
            lock.unlock();
        }
    }

    // TODO
    public ClientCenterHandler getHandler() {
        int size = connectedHandlers.size();
        // 如果handler队列中没有相应的handler
        while (isRunning & size <= 0) {
            try {
                boolean available = waitingForHandler();
                if (available) {
                    size = connectedHandlers.size();
                }
            } catch (InterruptedException e) {
                System.out.println("cannot connect to any server");
                e.printStackTrace();
            }
        }
        int index = (roundRobin.getAndAdd(1) + size) % size;
        return connectedHandlers.get(index);
    }
}
