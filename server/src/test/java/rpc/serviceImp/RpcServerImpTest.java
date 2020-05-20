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
        RpcServer rpcServer = new RpcServerImp();
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("ioc.xml");
        Map<String, Object> result = rpcServer.setApplicationContext(applicationContext);
        for (Object bean:result.values()) {
            RpcService service = bean.getClass().getAnnotation(RpcService.class);
            String serviceName = service.getValue().getName();
            assertEquals(serviceName, "rpc.service.Services");
        }
    }
}
