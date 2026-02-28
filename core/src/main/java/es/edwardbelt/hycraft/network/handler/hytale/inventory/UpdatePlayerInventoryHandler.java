package es.edwardbelt.hycraft.network.handler.hytale.inventory;

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
        List<ItemStack> inventoryItems = new ArrayList<>(46);

        for (int i = 0; i < 5; i++) {
            inventoryItems.add(ItemStack.EMPTY);
        }

        if (packet.armor != null && packet.armor.items != null) {
            for (int i = 0; i < 4; i++) {
                ItemWithAllMetadata hytaleItem = packet.armor.items.get(i);
                inventoryItems.add(hytaleItem != null ? ItemStack.fromHytale(hytaleItem) : ItemStack.EMPTY);
            }
        } else {
            for (int i = 0; i < 4; i++) {
                inventoryItems.add(ItemStack.EMPTY);
            }
        }

        if (packet.storage != null && packet.storage.items != null) {
            for (int i = 0; i < 27; i++) {
                ItemWithAllMetadata hytaleItem = packet.storage.items.get(i);
                inventoryItems.add(hytaleItem != null ? ItemStack.fromHytale(hytaleItem) : ItemStack.EMPTY);
            }
        } else {
            for (int i = 0; i < 27; i++) {
                inventoryItems.add(ItemStack.EMPTY);
            }
        }

        if (packet.hotbar != null && packet.hotbar.items != null) {
            for (int i = 0; i < 9; i++) {
                ItemWithAllMetadata hytaleItem = packet.hotbar.items.get(i);
                ItemStack item = hytaleItem != null ? ItemStack.fromHytale(hytaleItem) : ItemStack.EMPTY;
                item.setCooldown(1, "slot:"+i);
                inventoryItems.add(item);
            }
        } else {
            for (int i = 0; i < 9; i++) {
                inventoryItems.add(ItemStack.EMPTY);
            }
        }

        inventoryItems.add(ItemStack.EMPTY);

        InventoryCursor cursor = connection.getCursor();
        ItemStack cursorItem = cursor.heldItem != null ? ItemStack.fromHytale(cursor.heldItem.toPacket()) : ItemStack.EMPTY;
        int cursorSlotId = cursor.itemSlot;
        if (cursor.heldItem != null) inventoryItems.set(cursorSlotId, ItemStack.EMPTY);

        SetContainerContentPacket inventoryPacket = new SetContainerContentPacket(0, 0, inventoryItems, cursorItem);
        connection.getChannel().writeAndFlush(inventoryPacket);
    }
}