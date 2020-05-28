package rpc.serviceImp;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import rpc.service.RpcServer;
import rpc.service.RpcService;

import static org.junit.Assert.*;

import java.util.Map;

public class RpcServerImpTest {
    @Test
    public void testSetApplicationContext() {
        ApplicationContext context = new ClassPathXmlApplicationContext("ioc.xml");
        context.getBean("rpcServer");
        System.out.println("aaa");
        ((ClassPathXmlApplicationContext)context).close();
//        RpcServer rpcServer = new RpcServerImp("127.0.0.1");
//        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("ioc.xml");
//        Map<String, Object> result = rpcServer.setApplicationContext(applicationContext);
//        for (Object bean:result.values()) {
//            RpcService service = bean.getClass().getAnnotation(RpcService.class);
//            String serviceName = service.getValue().getName();
//            assertEquals(serviceName, "rpc.service.Services");
//        }
    }
}
