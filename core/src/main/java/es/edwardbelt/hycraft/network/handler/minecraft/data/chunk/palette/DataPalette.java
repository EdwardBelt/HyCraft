package es.edwardbelt.hycraft.network.handler.minecraft.data.chunk.palette;

import es.edwardbelt.hycraft.protocol.io.PacketBuffer;
import lombok.Getter;

public abstract class DataPalette {
    private final int bitsPerEntry;
    private final int[] blocksData;
    @Getter
    private short nonAirBlocks;

    public DataPalette(int bitsPerEntry) {
        this.bitsPerEntry = bitsPerEntry;
        blocksData = new int[4096];
    }

    abstract int getPaletteIndex(int blockId);
    abstract int getPaletteBlockId(int index);
    abstract void innerSerialize(PacketBuffer buffer);

    public void set(int index, int blockId) {
        if (blockId != 0) nonAirBlocks++;
        if (bitsPerEntry == 15) {
            blocksData[index] = blockId;
            return;
        }

        int paletteIndex = getPaletteIndex(blockId);
        blocksData[index] = paletteIndex;
    }

    public void serialize(PacketBuffer buffer) {
        buffer.writeUnsignedByte(bitsPerEntry);
        innerSerialize(buffer);

        if (bitsPerEntry == 0) {
            return;
        }

        int entriesPerLong = 64 / bitsPerEntry;
        int numLongs = (4096 + entriesPerLong - 1) / entriesPerLong;

        long[] dataArray = new long[numLongs];

        for (int i = 0; i < 4096; i++) {
            int longIndex = i / entriesPerLong;
            int bitIndex = (i % entriesPerLong) * bitsPerEntry;

            dataArray[longIndex] |= ((long) blocksData[i]) << bitIndex;
        }

        for (long value : dataArray) {
            buffer.writeLong(value);
        }
    }

}
