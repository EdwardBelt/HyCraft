package es.edwardbelt.hycraft.network.handler.hytale.inventory;

import com.hypixel.hytale.protocol.InventorySection;
import com.hypixel.hytale.protocol.ItemWithAllMetadata;
import com.hypixel.hytale.protocol.packets.inventory.UpdatePlayerInventory;
import es.edwardbelt.hycraft.network.handler.PacketHandler;
import es.edwardbelt.hycraft.network.handler.minecraft.data.item.ItemStack;
import es.edwardbelt.hycraft.network.handler.minecraft.manager.inventory.InventoryCursor;
import es.edwardbelt.hycraft.network.player.ClientConnection;
import es.edwardbelt.hycraft.protocol.packet.play.SetContainerContentPacket;

import java.util.ArrayList;
import java.util.List;

public class UpdatePlayerInventoryHandler implements PacketHandler<UpdatePlayerInventory> {
    @Override
    public void handle(UpdatePlayerInventory packet, ClientConnection connection) {
        if (packet.hotbar != null) {
            connection.setCachedHotbar(packet.hotbar);
        }
        if (packet.storage != null) {
            connection.setCachedStorage(packet.storage);
        }
        if (packet.armor != null) {
            connection.setCachedArmor(packet.armor);
        }

        InventorySection hotbar = connection.getCachedHotbar();
        InventorySection storage = connection.getCachedStorage();
        InventorySection armor = connection.getCachedArmor();

        List<ItemStack> inventoryItems = new ArrayList<>(46);

        // crafting slots
        for (int i = 0; i < 5; i++) {
            inventoryItems.add(ItemStack.EMPTY);
        }

        // armor
        for (int i = 0; i < 4; i++) {
            inventoryItems.add(getItem(armor, i));
        }

        // storage
        for (int i = 0; i < 27; i++) {
            inventoryItems.add(getItem(storage, i));
        }

        // hotbar
        for (int i = 0; i < 9; i++) {
            ItemStack item = getItem(hotbar, i);
            item.setCooldown(1, "slot:" + i);
            inventoryItems.add(item);
        }

        // offhand
        inventoryItems.add(ItemStack.EMPTY);

        InventoryCursor cursor = connection.getCursor();
        ItemStack cursorItem = cursor.heldItem != null ? ItemStack.fromHytale(cursor.heldItem.toPacket()) : ItemStack.EMPTY;
        int cursorSlotId = cursor.itemSlot;
        if (cursor.heldItem != null) inventoryItems.set(cursorSlotId, ItemStack.EMPTY);

        SetContainerContentPacket inventoryPacket = new SetContainerContentPacket(0, 0, inventoryItems, cursorItem);
        connection.getChannel().writeAndFlush(inventoryPacket);
    }

    private ItemStack getItem(InventorySection section, int slot) {
        if (section == null || section.items == null) return ItemStack.EMPTY;
        ItemWithAllMetadata hytaleItem = section.items.get(slot);
        return hytaleItem != null ? ItemStack.fromHytale(hytaleItem) : ItemStack.EMPTY;
    }
}