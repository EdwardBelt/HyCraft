package es.edwardbelt.hycraft.network.handler;

import es.edwardbelt.hycraft.network.player.ClientConnection;

public interface PacketHandler<T> {
    void handle(T packet, ClientConnection connection);
}
