package nettyrpc.client.proxy;

import nettyrpc.client.handler.ClientCenterHandler;
import nettyrpc.connections.ConnectionManager;
import nettyrpc.connections.RpcFuture;
import nettyrpc.connections.RpcRequest;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.UUID;

public class ObjectProxy<T> implements InvocationHandler {
    private Class<T> clazz;


    public ObjectProxy(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.setRequestId(UUID.randomUUID().toString());
        rpcRequest.setMethodName(method.getName());
        rpcRequest.setClassName(method.getDeclaringClass().getName());
        rpcRequest.setParameters(args);
        System.out.println("=====================");
        System.out.println(proxy.getClass().getName());
        // invoke做进行异步调用server中的现成服务，需要用到Future
        System.out.println(rpcRequest.toString());
        ClientCenterHandler handler = ConnectionManager.getInstance().getHandler();
        RpcFuture rpcFuture = handler.sendRequest(rpcRequest);
        return rpcFuture.get();
    }
}
