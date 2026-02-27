package es.edwardbelt.hycraft.network.handler.minecraft.data;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class StatusResponse {
    private Version version;
    private Players players;
    private String description;

    public StatusResponse(String version, int protocol, int max, int online, String description) {
        this.version = new Version(version, protocol);
        this.players = new Players(max, online);
        this.description = description;
    }

    @AllArgsConstructor
    public static class Version {
        private String name;
        private int protocol;
    }

    @AllArgsConstructor
    public static class Players {
        private int max;
        private int online;
    }
}
