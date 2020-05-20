package rpc;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class RpcServerInitializerTest {
    @Test
    public void testClassPathXmlApplicationContext() {
        ApplicationContext context = new ClassPathXmlApplicationContext("ioc.xml");
    }

    @Test
    public void testRegisterShutdownHook() {
        ConfigurableApplicationContext context = new ClassPathXmlApplicationContext("ioc.xml");
        context.registerShutdownHook();
    }
}
