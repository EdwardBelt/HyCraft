package es.edwardbelt.hycraft.network.handler.hytale.world;

import com.hypixel.hytale.protocol.packets.world.ServerSetBlocks;
import com.hypixel.hytale.protocol.packets.world.SetBlockCmd;
import es.edwardbelt.hycraft.mapping.MappingRegistry;
import es.edwardbelt.hycraft.network.handler.PacketHandler;
import es.edwardbelt.hycraft.network.player.ClientConnection;
import es.edwardbelt.hycraft.protocol.packet.play.UpdateSectionBlocksPacket;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServerSetBlocksHandler implements PacketHandler<ServerSetBlocks> {
    private static final int HYTALE_CHUNK_SIZE = 32;
    private static final int MC_SECTION_SIZE = 16;

    @Override
    public void handle(ServerSetBlocks packet, ClientConnection connection) {
        Map<Long, List<Long>> sectionBlocks = new HashMap<>();

        for (SetBlockCmd blockCmd : packet.cmds) {
            int localX = blockCmd.index % HYTALE_CHUNK_SIZE;
            int localZ = (blockCmd.index / HYTALE_CHUNK_SIZE) % HYTALE_CHUNK_SIZE;
            int localY = blockCmd.index / (HYTALE_CHUNK_SIZE * HYTALE_CHUNK_SIZE);

            int worldX = packet.x * HYTALE_CHUNK_SIZE + localX;
            int worldY = packet.y * HYTALE_CHUNK_SIZE + localY;
            int worldZ = packet.z * HYTALE_CHUNK_SIZE + localZ;

            int sectionX = Math.floorDiv(worldX, MC_SECTION_SIZE);
            int sectionY = Math.floorDiv(worldY, MC_SECTION_SIZE);
            int sectionZ = Math.floorDiv(worldZ, MC_SECTION_SIZE);

            int blockLocalX = Math.floorMod(worldX, MC_SECTION_SIZE);
            int blockLocalY = Math.floorMod(worldY, MC_SECTION_SIZE);
            int blockLocalZ = Math.floorMod(worldZ, MC_SECTION_SIZE);

            long sectionPos = UpdateSectionBlocksPacket.encodeSectionPosition(sectionX, sectionY, sectionZ);

            int blockId = MappingRegistry.get().getBlockMapper().getMapping(blockCmd.blockId);
            long encodedBlock = UpdateSectionBlocksPacket.encodeBlockChange(
                    blockId, blockLocalX, blockLocalY, blockLocalZ
            );

            sectionBlocks.computeIfAbsent(sectionPos, k -> new ArrayList<>()).add(encodedBlock);
        }

        for (Map.Entry<Long, List<Long>> entry : sectionBlocks.entrySet()) {
            long sectionPos = entry.getKey();
            List<Long> blocksList = entry.getValue();

            long[] blocks = blocksList.stream().mapToLong(Long::longValue).toArray();

            UpdateSectionBlocksPacket sectionPacket = new UpdateSectionBlocksPacket(sectionPos, blocks);
            connection.getChannel().write(sectionPacket);
        }

        connection.getChannel().flush();
    }
}