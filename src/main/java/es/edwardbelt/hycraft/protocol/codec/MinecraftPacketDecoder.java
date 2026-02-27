package es.edwardbelt.hycraft.protocol.codec;

import es.edwardbelt.hycraft.network.player.ClientConnection;
import es.edwardbelt.hycraft.protocol.PacketDirection;
import es.edwardbelt.hycraft.protocol.io.PacketBuffer;
import es.edwardbelt.hycraft.protocol.packet.Packet;
import es.edwardbelt.hycraft.protocol.packet.PacketRegistry;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import java.util.List;

public class MinecraftPacketDecoder extends MessageToMessageDecoder<ByteBuf> {
    private final ClientConnection connection;

    public MinecraftPacketDecoder(ClientConnection connection) {
        this.connection = connection;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) {
        if (!msg.isReadable()) {
            return;
        }

        PacketBuffer buffer = new PacketBuffer(msg);

        int packetId = buffer.readVarInt();

        Packet packet = PacketRegistry.createPacket(packetId, connection.getState(), PacketDirection.SERVERBOUND);

        if (packet == null) return;

        try {
            packet.read(buffer);
        } catch (Exception e) {
            throw new RuntimeException(
                    String.format("Failed to decode packet %s (ID 0x%02X)",
                            packet.getClass().getSimpleName(), packetId), e
            );
        }

        out.add(packet);
    }
}