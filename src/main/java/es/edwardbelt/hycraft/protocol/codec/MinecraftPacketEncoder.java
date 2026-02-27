package es.edwardbelt.hycraft.protocol.codec;

import es.edwardbelt.hycraft.network.player.ClientConnection;
import es.edwardbelt.hycraft.protocol.io.PacketBuffer;
import es.edwardbelt.hycraft.protocol.packet.Packet;
import es.edwardbelt.hycraft.protocol.packet.PacketRegistry;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class MinecraftPacketEncoder extends MessageToByteEncoder<Packet> {
    private final ClientConnection connection;

    public MinecraftPacketEncoder(ClientConnection connection) {
        this.connection = connection;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Packet packet, ByteBuf out) {
        Integer packetId = PacketRegistry.getPacketByType(packet.getClass());

        if (packetId == null) {
            throw new RuntimeException(
                    String.format("Packet %s is not registered for state %s",
                            packet.getClass().getSimpleName(), connection.getState())
            );
        }

        PacketBuffer buffer = new PacketBuffer(out);

        buffer.writeVarInt(packetId);

        try {
            packet.write(buffer);
        } catch (Exception e) {
            throw new RuntimeException(
                    String.format("Failed to encode packet %s (ID 0x%02X)",
                            packet.getClass().getSimpleName(), packetId), e
            );
        }
    }

}
