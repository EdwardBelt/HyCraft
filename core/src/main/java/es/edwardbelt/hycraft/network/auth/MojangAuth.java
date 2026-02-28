package es.edwardbelt.hycraft.network.auth;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.UUID;

public class MojangAuth {
    private static final String HAS_JOINED_URL = "https://sessionserver.mojang.com/session/minecraft/hasJoined?username=%s&serverId=%s";
    private static final HttpClient HTTP_CLIENT = HttpClient.newHttpClient();

    public static UUID authenticate(String username, String serverHash) {
        try {
            String url = String.format(HAS_JOINED_URL, username, serverHash);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) return null;

            JsonObject json = JsonParser.parseString(response.body()).getAsJsonObject();
            String uuidStr = json.get("id").getAsString();

            String formatted = uuidStr.replaceFirst(
                    "(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})",
                    "$1-$2-$3-$4-$5"
            );

            return UUID.fromString(formatted);
        } catch (Exception e) {
            System.err.println("Mojang auth failed for " + username + ": " + e.getMessage());
            return null;
        }
    }
}

