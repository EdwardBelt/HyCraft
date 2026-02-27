package es.edwardbelt.hycraft.protocol.packet.play;

import es.edwardbelt.hycraft.network.handler.minecraft.data.chunk.ChunkCoordIntPair;
import es.edwardbelt.hycraft.protocol.io.PacketBuffer;
import es.edwardbelt.hycraft.protocol.packet.Packet;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class SetCenterChunkPacket implements Packet {
    private ChunkCoordIntPair chunkCoords;

    @Override
    public void write(PacketBuffer buffer) {
        buffer.writeVarInt(chunkCoords.getX());
        buffer.writeVarInt(chunkCoords.getZ());
    }
}
