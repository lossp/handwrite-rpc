package nettyrpc.client.handler;

import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.util.CharsetUtil;
import nettyrpc.connections.RpcFuture;
import nettyrpc.connections.RpcRequest;
import nettyrpc.connections.RpcResponse;

import java.net.SocketAddress;

@ChannelHandler.Sharable
public class ClientCenterHandler extends SimpleChannelInboundHandler<RpcResponse> {

    private volatile Channel channel;
    private SocketAddress socketAddress;

    public SocketAddress getSocketAddress() {
        return socketAddress;
    }

    public Channel getChannel() {
        return this.channel;
    }

    @Override
    public void channelRegistered(ChannelHandlerContext context) throws Exception{
        super.channelRegistered(context);
        this.channel = context.channel();
    }

    @Override
    public void channelActive(ChannelHandlerContext context) {
        this.socketAddress = this.channel.remoteAddress();
        context.writeAndFlush(Unpooled.copiedBuffer("Netty Rocks", CharsetUtil.UTF_8));

    }

    @Override
    public void channelRead0(ChannelHandlerContext context, RpcResponse rpcResponse) {
//        System.out.println("Client received:" + ByteBufUtil.hexDump(in.readBytes(in.readableBytes())));
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext context, Throwable cause) {
        cause.printStackTrace();
        context.close();
    }

    public RpcFuture sendRequest(RpcRequest rpcRequest) {
        return null;
    }

    public void close() {
        this.channel.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
    }

}
