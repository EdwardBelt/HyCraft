package es.edwardbelt.hycraft.util;

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
}
