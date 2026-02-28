package es.edwardbelt.hycraft.util;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class UUIDUtil {
    private static final String MOJANG_API = "https://api.mojang.com/users/profiles/minecraft/";
    private static final HttpClient httpClient = HttpClient.newHttpClient();

    public static UUID getOfflineUUID(String playerName) {
        String offlineString = "OfflinePlayer:" + playerName;
        return UUID.nameUUIDFromBytes(offlineString.getBytes(StandardCharsets.UTF_8));
    }

    public static UUID getOnlineUUID(String playerName) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(MOJANG_API + playerName))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            JsonObject json = JsonParser.parseString(response.body()).getAsJsonObject();
            String uuidString = json.get("id").getAsString();
            return formatUUID(uuidString);
        }

        return null;
    }

    public static String[] getSkinByUUID(UUID uuid) {
        String uuidString = uuid.toString().replace("-", "");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://sessionserver.mojang.com/session/minecraft/profile/" + uuidString + "?unsigned=false"))
                .GET()
                .build();

        HttpResponse<String> response = null;
        try {
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            return null;
        }

        if (response.statusCode() == 200) {
            JsonObject json = JsonParser.parseString(response.body()).getAsJsonObject();
            String skinValue = json.getAsJsonArray("properties")
                    .get(0)
                    .getAsJsonObject()
                    .get("value")
                    .getAsString();
            if (skinValue == null) return null;

            String signature = json.getAsJsonArray("properties")
                    .get(0)
                    .getAsJsonObject()
                    .get("value")
                    .getAsString();
            if (signature == null) return null;
            return new String[]{skinValue, signature};
        }

        return null;
    }

    private static UUID formatUUID(String uuidWithoutDashes) {
        StringBuilder sb = new StringBuilder(uuidWithoutDashes);
        sb.insert(8, "-");
        sb.insert(13, "-");
        sb.insert(18, "-");
        sb.insert(23, "-");
        return UUID.fromString(sb.toString());
    }
}
