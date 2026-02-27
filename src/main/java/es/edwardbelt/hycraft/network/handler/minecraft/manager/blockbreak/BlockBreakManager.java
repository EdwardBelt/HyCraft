package es.edwardbelt.hycraft.network.handler.minecraft.manager.blockbreak;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.math.util.ChunkUtil;
import com.hypixel.hytale.math.vector.Vector3i;
import com.hypixel.hytale.protocol.InteractionType;
import com.hypixel.hytale.server.core.HytaleServer;
import com.hypixel.hytale.server.core.asset.type.blocktype.config.BlockType;
import com.hypixel.hytale.server.core.asset.type.item.config.Item;
import com.hypixel.hytale.server.core.entity.LivingEntity;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.inventory.Inventory;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.modules.interaction.BlockHarvestUtils;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.Interaction;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.RootInteraction;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.chunk.WorldChunk;
import com.hypixel.hytale.server.core.universe.world.storage.ChunkStore;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import es.edwardbelt.hycraft.mapping.MappingRegistry;
import es.edwardbelt.hycraft.network.handler.hytale.manager.interaction.InteractionContext;
import es.edwardbelt.hycraft.network.handler.hytale.manager.interaction.InteractionExtractorResponse;
import es.edwardbelt.hycraft.network.handler.hytale.manager.interaction.InteractionManager;
import es.edwardbelt.hycraft.network.handler.minecraft.data.BlockPosition;
import es.edwardbelt.hycraft.network.player.ClientConnection;
import es.edwardbelt.hycraft.protocol.packet.play.*;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class BlockBreakManager {
    private static BlockBreakManager INSTANCE = new BlockBreakManager();
    public static BlockBreakManager get() { return INSTANCE; }

    private static final long DEFAULT_DAMAGE_INTERVAL_MS = 350;
    private static final long COOLDOWN_MS = 200;

    public void handle(ClientConnection connection, BlockPosition target, PlayerActionPacket.Status status, int sequence) {
        switch (status) {
            case STARTED_DIGGING -> handleStartDigging(connection, target, sequence);
            case CANCELLED_DIGGING -> cancelBreaking(connection);
        }
    }

    private void handleStartDigging(ClientConnection connection, BlockPosition targetBlock, int sequence) {
        BlockBreakTracker existing = connection.getBlockBreakTracker();

        if (existing != null) {
            existing.cancel();
        }

        BlockBreakTracker tracker = new BlockBreakTracker(targetBlock, sequence);
        connection.setBlockBreakTracker(tracker);

        PlayerRef playerRefWrapper = connection.getPlayerRef();
        Ref<EntityStore> entityRef = playerRefWrapper.getReference();
        Store<EntityStore> store = entityRef.getStore();
        EntityStore entityStore = store.getExternalData();
        World world = entityStore.getWorld();

        world.execute(() -> {
            long delay = 0;
            long currentTime = System.currentTimeMillis();
            if (currentTime - connection.getLastBreakTime() < COOLDOWN_MS) {
                delay = COOLDOWN_MS;
            }

            long cooldown = getItemInHandBreakCooldown(store, entityRef);
            if (cooldown == 0) return;

            tracker.damageTask = HytaleServer.SCHEDULED_EXECUTOR.scheduleAtFixedRate(() -> {
                world.execute(() -> {
                    applyBlockDamage(connection, tracker, store, entityRef, world);
                });
            }, delay, cooldown, TimeUnit.MILLISECONDS);
        });
    }

    private long getItemInHandBreakCooldown(Store<EntityStore> store, Ref<EntityStore> entityRef) {
        LivingEntity playerEntity = store.getComponent(entityRef, Player.getComponentType());

        Inventory inventory = playerEntity.getInventory();
        ItemStack hand = inventory.getActiveHotbarItem();
        if (hand == null || hand.isEmpty()) return DEFAULT_DAMAGE_INTERVAL_MS;
        String interactionId = hand.getItem().getInteractions().get(InteractionType.Primary);
        if (interactionId == null) return DEFAULT_DAMAGE_INTERVAL_MS;

        RootInteraction interaction = RootInteraction.getAssetMap().getAsset(interactionId);
        if (interaction == null || interaction.getCooldown() == null) return DEFAULT_DAMAGE_INTERVAL_MS;

        return (long) interaction.getCooldown().cooldown;
    }

    private void applyBlockDamage(ClientConnection connection, BlockBreakTracker tracker,
                                  Store<EntityStore> store, Ref<EntityStore> entityRef, World world) {
        Vector3i targetBlock = tracker.position.toVector3i();

        try {
            LivingEntity playerEntity = store.getComponent(entityRef, Player.getComponentType());
            if (playerEntity == null) {
                cancelBreaking(connection);
                return;
            }

            ChunkStore chunkStore = world.getChunkStore();
            Store<ChunkStore> chunkStoreStore = chunkStore.getStore();

            long chunkIndex = ChunkUtil.indexChunkFromBlock(targetBlock.x, targetBlock.z);
            Ref<ChunkStore> chunkReference = chunkStore.getChunkReference(chunkIndex);

            if (chunkReference == null || !chunkReference.isValid()) {
                cancelBreaking(connection);
                return;
            }

            WorldChunk worldChunk = chunkStoreStore.getComponent(chunkReference, WorldChunk.getComponentType());
            if (worldChunk == null) {
                cancelBreaking(connection);
                return;
            }

            BlockType blockType = worldChunk.getBlockType(targetBlock.x, targetBlock.y, targetBlock.z);
            if (blockType == null || blockType.getId().equals("Empty")) {
                BlockUpdatePacket updateBlockPacket = new BlockUpdatePacket(tracker.position, 0);
                connection.getChannel().writeAndFlush(updateBlockPacket);
                cancelBreaking(connection);
                return;
            }

            Inventory inventory = playerEntity.getInventory();
            ItemStack hand = inventory.getActiveHotbarItem();

            Item item = hand != null ? hand.getItem() : null;
            String itemInHandId = (hand != null && !hand.isEmpty()) ? hand.getItemId() : null;

            Map<String, String> interactionVars = item != null ? item.getInteractionVars() : new HashMap<>();

            String interactionId = item != null ? item.getInteractions().get(InteractionType.Primary) : "*Empty_Interactions_Primary";
            if (interactionId == null) return;

            RootInteraction rootInteraction = RootInteraction.getAssetMap().getAsset(interactionId);
            if (rootInteraction == null) return;

            Interaction mainInteraction = InteractionManager.getInteractionFromRoot(rootInteraction);

            InteractionExtractorResponse interactionResponse = InteractionManager.get().extract(new InteractionContext(connection, interactionVars), mainInteraction);
            interactionResponse.getInteractionPath().forEach(i -> InteractionManager.get().playInteraction(connection, i, InteractionType.Primary));

            connection.setLastBreakTime(System.currentTimeMillis());
            boolean destroyed = BlockHarvestUtils.performBlockDamage(
                    playerEntity,
                    entityRef,
                    targetBlock,
                    hand,
                    null,
                    itemInHandId,
                    false,
                    1.0F,
                    0,
                    chunkReference,
                    store,
                    chunkStoreStore
            );

            if (destroyed) {
                AcknowledgeBlockChangePacket ackPacket = new AcknowledgeBlockChangePacket(tracker.sequence);
                BlockUpdatePacket blockUpdatePacket = new BlockUpdatePacket(tracker.position, 0);
                int blockStateId = MappingRegistry.get().getBlockMapper().getMapping(BlockType.getAssetMap().getIndex(blockType.getId()));
                WorldEventPacket worldEventPacket = new WorldEventPacket(2001, tracker.position, blockStateId);

                connection.getChannel().write(ackPacket);
                connection.getChannel().write(blockUpdatePacket);
                connection.getChannel().write(worldEventPacket);
                connection.getChannel().flush();

                tracker.cancel();
                connection.setBlockBreakTracker(null);
            }

        } catch (Exception e) {
            e.printStackTrace();
            cancelBreaking(connection);
        }
    }

    public void changePlayerMiningSpeed(ClientConnection connection) {
        UpdateAttributesPacket.Property blockBreakSpeed = new UpdateAttributesPacket.Property(
                UpdateAttributesPacket.Attributes.BLOCK_BREAK_SPEED,
                0.0,
                Collections.emptyList()
        );

        List<UpdateAttributesPacket.Property> properties = new ArrayList<>();
        properties.add(blockBreakSpeed);

        UpdateAttributesPacket packet = new UpdateAttributesPacket(connection.getNetworkId(), properties);
        connection.getChannel().writeAndFlush(packet);
    }

    private void cancelBreaking(ClientConnection connection) {
        if (connection.getBlockBreakTracker() != null) {
            connection.getBlockBreakTracker().cancel();
            connection.setBlockBreakTracker(null);
        }
    }
}
