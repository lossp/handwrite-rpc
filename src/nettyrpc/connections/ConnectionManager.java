package nettyrpc.connections;

import nettyrpc.client.handler.ClientCenterHandler;

public class ConnectionManager {
    private volatile static ConnectionManager connectionManager;

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

    public ClientCenterHandler getHandler() {
        return null;
    }
}
