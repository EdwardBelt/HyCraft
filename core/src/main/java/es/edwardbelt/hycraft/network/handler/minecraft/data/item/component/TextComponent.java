package es.edwardbelt.hycraft.network.handler.minecraft.data.item.component;

import es.edwardbelt.hycraft.protocol.io.PacketBuffer;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class TextComponent implements Component {
    private String text;

    @Override
    public void serialize(PacketBuffer buffer) {
        buffer.writeNBTStringTag(text);
    }
}
