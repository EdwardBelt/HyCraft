package es.edwardbelt.hycraft.protocol.packet.handshake;

import es.edwardbelt.hycraft.protocol.ConnectionState;
import es.edwardbelt.hycraft.protocol.io.PacketBuffer;
import es.edwardbelt.hycraft.protocol.packet.Packet;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class HandshakePacket implements Packet {
    private int protocolVersion;
    private String serverAddress;
    private int serverPort;
    private int intent;

    @Override
    public void read(PacketBuffer buffer) {
        this.protocolVersion = buffer.readVarInt();
        this.serverAddress = buffer.readString(255);
        this.serverPort = buffer.readUnsignedShort();
        this.intent = buffer.readVarInt();
    }

    public ConnectionState getConnectionState() {
        return ConnectionState.fromHandshakeIntent(intent);
    }
}