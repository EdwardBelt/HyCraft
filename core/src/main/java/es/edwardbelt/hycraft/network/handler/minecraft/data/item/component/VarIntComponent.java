package es.edwardbelt.hycraft.network.handler.minecraft.data.item.component;

import es.edwardbelt.hycraft.protocol.io.PacketBuffer;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class VarIntComponent implements Component {
    private int varInt;

    @Override
    public void serialize(PacketBuffer buffer) {
        buffer.writeVarInt(varInt);
    }
}
