package es.edwardbelt.hycraft.protocol.codec;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class MinecraftFrameEncoder extends MessageToByteEncoder<ByteBuf> {

    @Override
    protected void encode(ChannelHandlerContext ctx, ByteBuf msg, ByteBuf out) {
        int packetLength = msg.readableBytes();
        VarIntUtil.writeVarInt(out, packetLength);
        out.writeBytes(msg);
    }
}
