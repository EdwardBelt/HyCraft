package es.edwardbelt.hycraft.network.handler.hytale.data;

import es.edwardbelt.hycraft.protocol.io.PacketBuffer;

import java.util.HashMap;
import java.util.Map;

public class HyBytePalette implements HyPalette {
    private final Map<Byte, Integer> internalToExternal = new HashMap<>();
    private final byte[] blocks = new byte[32768];

    @Override
    public int getPaletteSize() {
        return internalToExternal.size();
    }

    @Override
    public void deserialize(PacketBuffer buffer) {
        short internalToExternalSize = buffer.readShortLE();
        for (short s=0; s<internalToExternalSize; s++) {
            byte internalId = buffer.readByte();
            int externalId = buffer.readIntLE();
            short count = buffer.readShortLE();

            internalToExternal.put(internalId, externalId);
        }

        buffer.readBytes(blocks);
    }

    @Override
    public int getBlock(int index) {
        byte paletteIndex = blocks[index];
        return internalToExternal.get(paletteIndex);
    }
}
