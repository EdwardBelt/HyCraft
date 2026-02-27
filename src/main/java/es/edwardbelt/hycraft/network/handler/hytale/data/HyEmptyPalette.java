package es.edwardbelt.hycraft.network.handler.hytale.data;

import es.edwardbelt.hycraft.protocol.io.PacketBuffer;

public class HyEmptyPalette implements HyPalette {
    @Override
    public int getPaletteSize() {
        return 0;
    }

    @Override
    public void deserialize(PacketBuffer buffer) {
    }

    @Override
    public int getBlock(int index) {
        return 0;
    }
}
