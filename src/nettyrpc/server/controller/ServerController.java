package nettyrpc.server.controller;

import nettyrpc.registry.ServiceRegistry;
import nettyrpc.server.serviceImp.ServerCenter;

public class ServerController {
    public static void main(String[] args) throws Exception{
        if (args.length != 1) {
            System.err.println("Usage:" + ServerCenter.class.getSimpleName());
            return;
        }
        int port = Integer.parseInt(args[0]);
        ServiceRegistry serviceRegistry = new ServiceRegistry();
        new ServerCenter(port, serviceRegistry).start();
    }
}
