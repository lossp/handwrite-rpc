package server.lossp.serviceImp;

import server.lossp.service.Server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerCenter implements Server {
    private static ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    private static final HashMap<String, Class> serviceRegistry = new HashMap<>();

    private static boolean isRunning = false;

    private static int port;

    public ServerCenter(int port) {
        this.port = port;
    }

    public void stop() {
        isRunning = false;
        executorService.shutdown();
    }

    public void start() throws IOException {
        ServerSocket serverSocket = new ServerSocket();
        serverSocket.bind(new InetSocketAddress(port));
        System.out.println("start server");

        try {
            while (true) {
                executorService.execute(new ServiceTask(serverSocket.accept()));
            }
        } finally {
            serverSocket.close();
        }
    }

    public boolean isRunning() {
        return isRunning;
    }

    public int getPort() {
        return port;
    }

    public void register(Class serviceInterface, Class imp) {
        serviceRegistry.put(serviceInterface.getName(), imp);
    }
}
