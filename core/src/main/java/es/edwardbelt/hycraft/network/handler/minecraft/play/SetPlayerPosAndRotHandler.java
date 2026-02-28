package es.edwardbelt.hycraft.network.handler.minecraft.play;

import com.hypixel.hytale.protocol.Direction;
import com.hypixel.hytale.protocol.Position;
import com.hypixel.hytale.protocol.packets.player.ClientMovement;
import es.edwardbelt.hycraft.network.handler.PacketHandler;
import es.edwardbelt.hycraft.network.handler.hytale.HytaleUtil;
import es.edwardbelt.hycraft.network.player.ClientConnection;
import es.edwardbelt.hycraft.protocol.packet.play.SetPlayerPositionAndRotationPacket;

public class SetPlayerPosAndRotHandler implements PacketHandler<SetPlayerPositionAndRotationPacket> {
    @Override
    public void handle(SetPlayerPositionAndRotationPacket packet, ClientConnection connection) {
        Position position = new Position(packet.getX(), packet.getFeetY(), packet.getZ());
        Direction lookAt = HytaleUtil.getHytaleDirection(packet.getYaw(), packet.getPitch());
        Direction bodyAt = new Direction(lookAt.yaw, 0, 0);

        connection.getMovementStates().jumping = !packet.isOnGround();
        connection.getMovementStates().onGround = packet.isOnGround();

        connection.checkLastCenterChunk(position.x, position.z);

        ClientMovement hytalePacket = new ClientMovement(connection.getMovementStates(), null, position, bodyAt, lookAt, null, null, null, 0, null);
        connection.getHytaleChannel().sendPacket(hytalePacket);
    }
}
