package nettyrpc.client.controller;

import nettyrpc.client.serviceImp.ClientCenter;

public class ClientController {
    public static void main(String[] args) throws Exception{
        if (args.length != 2) {
            System.err.println("Usage: " + ClientCenter.class.getSimpleName() + "<host> <port>");
            return;
        }

        final String host = args[0];
        final int port = Integer.parseInt(args[1]);

        new ClientCenter(host, port).start();
    }
}
