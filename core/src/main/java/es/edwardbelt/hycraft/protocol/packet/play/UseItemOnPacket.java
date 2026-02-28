package es.edwardbelt.hycraft.protocol.packet.play;

import es.edwardbelt.hycraft.network.handler.minecraft.data.BlockPosition;
import es.edwardbelt.hycraft.protocol.io.PacketBuffer;
import es.edwardbelt.hycraft.protocol.packet.Packet;
import lombok.Getter;

@Getter
public class UseItemOnPacket implements Packet {
    private int hand;
    private BlockPosition position;
    private int face;
    private float cursorX;
    private float cursorY;
    private float cursorZ;
    private boolean insideBlock;
    private boolean worldBorderHit;
    private int sequence;

    @Override
    public void read(PacketBuffer buffer) {
        this.hand = buffer.readVarInt();
        this.position = BlockPosition.fromLong(buffer.readLong());
        this.face = buffer.readVarInt();
        this.cursorX = buffer.readFloat();
        this.cursorY = buffer.readFloat();
        this.cursorZ = buffer.readFloat();
        this.insideBlock = buffer.readBoolean();
        this.worldBorderHit = buffer.readBoolean();
        this.sequence = buffer.readVarInt();
    }
}
