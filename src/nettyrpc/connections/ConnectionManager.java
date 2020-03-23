package nettyrpc.connections;

import nettyrpc.client.handler.ClientCenterHandler;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class ConnectionManager {
    private volatile static ConnectionManager connectionManager;

    private CopyOnWriteArrayList<ClientCenterHandler> connectedHandlers = new CopyOnWriteArrayList<>();
    private Map<InetSocketAddress, ClientCenterHandler> connectedServerNodes = new ConcurrentHashMap<>();

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

    public void addHandler(ClientCenterHandler clientCenterHandler) {
        connectedHandlers.add(clientCenterHandler);
        // 需要将具体的客户端地址和提供的服务联系起来
        InetSocketAddress remoteAddress = (InetSocketAddress) clientCenterHandler.getChannel().remoteAddress();
        connectedServerNodes.put(remoteAddress, clientCenterHandler);
        // TODO why would it need to notify all the other threads?
        // TODO Notify()
    }

    // TODO
    public ClientCenterHandler getHandler() {
        return null;
    }
}
