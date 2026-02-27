package es.edwardbelt.hycraft.network.handler.hytale.inventory;

import com.hypixel.hytale.protocol.packets.inventory.SetActiveSlot;
import es.edwardbelt.hycraft.network.handler.PacketHandler;
import es.edwardbelt.hycraft.network.player.ClientConnection;
import es.edwardbelt.hycraft.protocol.packet.play.SetHeldSlotPacket;

public class SetActiveSlotHandler implements PacketHandler<SetActiveSlot> {
    private final static int INVENTORY_HOTBAR_SECTION_ID = -1;

    @Override
    public void handle(SetActiveSlot packet, ClientConnection connection) {
        if (packet.inventorySectionId != INVENTORY_HOTBAR_SECTION_ID) return;

        SetHeldSlotPacket heldSlotPacket = new SetHeldSlotPacket(packet.activeSlot);
        connection.getChannel().writeAndFlush(heldSlotPacket);
    }
}
