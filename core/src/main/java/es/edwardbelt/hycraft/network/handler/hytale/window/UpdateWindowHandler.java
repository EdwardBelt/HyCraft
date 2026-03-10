package es.edwardbelt.hycraft.network.handler.hytale.window;

import com.hypixel.hytale.protocol.packets.window.UpdateWindow;
import es.edwardbelt.hycraft.network.handler.PacketHandler;
import es.edwardbelt.hycraft.network.handler.hytale.manager.window.WindowManager;
import es.edwardbelt.hycraft.network.player.ClientConnection;

public class UpdateWindowHandler implements PacketHandler<UpdateWindow> {
    @Override
    public void handle(UpdateWindow packet, ClientConnection connection) {
        if (packet.inventory != null) {
            WindowManager.get().updateContainer(connection, packet.inventory);
        }
    }
}
