package es.edwardbelt.hycraft.network.handler.minecraft.data.item.component;

import es.edwardbelt.hycraft.network.handler.minecraft.data.nbt.NbtString;
import es.edwardbelt.hycraft.protocol.io.PacketBuffer;

import java.util.List;

public class TextListComponent implements Component {
    private List<NbtString> textList;

    public TextListComponent(List<String> valueList) {
        this.textList = valueList.stream().map(NbtString::new).toList();
    }

    @Override
    public void serialize(PacketBuffer buffer) {
        buffer.writeVarInt(textList.size());
        textList.forEach(text -> text.write(buffer));
    }
}
