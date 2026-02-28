package es.edwardbelt.hycraft.protocol.codec;

import es.edwardbelt.hycraft.protocol.ProtocolConstants;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import java.util.List;

public class MinecraftFrameDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        if (!in.isReadable()) {
            return;
        }

        in.markReaderIndex();

        int packetLength = VarIntUtil.readVarInt(in);

        if (packetLength == -1) {
            in.resetReaderIndex();
            return;
        }

        if (packetLength > ProtocolConstants.MAX_PACKET_SIZE) {
            throw new RuntimeException("Packet too large: " + packetLength + " bytes");
        }

        if (packetLength < 1) {
            throw new RuntimeException("Packet length cannot be less than 1");
        }

        if (in.readableBytes() < packetLength) {
            in.resetReaderIndex();
            return;
        }

        ByteBuf packetData = in.readBytes(packetLength);
        out.add(packetData);
    }
}
