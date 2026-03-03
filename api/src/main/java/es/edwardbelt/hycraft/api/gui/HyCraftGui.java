package es.edwardbelt.hycraft.api.gui;

import lombok.Getter;

import java.util.Map;

@Getter
public abstract class HyCraftGui {
    private String title;
    private int size;
    private Map<Integer, HyCraftItemStack> items;

    public HyCraftGui(String title, int size) {
        this.title = title;
        this.size = size;
    }

    public void setItem(int slot, HyCraftItemStack item) {
        items.put(slot, item);
    }

    abstract void onClick(int slot, HyCraftClickType clickType);
}
