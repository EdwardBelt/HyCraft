package es.edwardbelt.hycraft.config.type;

import java.util.HashMap;
import java.util.Map;

public class ConfigTypeRegistry {
    private final Map<Class<?>, ConfigType<?>> types;

    public ConfigTypeRegistry() {
        this.types = new HashMap<>();
        register(new StringType());
        register(new IntegerType());
        register(new ByteType());
        register(new DoubleType());
        register(new FloatType());
        register(new IntegerType());
        register(new BooleanType());
        register(new JsonObjectType());
    }

    public void register(ConfigType<?> type) {
        for (Class<?> clazz : type.supportedTypes()) {
            types.put(clazz, type);
        }
    }

    public ConfigType<?> get(Class<?> clazz) {
        return types.get(clazz);
    }
}
