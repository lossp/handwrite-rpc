package nettyrpc.client.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;
import nettyrpc.connections.RpcFuture;
import nettyrpc.connections.RpcRequest;
import nettyrpc.connections.RpcResponse;

@ChannelHandler.Sharable
public class ClientCenterHandler extends SimpleChannelInboundHandler<RpcResponse> {

    private volatile Channel channel;

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

}
