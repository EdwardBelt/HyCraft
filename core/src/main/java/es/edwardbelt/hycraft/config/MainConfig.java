package es.edwardbelt.hycraft.config;

import es.edwardbelt.hycraft.config.annotation.ConfigProperty;
import lombok.Getter;

@Getter
public class MainConfig implements Config {
    @ConfigProperty("port")
    private int port;
}
