package es.edwardbelt.hycraft.network.handler.minecraft.play;

import com.hypixel.hytale.protocol.Direction;
import com.hypixel.hytale.protocol.packets.player.ClientMovement;
import es.edwardbelt.hycraft.network.handler.PacketHandler;
import es.edwardbelt.hycraft.network.handler.hytale.HytaleUtil;
import es.edwardbelt.hycraft.network.player.ClientConnection;
import es.edwardbelt.hycraft.protocol.packet.play.SetPlayerRotationPacket;

public class SetPlayerRotHandler implements PacketHandler<SetPlayerRotationPacket> {
    @Override
    public void handle(SetPlayerRotationPacket packet, ClientConnection connection) {
        Direction lookAt = HytaleUtil.getHytaleDirection(packet.getYaw(), packet.getPitch());
        Direction bodyAt = new Direction(lookAt.yaw, 0, 0);

        connection.getMovementStates().jumping = !packet.isOnGround();
        connection.getMovementStates().onGround = packet.isOnGround();

        ClientMovement hytalePacket = new ClientMovement(connection.getMovementStates(), null, null, bodyAt, lookAt, null, null, null, 0, null);
        connection.getHytaleChannel().sendPacket(hytalePacket);
    }
}
