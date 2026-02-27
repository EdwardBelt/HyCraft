package es.edwardbelt.hycraft.protocol.packet.play;

import es.edwardbelt.hycraft.network.handler.minecraft.data.BlockPosition;
import es.edwardbelt.hycraft.protocol.io.PacketBuffer;
import es.edwardbelt.hycraft.protocol.packet.Packet;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SetBlockDestroyStagePacket implements Packet {
    private int entityId;
    private BlockPosition location;
    private int destroyStage;

    @Override
    public void write(PacketBuffer buffer) {
        buffer.writeVarInt(entityId);
        buffer.writeLong(location.toLong());
        buffer.writeByte(destroyStage);
    }
}