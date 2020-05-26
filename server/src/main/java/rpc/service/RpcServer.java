package rpc.service;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;

import java.util.Map;

public interface RpcServer {
    public void start() throws Exception;

    public void close();

}
