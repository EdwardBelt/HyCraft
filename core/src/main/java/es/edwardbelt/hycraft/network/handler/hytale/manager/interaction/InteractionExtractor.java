package es.edwardbelt.hycraft.network.handler.hytale.manager.interaction;

import com.hypixel.hytale.server.core.modules.interaction.interaction.config.Interaction;

public interface InteractionExtractor<I extends Interaction> {
    Interaction extract(I interaction, InteractionContext context);
}
