package es.edwardbelt.hycraft.protocol.packet.play;

import es.edwardbelt.hycraft.network.handler.minecraft.data.BlockPosition;
import es.edwardbelt.hycraft.protocol.io.PacketBuffer;
import es.edwardbelt.hycraft.protocol.packet.Packet;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BlockUpdatePacket implements Packet {
    private BlockPosition location;
    private int blockId;

    @Override
    public void write(PacketBuffer buffer) {
        buffer.writeLong(location.toLong());
        buffer.writeVarInt(blockId);
    }

    @Override
    public void read(PacketBuffer buffer) {
        this.location = BlockPosition.fromLong(buffer.readLong());
        this.blockId = buffer.readVarInt();
    }
}