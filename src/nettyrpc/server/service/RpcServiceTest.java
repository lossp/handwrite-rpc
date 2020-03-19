package nettyrpc.server.service;

import nettyrpc.client.serviceImp.ClientCenter;

public class RpcServiceTest {
   public static void main(String[] args) {
       Hello helloService = ClientCenter.create(Hello.class);
       String result = helloService.sayHi();
       System.out.println("aaaaaaa" + result);
   }
}
