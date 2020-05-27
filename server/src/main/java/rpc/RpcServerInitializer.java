package rpc;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class RpcServerInitializer {
    public static void main(String[] args) { new ClassPathXmlApplicationContext("ioc.xml"); }
}
