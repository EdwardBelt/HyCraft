package es.edwardbelt.hycraft.network.handler.minecraft.data.entity.metadata;

import es.edwardbelt.hycraft.network.handler.minecraft.data.item.ItemStack;
import es.edwardbelt.hycraft.protocol.io.PacketBuffer;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class SlotMetadataValue implements MetadataValue {
    private ItemStack item;

    @Override
    public void serialize(PacketBuffer buffer) {
        item.serialize(buffer);
    }

    @Override
    public int getTypeId() {
        return 7;
    }
}
