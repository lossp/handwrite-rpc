package nettyrpc.server.service;

public interface Server {
    public void start() throws Exception;

    public void stop();

    public boolean isRunning();

    public int getPort();
}
