package es.edwardbelt.hycraft.network.handler.hytale.manager.interaction;


import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.spatial.SpatialResource;
import com.hypixel.hytale.protocol.InteractionType;
import com.hypixel.hytale.protocol.packets.interaction.PlayInteractionFor;
import com.hypixel.hytale.server.core.modules.entity.EntityModule;
import com.hypixel.hytale.server.core.modules.entity.component.TransformComponent;
import com.hypixel.hytale.server.core.modules.entity.tracker.NetworkId;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.Interaction;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.RootInteraction;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.SimpleInteraction;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.client.ChainingInteraction;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.client.ChargingInteraction;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.client.UseBlockInteraction;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.none.*;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.server.DamageEntityInteraction;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import es.edwardbelt.hycraft.network.handler.hytale.manager.interaction.impl.*;
import es.edwardbelt.hycraft.network.player.ClientConnection;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;

import java.util.*;

public class InteractionManager {
    private static final InteractionManager INSTANCE = new InteractionManager();
    public static InteractionManager get() { return INSTANCE; }

    private static final int MAX_EXTRACTOR_ITERATIONS = 50;
    private static final int INTERACTION_RADIUS = 96;

    private final Map<Class, InteractionExtractor> interactionExtractors = new HashMap<>();

    public InteractionManager() {
        interactionExtractors.put(ChainingInteraction.class, new ChainingInteractionExtractor());
        interactionExtractors.put(ChargingInteraction.class, new ChargingInteractionExtractor());
        interactionExtractors.put(ParallelInteraction.class, new ParallelInteractionExtractor());
        interactionExtractors.put(ReplaceInteraction.class, new ReplaceInteractionExtractor());
        interactionExtractors.put(SelectInteraction.class, new SelectInteractionExtractor());
        interactionExtractors.put(SimpleInteraction.class, new SimpleInteractionExtractor());
        interactionExtractors.put(UseBlockInteraction.class, new UseBlockInteractionExtractor());
        interactionExtractors.put(ConditionInteraction.class, new ConditionInteractionExtractor());
        interactionExtractors.put(SerialInteraction.class, new SerialInteractionExtractor());
    }

    public void playInteraction(ClientConnection connection, String id, InteractionType type) {
        int interactionId = Interaction.getAssetMap().getIndex(id);
        playInteraction(connection, interactionId, type);
    }

    public void playInteraction(ClientConnection connection, int id, InteractionType type) {
        Ref<EntityStore> entityRef = connection.getPlayerRef().getReference();
        Store<EntityStore> store = entityRef.getStore();

        EntityStore entityStore = store.getExternalData();
        var world = entityStore.getWorld();

        world.execute(() -> {
            try {
                TransformComponent transform = store.getComponent(entityRef, TransformComponent.getComponentType());
                NetworkId networkIdComp = store.getComponent(entityRef, NetworkId.getComponentType());

                if (transform == null || networkIdComp == null) return;

                PlayInteractionFor animPacket = new PlayInteractionFor(
                        networkIdComp.getId(),
                        connection.clientChainId.getAndIncrement(),
                        null,
                        0,
                        id,
                        null,
                        type,
                        false
                );

                SpatialResource<Ref<EntityStore>, EntityStore> playerSpatialResource =
                        store.getResource(EntityModule.get().getPlayerSpatialResourceType());

                ObjectList<Ref<EntityStore>> nearbyPlayers = new ObjectArrayList<>();
                playerSpatialResource.getSpatialStructure().collect(transform.getPosition(), INTERACTION_RADIUS, nearbyPlayers);

                for (Ref<EntityStore> targetRef : nearbyPlayers) {
                    if (targetRef.equals(entityRef)) continue;

                    PlayerRef targetPlayerComp = store.getComponent(targetRef, PlayerRef.getComponentType());
                    if (targetPlayerComp == null) return;

                    targetPlayerComp.getPacketHandler().writeNoCache(animPacket);
                }

            } catch (Exception _) {
            }
        });
    }

    public InteractionExtractorResponse extract(InteractionContext context, Interaction interaction) {
        int iterations = 0;
        List<String> interactionPath = new ArrayList<>();
        interactionPath.add(interaction.getId());

        while (!(interaction instanceof DamageEntityInteraction) && iterations < MAX_EXTRACTOR_ITERATIONS) {
            InteractionExtractor extractor = interactionExtractors.get(interaction.getClass());
            if (extractor == null) {
                System.out.println("no extractor found for " + interaction.getClass());
                break;
            }

            interaction = extractor.extract(interaction, context);
            if (interaction == null) break;

            interactionPath.add(interaction.getId());
            iterations++;
        }

        return new InteractionExtractorResponse(interaction, interactionPath);
    }

    public static Interaction getInteractionFromRoot(RootInteraction root) {
        if (root == null) return null;

        String[] interactionIds = root.getInteractionIds();
        if (interactionIds == null || interactionIds.length == 0) return null;

        return Interaction.getAssetMap().getAsset(interactionIds[0]);
    }
}
