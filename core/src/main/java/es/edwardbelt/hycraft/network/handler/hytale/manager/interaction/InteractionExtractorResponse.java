package es.edwardbelt.hycraft.network.handler.hytale.manager.interaction;

import com.hypixel.hytale.server.core.modules.interaction.interaction.config.Interaction;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class InteractionExtractorResponse {
    private final Interaction interaction;
    private final List<String> interactionPath;
}
