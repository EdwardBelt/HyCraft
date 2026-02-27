package es.edwardbelt.hycraft.network.handler.hytale.manager.interaction.impl;

import com.hypixel.hytale.server.core.modules.interaction.interaction.config.Interaction;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.none.SerialInteraction;
import es.edwardbelt.hycraft.network.handler.hytale.manager.interaction.InteractionContext;
import es.edwardbelt.hycraft.network.handler.hytale.manager.interaction.InteractionExtractor;
import es.edwardbelt.hycraft.util.reflection.FieldAccessor;
import es.edwardbelt.hycraft.util.reflection.Reflections;

import java.util.Arrays;

public class SerialInteractionExtractor implements InteractionExtractor<SerialInteraction> {
    private static final FieldAccessor<String[]> INTERACTIONS_FIELD = Reflections.getField(SerialInteraction.class, String[].class);

    @Override
    public Interaction extract(SerialInteraction interaction, InteractionContext context) {
        String[] interactions = INTERACTIONS_FIELD.get(interaction);
        return Interaction.getAssetMap().getAsset(interactions[1]);
    }
}
