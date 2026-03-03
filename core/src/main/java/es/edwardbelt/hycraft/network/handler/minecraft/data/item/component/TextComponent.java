package es.edwardbelt.hycraft.network.handler.minecraft.data.item.component;

import es.edwardbelt.hycraft.network.handler.minecraft.data.nbt.NbtString;
import es.edwardbelt.hycraft.protocol.io.PacketBuffer;

public class TextComponent implements Component {
    private final NbtString text;

    public TextComponent(String value) {
        this.text = new NbtString(value);
    }

    @Override
    public void serialize(PacketBuffer buffer) {
        text.write(buffer);
    }
}
