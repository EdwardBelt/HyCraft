package es.edwardbelt.hycraft.config;

import es.edwardbelt.hycraft.config.factory.ConfigFactory;
import es.edwardbelt.hycraft.config.type.ConfigTypeRegistry;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public class ConfigManager {
    private final Map<Class<? extends Config>, ConfigFactory<?>> factories;
    @Getter
    private MainConfig main;
    private JsonConfig mainJson;

    @Getter
    private final ConfigTypeRegistry typeRegistry;

    public ConfigManager() {
        this.factories = new HashMap<>();
        this.typeRegistry = new ConfigTypeRegistry();

        this.mainJson = new JsonConfig("main");
    }

    public void reload() {
        this.main = getFactory(MainConfig.class).newInstance(mainJson.get());
    }

    public <T extends Config> void registerFactory(Class<T> configClass) {
        ConfigFactory<T> factory = new ConfigFactory<>(configClass);
        factories.put(configClass, factory);
        System.out.println("Registered config factory " + configClass.getName());
    }

    public <T extends Config> ConfigFactory<T> getFactory(Class<T> configClass) {
        if (!factories.containsKey(configClass)) registerFactory(configClass);
        return (ConfigFactory<T>) factories.get(configClass);
    }
}
