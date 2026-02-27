package es.edwardbelt.hycraft.protocol.packet.play;

import es.edwardbelt.hycraft.network.handler.minecraft.data.entity.Entity;
import es.edwardbelt.hycraft.protocol.io.PacketBuffer;
import es.edwardbelt.hycraft.protocol.packet.Packet;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class SpawnEntityPacket implements Packet {
    private Entity entity;

    @Override
    public void write(PacketBuffer buffer) {
        entity.serialize(buffer);
    }
}
