package es.edwardbelt.hycraft.network.handler.minecraft.manager.inventory;

import com.hypixel.hytale.server.core.inventory.ItemStack;

public class InventoryCursor {
    public ItemStack heldItem = null;
    int lastClickedSection = 0;
    int lastClickedSlot = 0;
    public int itemSlot = 0;
}
