package es.edwardbelt.hycraft.network.handler.minecraft.play;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.protocol.packets.inventory.DropItemStack;
import com.hypixel.hytale.server.core.entity.LivingEntity;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.inventory.Inventory;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.npc.util.InventoryHelper;
import es.edwardbelt.hycraft.network.handler.PacketHandler;
import es.edwardbelt.hycraft.network.handler.minecraft.manager.blockbreak.BlockBreakManager;
import es.edwardbelt.hycraft.network.player.ClientConnection;
import es.edwardbelt.hycraft.protocol.packet.play.PlayerActionPacket;

public class PlayerActionHandler implements PacketHandler<PlayerActionPacket> {
    @Override
    public void handle(PlayerActionPacket packet, ClientConnection connection) {
        if (packet.getStatus().name().endsWith("DIGGING")) {
            BlockBreakManager.get().handle(connection, packet.getPosition(), packet.getStatus(), packet.getSequence());
        } else if (packet.getStatus().name().startsWith("DROP")) {
            handleItemDrop(packet.getStatus(), connection);
        }
    }

    private void handleItemDrop(PlayerActionPacket.Status type, ClientConnection connection) {
        Ref<EntityStore> ref = connection.getPlayerRef().getReference();
        Store<EntityStore> store = ref.getStore();
        World world = store.getExternalData().getWorld();

        world.execute(() -> {
            LivingEntity playerEntity = store.getComponent(ref, Player.getComponentType());
            Inventory inventory = playerEntity.getInventory();
            ItemStack hand = inventory.getActiveHotbarItem();
            if (hand == null || hand.isEmpty()) return;

            int quantity = type.equals(PlayerActionPacket.Status.DROP_ITEM) ? 1 : hand.getQuantity();
            int slotId = inventory.getActiveHotbarSlot();

            DropItemStack packet = new DropItemStack(-1, slotId, quantity);
            connection.getHytaleChannel().sendPacket(packet);
        });
    }
}
