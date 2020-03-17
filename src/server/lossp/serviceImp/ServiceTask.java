package server.lossp.serviceImp;

import java.net.Socket;

public class ServiceTask implements Runnable {
    private final Socket client;

    public ServiceTask(Socket client) {
        this.client = client;
    }

    @Override
    public void run() {
        //TODO 将客户端发送的码流反序列化成对象，反射调用服务实现者，获取执行结果
        //TODO 将函数参数，具体方法名称传输
    }
}
