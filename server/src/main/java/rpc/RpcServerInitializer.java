package rpc;

import rpc.handlers.HandlerTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class RpcServerInitializer {
    public static void main(String[] args) {
        String[] locations = {"ioc.xml"};
        // 基于注解的方式来创建Spring IoC容器
        ApplicationContext context = new ClassPathXmlApplicationContext("ioc.xml");
        HandlerTest handlerTest = (HandlerTest) context.getBean("HandlerTest");
        System.out.println(handlerTest.getName());
    }
}
