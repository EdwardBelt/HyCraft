package es.edwardbelt.hycraft.api.gui;

import es.edwardbelt.hycraft.api.connection.HyCraftConnection;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public abstract class HyCraftGui {
    private String title;
    private HyCraftGuiType type;
    private Map<Integer, HyCraftItemStack> items;

    public HyCraftGui(String title, HyCraftGuiType type) {
        this.title = title;
        this.type = type;
        this.items = new HashMap<>();
    }

    public void setItem(int slot, HyCraftItemStack item) {
        items.put(slot, item);
    }

    public abstract boolean onClick(HyCraftConnection connection, int slot, HyCraftClickType clickType);
    public abstract void onClose(HyCraftConnection connection);
}
