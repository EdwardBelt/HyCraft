package es.edwardbelt.hycraft.network.handler.hytale.world;

import com.hypixel.hytale.protocol.packets.world.PaletteType;
import com.hypixel.hytale.protocol.packets.world.SetChunk;
import es.edwardbelt.hycraft.mapping.MappingRegistry;
import es.edwardbelt.hycraft.network.handler.hytale.data.HyPalette;
import es.edwardbelt.hycraft.network.handler.PacketHandler;
import es.edwardbelt.hycraft.network.player.ChunkBuffer;
import es.edwardbelt.hycraft.network.player.ClientConnection;
import es.edwardbelt.hycraft.network.handler.minecraft.data.chunk.ChunkCoordIntPair;
import es.edwardbelt.hycraft.network.handler.minecraft.data.chunk.ChunkSection;
import es.edwardbelt.hycraft.network.handler.minecraft.data.chunk.palette.DataPalette;
import es.edwardbelt.hycraft.network.handler.minecraft.data.chunk.palette.IndirectPalette;
import es.edwardbelt.hycraft.protocol.io.PacketBuffer;
import io.netty.buffer.Unpooled;

public class SetChunkHandler implements PacketHandler<SetChunk> {
    @Override
    public void handle(SetChunk packet, ClientConnection connection) {
        byte[] chunkData = packet.data;

        ChunkBuffer chunkBuffer = connection.getChunkBuffer();

        if (chunkData == null) {
            for (int dy = 0; dy < 2; dy++) {
                for (int dz = 0; dz < 2; dz++) {
                    for (int dx = 0; dx < 2; dx++) {
                        int newX = packet.x * 2 + dx;
                        int newY = packet.y * 2 + dy;
                        int newZ = packet.z * 2 + dz;

                        ChunkCoordIntPair chunkCoords = new ChunkCoordIntPair(newX, newZ);

                        boolean isChunkSequential = chunkBuffer.isChunkSequential(chunkCoords, newY);
                        if (!isChunkSequential) {
                            chunkBuffer.breakSequentialChunkChain(chunkCoords);
                            continue;
                        }

                        chunkBuffer.addChunkSection(chunkCoords, newY, ChunkSection.EMPTY_AIR);
                    }
                }
            }
            return;
        }

        PacketBuffer packetBuffer = new PacketBuffer(Unpooled.wrappedBuffer(chunkData));

        PaletteType chunkSectionPaletteType = PaletteType.fromValue(packetBuffer.readByte());

        HyPalette hyPalette = HyPalette.create(chunkSectionPaletteType);
        hyPalette.deserialize(packetBuffer);

        int bitsPerEntry = getBitsPerEntry(hyPalette.getPaletteSize());

        for (int dy = 0; dy < 2; dy++) {
            for (int dz = 0; dz < 2; dz++) {
                for (int dx = 0; dx < 2; dx++) {
                    int newX = packet.x * 2 + dx;
                    int newY = packet.y * 2 + dy;
                    int newZ = packet.z * 2 + dz;

                    ChunkCoordIntPair chunkCoords = new ChunkCoordIntPair(newX, newZ);
                    boolean isChunkSequential = chunkBuffer.isChunkSequential(chunkCoords, newY);

                    if (!isChunkSequential) {
                        chunkBuffer.breakSequentialChunkChain(chunkCoords);
                        continue;
                    }

                    if (chunkSectionPaletteType.equals(PaletteType.Empty)) {
                        chunkBuffer.addChunkSection(chunkCoords, newY, ChunkSection.EMPTY_AIR);
                        continue;
                    }

                    DataPalette mcPalette = new IndirectPalette(bitsPerEntry);

                    for (int localY = 0; localY < 16; localY++) {
                        for (int localZ = 0; localZ < 16; localZ++) {
                            for (int localX = 0; localX < 16; localX++) {

                                int globalX = dx * 16 + localX;
                                int globalY = dy * 16 + localY;
                                int globalZ = dz * 16 + localZ;

                                int sourceIndex = (globalY * 1024) + (globalZ * 32) + globalX;
                                int localIndex = (localY * 256) + (localZ * 16) + localX;

                                int hytaleBlockId = hyPalette.getBlock(sourceIndex);
                                int minecraftBlockId = MappingRegistry.get().getBlockMapper().getMapping(hytaleBlockId);

                                mcPalette.set(localIndex, minecraftBlockId);
                            }
                        }
                    }

                    ChunkSection section = new ChunkSection(mcPalette.getNonAirBlocks(), mcPalette);
                    chunkBuffer.addChunkSection(chunkCoords, newY, section);
                }
            }
        }
    }

    public int getBitsPerEntry(int size) {
        int bitsPerEntry = (int) Math.ceil(Math.log10(size)/Math.log10(2));
        return Math.max(4, bitsPerEntry);
    }


}
