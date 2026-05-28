package es.edwardbelt.hycraft.network.handler.minecraft.play;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.protocol.packets.inventory.DropItemStack;
import com.hypixel.hytale.server.core.inventory.InventoryComponent;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
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
            ItemStack hand = InventoryComponent.getItemInHand(store, ref);
            if (hand == null || hand.isEmpty()) return;

            InventoryComponent.Hotbar hotbarComponent = store.getComponent(ref, InventoryComponent.Hotbar.getComponentType());
            if (hotbarComponent == null) return;

            int quantity = type.equals(PlayerActionPacket.Status.DROP_ITEM) ? 1 : hand.getQuantity();
            int slotId = hotbarComponent.getActiveSlot();

            DropItemStack packet = new DropItemStack(InventoryComponent.HOTBAR_SECTION_ID, slotId, quantity);
            connection.getHytaleChannel().sendPacket(packet);
        });
    }
}
