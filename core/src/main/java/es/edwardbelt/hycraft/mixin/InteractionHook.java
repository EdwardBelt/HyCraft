package es.edwardbelt.hycraft.mixin;

import com.hypixel.hytale.server.core.entity.InteractionChain;
import es.edwardbelt.hycraft.network.handler.hytale.manager.interaction.ForcedLocalClientChain;

public class InteractionHook {
    public boolean isInteractionChainForced(InteractionChain chain) {
        return chain instanceof ForcedLocalClientChain;
    }
}
