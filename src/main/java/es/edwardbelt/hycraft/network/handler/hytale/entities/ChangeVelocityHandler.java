package es.edwardbelt.hycraft.network.handler.hytale.entities;

import com.hypixel.hytale.protocol.ChangeVelocityType;
import com.hypixel.hytale.protocol.Vector3f;
import com.hypixel.hytale.protocol.packets.entities.ChangeVelocity;
import es.edwardbelt.hycraft.network.handler.PacketHandler;
import es.edwardbelt.hycraft.network.player.ClientConnection;
import es.edwardbelt.hycraft.protocol.packet.play.SetEntityVelocityPacket;

public class ChangeVelocityHandler implements PacketHandler<ChangeVelocity> {
    @Override
    public void handle(ChangeVelocity packet, ClientConnection connection) {
        /*System.out.println("received velocity: " + packet.x + "," + packet.y + ", " + packet.z);
        System.out.println("type: " + packet.changeType);
        System.out.println("cfg null? " + (packet.config == null));*/
        float velX = packet.x;
        float velY = packet.y;
        float velZ = packet.z;
        if (packet.changeType.equals(ChangeVelocityType.Add) && packet.config == null) {
            velX /= 10;
            velY /= 10;
            velZ /= 10;
        }
        Vector3f velocity = new Vector3f(velX, velY, velZ);
        SetEntityVelocityPacket velocityPacket = new SetEntityVelocityPacket(connection.getNetworkId(), velocity);
        connection.getChannel().writeAndFlush(velocityPacket);
    }
}
