package server.lossp.service;

import java.io.IOException;

public interface Server {
    public void start() throws IOException;

    public void stop();

    public boolean isRunning();

    public void register(Class serviceInterface, Class imp);

    public int getPort();
}
