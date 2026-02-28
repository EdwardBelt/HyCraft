package es.edwardbelt.hycraft.network.handler.minecraft.data.profile;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class GameProfile {
    private final UUID uuid;
    private final String username;
    private final List<Property> properties;

    public GameProfile(UUID uuid, String username) {
        this.uuid = uuid;
        this.username = username;
        this.properties = new ArrayList<>();
    }

    public GameProfile(UUID uuid, String username, List<Property> properties) {
        this.uuid = uuid;
        this.username = username;
        this.properties = new ArrayList<>(properties);
    }

    public void addProperty(Property property) {
        this.properties.add(property);
    }

    public void addProperty(String name, String value) {
        this.properties.add(new Property(name, value, null));
    }

    public void addProperty(String name, String value, String signature) {
        this.properties.add(new Property(name, value, signature));
    }

    public static GameProfile createOffline(UUID uuid, String username) {
        return new GameProfile(uuid, username);
    }

    public static GameProfile createWithTextures(UUID uuid, String username, String texturesJson) {
        GameProfile profile = new GameProfile(uuid, username);
        profile.addProperty("textures", texturesJson);
        return profile;
    }
}
