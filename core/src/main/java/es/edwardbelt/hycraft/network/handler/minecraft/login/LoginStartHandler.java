package es.edwardbelt.hycraft.network.handler.minecraft.login;

import es.edwardbelt.hycraft.network.auth.EncryptionUtil;
import es.edwardbelt.hycraft.network.handler.PacketHandler;
import es.edwardbelt.hycraft.network.player.ClientConnection;
import es.edwardbelt.hycraft.protocol.packet.login.EncryptionRequestPacket;
import es.edwardbelt.hycraft.protocol.packet.login.LoginStartPacket;

public class LoginStartHandler implements PacketHandler<LoginStartPacket> {
    @Override
    public void handle(LoginStartPacket packet, ClientConnection connection) {
        String username = packet.getName();

        connection.setUsername(username);
        connection.setPendingUsername(username);

        System.out.println("received minecraft login request from username: " + username);

        byte[] verifyToken = EncryptionUtil.generateVerifyToken();
        connection.setPendingVerifyToken(verifyToken);

        EncryptionRequestPacket encryptionRequest = new EncryptionRequestPacket(
                "",
                EncryptionUtil.getEncodedPublicKey(),
                verifyToken,
                true
        );
        connection.getChannel().writeAndFlush(encryptionRequest);
    }
}
