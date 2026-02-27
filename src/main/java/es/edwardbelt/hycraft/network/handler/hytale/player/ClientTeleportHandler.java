package es.edwardbelt.hycraft.network.handler.hytale.player;

import com.hypixel.hytale.protocol.packets.player.ClientTeleport;
import es.edwardbelt.hycraft.network.handler.PacketHandler;
import es.edwardbelt.hycraft.network.handler.hytale.HytaleUtil;
import es.edwardbelt.hycraft.network.player.ClientConnection;
import es.edwardbelt.hycraft.protocol.packet.play.PlayerPositionPacket;

public class ClientTeleportHandler implements PacketHandler<ClientTeleport> {
    @Override
    public void handle(ClientTeleport packet, ClientConnection connection) {
        double x = packet.modelTransform.position.x;
        double y = packet.modelTransform.position.y;
        double z = packet.modelTransform.position.z;

        float yaw = HytaleUtil.getMinecraftYaw(packet.modelTransform.bodyOrientation.yaw);
        float pitch = HytaleUtil.getMinecraftPitch(packet.modelTransform.lookOrientation.pitch);

        connection.checkLastCenterChunk(x, z);

        PlayerPositionPacket tpPacket = new PlayerPositionPacket(
                packet.teleportId,
                x, y, z,
                0, 0, 0,
                yaw, pitch,
                0
        );
        connection.getChannel().writeAndFlush(tpPacket);

        connection.getTpConfirmations().put(packet.teleportId, packet.modelTransform.position);
    }
}
