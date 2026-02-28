package es.edwardbelt.hycraft.network.player;

import es.edwardbelt.hycraft.network.handler.minecraft.data.chunk.Chunk;
import es.edwardbelt.hycraft.network.handler.minecraft.data.chunk.ChunkCoordIntPair;
import es.edwardbelt.hycraft.network.handler.minecraft.data.chunk.ChunkSection;
import es.edwardbelt.hycraft.protocol.packet.play.LevelChunkWithLightPacket;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChunkBuffer {
    private Map<ChunkCoordIntPair, Chunk> chunks = new HashMap<>();
    private Map<ChunkCoordIntPair, Integer> chunksYCoords = new HashMap<>();
    private List<Chunk> pendingChunks = new ArrayList<>();
    private ClientConnection connection;
    @Getter
    @Setter
    private ChunkCoordIntPair lastChunkSent;

    public ChunkBuffer(ClientConnection connection) {
        this.connection = connection;
    }

    public boolean isChunkSequential(ChunkCoordIntPair chunkCoords, int y) {
        if (y == 0) {
            chunks.remove(chunkCoords);
            return true;
        }

        if (!chunksYCoords.containsKey(chunkCoords)) {
            return false;
        }

        return chunksYCoords.get(chunkCoords) + 1 == y;
    }

    public void breakSequentialChunkChain(ChunkCoordIntPair chunkCoords) {
        System.out.println("BROKE SEQUENCE!");
        chunks.remove(chunkCoords);
        chunksYCoords.remove(chunkCoords);
    }

    public void addChunkSection(ChunkCoordIntPair chunkCoords, int y, ChunkSection section) {
        if (y == 0) chunks.put(chunkCoords, new Chunk(chunkCoords));
        chunks.get(chunkCoords).addChunkSection(section, y);
        chunksYCoords.put(chunkCoords, y);

        if (y == 19) {
            Chunk chunk = chunks.remove(chunkCoords);
            if (connection.isInitialized()) sendChunk(chunk);
            else pendingChunks.add(chunk);
        }
    }

    public void sendPendingChunks() {
        pendingChunks.forEach(this::sendChunk);
    }

    public void sendChunk(Chunk chunk) {
        LevelChunkWithLightPacket packet = new LevelChunkWithLightPacket(chunk);
        connection.getChannel().writeAndFlush(packet);
    }
}
