package rpc.handlers;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import valueObject.RpcRequestProto;


public class ServerHandler extends SimpleChannelInboundHandler<RpcRequestProto> {

    @Override
    public void channelRead0(ChannelHandlerContext context, RpcRequestProto request) {

    }

    @Override
    public void channelInactive(ChannelHandlerContext context) {

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext context, Throwable cause) {

    }

}
