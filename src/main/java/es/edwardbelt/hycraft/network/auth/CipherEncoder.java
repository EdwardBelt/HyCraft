package es.edwardbelt.hycraft.network.auth;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.AllArgsConstructor;

import javax.crypto.Cipher;

@AllArgsConstructor
public class CipherEncoder extends MessageToByteEncoder<ByteBuf> {
    private final Cipher cipher;

    @Override
    protected void encode(ChannelHandlerContext ctx, ByteBuf in, ByteBuf out) {
        byte[] bytes = new byte[in.readableBytes()];
        in.readBytes(bytes);
        byte[] encrypted = cipher.update(bytes);
        out.writeBytes(encrypted);
    }
}

