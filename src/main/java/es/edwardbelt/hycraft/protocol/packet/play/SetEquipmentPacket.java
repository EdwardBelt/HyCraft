package es.edwardbelt.hycraft.protocol.packet.play;

import es.edwardbelt.hycraft.network.handler.minecraft.data.item.ItemStack;
import es.edwardbelt.hycraft.protocol.io.PacketBuffer;
import es.edwardbelt.hycraft.protocol.packet.Packet;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
public class SetEquipmentPacket implements Packet {
    private int entityId;
    private Map<Type, ItemStack> equipment;

    @Override
    public void write(PacketBuffer buffer) {
        buffer.writeVarInt(entityId);

        List<Map.Entry<Type, ItemStack>> entries = new ArrayList<>(equipment.entrySet());
        for (int i = 0; i < entries.size(); i++) {
            Map.Entry<Type, ItemStack> entry = entries.get(i);
            boolean hasNext = i < entries.size() - 1;

            int slotByte = entry.getKey().getId();
            if (hasNext) slotByte |= 0x80;

            buffer.writeByte(slotByte);
            entry.getValue().serialize(buffer);
        }
    }

    @AllArgsConstructor
    public enum Type {
        MAIN_HAND(0),
        OFF_HAND(1),
        BOOTS(2),
        LEGGINGS(3),
        CHESTPLATE(4),
        HELMET(5),
        BODY(6),
        SADDLE(7);

        @Getter
        private final int id;
        private static final Map<Integer, Type> idToType = new HashMap<>();

        static {
            for (Type type : values()) {
                idToType.put(type.id, type);
            }
        }

        public static Type fromId(int id) {
            return idToType.get(id);
        }
    }
}
