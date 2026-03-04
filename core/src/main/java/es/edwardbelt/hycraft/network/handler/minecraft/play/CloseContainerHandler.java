package es.edwardbelt.hycraft.network.handler.minecraft.play;

import es.edwardbelt.hycraft.network.handler.PacketHandler;
import es.edwardbelt.hycraft.network.handler.minecraft.manager.gui.GuiManager;
import es.edwardbelt.hycraft.network.player.ClientConnection;
import es.edwardbelt.hycraft.protocol.packet.play.CloseContainerPacket;

public class CloseContainerHandler implements PacketHandler<CloseContainerPacket> {
    @Override
    public void handle(CloseContainerPacket packet, ClientConnection connection) {
        GuiManager.get().onCloseGui(connection);
    }
}
