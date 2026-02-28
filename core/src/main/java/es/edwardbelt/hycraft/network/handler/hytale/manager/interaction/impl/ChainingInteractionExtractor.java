package es.edwardbelt.hycraft.network.handler.hytale.manager.interaction.impl;

import com.hypixel.hytale.server.core.modules.interaction.interaction.config.Interaction;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.client.ChainingInteraction;
import es.edwardbelt.hycraft.network.handler.hytale.manager.interaction.InteractionContext;
import es.edwardbelt.hycraft.network.handler.hytale.manager.interaction.InteractionExtractor;
import es.edwardbelt.hycraft.util.reflection.FieldAccessor;
import es.edwardbelt.hycraft.util.reflection.Reflections;

public class ChainingInteractionExtractor implements InteractionExtractor<ChainingInteraction> {
    private static final FieldAccessor<String[]> NEXT_FIELD = Reflections.getField(ChainingInteraction.class, String[].class);

    @Override
    public Interaction extract(ChainingInteraction interaction, InteractionContext context) {
        String[] nextInteractions = NEXT_FIELD.get(interaction);
        if (nextInteractions == null || nextInteractions.length == 0) return null;

        int selectedIndex = selectChainIndex(nextInteractions, context.getConnection().getLastAttackInteraction());
        String selectedInteractionId = nextInteractions[selectedIndex];
        context.getConnection().setLastAttackInteraction(selectedInteractionId);

        return Interaction.getAssetMap().getAsset(selectedInteractionId);
    }

    private int selectChainIndex(String[] nextInteractions, String lastAttackInteraction) {
        if (lastAttackInteraction == null) return 0;

        for (int i = 0; i < nextInteractions.length; i++) {
            if (nextInteractions[i].equals(lastAttackInteraction)) return (i + 1) % nextInteractions.length;
        }

        return 0;
    }
}
