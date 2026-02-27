package es.edwardbelt.hycraft.network.auth;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import lombok.AllArgsConstructor;

import javax.crypto.Cipher;
import java.util.List;

@AllArgsConstructor
public class CipherDecoder extends MessageToMessageDecoder<ByteBuf> {
    private final Cipher cipher;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        byte[] bytes = new byte[in.readableBytes()];
        in.readBytes(bytes);
        byte[] decrypted = cipher.update(bytes);
        out.add(Unpooled.wrappedBuffer(decrypted));
    }
}

