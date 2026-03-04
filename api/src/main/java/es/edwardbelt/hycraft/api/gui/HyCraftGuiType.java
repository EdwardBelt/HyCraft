package es.edwardbelt.hycraft.api.gui;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum HyCraftGuiType {
    ONE_ROW(0),
    TWO_ROWS(1),
    THREE_ROWS(2),
    FOUR_ROWS(3),
    FIVE_ROWS(4),
    SIX_ROWS(5),
    DISPENSER(6),
    CRAFTER(7),
    ANVIL(8),
    BEACON(9),
    BLAST_FURNACE(10),
    BREWING_STAND(11),
    CRAFTING(12),
    ENCHANTMENT(13),
    FURNACE(14),
    GRINDSTONE(15),
    HOPPER(16),
    LECTERN(17),
    LOOM(18),
    MERCHANT(19),
    SHULKER_BOX(20),
    SMITHING(21),
    SMOKER(22),
    CARTOGRAPHY(23),
    STONECUTTER(24)
    ;

    private final int id;
}
