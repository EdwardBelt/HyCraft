package es.edwardbelt.hycraft.network.handler.minecraft.manager.gui;

import com.hypixel.hytale.server.core.inventory.Inventory;
import es.edwardbelt.hycraft.api.gui.HyCraftClickType;
import es.edwardbelt.hycraft.api.gui.HyCraftGui;
import es.edwardbelt.hycraft.api.gui.HyCraftItemStack;
import es.edwardbelt.hycraft.network.handler.minecraft.data.item.ItemStack;
import es.edwardbelt.hycraft.network.handler.minecraft.manager.inventory.InventoryManager;
import es.edwardbelt.hycraft.network.player.ClientConnection;
import es.edwardbelt.hycraft.protocol.packet.play.ClickContainerPacket;
import es.edwardbelt.hycraft.protocol.packet.play.CloseContainerPacket;
import es.edwardbelt.hycraft.protocol.packet.play.OpenScreenPacket;
import es.edwardbelt.hycraft.protocol.packet.play.SetContainerContentPacket;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class GuiManager {
    private static GuiManager INSTANCE = new GuiManager();
    public static GuiManager get() { return INSTANCE; }

    public void openGui(ClientConnection connection, HyCraftGui gui) {
        int windowId = connection.getNextWindowId();
        connection.setOpenedGui(gui);
        sendGuiPackets(connection, windowId, gui);
    }

    public void updateGui(ClientConnection connection, HyCraftGui gui) {
        sendGuiPackets(connection, connection.getWindowId(), gui);
    }

    public void sendGuiPackets(ClientConnection connection, int windowId, HyCraftGui gui) {
        OpenScreenPacket packet = new OpenScreenPacket(windowId, gui.getType(), gui.getTitle());
        SetContainerContentPacket contentPacket = new SetContainerContentPacket(windowId, 0, itemsToList(gui.getItems()), ItemStack.EMPTY);

        connection.getChannel().write(packet);
        connection.getChannel().write(contentPacket);
        connection.getChannel().flush();
    }

    public void onCloseGui(ClientConnection connection) {
        HyCraftGui gui = connection.getOpenedGui();
        if (gui == null) return;
        gui.onClose(connection);
        connection.setOpenedGui(null);
    }

    public void closeGui(ClientConnection connection) {
        onCloseGui(connection);
        CloseContainerPacket packet = new CloseContainerPacket(0);
        connection.getChannel().writeAndFlush(packet);
    }

    public void clickGui(ClientConnection connection, Inventory inventory, int slot, ClickContainerPacket.Mode mode, int button) {
        HyCraftGui gui = connection.getOpenedGui();
        if (gui == null) {
            closeGui(connection);
            InventoryManager.get().resyncInventory(connection, inventory);
            return;
        }

        HyCraftClickType clickType = getClickType(mode, button);

        if (gui.blockItemUpdates() || clickType == null) {
            InventoryManager.get().resyncInventory(connection, inventory);
            updateGui(connection, gui);
            if (clickType == null) return;
        }

        gui.onClick(connection, slot, clickType);
    }

    public HyCraftGui getOpenedGui(ClientConnection connection) {
        return connection.getOpenedGui();
    }

    public HyCraftClickType getClickType(ClickContainerPacket.Mode mode, int button) {
        switch (mode) {
            case NORMAL_CLICK -> {
                if (button == 0) return HyCraftClickType.LEFT_CLICK;
                else if (button == 1) return HyCraftClickType.RIGHT_CLICK;
            }
            case SHIFT_CLICK -> {
                if (button == 0) return HyCraftClickType.SHIFT_LEFT_CLICK;
                else if (button == 1) return HyCraftClickType.SHIFT_RIGHT_CLICK;
            }
            case DROP -> {
                if (button == 0) return HyCraftClickType.DROP;
                else if (button == 1) return HyCraftClickType.DROP_ALL;
            }
            case DOUBLE_CLICK -> {
                if (button == 0) return HyCraftClickType.DOUBLE_CLICK;
            }
        }

        return null;
    }

    public List<ItemStack> itemsToList(Map<Integer, HyCraftItemStack> items) {
        if (items.isEmpty()) {
            return new ArrayList<>();
        }

        int min = Collections.min(items.keySet());
        int max = Collections.max(items.keySet());

        List<ItemStack> list = new ArrayList<>();
        for (int i = min; i <= max; i++) {
            HyCraftItemStack apiItem = items.get(i);
            ItemStack item = apiItem != null ? ItemStack.fromApi(apiItem) : ItemStack.EMPTY;
            list.add(item);
        }

        return list;
    }
}
