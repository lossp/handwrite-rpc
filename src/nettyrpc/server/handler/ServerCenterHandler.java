package nettyrpc.server.handler;

import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import nettyrpc.connections.RpcRequest;

// 这里使用SimpleChannelInboundHandler<I>，是因为SimpleChannelInboundHandler<I>已经指定了相应的消息类型，即I，
// 原书中是这样写到 To create such a channelHandler, you need only extend the base class SimpleChannelInboundHandler<I>, where I is the Java type of the message you want to process
@ChannelHandler.Sharable
public class ServerCenterHandler extends SimpleChannelInboundHandler<RpcRequest> {
    @Override
    public void channelRead0(ChannelHandlerContext context, RpcRequest rpcRequest) {

    }

    @Override
    public void channelRead(ChannelHandlerContext context, Object message) {
        System.out.println("Server received:" + message);
        context.write(message);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext context) {
        context.writeAndFlush(Unpooled.EMPTY_BUFFER)
                .addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext context, Throwable cause) {
        cause.printStackTrace();
        context.close();
    }
}
