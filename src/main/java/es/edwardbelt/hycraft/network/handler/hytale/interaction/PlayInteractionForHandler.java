package es.edwardbelt.hycraft.network.handler.hytale.interaction;

import com.hypixel.hytale.protocol.Animation;
import com.hypixel.hytale.protocol.packets.interaction.PlayInteractionFor;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.Interaction;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.RootInteraction;
import es.edwardbelt.hycraft.network.handler.PacketHandler;
import es.edwardbelt.hycraft.network.handler.minecraft.data.entity.Entity;
import es.edwardbelt.hycraft.network.player.ClientConnection;
import es.edwardbelt.hycraft.protocol.packet.play.EntityAnimationPacket;

public class PlayInteractionForHandler implements PacketHandler<PlayInteractionFor> {
    private static final long INTERACTION_INTERVAL = 500L;

    @Override
    public void handle(PlayInteractionFor packet, ClientConnection connection) {
        /*System.out.println("sent play interaction for");
        System.out.println("interaction type. " + packet.interactionType);
        System.out.println("interaction id: " + packet.interactionId);

        Interaction interaction = Interaction.getAssetMap().getAsset(packet.interactionId);
        }*/
        Entity entity = connection.getSpawnedEntity(packet.entityId);
        if (entity == null) return;

        long current = System.currentTimeMillis();
        long last = connection.getLastAnimation();

        if (current - last < INTERACTION_INTERVAL) return;

        EntityAnimationPacket.Animation animation;

        switch (packet.interactionType) {
            case Primary -> animation = EntityAnimationPacket.Animation.SWING_HAND;
            case Secondary -> animation = EntityAnimationPacket.Animation.SWING_OFFHAND;
            default -> {
                return;
            }
        }

        connection.setLastAnimation(current);

        EntityAnimationPacket animationPacket = new EntityAnimationPacket(packet.entityId, animation);
        connection.getChannel().writeAndFlush(animationPacket);
    }
}
