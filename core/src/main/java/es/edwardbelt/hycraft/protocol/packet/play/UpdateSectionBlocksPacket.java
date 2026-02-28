package es.edwardbelt.hycraft.protocol.packet.play;

import es.edwardbelt.hycraft.protocol.io.PacketBuffer;
import es.edwardbelt.hycraft.protocol.packet.Packet;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateSectionBlocksPacket implements Packet {
    private long sectionPosition;
    private long[] blocks;

    @Override
    public void write(PacketBuffer buffer) {
        buffer.writeLong(sectionPosition);
        buffer.writeVarInt(blocks.length);
        for (long block : blocks) {
            buffer.writeVarLong(block);
        }
    }

    public static long encodeSectionPosition(int sectionX, int sectionY, int sectionZ) {
        return ((long)(sectionX & 0x3FFFFF) << 42) |
                (sectionY & 0xFFFFF) |
                ((long)(sectionZ & 0x3FFFFF) << 20);
    }

    public static long encodeBlockChange(int blockStateId, int localX, int localY, int localZ) {
        return ((long)blockStateId << 12) |
                (localX << 8) |
                (localZ << 4) |
                localY;
    }
}