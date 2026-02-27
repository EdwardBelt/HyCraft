package es.edwardbelt.hycraft.network.handler.hytale.entities;

import com.hypixel.hytale.protocol.packets.entities.PlayAnimation;
import es.edwardbelt.hycraft.network.handler.PacketHandler;
import es.edwardbelt.hycraft.network.handler.minecraft.data.entity.Entity;
import es.edwardbelt.hycraft.network.player.ClientConnection;
import es.edwardbelt.hycraft.protocol.packet.play.EntityEventPacket;
import es.edwardbelt.hycraft.protocol.packet.play.HurtAnimationPacket;

public class PlayAnimationHandler implements PacketHandler<PlayAnimation> {
    @Override
    public void handle(PlayAnimation packet, ClientConnection connection) {
        if (packet.animationId == null) return;
        int entityId = packet.entityId;
        if (entityId == connection.getNetworkId()) return;

        switch (packet.animationId) {
            case "Hurt":
                HurtAnimationPacket hurtPacket = new HurtAnimationPacket(entityId, 0);
                connection.getChannel().writeAndFlush(hurtPacket);
                break;
            case "Death":
                Entity entity = connection.getSpawnedEntity(entityId);
                entity.setHealth(0);
                entity.sendMetadata(connection);
                EntityEventPacket deathPacket = new EntityEventPacket(entityId, 3);
                connection.getChannel().writeAndFlush(deathPacket);
                break;
        }
    }
}
