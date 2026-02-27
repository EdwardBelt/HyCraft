package es.edwardbelt.hycraft.network.handler.minecraft.play;

import com.hypixel.hytale.protocol.Position;
import com.hypixel.hytale.protocol.packets.player.ClientMovement;
import es.edwardbelt.hycraft.network.handler.PacketHandler;
import es.edwardbelt.hycraft.network.player.ClientConnection;
import es.edwardbelt.hycraft.protocol.packet.play.SetPlayerPositionPacket;

public class SetPlayerPosHandler implements PacketHandler<SetPlayerPositionPacket> {
    @Override
    public void handle(SetPlayerPositionPacket packet, ClientConnection connection) {
        Position position = new Position(packet.getX(), packet.getFeetY(), packet.getZ());

        connection.getMovementStates().jumping = !packet.isOnGround();
        connection.getMovementStates().onGround = packet.isOnGround();

        connection.checkLastCenterChunk(position.x, position.z);

        ClientMovement hytalePacket = new ClientMovement(connection.getMovementStates(), null, position, null, null, null, null, null, 0, null);
        connection.getHytaleChannel().sendPacket(hytalePacket);
    }
}
