package es.edwardbelt.hycraft.network.handler.hytale.manager.interaction.impl;

import com.hypixel.hytale.server.core.modules.interaction.interaction.config.Interaction;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.RootInteraction;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.none.ReplaceInteraction;
import es.edwardbelt.hycraft.network.handler.hytale.manager.interaction.InteractionContext;
import es.edwardbelt.hycraft.network.handler.hytale.manager.interaction.InteractionExtractor;
import es.edwardbelt.hycraft.network.handler.hytale.manager.interaction.InteractionManager;
import es.edwardbelt.hycraft.util.reflection.FieldAccessor;
import es.edwardbelt.hycraft.util.reflection.Reflections;

public class ReplaceInteractionExtractor implements InteractionExtractor<ReplaceInteraction> {
    private static final FieldAccessor<String> DEFAULT_VALUE_FIELD = Reflections.getField(ReplaceInteraction.class, String.class);
    private static final FieldAccessor<String> VARIABLE_FIELD = Reflections.getField(ReplaceInteraction.class, String.class, 1);

    @Override
    public Interaction extract(ReplaceInteraction interaction, InteractionContext context) {
        String defaultValue = DEFAULT_VALUE_FIELD.get(interaction);
        String variable = VARIABLE_FIELD.get(interaction);

        String nextInteraction = context.getVars().get(variable);
        if (nextInteraction == null) nextInteraction = defaultValue;

        RootInteraction defaultRoot = RootInteraction.getAssetMap().getAsset(nextInteraction);
        if (defaultRoot == null) return null;

        return InteractionManager.getInteractionFromRoot(defaultRoot);
    }
}
