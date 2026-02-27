package es.edwardbelt.hycraft.network.handler.minecraft.play;

import com.hypixel.hytale.protocol.MovementStates;
import com.hypixel.hytale.protocol.packets.player.ClientMovement;
import es.edwardbelt.hycraft.network.handler.PacketHandler;
import es.edwardbelt.hycraft.network.player.ClientConnection;
import es.edwardbelt.hycraft.protocol.packet.play.PlayerCommandPacket;

public class PlayerCommandHandler implements PacketHandler<PlayerCommandPacket> {
    @Override
    public void handle(PlayerCommandPacket packet, ClientConnection connection) {
        MovementStates movementStates = connection.getMovementStates();

        switch (packet.getAction()) {
            case START_SPRINTING:
                movementStates.sprinting = true;
                break;
            case STOP_SPRINTING:
                movementStates.sprinting = false;
                break;
            case START_JUMP_WITH_HORSE:
                movementStates.jumping = true;
                break;
            case STOP_JUMP_WITH_HORSE:
                movementStates.jumping = false;
                break;
            case START_FLYING_WITH_ELYTRA:
                movementStates.gliding = true;
                break;
            case LEAVE_BED:
                movementStates.sleeping = false;
                break;
            case OPEN_VEHICLE_INVENTORY:
                break;
        }

        ClientMovement hytalePacket = new ClientMovement(movementStates, null, null, null, null, null, null, null, 0, null);
        connection.getHytaleChannel().sendPacket(hytalePacket);
    }
}
