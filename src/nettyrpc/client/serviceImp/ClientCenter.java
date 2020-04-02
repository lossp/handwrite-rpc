package nettyrpc.client.serviceImp;

import nettyrpc.client.proxy.ObjectProxy;
import nettyrpc.client.service.Client;
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
