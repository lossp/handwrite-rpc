package rpc.handlers;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import valueObject.RpcRequestProto;

public class ServerHandlerInitializer extends ChannelInitializer<SocketChannel> {
    // TODO 此部分由于处理Request 以及Response的decode和encode
    @Override
    public void initChannel (SocketChannel socketChannel) {
        ChannelPipeline pipeline = socketChannel.pipeline();
        // addLast主要由于在管道中增加相应的编码解码
        // decode encode protobuf
        pipeline.addLast(new ProtobufVarint32FrameDecoder());
        pipeline.addLast(new ProtobufDecoder(RpcRequestProto.RpcRequest.getDefaultInstance()));
        pipeline.addLast(new ProtobufVarint32LengthFieldPrepender());
        pipeline.addLast(new ProtobufEncoder());
        // 最后增加ServerHandler
        pipeline.addLast(new ServerHandler());

    }

}
