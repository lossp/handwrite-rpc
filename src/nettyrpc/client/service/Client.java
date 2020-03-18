package nettyrpc.client.service;

public interface Client {
    public void start() throws Exception;

    public void stop();

    public boolean isRunning();

    public int getPort();
}
