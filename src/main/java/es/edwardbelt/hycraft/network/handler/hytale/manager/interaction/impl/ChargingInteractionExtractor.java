package es.edwardbelt.hycraft.network.handler.hytale.manager.interaction.impl;

import com.hypixel.hytale.server.core.modules.interaction.interaction.config.Interaction;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.client.ChargingInteraction;
import es.edwardbelt.hycraft.network.handler.hytale.manager.interaction.InteractionContext;
import es.edwardbelt.hycraft.network.handler.hytale.manager.interaction.InteractionExtractor;
import es.edwardbelt.hycraft.util.reflection.FieldAccessor;
import es.edwardbelt.hycraft.util.reflection.Reflections;
import it.unimi.dsi.fastutil.floats.Float2ObjectMap;

public class ChargingInteractionExtractor implements InteractionExtractor<ChargingInteraction> {
    private static final FieldAccessor<Float2ObjectMap> NEXT_FIELD = Reflections.getField(ChargingInteraction.class, Float2ObjectMap.class);

    @Override
    public Interaction extract(ChargingInteraction interaction, InteractionContext context) {
        Float2ObjectMap<String> nextMap = NEXT_FIELD.get(interaction);
        if (nextMap == null || nextMap.isEmpty()) {
            return null;
        }

        String firstChainId = nextMap.values().iterator().next();

        return Interaction.getAssetMap().getAsset(firstChainId);
    }
}
