package es.edwardbelt.hycraft.protocol.packet.login;

import es.edwardbelt.hycraft.protocol.io.PacketBuffer;
import es.edwardbelt.hycraft.protocol.packet.Packet;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class EncryptionRequestPacket implements Packet {
    private final String serverId;
    private final byte[] publicKey;
    private final byte[] verifyToken;
    private final boolean shouldAuthenticate;

    @Override
    public void write(PacketBuffer buffer) {
        buffer.writeString(serverId);

        buffer.writeVarInt(publicKey.length);
        buffer.writeBytes(publicKey);

        buffer.writeVarInt(verifyToken.length);
        buffer.writeBytes(verifyToken);

        buffer.writeBoolean(shouldAuthenticate);
    }
}