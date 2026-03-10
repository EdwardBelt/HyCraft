package es.edwardbelt.hycraft.network.handler.hytale.manager.interaction;

import com.hypixel.hytale.protocol.InteractionChainData;
import com.hypixel.hytale.protocol.InteractionType;
import com.hypixel.hytale.server.core.entity.InteractionChain;
import com.hypixel.hytale.server.core.entity.InteractionContext;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.RootInteraction;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;

public class ForcedLocalClientChain extends InteractionChain {
    public ForcedLocalClientChain(InteractionType type, InteractionContext context, InteractionChainData chainData, @NonNullDecl RootInteraction rootInteraction, @NullableDecl Runnable onCompletion, boolean requiresClient) {
        super(type, context, chainData, rootInteraction, onCompletion, requiresClient);
    }
}
