package es.edwardbelt.hycraft.network.handler.hytale.manager.window;

import com.hypixel.hytale.protocol.InventorySection;
import es.edwardbelt.hycraft.api.gui.HyCraftGui;
import es.edwardbelt.hycraft.network.handler.minecraft.data.item.ItemStack;
import es.edwardbelt.hycraft.network.handler.minecraft.manager.gui.GuiManager;
import es.edwardbelt.hycraft.network.player.ClientConnection;

public class WindowManager {
    private static final WindowManager INSTANCE = new WindowManager();
    public static WindowManager get() { return INSTANCE; }

    public void openContainer(int id, ClientConnection connection, InventorySection inventory) {
        ContainerGui gui = new ContainerGui(id, "Chest", inventory.capacity, inventory.items);
        GuiManager.get().openGui(connection, gui);
    }

    public void updateContainer(ClientConnection connection, InventorySection inventory) {
        HyCraftGui gui = GuiManager.get().getOpenedGui(connection);
        if (!(gui instanceof ContainerGui container)) return;
        container.clearItems();
        inventory.items.forEach((slot, hytaleItem) -> gui.setItem(slot, ItemStack.fromHytaleToApi(hytaleItem)));
        GuiManager.get().updateGui(connection, gui);
    }
}
