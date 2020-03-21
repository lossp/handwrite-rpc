package nettyrpc.client.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;
import nettyrpc.connections.RpcFuture;
import nettyrpc.connections.RpcRequest;

@ChannelHandler.Sharable
public class ClientCenterHandler extends SimpleChannelInboundHandler<ByteBuf> {
    @Override
    public void channelActive(ChannelHandlerContext context) {
        context.writeAndFlush(Unpooled.copiedBuffer("Netty Rocks", CharsetUtil.UTF_8));
    }

    @Override
    public void channelRead0(ChannelHandlerContext context, ByteBuf in) {
        System.out.println("Client received:" + ByteBufUtil.hexDump(in.readBytes(in.readableBytes())));
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext context, Throwable cause) {
        cause.printStackTrace();
        context.close();
    }

    public RpcFuture sendRequest(RpcRequest rpcRequest) {
        return null;

    }

}
