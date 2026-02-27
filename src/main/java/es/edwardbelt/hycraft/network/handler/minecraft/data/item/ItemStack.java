package es.edwardbelt.hycraft.network.handler.minecraft.data.item;

import com.hypixel.hytale.protocol.ItemWithAllMetadata;
import com.hypixel.hytale.server.core.asset.type.item.config.Item;
import es.edwardbelt.hycraft.mapping.MappingRegistry;
import es.edwardbelt.hycraft.network.handler.minecraft.data.item.component.*;
import es.edwardbelt.hycraft.protocol.io.PacketBuffer;
import es.edwardbelt.hycraft.util.ItemUtil;
import es.edwardbelt.hycraft.util.LanguageUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemStack {
    public static ItemStack EMPTY = new ItemStack(0, 0);

    private int amount;
    private int itemId;
    private Map<Integer, Component> components;

    public ItemStack(int amount, int itemId) {
        this.amount = amount;
        this.itemId = itemId;
        this.components = new HashMap<>();
    }

    public static ItemStack fromHytale(ItemWithAllMetadata item) {
        int itemId = MappingRegistry.get().getItemMapper().getMapping(item.itemId);
        Item itemConfig = ItemUtil.getItemConfig(item.itemId);
        if (itemConfig == null) return ItemStack.EMPTY;

        String name = LanguageUtil.getMessage(itemConfig.getTranslationProperties().getName());
        String desc = LanguageUtil.getMessage(itemConfig.getTranslationProperties().getDescription());

        List<String> lore = new ArrayList<>();
        lore.add("§8ID: " + item.itemId);

        if (desc != null) {
            lore.add("§r");
            lore.addAll(ItemUtil.hytaleItemDescToList(desc));
        }

        int maxStack = itemConfig.getMaxStack();
        if (maxStack > 99) maxStack = 99;

        ItemStack itemStack = new ItemStack(item.quantity, itemId);
        itemStack.setName("§f"+name);
        itemStack.setMaxStack(maxStack);
        itemStack.setLore(lore);

        if (item.maxDurability > 0) {
            itemStack.setMaxDamage((int) item.maxDurability);
            itemStack.setDamage((int) (item.maxDurability-item.durability));
        }

        return itemStack;
    }

    public static ItemStack fromHytaleItemId(String itemId) {
        return fromHytale(new ItemWithAllMetadata(itemId, 1, 1, 1, false, null));
    }

    public void setCooldown(float seconds, String identifier) {
        components.put(23, new CooldownComponent(seconds, identifier));
    }

    public void setCanBlockAttacks() {
        components.put(33, new BlockAttacksComponent());
    }

    public void setName(String name) {
        components.put(5, new TextComponent(name));
    }

    public void setMaxStack(int maxStack) {
        components.put(1, new VarIntComponent(maxStack));
    }

    public void setDamage(int damage) {
        components.put(3, new VarIntComponent(damage));
    }

    public void setMaxDamage(int maxDamage) {
        components.put(2, new VarIntComponent(maxDamage));
    }

    public void setLore(List<String> lore) {
        components.put(8, new TextListComponent(lore));
    }

    public void serialize(PacketBuffer buffer) {
        buffer.writeVarInt(amount);
        if (amount == 0) return;

        buffer.writeVarInt(itemId);
        buffer.writeVarInt(components.size());
        buffer.writeVarInt(0);

        components.forEach((id, component) -> {
            buffer.writeVarInt(id);
            component.serialize(buffer);
        });
    }
}
