package es.edwardbelt.hycraft.protocol.packet.play;

import es.edwardbelt.hycraft.network.handler.minecraft.data.chunk.Chunk;
import es.edwardbelt.hycraft.protocol.io.PacketBuffer;
import es.edwardbelt.hycraft.protocol.packet.Packet;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class LevelChunkWithLightPacket implements Packet {
    private Chunk chunk;

    @Override
    public void write(PacketBuffer buffer) {
        chunk.serialize(buffer);

        writeLightData(buffer);
    }

    private void writeLightData(PacketBuffer buffer) {
        int worldHeight = 384;
        int sectionsCount = worldHeight / 16;
        int totalSections = sectionsCount + 2;

        writeBitSet(buffer, createSectionsBitSet(totalSections));
        writeBitSet(buffer, new long[0]);
        writeBitSet(buffer, createEmptySkyBitSet(totalSections));
        writeBitSet(buffer, createAllSetBitSet(totalSections));

        buffer.writeVarInt(sectionsCount);
        for (int i = 0; i < sectionsCount; i++) {
            buffer.writeVarInt(2048);

            for (int j = 0; j < 2048; j++) {
                buffer.writeByte((byte) 0xFF);
            }
        }

        buffer.writeVarInt(0);
    }

    private long[] createSectionsBitSet(int totalSections) {
        int longsNeeded = (totalSections + 63) / 64;
        long[] bitset = new long[longsNeeded];

        for (int i = 1; i < totalSections - 1; i++) {
            int longIndex = i / 64;
            int bitIndex = i % 64;
            bitset[longIndex] |= (1L << bitIndex);
        }

        return bitset;
    }

    private long[] createEmptySkyBitSet(int totalSections) {
        int longsNeeded = (totalSections + 63) / 64;
        long[] bitset = new long[longsNeeded];

        bitset[0] |= 1L;

        int lastBitIndex = totalSections - 1;
        int longIndex = lastBitIndex / 64;
        int bitIndex = lastBitIndex % 64;
        bitset[longIndex] |= (1L << bitIndex);

        return bitset;
    }

    private long[] createAllSetBitSet(int totalSections) {
        int longsNeeded = (totalSections + 63) / 64;
        long[] bitset = new long[longsNeeded];

        for (int i = 0; i < totalSections; i++) {
            int longIndex = i / 64;
            int bitIndex = i % 64;
            bitset[longIndex] |= (1L << bitIndex);
        }

        return bitset;
    }

    private void writeBitSet(PacketBuffer buffer, long[] data) {
        buffer.writeVarInt(data.length);
        for (long value : data) {
            buffer.writeLong(value);
        }
    }
}
