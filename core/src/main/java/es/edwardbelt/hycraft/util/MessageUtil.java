package es.edwardbelt.hycraft.util;

import es.edwardbelt.hycraft.network.player.ClientConnection;
import es.edwardbelt.hycraft.protocol.packet.play.SystemMessagePacket;

import java.util.List;

public class MessageUtil {
    public static String colorize(String message) {
        return message.replaceAll("&([0-9a-fk-or])", "§$1");
    }

    public static String parse(String message, String... vars) {
        message = colorize(message);

        for (int i = 0; i < vars.length; i += 2) {
            message = message.replace("{" + vars[i] + "}", vars[i + 1]);
        }

        return message;
    }

    public static void send(ClientConnection connection, String message, boolean flush, String... vars) {
        if (message == null) return;
        message = parse(message, vars);
        message = colorize(message);
        SystemMessagePacket messagePacket = new SystemMessagePacket(message);
        if (flush) connection.getChannel().writeAndFlush(messagePacket);
        else connection.getChannel().write(messagePacket);
    }

    public static void send(ClientConnection connection, String message, String... vars) {
        send(connection, message, true, vars);
    }

    public static void send(ClientConnection connection, List<String> messages, String... vars) {
        if (messages == null || messages.isEmpty()) return;
        messages.forEach(message -> send(connection, message, false, vars));
        connection.getChannel().flush();
    }
}
