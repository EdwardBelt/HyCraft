package es.edwardbelt.hycraft.network.handler.hytale.manager.interaction.impl;

import com.hypixel.hytale.server.core.modules.interaction.interaction.config.Interaction;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.RootInteraction;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.none.ParallelInteraction;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.none.SelectInteraction;
import es.edwardbelt.hycraft.network.handler.hytale.manager.interaction.InteractionContext;
import es.edwardbelt.hycraft.network.handler.hytale.manager.interaction.InteractionExtractor;
import es.edwardbelt.hycraft.network.handler.hytale.manager.interaction.InteractionManager;
import es.edwardbelt.hycraft.util.reflection.FieldAccessor;
import es.edwardbelt.hycraft.util.reflection.Reflections;

public class ParallelInteractionExtractor implements InteractionExtractor<ParallelInteraction> {
    private static final FieldAccessor<String[]> INTERACTIONS_FIELD = Reflections.getField(ParallelInteraction.class, String[].class);

    @Override
    public Interaction extract(ParallelInteraction interaction, InteractionContext context) {
        String[] parallelInteractions = INTERACTIONS_FIELD.get(interaction);
        if (parallelInteractions == null) return null;

        for (String parallelId : parallelInteractions) {
            RootInteraction parallelRoot = RootInteraction.getAssetMap().getAsset(parallelId);
            if (parallelRoot == null) continue;

            Interaction parallelInner = InteractionManager.getInteractionFromRoot(parallelRoot);
            if (parallelInner instanceof SelectInteraction) return parallelInner;
        }

        return null;
    }
}
