package es.edwardbelt.hycraft.network.handler.minecraft.play;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.math.vector.Vector3i;
import com.hypixel.hytale.protocol.BlockPosition;
import com.hypixel.hytale.protocol.InteractionChainData;
import com.hypixel.hytale.protocol.InteractionType;
import com.hypixel.hytale.server.core.asset.type.blocktype.config.BlockType;
import com.hypixel.hytale.server.core.entity.InteractionContext;
import com.hypixel.hytale.server.core.entity.InteractionManager;
import com.hypixel.hytale.server.core.modules.interaction.InteractionModule;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.Interaction;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.RootInteraction;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.client.PlaceBlockInteraction;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.core.util.UUIDUtil;
import es.edwardbelt.hycraft.network.handler.PacketHandler;
import es.edwardbelt.hycraft.network.handler.hytale.manager.interaction.ForcedLocalClientChain;
import es.edwardbelt.hycraft.network.handler.minecraft.manager.use.UseManager;
import es.edwardbelt.hycraft.network.player.ClientConnection;
import es.edwardbelt.hycraft.protocol.packet.play.UseItemOnPacket;

public class UseItemOnHandler implements PacketHandler<UseItemOnPacket> {
    @Override
    public void handle(UseItemOnPacket packet, ClientConnection connection) {
        if (packet.getHand() == 1) return;
        PlayerRef playerRefWrapper = connection.getPlayerRef();
        Ref<EntityStore> entityRef = playerRefWrapper.getReference();
        Store<EntityStore> store = entityRef.getStore();
        EntityStore entityStore = store.getExternalData();
        World world = entityStore.getWorld();
        BlockPosition blockPosition = new BlockPosition(packet.getPosition().getX(), packet.getPosition().getY(), packet.getPosition().getZ());

        world.execute(() -> {
            BlockType blockType = world.getBlockType(new Vector3i(blockPosition.x, blockPosition.y, blockPosition.z));
            String interactionId = getInteractionFromBlockType(blockType);

            if (interactionId != null) {
                RootInteraction interaction = RootInteraction.getAssetMap().getAsset(interactionId);

                InteractionManager interactionManager = store.getComponent(entityRef, InteractionModule.get().getInteractionManagerComponent());
                InteractionContext context = InteractionContext.forInteraction(interactionManager, entityRef, InteractionType.Use, store);
                context.getMetaStore().putMetaObject(Interaction.TARGET_BLOCK, blockPosition);
                context.getMetaStore().putMetaObject(Interaction.TARGET_BLOCK_RAW, blockPosition);
                ForcedLocalClientChain chain = initChain(interaction, context, blockPosition);
                interactionManager.queueExecuteChain(chain);
            } else {
                boolean harvest = UseManager.get().harvestBlock(connection, packet.getPosition(), world, store, entityRef);
                if (harvest) return;
                UseManager.get().placeBlock(connection, packet.getPosition(), packet.getFace(), world, store, entityRef);
            }
        });
    }

    private String getInteractionFromBlockType(BlockType blockType) {
        InteractionType[] types = new InteractionType[]{InteractionType.Use, InteractionType.Pickup, InteractionType.Pick};

        for (InteractionType type : types) {
            String interactionId = blockType.getInteractions().get(type);
            if (interactionId != null) return interactionId;
        }

        return null;
    }

    private ForcedLocalClientChain initChain(RootInteraction root, InteractionContext context, BlockPosition blockPosition) {
        InteractionChainData data = new InteractionChainData(-1, UUIDUtil.EMPTY_UUID, null, null, blockPosition, Integer.MIN_VALUE, null);
        return new ForcedLocalClientChain(InteractionType.Use, context, data, root, null, false);
    }
}
