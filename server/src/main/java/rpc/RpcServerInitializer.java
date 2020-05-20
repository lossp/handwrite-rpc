package rpc;

import rpc.handlers.HandlerTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import rpc.service.RpcServer;
import rpc.serviceImp.RpcServerImp;

public class RpcServerInitializer {
    public static void main(String[] args) {
        String[] locations = {"ioc.xml"};
        RpcServer rpcServer = new RpcServerImp();

        // 基于注解的方式来创建Spring IoC容器
        ApplicationContext context = new ClassPathXmlApplicationContext("ioc.xml");
        rpcServer.setApplicationContext(context);

        HandlerTest handlerTest = (HandlerTest) context.getBean("HandlerTest");
        System.out.println(handlerTest.getName());
    }
}
