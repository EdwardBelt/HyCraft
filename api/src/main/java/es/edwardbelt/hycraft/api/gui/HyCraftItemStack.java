package es.edwardbelt.hycraft.api.gui;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class HyCraftItemStack {
    private int amount;
    private HyCraftMaterial material;
    private String name;
    private List<String> lore;

    public HyCraftItemStack(int amount, HyCraftMaterial material) {
        this.amount = amount;
        this.material = material;
    }
}
