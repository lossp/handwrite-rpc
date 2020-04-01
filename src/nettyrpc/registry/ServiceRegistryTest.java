package nettyrpc.registry;

public class ServiceRegistryTest {
    public static void main(String[] args) {
//        ServiceRegistry serviceRegistry = new ServiceRegistry("127.0.0.1:2181");
//        serviceRegistry.register("tellme");

        ServiceDiscovery serviceDiscovery = new ServiceDiscovery("127.0.0.1:2181");
    }
}
