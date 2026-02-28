package es.edwardbelt.hycraft.network.handler.minecraft.data.chunk.palette;

import es.edwardbelt.hycraft.protocol.io.PacketBuffer;

import java.util.HashMap;
import java.util.Map;

public class IndirectPalette extends DataPalette {
    private final Map<Integer, Integer> internalToExternal;
    private final Map<Integer, Integer> externalToInternal;

    public IndirectPalette(int bitsPerEntry) {
        super(bitsPerEntry);
        this.internalToExternal = new HashMap<>();
        this.externalToInternal = new HashMap<>();
    }

    @Override
    public int getPaletteIndex(int blockId) {
        Integer index = externalToInternal.get(blockId);
        if (index == null) {
            index = internalToExternal.size();
            internalToExternal.put(index, blockId);
            externalToInternal.put(blockId, index);
        }

        return index;
    }

    @Override
    public int getPaletteBlockId(int index) {
        return internalToExternal.get(index);
    }

    @Override
    void innerSerialize(PacketBuffer buffer) {
        buffer.writeVarInt(internalToExternal.size());
        for (int i = 0; i < internalToExternal.size(); i++) {
            buffer.writeVarInt(internalToExternal.get(i));
        }
    }
}
