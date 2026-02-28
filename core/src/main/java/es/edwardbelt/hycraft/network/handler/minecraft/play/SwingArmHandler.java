package es.edwardbelt.hycraft.network.handler.minecraft.play;

import es.edwardbelt.hycraft.network.handler.PacketHandler;
import es.edwardbelt.hycraft.network.player.ClientConnection;
import es.edwardbelt.hycraft.protocol.packet.play.SwingArmPacket;

public class SwingArmHandler implements PacketHandler<SwingArmPacket> {
    @Override
    public void handle(SwingArmPacket packet, ClientConnection connection) {
    }
}
