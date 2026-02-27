package es.edwardbelt.hycraft.network.handler.minecraft.data.item.component;

import es.edwardbelt.hycraft.protocol.io.PacketBuffer;

public interface Component {
    public void serialize(PacketBuffer buffer);
}
