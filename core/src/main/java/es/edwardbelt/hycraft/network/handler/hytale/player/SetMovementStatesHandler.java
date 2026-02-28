package es.edwardbelt.hycraft.network.handler.hytale.player;

import com.hypixel.hytale.protocol.packets.player.SetMovementStates;
import es.edwardbelt.hycraft.network.handler.PacketHandler;
import es.edwardbelt.hycraft.network.player.ClientConnection;
import es.edwardbelt.hycraft.protocol.packet.play.PlayerAbilitiesPacket;

public class SetMovementStatesHandler implements PacketHandler<SetMovementStates> {
    @Override
    public void handle(SetMovementStates packet, ClientConnection connection) {
        byte flags = 0;
        if (packet.movementStates.flying) {
            flags |= PlayerAbilitiesPacket.PlayerAbilityFlags.FLYING;
            flags |= PlayerAbilitiesPacket.PlayerAbilityFlags.ALLOW_FLYING;
        }

        PlayerAbilitiesPacket abilitiesPacket = new PlayerAbilitiesPacket(flags, 0.05f, 0.1f);
        connection.getChannel().writeAndFlush(abilitiesPacket);
    }
}
