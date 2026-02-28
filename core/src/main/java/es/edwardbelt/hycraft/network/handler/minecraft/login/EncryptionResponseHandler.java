package es.edwardbelt.hycraft.network.handler.minecraft.login;

import es.edwardbelt.hycraft.network.MinecraftServerBootstrap;
import es.edwardbelt.hycraft.network.auth.EncryptionUtil;
import es.edwardbelt.hycraft.network.auth.MojangAuth;
import es.edwardbelt.hycraft.network.handler.PacketHandler;
import es.edwardbelt.hycraft.network.handler.minecraft.data.profile.GameProfile;
import es.edwardbelt.hycraft.network.handler.minecraft.data.profile.Property;
import es.edwardbelt.hycraft.network.player.ClientConnection;
import es.edwardbelt.hycraft.protocol.packet.login.EncryptionResponsePacket;
import es.edwardbelt.hycraft.protocol.packet.login.LoginSuccessPacket;
import es.edwardbelt.hycraft.util.UUIDUtil;

import java.util.Arrays;
import java.util.UUID;

public class EncryptionResponseHandler implements PacketHandler<EncryptionResponsePacket> {
    @Override
    public void handle(EncryptionResponsePacket packet, ClientConnection connection) {
        try {
            byte[] sharedSecret = EncryptionUtil.rsaDecrypt(packet.getEncryptedSharedSecret());
            byte[] verifyToken = EncryptionUtil.rsaDecrypt(packet.getEncryptedVerifyToken());

            if (!Arrays.equals(verifyToken, connection.getPendingVerifyToken())) {
                MinecraftServerBootstrap.get().disconnectConnection(connection, "Verify token mismatch");
                return;
            }

            connection.enableEncryption(sharedSecret);

            String serverHash = EncryptionUtil.computeServerHash(sharedSecret);
            UUID uuid = MojangAuth.authenticate(connection.getPendingUsername(), serverHash);

            if (uuid == null) {
                MinecraftServerBootstrap.get().disconnectConnection(connection, "Failed to verify username!");
                return;
            }

            String hytaleUsername = "." + connection.getPendingUsername();

            MinecraftServerBootstrap.get().setConnection(uuid, connection);
            connection.setUuid(uuid);
            connection.setUsername(hytaleUsername);

            GameProfile profile = new GameProfile(uuid, connection.getUsername());
            String[] skin = UUIDUtil.getSkinByUUID(uuid);
            if (skin != null) {
                Property skinProperty = new Property("textures", skin[0], skin[1]);
                profile.addProperty(skinProperty);
            }

            connection.setProfile(profile);

            LoginSuccessPacket successPacket = new LoginSuccessPacket(profile);
            connection.getChannel().writeAndFlush(successPacket);
        } catch (Exception e) {
            System.out.println("error while processing encryption response for username: " + connection.getPendingUsername());
            MinecraftServerBootstrap.get().disconnectConnection(connection, "Error while encrypting!");
        }
    }
}
