package es.edwardbelt.hycraft.network.handler.hytale.data;

import com.hypixel.hytale.protocol.packets.world.PaletteType;
import es.edwardbelt.hycraft.protocol.io.PacketBuffer;

public interface HyPalette {
    static HyPalette create(PaletteType type) {
        switch (type) {
            case Empty: return new HyEmptyPalette();
            case HalfByte: return new HyHalfBytePalette();
            case Byte: return new HyBytePalette();
            case Short:
                System.out.println("short palette received");
                break;
        }
        return null;
    }

    int getPaletteSize();
    void deserialize(PacketBuffer buffer);
    int getBlock(int index);
}
