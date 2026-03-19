package es.edwardbelt.hycraft.protocol.packet.play;

import es.edwardbelt.hycraft.network.handler.minecraft.data.nbt.NbtTag;
import es.edwardbelt.hycraft.protocol.io.PacketBuffer;
import es.edwardbelt.hycraft.protocol.packet.Packet;

public class SystemMessagePacket implements Packet {
    private String message;
    private NbtTag tag;

    public SystemMessagePacket(String message) {
        this.message = message;
    }

    public SystemMessagePacket(NbtTag tag) {
        this.tag = tag;
    }

    @Override
    public void write(PacketBuffer buffer) {
        if (tag != null) {
            tag.write(buffer);
        } else {
            buffer.writeNBTStringTag(message);
        }
        buffer.writeBoolean(false);
    }
}
