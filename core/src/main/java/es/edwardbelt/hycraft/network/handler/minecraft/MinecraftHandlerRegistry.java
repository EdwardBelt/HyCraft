package es.edwardbelt.hycraft.network.handler.minecraft;


import es.edwardbelt.hycraft.network.handler.HandlerRegistry;
import es.edwardbelt.hycraft.network.handler.minecraft.config.AckFinishConfigurationHandler;
import es.edwardbelt.hycraft.network.handler.minecraft.config.ClientInformationHandler;
import es.edwardbelt.hycraft.network.handler.minecraft.config.ResponseKnownPacksHandler;
import es.edwardbelt.hycraft.network.handler.minecraft.handshake.HandshakeHandler;
import es.edwardbelt.hycraft.network.handler.minecraft.login.EncryptionResponseHandler;
import es.edwardbelt.hycraft.network.handler.minecraft.login.LoginAckHandler;
import es.edwardbelt.hycraft.network.handler.minecraft.login.LoginStartHandler;
import es.edwardbelt.hycraft.network.handler.minecraft.play.*;
import es.edwardbelt.hycraft.network.handler.minecraft.status.PingRequestHandler;
import es.edwardbelt.hycraft.network.handler.minecraft.status.StatusRequestHandler;
import es.edwardbelt.hycraft.protocol.packet.Packet;
import es.edwardbelt.hycraft.protocol.packet.configuration.AckFinishConfigurationPacket;
import es.edwardbelt.hycraft.protocol.packet.configuration.ClientInformationPacket;
import es.edwardbelt.hycraft.protocol.packet.configuration.ResponseKnownPacksPacket;
import es.edwardbelt.hycraft.protocol.packet.handshake.HandshakePacket;
import es.edwardbelt.hycraft.protocol.packet.login.EncryptionResponsePacket;
import es.edwardbelt.hycraft.protocol.packet.login.LoginAcknowledgedPacket;
import es.edwardbelt.hycraft.protocol.packet.login.LoginStartPacket;
import es.edwardbelt.hycraft.protocol.packet.play.*;
import es.edwardbelt.hycraft.protocol.packet.status.PingRequestPacket;
import es.edwardbelt.hycraft.protocol.packet.status.StatusRequestPacket;

public class MinecraftHandlerRegistry extends HandlerRegistry<Packet> {
    public MinecraftHandlerRegistry() {
        // config
        addHandler(AckFinishConfigurationPacket.class, new AckFinishConfigurationHandler());
        addHandler(ClientInformationPacket.class, new ClientInformationHandler());
        addHandler(ResponseKnownPacksPacket.class, new ResponseKnownPacksHandler());

        // handshake
        addHandler(HandshakePacket.class, new HandshakeHandler());

        // status
        addHandler(PingRequestPacket.class, new PingRequestHandler());
        addHandler(StatusRequestPacket.class, new StatusRequestHandler());

        // login
        addHandler(LoginStartPacket.class, new LoginStartHandler());
        addHandler(EncryptionResponsePacket.class, new EncryptionResponseHandler());
        addHandler(LoginAcknowledgedPacket.class, new LoginAckHandler());

        // play
        addHandler(ClickContainerPacket.class, new ClickContainerHandler());
        addHandler(KeepAliveResponsePacket.class, new KeepAliveResponseHandler());
        addHandler(PlayerActionPacket.class, new PlayerActionHandler());
        addHandler(PlayerCommandPacket.class, new PlayerCommandHandler());
        addHandler(PlayerInputPacket.class, new PlayerInputHandler());
        addHandler(SetCarriedSlotPacket.class, new SetCarriedSlotHandler());
        addHandler(SetPlayerPositionAndRotationPacket.class, new SetPlayerPosAndRotHandler());
        addHandler(SetPlayerPositionPacket.class, new SetPlayerPosHandler());
        addHandler(SetPlayerRotationPacket.class, new SetPlayerRotHandler());
        addHandler(SwingArmPacket.class, new SwingArmHandler());
        addHandler(ConfirmTeleportPacket.class, new ConfirmTeleportHandler());
        addHandler(UseItemOnPacket.class, new UseItemOnHandler());
        addHandler(ClientCommandPacket.class, new ClientCommandHandler());
        addHandler(ChatMessagePacket.class, new ChatMessageReceivedHandler());
        addHandler(ChatCommandPacket.class, new ChatCommandHandler());
        addHandler(EntityInteractPacket.class, new EntityInteractHandler());
    }
}