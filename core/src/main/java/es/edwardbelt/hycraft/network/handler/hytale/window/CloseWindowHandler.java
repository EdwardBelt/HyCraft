package es.edwardbelt.hycraft.network.handler.hytale.window;

import com.hypixel.hytale.protocol.packets.window.CloseWindow;
import es.edwardbelt.hycraft.network.handler.PacketHandler;
import es.edwardbelt.hycraft.network.handler.minecraft.manager.gui.GuiManager;
import es.edwardbelt.hycraft.network.player.ClientConnection;

public class CloseWindowHandler implements PacketHandler<CloseWindow> {
    @Override
    public void handle(CloseWindow packet, ClientConnection connection) {
        GuiManager.get().closeGui(connection, false);
    }
}
