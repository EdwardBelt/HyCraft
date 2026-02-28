package es.edwardbelt.hycraft.protocol.packet.play;

import es.edwardbelt.hycraft.network.handler.minecraft.data.item.ItemStack;
import es.edwardbelt.hycraft.protocol.io.PacketBuffer;
import es.edwardbelt.hycraft.protocol.packet.Packet;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class SetContainerContentPacket implements Packet {
    private int windowId;
    private int stateId;
    private List<ItemStack> items;
    private ItemStack carriedItem;

    @Override
    public void write(PacketBuffer buffer) {
        buffer.writeVarInt(windowId);
        buffer.writeVarInt(stateId);

        buffer.writeVarInt(items.size());
        for (ItemStack item : items) {
            item.serialize(buffer);
        }

        carriedItem.serialize(buffer);
    }
}
