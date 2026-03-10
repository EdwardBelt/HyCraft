package es.edwardbelt.hycraft.network.handler.hytale.window;

import com.hypixel.hytale.protocol.packets.window.OpenWindow;
import com.hypixel.hytale.protocol.packets.window.WindowType;
import es.edwardbelt.hycraft.network.handler.PacketHandler;
import es.edwardbelt.hycraft.network.handler.hytale.manager.window.WindowManager;
import es.edwardbelt.hycraft.network.player.ClientConnection;

public class OpenWindowHandler implements PacketHandler<OpenWindow> {
    @Override
    public void handle(OpenWindow packet, ClientConnection connection) {
        if (packet.windowType.equals(WindowType.Container)) WindowManager.get().openContainer(packet.id, connection, packet.inventory);
    }
}
