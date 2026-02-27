package es.edwardbelt.hycraft.network.handler.minecraft.data.item.component;

import es.edwardbelt.hycraft.protocol.io.PacketBuffer;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class TextListComponent implements Component {
    private List<String> textList;

    @Override
    public void serialize(PacketBuffer buffer) {
        buffer.writeVarInt(textList.size());
        textList.forEach(buffer::writeNBTStringTag);
    }
}
