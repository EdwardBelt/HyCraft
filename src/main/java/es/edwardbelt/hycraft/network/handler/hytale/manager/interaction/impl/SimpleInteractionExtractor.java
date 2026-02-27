package es.edwardbelt.hycraft.network.handler.hytale.manager.interaction.impl;

import com.hypixel.hytale.server.core.modules.interaction.interaction.config.Interaction;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.SimpleInteraction;
import es.edwardbelt.hycraft.network.handler.hytale.manager.interaction.InteractionContext;
import es.edwardbelt.hycraft.network.handler.hytale.manager.interaction.InteractionExtractor;
import es.edwardbelt.hycraft.util.reflection.FieldAccessor;
import es.edwardbelt.hycraft.util.reflection.Reflections;

public class SimpleInteractionExtractor implements InteractionExtractor<SimpleInteraction> {
    public static final FieldAccessor<String> NEXT_FIELD = Reflections.getField(SimpleInteraction.class, String.class);
    public static final FieldAccessor<String> FAILED_FIELD = Reflections.getField(SimpleInteraction.class, String.class, 1);

    @Override
    public Interaction extract(SimpleInteraction interaction, InteractionContext context) {
        String nextId = NEXT_FIELD.get(interaction);
        if (nextId == null) return null;

        return Interaction.getAssetMap().getAsset(nextId);
    }
}
