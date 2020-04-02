package nettyrpc.connections;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.springframework.util.SerializationUtils;

import java.util.List;

public class RpcDecoder extends ByteToMessageDecoder {
    private Class<?> genericClass;
    public RpcDecoder(Class<?> genericClass) {
        this.genericClass = genericClass;
    }

    @Override
    public void decode(ChannelHandlerContext context, ByteBuf in, List<Object> out) throws Exception {
        // 检查是否传送了基本文件头部，即文件长度信息
        if (in.readableBytes() < 4) { return; }

        in.markReaderIndex();
        int length = in.readInt();

        if (in.readableBytes() < length) {
            in.resetReaderIndex();
            return;
        }
        byte[] bytes = new byte[length];
        in.readBytes(bytes);

        Object obj = SerializationUtils.deserialize(bytes);

        out.add(obj);
        //TODO understand the in.....
    }
}
