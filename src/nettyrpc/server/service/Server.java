package nettyrpc.server.service;

public interface Server {
    public void start() throws Exception;

    public void stop();

    public boolean isRunning();

    public String getAddress();

    public <T>T addService(Class<T> obj, String interfaceName);
}
