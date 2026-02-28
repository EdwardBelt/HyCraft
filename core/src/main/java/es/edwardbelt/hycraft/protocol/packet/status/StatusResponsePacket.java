package es.edwardbelt.hycraft.protocol.packet.status;

import es.edwardbelt.hycraft.network.handler.minecraft.data.StatusResponse;
import es.edwardbelt.hycraft.protocol.io.PacketBuffer;
import es.edwardbelt.hycraft.protocol.packet.Packet;
import es.edwardbelt.hycraft.util.GsonUtil;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class StatusResponsePacket implements Packet {
    private String response;

    public StatusResponsePacket(StatusResponse response) {
        this.response = GsonUtil.GSON.toJson(response);
    }

    @Override
    public void write(PacketBuffer buffer) {
        buffer.writeString(response);
    }
}
