package nettyrpc.server.controller;

import nettyrpc.server.serviceImp.HelloImp;
import nettyrpc.server.serviceImp.ServerCenter;

public class ServerController {
    public static void main(String[] args){
        String address = "127.0.0.1:3333";
        ServerCenter serverCenter = new ServerCenter(address);
        HelloImp helloImp = new HelloImp();
        serverCenter.addService(helloImp.getClass(), "Hello");
        try {
            System.out.println("启动中...");
            serverCenter.start();
            // start之后，都会进行相应阻塞
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
