package es.edwardbelt.hycraft.network.handler.hytale.entities;

import com.hypixel.hytale.protocol.packets.entities.ApplyKnockback;
import es.edwardbelt.hycraft.network.handler.PacketHandler;
import es.edwardbelt.hycraft.network.player.ClientConnection;

public class ApplyKnockbackHandler implements PacketHandler<ApplyKnockback> {
    @Override
    public void handle(ApplyKnockback packet, ClientConnection connection) {
        /*System.out.println("received knockback");
        System.out.println(packet.x);
        System.out.println(packet.y);
        System.out.println(packet.z);
        System.out.println(packet.changeType.name());*/
    }
}
