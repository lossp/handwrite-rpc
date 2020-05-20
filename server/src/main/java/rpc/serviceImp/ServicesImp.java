package rpc.serviceImp;

import rpc.service.RpcService;
import rpc.service.Services;


@RpcService(getValue = Services.class)
public class ServicesImp implements Services {
    @Override
    public boolean isSaid() {
        return false;
    }

    @Override
    public String sayHello() {
        return "say hello from services implementation";
    }

    @Override
    public void check() {

    }
}
