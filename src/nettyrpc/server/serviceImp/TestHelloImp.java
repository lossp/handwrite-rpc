package nettyrpc.server.serviceImp;

import nettyrpc.server.service.RpcService;

import java.lang.annotation.Annotation;
import java.lang.reflect.Proxy;

public class TestHelloImp {
    public static void main(String[] args) {
        HelloImp helloImp = new HelloImp();
        try {
            Annotation service = helloImp.getClass().getAnnotation(RpcService.class);
            RpcService rpcService = (RpcService) service;
            System.out.println(rpcService.getValue());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
