package es.edwardbelt.hycraft.network.handler.hytale.entities;

import com.hypixel.hytale.protocol.*;
import com.hypixel.hytale.protocol.packets.entities.EntityUpdates;
import es.edwardbelt.hycraft.mapping.managers.EntityMapper;
import es.edwardbelt.hycraft.network.handler.PacketHandler;
import es.edwardbelt.hycraft.network.handler.minecraft.manager.entity.EntityManager;
import es.edwardbelt.hycraft.network.player.ClientConnection;

import java.util.Arrays;

public class EntityUpdatesHandler implements PacketHandler<EntityUpdates> {
    @Override
    public void handle(EntityUpdates packet, ClientConnection connection) {
        if (packet.updates == null) return;

        if (packet.removed != null && packet.removed.length > 0) EntityManager.get().removeEntities(connection, packet.removed);

        for (EntityUpdate update : packet.updates) {
            if (update.removed != null) {
                for (ComponentUpdateType removedType : update.removed) {
                    //System.out.println("REMOVED :" + removedType.name());
                }
            }

            if (update.updates != null) EntityManager.get().handleEntityUpdate(connection, update.networkId, update.updates);
        }
    }
}
