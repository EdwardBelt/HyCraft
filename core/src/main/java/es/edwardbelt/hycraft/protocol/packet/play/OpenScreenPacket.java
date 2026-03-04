package es.edwardbelt.hycraft.protocol.packet.play;

import es.edwardbelt.hycraft.api.gui.HyCraftGuiType;
import es.edwardbelt.hycraft.network.handler.minecraft.data.nbt.NbtString;
import es.edwardbelt.hycraft.protocol.io.PacketBuffer;
import es.edwardbelt.hycraft.protocol.packet.Packet;

public class OpenScreenPacket implements Packet {
    private int windowId;
    private HyCraftGuiType windowType;
    private NbtString title;

    public OpenScreenPacket(int windowId, HyCraftGuiType windowType, String title) {
        this.windowId = windowId;
        this.windowType = windowType;
        this.title = new NbtString(title);
    }

    @Override
    public void write(PacketBuffer buffer) {
        buffer.writeVarInt(windowId);
        buffer.writeVarInt(windowType.getId());
        title.write(buffer);
    }
}
