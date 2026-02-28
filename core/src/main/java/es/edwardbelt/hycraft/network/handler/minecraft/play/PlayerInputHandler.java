package es.edwardbelt.hycraft.network.handler.minecraft.play;

import com.hypixel.hytale.protocol.MovementStates;
import com.hypixel.hytale.protocol.packets.player.ClientMovement;
import es.edwardbelt.hycraft.network.handler.PacketHandler;
import es.edwardbelt.hycraft.network.player.ClientConnection;
import es.edwardbelt.hycraft.protocol.packet.play.PlayerInputPacket;

public class PlayerInputHandler implements PacketHandler<PlayerInputPacket> {
    @Override
    public void handle(PlayerInputPacket packet, ClientConnection connection) {
        MovementStates movementStates = connection.getMovementStates();

        boolean hasHorizontalMovement = packet.isForward() || packet.isBackward() ||
                packet.isLeft() || packet.isRight();

        if (hasHorizontalMovement) {
            if (packet.isSprint()) {
                movementStates.running = true;
                movementStates.walking = true;
            } else {
                movementStates.walking = true;
            }
            movementStates.idle = false;
            movementStates.horizontalIdle = false;
        } else {
            movementStates.idle = true;
            movementStates.horizontalIdle = true;
        }

        if (packet.isSneak()) {
            movementStates.crouching = true;
        } else {
            movementStates.crouching = false;
        }

        ClientMovement hytalePacket = new ClientMovement(movementStates, null, null, null, null, null, null, null, 0, null);
        connection.getHytaleChannel().sendPacket(hytalePacket);
    }
}
