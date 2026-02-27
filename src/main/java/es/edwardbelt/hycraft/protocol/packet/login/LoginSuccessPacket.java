package es.edwardbelt.hycraft.protocol.packet.login;

import es.edwardbelt.hycraft.network.handler.minecraft.data.profile.GameProfile;
import es.edwardbelt.hycraft.network.handler.minecraft.data.profile.Property;
import es.edwardbelt.hycraft.protocol.io.PacketBuffer;
import es.edwardbelt.hycraft.protocol.packet.Packet;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class LoginSuccessPacket implements Packet {
    private GameProfile profile;

    @Override
    public void write(PacketBuffer buffer) {
        buffer.writeUUID(profile.getUuid());
        buffer.writeString(profile.getUsername());
        buffer.writeVarInt(profile.getProperties().size());

        for (Property property : profile.getProperties()) {
            buffer.writeString(property.getName());
            buffer.writeString(property.getValue());

            if (property.isSigned()) {
                buffer.writeBoolean(true);
                buffer.writeString(property.getSignature());
            } else {
                buffer.writeBoolean(false);
            }
        }
    }
}
