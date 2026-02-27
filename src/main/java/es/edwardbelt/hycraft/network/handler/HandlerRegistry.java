package es.edwardbelt.hycraft.network.handler;

import es.edwardbelt.hycraft.network.player.ClientConnection;

import java.util.HashMap;
import java.util.Map;

public abstract class HandlerRegistry<T> {
    private final Map<Class<? extends T>, PacketHandler<? extends T>> packetHandlers = new HashMap<>();

    @SuppressWarnings("unchecked")
    public <P extends T> void handlePacket(P packet, ClientConnection connection) {
        PacketHandler<P> handler = (PacketHandler<P>) getPacketHandler((Class<P>) packet.getClass());
        if (handler == null) {
            return;
        }
        this.handle(handler, packet, connection);
    };

    public boolean hasHandler(Class<? extends T> clazz) {
        return packetHandlers.containsKey(clazz);
    }

    public void addHandler(Class<? extends T> clazz, PacketHandler<? extends T> handler) {
        packetHandlers.put(clazz, handler);
    }

    public <P extends T> void handle(PacketHandler<P> handler, P packet, ClientConnection connection) {
        handler.handle(packet, connection);
    }

    public PacketHandler<? extends T> getPacketHandler(Class<? extends T> packetType) {
        return packetHandlers.get(packetType);
    }
}
