package es.edwardbelt.hycraft.network.handler.minecraft.manager.inventory;

import com.hypixel.hytale.protocol.SmartMoveType;
import com.hypixel.hytale.protocol.packets.inventory.DropItemStack;
import com.hypixel.hytale.protocol.packets.inventory.MoveItemStack;
import com.hypixel.hytale.protocol.packets.inventory.SmartMoveItemStack;
import com.hypixel.hytale.server.core.inventory.Inventory;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.inventory.container.ItemContainer;
import es.edwardbelt.hycraft.network.player.ClientConnection;
import es.edwardbelt.hycraft.protocol.packet.play.ClickContainerPacket;
import es.edwardbelt.hycraft.protocol.packet.play.PlayerActionPacket;

public class InventoryManager {
    private static InventoryManager INSTANCE = new InventoryManager();
    public static InventoryManager get() { return INSTANCE; }

    private static final String EMPTY_ITEM_KEY = "Empty";

    public void handleClick(ClientConnection connection, Inventory inventory, short slot, byte button, ClickContainerPacket.Mode mode) {
/*
        System.out.println("received click in slot: " + slot);
        System.out.println("button was: " + button);
        System.out.println("mode was: " + mode.name());
        System.out.println("is cursor item null? " + (connection.getCursor().heldItem == null));
        System.out.println();*/

        if (mode.equals(ClickContainerPacket.Mode.DRAG)) {
            if (slot == -999) return;
            mode = ClickContainerPacket.Mode.NORMAL_CLICK;
            if (button == 1) button = 0;
            else if (button == 5) button = 1;
        }

        if (slot == -999) {
            handleClickOutside(connection, inventory);
            return;
        }

        switch (mode) {
            case NORMAL_CLICK -> handleNormalClick(connection, inventory, slot, button);
            case SHIFT_CLICK -> handleShiftClick(connection, inventory, slot);
            case NUMBER_KEY -> handleNumberKey(connection, inventory, slot, button);
            case DROP -> handleDrop(connection, inventory, slot, button);
            case DOUBLE_CLICK -> handleDoubleClick(connection, inventory, slot);
        }
    }

    private void handleClickOutside(ClientConnection connection, Inventory inventory) {
        InventoryCursor cursor = connection.getCursor();
        if (cursor.heldItem == null) return;

        resyncInventory(connection, inventory);
    }

    private void handleNormalClick(ClientConnection connection, Inventory inventory, short mcSlot, byte button) {
        HytaleSlot hytaleSlot = mcSlotToHytale(mcSlot);
        if (hytaleSlot == null) {
            resyncInventory(connection, inventory);
            return;
        }

        ItemContainer container = inventory.getSectionById(hytaleSlot.sectionId);
        if (container == null) return;

        ItemStack slotItem = container.getItemStack((short) hytaleSlot.slotId);

        if (button == 0) {
            handleLeftClick(connection, mcSlot, hytaleSlot, slotItem);
        } else if (button == 1) {
            handleRightClick(connection, mcSlot, hytaleSlot, slotItem);
        }
    }

    private void clearCursor(InventoryCursor cursor) {
        cursor.heldItem = null;
        cursor.lastClickedSection = -1;
        cursor.lastClickedSlot = -1;
        cursor.itemSlot = -1;
    }

    private void handleLeftClick(ClientConnection connection, short mcSlot, HytaleSlot hytaleSlot, ItemStack slotItem) {
        InventoryCursor cursor = connection.getCursor();
        boolean slotEmpty = isHytaleItemEmpty(slotItem);
        boolean cursorEmpty = cursor.heldItem == null;

        if (cursorEmpty && !slotEmpty) {
            cursor.heldItem = slotItem;
            cursor.lastClickedSection = hytaleSlot.sectionId;
            cursor.lastClickedSlot = hytaleSlot.slotId;
            cursor.itemSlot = mcSlot;
            return;
        }

        if (!cursorEmpty && slotEmpty) {
            MoveItemStack movePacket = new MoveItemStack(
                    cursor.lastClickedSection, cursor.lastClickedSlot,
                    cursor.heldItem.getQuantity(),
                    hytaleSlot.sectionId, hytaleSlot.slotId
            );
            connection.getHytaleChannel().sendPacket(movePacket);
            clearCursor(cursor);
            return;
        }

        if (!cursorEmpty && !slotEmpty) {
            boolean sameType = cursor.heldItem.getItemId().equals(slotItem.getItemId());

            if (sameType) {
                int total = cursor.heldItem.getQuantity() + slotItem.getQuantity();
                int maxStack = cursor.heldItem.getItem().getMaxStack();

                if (total <= maxStack) {
                    MoveItemStack movePacket = new MoveItemStack(
                            cursor.lastClickedSection, cursor.lastClickedSlot,
                            cursor.heldItem.getQuantity(),
                            hytaleSlot.sectionId, hytaleSlot.slotId
                    );
                    connection.getHytaleChannel().sendPacket(movePacket);
                    clearCursor(cursor);
                } else {
                    int canMove = maxStack - slotItem.getQuantity();
                    if (canMove > 0) {
                        MoveItemStack movePacket = new MoveItemStack(
                                cursor.lastClickedSection, cursor.lastClickedSlot,
                                canMove,
                                hytaleSlot.sectionId, hytaleSlot.slotId
                        );
                        connection.getHytaleChannel().sendPacket(movePacket);
                        cursor.heldItem = cursor.heldItem.withQuantity(cursor.heldItem.getQuantity() - canMove);
                    }
                }
            } else {
                MoveItemStack movePacket = new MoveItemStack(
                        cursor.lastClickedSection, cursor.lastClickedSlot,
                        cursor.heldItem.getQuantity(),
                        hytaleSlot.sectionId, hytaleSlot.slotId
                );
                connection.getHytaleChannel().sendPacket(movePacket);

                cursor.heldItem = slotItem;
                if (cursor.itemSlot < 0) cursor.itemSlot = mcSlot;
            }
        }
    }

    private void handleRightClick(ClientConnection connection, short mcSlot, HytaleSlot hytaleSlot, ItemStack slotItem) {
        InventoryCursor cursor = connection.getCursor();
        boolean slotEmpty = isHytaleItemEmpty(slotItem);
        boolean cursorEmpty = cursor.heldItem == null;

        if (cursorEmpty && !slotEmpty) {
            int totalQuantity = slotItem.getQuantity();
            int pickUpAmount = (totalQuantity + 1) / 2;

            cursor.heldItem = slotItem.withQuantity(pickUpAmount);
            cursor.lastClickedSection = hytaleSlot.sectionId;
            cursor.lastClickedSlot = hytaleSlot.slotId;
            cursor.itemSlot = mcSlot;
            return;
        }

        if (!cursorEmpty && slotEmpty) {
            MoveItemStack movePacket = new MoveItemStack(
                    cursor.lastClickedSection, cursor.lastClickedSlot,
                    1,
                    hytaleSlot.sectionId, hytaleSlot.slotId
            );
            connection.getHytaleChannel().sendPacket(movePacket);

            if (cursor.heldItem.getQuantity() <= 1) {
                clearCursor(cursor);
            } else {
                cursor.heldItem = cursor.heldItem.withQuantity(cursor.heldItem.getQuantity() - 1);
            }
            return;
        }

        if (!cursorEmpty && !slotEmpty) {
            boolean sameType = cursor.heldItem.getItemId().equals(slotItem.getItemId());

            if (sameType) {
                int maxStack = slotItem.getItem().getMaxStack();
                if (slotItem.getQuantity() < maxStack) {
                    MoveItemStack movePacket = new MoveItemStack(
                            cursor.lastClickedSection, cursor.lastClickedSlot,
                            1,
                            hytaleSlot.sectionId, hytaleSlot.slotId
                    );
                    connection.getHytaleChannel().sendPacket(movePacket);

                    if (cursor.heldItem.getQuantity() <= 1) {
                        clearCursor(cursor);
                    } else {
                        cursor.heldItem = cursor.heldItem.withQuantity(cursor.heldItem.getQuantity() - 1);
                    }
                }
            } else {
                MoveItemStack movePacket = new MoveItemStack(
                        cursor.lastClickedSection, cursor.lastClickedSlot,
                        cursor.heldItem.getQuantity(),
                        hytaleSlot.sectionId, hytaleSlot.slotId
                );
                connection.getHytaleChannel().sendPacket(movePacket);

                cursor.heldItem = slotItem;
                if (cursor.itemSlot < 0) cursor.itemSlot = mcSlot;
            }
        }
    }

    private void handleNumberKey(ClientConnection connection, Inventory inventory, short mcSlot, byte button) {
        InventoryCursor cursor = connection.getCursor();
        if (button < 0 || button > 8) return;

        HytaleSlot clickedSlot = mcSlotToHytale(mcSlot);
        if (clickedSlot == null) return;

        HytaleSlot hotbarSlot = new HytaleSlot(-1, button);

        ItemContainer clickedContainer = inventory.getSectionById(clickedSlot.sectionId);
        ItemContainer hotbarContainer = inventory.getSectionById(hotbarSlot.sectionId);

        if (clickedContainer == null || hotbarContainer == null) return;

        ItemStack clickedItem =
                clickedContainer.getItemStack((short) clickedSlot.slotId);
        ItemStack hotbarItem =
                hotbarContainer.getItemStack((short) hotbarSlot.slotId);

        boolean clickedEmpty = isHytaleItemEmpty(clickedItem);
        boolean hotbarEmpty = isHytaleItemEmpty(hotbarItem);

        if (!clickedEmpty) {
            MoveItemStack move1 = new MoveItemStack(
                    clickedSlot.sectionId, clickedSlot.slotId,
                    clickedItem.getQuantity(),
                    hotbarSlot.sectionId, hotbarSlot.slotId
            );
            connection.getHytaleChannel().sendPacket(move1);

        } else if (!hotbarEmpty) {
            MoveItemStack move = new MoveItemStack(
                    hotbarSlot.sectionId, hotbarSlot.slotId,
                    hotbarItem.getQuantity(),
                    clickedSlot.sectionId, clickedSlot.slotId
            );
            connection.getHytaleChannel().sendPacket(move);
        }

        cursor.heldItem = null;
    }

    private void handleDoubleClick(ClientConnection connection, Inventory inventory, short mcSlot) {
        InventoryCursor cursor = connection.getCursor();
        HytaleSlot hytaleSlot = mcSlotToHytale(mcSlot);
        if (hytaleSlot == null) return;

        ItemContainer container = inventory.getSectionById(hytaleSlot.sectionId);
        if (container == null) return;

        ItemStack slotItem =
                container.getItemStack((short) hytaleSlot.slotId);

        if (isHytaleItemEmpty(slotItem)) return;

        SmartMoveItemStack smartMove = new SmartMoveItemStack(
                hytaleSlot.sectionId,
                hytaleSlot.slotId,
                slotItem.getQuantity(),
                SmartMoveType.EquipOrMergeStack
        );
        connection.getHytaleChannel().sendPacket(smartMove);

        cursor.heldItem = null;
    }

    private void handleShiftClick(ClientConnection connection, Inventory inventory, short mcSlot) {
        HytaleSlot hytaleSlot = mcSlotToHytale(mcSlot);
        if (hytaleSlot == null) return;

        ItemContainer container = inventory.getSectionById(hytaleSlot.sectionId);
        ItemStack slotItem =
                container.getItemStack((short) hytaleSlot.slotId);

        if (slotItem == null || slotItem.isEmpty()) return;

        SmartMoveItemStack smartMove = new SmartMoveItemStack(
                hytaleSlot.sectionId,
                hytaleSlot.slotId,
                slotItem.getQuantity(),
                SmartMoveType.PutInHotbarOrWindow
        );
        connection.getHytaleChannel().sendPacket(smartMove);
    }

    private void handleDrop(ClientConnection connection, Inventory inventory,short mcSlot, int button) {
        HytaleSlot hytaleSlot = mcSlotToHytale(mcSlot);
        if (hytaleSlot == null) {
            resyncInventory(connection, inventory);
            return;
        }

        ItemContainer container = inventory.getSectionById(hytaleSlot.sectionId);
        if (container == null) return;

        ItemStack item = container.getItemStack((short) hytaleSlot.slotId);
        if (item == null || item.isEmpty()) {
            resyncInventory(connection, inventory);
            return;
        }

        int quantity = button == 0 ? 1 : item.getQuantity();

        DropItemStack packet = new DropItemStack(hytaleSlot.sectionId, hytaleSlot.slotId, quantity);
        connection.getHytaleChannel().sendPacket(packet);
    }


    private void resyncInventory(ClientConnection connection, Inventory inventory) {
        connection.getHytaleChannel().writeAndFlush(inventory.toPacket());
    }

    private HytaleSlot mcSlotToHytale(short mcSlot) {
        if (mcSlot >= 0 && mcSlot <= 4) {
            return null;
        } else if (mcSlot >= 5 && mcSlot <= 8) {
            return new HytaleSlot(-3, mcSlot - 5);
        } else if (mcSlot >= 9 && mcSlot <= 35) {
            return new HytaleSlot(-2, mcSlot - 9);
        } else if (mcSlot >= 36 && mcSlot <= 44) {
            return new HytaleSlot(-1, mcSlot - 36);
        } else if (mcSlot == 45) {
            return new HytaleSlot(-5, 0);
        }
        return null;
    }

    private boolean isHytaleItemEmpty(ItemStack item) {
        return item == null || item.getItemId().equals(EMPTY_ITEM_KEY);
    }

}
