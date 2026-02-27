package es.edwardbelt.hycraft.protocol.packet.play;

import es.edwardbelt.hycraft.protocol.io.PacketBuffer;
import es.edwardbelt.hycraft.protocol.packet.Packet;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class SetHealthPacket implements Packet {
    private float health;
    private int food;
    private float foodSaturation;

    @Override
    public void write(PacketBuffer buffer) {
        buffer.writeFloat(health);
        buffer.writeVarInt(food);
        buffer.writeFloat(foodSaturation);
    }
}
