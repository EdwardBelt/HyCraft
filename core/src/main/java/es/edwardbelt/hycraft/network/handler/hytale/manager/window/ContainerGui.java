package es.edwardbelt.hycraft.network.handler.hytale.manager.window;

import com.hypixel.hytale.protocol.ItemWithAllMetadata;
import com.hypixel.hytale.protocol.packets.window.CloseWindow;
import es.edwardbelt.hycraft.api.connection.HyCraftConnection;
import es.edwardbelt.hycraft.api.gui.*;
import es.edwardbelt.hycraft.network.handler.minecraft.data.item.ItemStack;
import es.edwardbelt.hycraft.network.player.ClientConnection;
import lombok.Getter;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Getter
public class ContainerGui extends HyCraftGui {
    private static final HyCraftItemStack INVALID_ITEM = new HyCraftItemStack(1, HyCraftMaterial.BARRIER);

    private int id;
    private int size;
    private Set<Integer> invalidSlots;

    public ContainerGui(int id, String title, int size, Map<Integer, ItemWithAllMetadata> items) {
        super(title, getTypeBySize(size));
        this.id = id;
        this.size = size;
        this.invalidSlots = new HashSet<>();

        items.forEach((slot, hytaleItem) -> setItem(slot, ItemStack.fromHytaleToApi(hytaleItem)));
        if (size >= 54) return;

        int extra = size % 9;
        if (extra == 0) return;

        int invalidItems = 9 - extra;
        for (int i = 0; i < invalidItems; i++) {
            int slot = size + i;
            setItem(slot, INVALID_ITEM);
            invalidSlots.add(slot);
        }
    }

    public int getGuiSlotCount() {
        return (getType().ordinal() + 1) * 9;
    }

    public void clearItems() {
        getItems().keySet().removeIf(key -> !invalidSlots.contains(key));
    }

    @Override
    public boolean onClick(HyCraftConnection connection, int slot, HyCraftClickType clickType) {
        if (invalidSlots.contains(slot)) return true;
        return false;
    }

    @Override
    public void onClose(HyCraftConnection connection) {
        CloseWindow packet = new CloseWindow(id);
        ((ClientConnection) connection).getHytaleChannel().sendPacket(packet);
    }

    private static HyCraftGuiType getTypeBySize(int size) {
        int rows = (int) Math.ceil((double) size / 9);
        if (rows > 6) rows = 6;
        return HyCraftGuiType.values()[rows-1];
    }
}
