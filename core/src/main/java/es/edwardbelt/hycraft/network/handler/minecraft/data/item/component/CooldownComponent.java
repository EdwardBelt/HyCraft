package es.edwardbelt.hycraft.network.handler.minecraft.data.item.component;

import es.edwardbelt.hycraft.protocol.io.PacketBuffer;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CooldownComponent implements Component {
    private float seconds;
    private String identifier;

    @Override
    public void serialize(PacketBuffer buffer) {
        buffer.writeFloat(seconds);
        buffer.writeBoolean(true);
        buffer.writeString(identifier);
    }
}
