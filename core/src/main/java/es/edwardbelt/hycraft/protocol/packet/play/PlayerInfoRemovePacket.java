package es.edwardbelt.hycraft.protocol.packet.play;

import es.edwardbelt.hycraft.protocol.io.PacketBuffer;
import es.edwardbelt.hycraft.protocol.packet.Packet;

import java.util.List;
import java.util.UUID;

public class PlayerInfoRemovePacket implements Packet {
    private final List<UUID> uuids;

    public PlayerInfoRemovePacket(List<UUID> uuids) {
        this.uuids = uuids;
    }

    @Override
    public void write(PacketBuffer buffer) {
        buffer.writeVarInt(uuids.size());
        for (UUID uuid : uuids) {
            buffer.writeUUID(uuid);
        }
    }
}