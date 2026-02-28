package es.edwardbelt.hycraft.network.handler.minecraft.play;

import com.hypixel.hytale.protocol.TeleportAck;
import com.hypixel.hytale.protocol.packets.player.ClientMovement;
import es.edwardbelt.hycraft.network.handler.PacketHandler;
import es.edwardbelt.hycraft.network.player.ClientConnection;
import es.edwardbelt.hycraft.protocol.packet.play.ConfirmTeleportPacket;

public class ConfirmTeleportHandler implements PacketHandler<ConfirmTeleportPacket> {
    @Override
    public void handle(ConfirmTeleportPacket packet, ClientConnection connection) {
        byte id = (byte) packet.getId();
        if (!connection.getTpConfirmations().containsKey(id)) return;

        ClientMovement ackTpPacket = new ClientMovement();
        ackTpPacket.teleportAck = new TeleportAck(id);
        ackTpPacket.absolutePosition = connection.getTpConfirmations().get(id);
        connection.getHytaleChannel().sendPacket(ackTpPacket);

        connection.getTpConfirmations().remove(id);
    }
}
