package nettyrpc.constant;

public class Constant {
    private static int ZOOKEEPER_SESSION_TIMEOUT = 5000;
    private static String ZOOKEEPER_REGISTRY_PATH = "/registry";
    private static String ZOOKEEPER_DATA_PATH = ZOOKEEPER_REGISTRY_PATH + "/data";

    public static int getZOOKEEPER_SESSION_TIMEOUT() {
        return ZOOKEEPER_SESSION_TIMEOUT;
    }
    public static String getZookeeperDataPath() { return ZOOKEEPER_DATA_PATH; }
    public static String getZookeeperRegistryPath() { return ZOOKEEPER_REGISTRY_PATH; }
}
