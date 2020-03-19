package nettyrpc.server.serviceImp;

import nettyrpc.server.service.Hello;
import nettyrpc.server.service.RpcService;


@RpcService(getValue = Hello.class)
public class HelloImp implements Hello {
    @Override
    public String sayHi() {
        return "Hello a";
    }
}
