package es.edwardbelt.hycraft.network.handler.minecraft.play;

import es.edwardbelt.hycraft.network.handler.PacketHandler;
import es.edwardbelt.hycraft.network.handler.minecraft.manager.entity.EntityManager;
import es.edwardbelt.hycraft.network.player.ClientConnection;
import es.edwardbelt.hycraft.protocol.packet.play.EntityInteractPacket;

public class EntityInteractHandler implements PacketHandler<EntityInteractPacket> {
    @Override
    public void handle(EntityInteractPacket packet, ClientConnection connection) {
        if (packet.getType() == EntityInteractPacket.Type.ATTACK) {
            EntityManager.get().hitEntity(connection, packet.getEntityId());
        }
    }
}