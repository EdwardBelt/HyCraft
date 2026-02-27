package es.edwardbelt.hycraft.protocol.packet.configuration;

import es.edwardbelt.hycraft.protocol.io.PacketBuffer;
import es.edwardbelt.hycraft.protocol.packet.Packet;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@ToString
@Getter
public class PluginMessagePacket implements Packet {
    private String channel;
    private byte[] data;

    @Override
    public void read(PacketBuffer buffer) {
        this.channel = buffer.readString(32767);

        int dataLength = buffer.readableBytes();
        this.data = new byte[dataLength];
        buffer.getBuffer().readBytes(this.data);
    }
}