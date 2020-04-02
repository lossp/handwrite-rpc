package nettyrpc.client.handler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

public class ClientCenterInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) {
        ChannelPipeline channelPipeline = socketChannel.pipeline();
        channelPipeline.addLast(new RpcEncoder());
        channelPipeline.addLast(new RpcDecoder());
        channelPipeline.addLast(new RpcRequestHandler());
    }

}
