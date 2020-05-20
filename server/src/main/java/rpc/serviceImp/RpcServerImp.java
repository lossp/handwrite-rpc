package rpc.serviceImp;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import rpc.service.RpcServer;
import rpc.service.RpcService;

import java.util.HashMap;
import java.util.Map;

public class RpcServerImp implements RpcServer {
    private Map<String, Object> handlerMap = new HashMap<>();

    @Override
    public Map<String, Object> setApplicationContext(ApplicationContext context) throws BeansException {
        return context.getBeansWithAnnotation(RpcService.class);
    }

    @Override
    public void start() throws Exception {

    }

    @Override
    public void close() throws Exception {

    }


}
