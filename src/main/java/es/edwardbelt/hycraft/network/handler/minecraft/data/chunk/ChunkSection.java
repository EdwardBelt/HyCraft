package es.edwardbelt.hycraft.network.handler.minecraft.data.chunk;

import es.edwardbelt.hycraft.network.handler.minecraft.data.chunk.palette.DataPalette;
import es.edwardbelt.hycraft.network.handler.minecraft.data.chunk.palette.EmptyPalette;
import es.edwardbelt.hycraft.protocol.io.PacketBuffer;
import lombok.Getter;

@Getter
public class ChunkSection {
    public static ChunkSection EMPTY_AIR = new ChunkSection((short) 0, new EmptyPalette(0));

    private short nonAirBlocks;
    private DataPalette blockPalette;
    private DataPalette biomePalette;

    public ChunkSection(short nonAirBlocks, DataPalette blockPalette) {
        this.nonAirBlocks = nonAirBlocks;
        this.blockPalette = blockPalette;
        this.biomePalette = new EmptyPalette(1);
    }

    public void serialize(PacketBuffer buffer) {
        buffer.writeShort(nonAirBlocks);
        blockPalette.serialize(buffer);
        biomePalette.serialize(buffer);
    }
}
