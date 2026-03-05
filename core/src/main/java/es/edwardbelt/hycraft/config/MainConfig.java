package es.edwardbelt.hycraft.config;

import es.edwardbelt.hycraft.config.annotation.ConfigProperty;
import lombok.Getter;

import java.util.List;

@Getter
public class MainConfig implements Config {
    @ConfigProperty("port")
    private int port;

    @ConfigProperty("player_prefix")
    private String playerPrefix;

    @ConfigProperty("item_notification")
    private String itemNotification;

    @ConfigProperty("join_message")
    private List<String> joinMessage;

    @ConfigProperty("log_debug")
    private boolean logDebug;
}
