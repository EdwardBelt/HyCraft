package es.edwardbelt.hycraft.network.handler.minecraft.data.chunk.palette;

import es.edwardbelt.hycraft.protocol.io.PacketBuffer;

public class EmptyPalette extends DataPalette {
    private final int blockId;

    public EmptyPalette(int blockId) {
        super(0);
        this.blockId = blockId;
    }

    @Override
    public int getPaletteIndex(int blockId) {
        return 0;
    }

    @Override
    public int getPaletteBlockId(int index) {
        return blockId;
    }

    @Override
    void innerSerialize(PacketBuffer buffer) {
        buffer.writeVarInt(blockId);
    }
}
