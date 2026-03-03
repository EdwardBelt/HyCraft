package es.edwardbelt.hycraft.protocol.packet.play;

import es.edwardbelt.hycraft.network.handler.minecraft.data.nbt.NbtString;
import es.edwardbelt.hycraft.protocol.io.PacketBuffer;
import es.edwardbelt.hycraft.protocol.packet.Packet;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class SetActionBarPacket implements Packet {
    private NbtString text;

    public SetActionBarPacket(String text) {
        this.text = new NbtString(text);
    }

    @Override
    public void write(PacketBuffer buffer) {
        text.write(buffer);
    }
}
