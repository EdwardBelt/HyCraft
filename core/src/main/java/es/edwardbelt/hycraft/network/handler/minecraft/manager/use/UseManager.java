package es.edwardbelt.hycraft.network.handler.minecraft.manager.use;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.math.util.ChunkUtil;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.protocol.BlockRotation;
import com.hypixel.hytale.protocol.GameMode;
import com.hypixel.hytale.protocol.Rotation;
import com.hypixel.hytale.server.core.asset.type.blocktype.config.BlockFace;
import com.hypixel.hytale.server.core.asset.type.blocktype.config.BlockType;
import com.hypixel.hytale.server.core.asset.type.gameplay.GameplayConfig;
import com.hypixel.hytale.server.core.asset.type.gameplay.WorldConfig;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.inventory.Inventory;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.inventory.container.ItemContainer;
import com.hypixel.hytale.server.core.modules.entity.component.TransformComponent;
import com.hypixel.hytale.server.core.modules.interaction.BlockHarvestUtils;
import com.hypixel.hytale.server.core.modules.interaction.BlockPlaceUtils;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.chunk.BlockChunk;
import com.hypixel.hytale.server.core.universe.world.chunk.WorldChunk;
import com.hypixel.hytale.server.core.universe.world.chunk.section.BlockSection;
import com.hypixel.hytale.server.core.universe.world.storage.ChunkStore;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import es.edwardbelt.hycraft.network.handler.minecraft.data.BlockPosition;
import es.edwardbelt.hycraft.network.player.ClientConnection;
import es.edwardbelt.hycraft.protocol.packet.play.EntityAnimationPacket;

public class UseManager {
    private final static UseManager INSTANCE = new UseManager();
    public static UseManager get() { return INSTANCE; }

    public boolean harvestBlock(ClientConnection connection, BlockPosition targetBlock, World world, Store<EntityStore> store, Ref<EntityStore> entityRef) {
        ChunkStore chunkStore = world.getChunkStore();
        Store<ChunkStore> chunkStoreStore = chunkStore.getStore();
        long chunkIndex = ChunkUtil.indexChunkFromBlock(targetBlock.getX(), targetBlock.getZ());
        Ref<ChunkStore> chunkReference = chunkStore.getChunkReference(chunkIndex);
        if (chunkReference != null && chunkReference.isValid()) {
            WorldChunk worldChunkComponent = chunkStoreStore.getComponent(chunkReference, WorldChunk.getComponentType());

            assert worldChunkComponent != null;

            BlockChunk blockChunkComponent = chunkStoreStore.getComponent(chunkReference, BlockChunk.getComponentType());

            assert blockChunkComponent != null;

            BlockSection blockSection = blockChunkComponent.getSectionAtBlockY(targetBlock.getY());
            GameplayConfig gameplayConfig = world.getGameplayConfig();
            WorldConfig worldConfig = gameplayConfig.getWorldConfig();

            int x = targetBlock.getX();
            int y = targetBlock.getY();
            int z = targetBlock.getZ();
            BlockType blockType = worldChunkComponent.getBlockType(x, y, z);
            if (blockType == null) return false;

            if (!worldConfig.isBlockGatheringAllowed()) return false;
            if (!BlockHarvestUtils.shouldPickupByInteraction(blockType)) return false;

            EntityAnimationPacket swingHandPacket = new EntityAnimationPacket(connection.getNetworkId(), EntityAnimationPacket.Animation.SWING_HAND);
            connection.getChannel().writeAndFlush(swingHandPacket);

            int filler = blockSection.getFiller(x, y, z);
            BlockHarvestUtils.performPickupByInteraction(entityRef, targetBlock.toVector3i(), blockType, filler, chunkReference, store, chunkStoreStore);
        }

        return true;
    }

    public void placeBlock(ClientConnection connection, BlockPosition targetBlock, int face, World world, Store<EntityStore> store, Ref<EntityStore> entityRef) {
        ChunkStore chunkStore = world.getChunkStore();
        Store<ChunkStore> chunkStoreStore = chunkStore.getStore();

        BlockPosition placementPosition = getPlacementPosition(targetBlock, face);

        long chunkIndex = ChunkUtil.indexChunkFromBlock(placementPosition.getX(), placementPosition.getZ());
        Ref<ChunkStore> chunkReference = chunkStore.getChunkReference(chunkIndex);

        if (chunkReference == null || !chunkReference.isValid()) {
            return;
        }

        Player playerComponent = store.getComponent(entityRef, Player.getComponentType());
        TransformComponent transformComponent = store.getComponent(entityRef, TransformComponent.getComponentType());

        Inventory inventory = playerComponent.getInventory();
        if (inventory == null) {
            return;
        }

        byte heldSlot = inventory.getActiveHotbarSlot();
        ItemContainer heldItemContainer = inventory.getHotbar();
        if (heldItemContainer == null) {
            return;
        }

        ItemStack heldItemStack = heldItemContainer.getItemStack(heldSlot);
        if (heldItemStack == null) {
            return;
        }

        if (transformComponent != null) {
            Vector3d playerPosition = transformComponent.getPosition();
            int playerBlockX = (int) Math.floor(playerPosition.x);
            int playerBlockY = (int) Math.floor(playerPosition.y);
            int playerBlockZ = (int) Math.floor(playerPosition.z);

            if ((playerBlockX == placementPosition.getX() &&
                    playerBlockZ == placementPosition.getZ() &&
                    (playerBlockY == placementPosition.getY() ||
                            playerBlockY + 1 == placementPosition.getY()))) {
                return;
            }
        }

        if (transformComponent != null && playerComponent.getGameMode() != GameMode.Creative) {
            Vector3d playerPosition = transformComponent.getPosition();
            Vector3d blockCenter = new Vector3d(
                    (double) placementPosition.getX() + 0.5,
                    (double) placementPosition.getY() + 0.5,
                    (double) placementPosition.getZ() + 0.5
            );

            if (playerPosition.distanceSquaredTo(blockCenter) > 36.0) {
                return;
            }
        }

        String blockTypeKey = heldItemStack.getBlockKey();
        if (blockTypeKey == null) return;

        BlockType blockType = BlockType.getAssetMap().getAsset(blockTypeKey);
        if (blockType == null) return;

        if (placementPosition.getY() < 0 || placementPosition.getY() >= 320) {
            return;
        }

        BlockFace blockFace = BlockFace.values()[Math.min(face, BlockFace.values().length - 1)];

        BlockRotation blockRotation = new BlockRotation(
                Rotation.None,
                Rotation.None,
                Rotation.None
        );

        BlockPlaceUtils.placeBlock(
                entityRef,
                heldItemStack,
                blockTypeKey,
                heldItemContainer,
                blockFace.getDirection(),
                placementPosition.toVector3i(),
                blockRotation,
                inventory,
                heldSlot,
                true,
                chunkReference,
                chunkStoreStore,
                store
        );
    }

    private BlockPosition getPlacementPosition(BlockPosition clickedBlock, int face) {
        int x = clickedBlock.getX();
        int y = clickedBlock.getY();
        int z = clickedBlock.getZ();

        switch (face) {
            case 0:
                y--;
                break;
            case 1:
                y++;
                break;
            case 2:
                z--;
                break;
            case 3:
                z++;
                break;
            case 4:
                x--;
                break;
            case 5:
                x++;
                break;
        }

        return new BlockPosition(x, y, z);
    }
}