package es.edwardbelt.hycraft.network.handler.minecraft.data.chunk;

import es.edwardbelt.hycraft.protocol.io.PacketBuffer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lombok.Getter;

@Getter
public class Chunk {
    private static final int SECTION_COUNT = 24;
    private static final int MAX_HEIGHT = 319;
    private static final int MIN_HEIGHT = -64;
    private static final int START_EMPTY_SECTIONS = 4;

    private final ChunkCoordIntPair chunkCoords;
    private final ChunkSection[] chunkSections;

    public Chunk(ChunkCoordIntPair chunkCoords) {
        this.chunkCoords = chunkCoords;
        chunkSections = new ChunkSection[24];

        for (int i=0; i<START_EMPTY_SECTIONS; i++) {
            chunkSections[i] = ChunkSection.EMPTY_AIR;
        }
    }

    public void addChunkSection(ChunkSection section, int y) {
        chunkSections[y+START_EMPTY_SECTIONS] = section;
    }

    public void serialize(PacketBuffer buffer) {
        buffer.writeInt(chunkCoords.getX());
        buffer.writeInt(chunkCoords.getZ());

        buffer.writeVarInt(0); // no height maps atm

        byte[] chunkSectionData = getChunkSectionData();
        buffer.writeVarInt(chunkSectionData.length);
        buffer.writeBytes(chunkSectionData);

        buffer.writeVarInt(0); // no tile entities atm
    }

    public byte[] getChunkSectionData() {
        ByteBuf byteBuf = Unpooled.buffer();
        PacketBuffer buffer = new PacketBuffer(byteBuf);

        for (ChunkSection section : chunkSections) {
            section.serialize(buffer);
        }

        byte[] result = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(result);
        byteBuf.release();

        return result;
    }

    public static ChunkCoordIntPair getChunkCoords(double x, double z) {
        return new ChunkCoordIntPair((int) Math.floor(x) >> 4, (int) Math.floor(z) >> 4);
    }
}
