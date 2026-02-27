package es.edwardbelt.hycraft.network.handler.hytale.interface_;

import com.hypixel.hytale.protocol.packets.interface_.ServerPlayerListUpdate;
import com.hypixel.hytale.protocol.packets.interface_.UpdateServerPlayerList;
import es.edwardbelt.hycraft.network.handler.PacketHandler;
import es.edwardbelt.hycraft.network.player.ClientConnection;

public class UpdateServerPlayerListHandler implements PacketHandler<UpdateServerPlayerList> {
    @Override
    public void handle(UpdateServerPlayerList packet, ClientConnection connection) {

    }
}
