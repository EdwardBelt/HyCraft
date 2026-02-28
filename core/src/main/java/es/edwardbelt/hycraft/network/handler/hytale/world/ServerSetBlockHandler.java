package es.edwardbelt.hycraft.network.handler.hytale.world;

import com.hypixel.hytale.protocol.packets.world.ServerSetBlock;
import es.edwardbelt.hycraft.mapping.MappingRegistry;
import es.edwardbelt.hycraft.network.handler.PacketHandler;
import es.edwardbelt.hycraft.network.player.ClientConnection;
import es.edwardbelt.hycraft.network.handler.minecraft.data.BlockPosition;
import es.edwardbelt.hycraft.protocol.packet.play.BlockUpdatePacket;

public class ServerSetBlockHandler implements PacketHandler<ServerSetBlock> {
    @Override
    public void handle(ServerSetBlock packet, ClientConnection connection) {
        BlockPosition blockPos = new BlockPosition(packet.x, packet.y, packet.z);
        int minecraftBlockId = MappingRegistry.get().getBlockMapper().getMapping(packet.blockId);

        BlockUpdatePacket blockUpdate = new BlockUpdatePacket(blockPos, minecraftBlockId);
        connection.getChannel().writeAndFlush(blockUpdate);
    }
}