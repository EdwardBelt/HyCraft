package es.edwardbelt.hycraft.protocol.packet.login;

import es.edwardbelt.hycraft.protocol.io.PacketBuffer;
import es.edwardbelt.hycraft.protocol.packet.Packet;
import lombok.Getter;

@Getter
public class EncryptionResponsePacket implements Packet {
    private byte[] encryptedSharedSecret;
    private byte[] encryptedVerifyToken;

    @Override
    public void read(PacketBuffer buffer) {
        int secretLength = buffer.readVarInt();
        this.encryptedSharedSecret = new byte[secretLength];
        buffer.readBytes(this.encryptedSharedSecret);

        int tokenLength = buffer.readVarInt();
        this.encryptedVerifyToken = new byte[tokenLength];
        buffer.readBytes(this.encryptedVerifyToken);
    }
}