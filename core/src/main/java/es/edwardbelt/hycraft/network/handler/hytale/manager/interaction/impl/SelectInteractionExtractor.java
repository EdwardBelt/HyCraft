package es.edwardbelt.hycraft.network.handler.hytale.manager.interaction.impl;

import com.hypixel.hytale.server.core.modules.interaction.interaction.config.Interaction;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.RootInteraction;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.none.SelectInteraction;
import es.edwardbelt.hycraft.network.handler.hytale.manager.interaction.InteractionContext;
import es.edwardbelt.hycraft.network.handler.hytale.manager.interaction.InteractionExtractor;
import es.edwardbelt.hycraft.network.handler.hytale.manager.interaction.InteractionManager;
import es.edwardbelt.hycraft.util.reflection.FieldAccessor;
import es.edwardbelt.hycraft.util.reflection.Reflections;

public class SelectInteractionExtractor implements InteractionExtractor<SelectInteraction> {
    private static final FieldAccessor<String> HIT_ENTITY_FIELD = Reflections.getField(SelectInteraction.class, String.class);

    @Override
    public Interaction extract(SelectInteraction interaction, InteractionContext context) {
        String hitEntityRootId = HIT_ENTITY_FIELD.get(interaction);
        if (hitEntityRootId == null) return null;

        RootInteraction hitEntityRoot = RootInteraction.getAssetMap().getAsset(hitEntityRootId);
        if (hitEntityRoot == null) return null;

        return InteractionManager.getInteractionFromRoot(hitEntityRoot);
    }
}
