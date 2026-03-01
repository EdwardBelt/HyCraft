package es.edwardbelt.hycraft.config.factory;

import com.google.gson.JsonObject;
import es.edwardbelt.hycraft.HyCraft;
import es.edwardbelt.hycraft.config.Config;
import es.edwardbelt.hycraft.config.annotation.ConfigProperty;
import es.edwardbelt.hycraft.config.type.ConfigType;
import es.edwardbelt.hycraft.util.reflection.FieldAccessor;
import es.edwardbelt.hycraft.util.reflection.Reflections;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ConfigFactory<T extends Config> {
    private final Class<T> clazz;
    private final List<ConfigField> fields;

    public ConfigFactory(Class<T> clazz) {
        this.clazz = clazz;
        this.fields = new ArrayList<>();
        loadFields();
    }

    public void loadFields() {
        for (Field field : Reflections.getAllFields(clazz)) {
            if (!field.isAnnotationPresent(ConfigProperty.class)) continue;

            String key = field.getAnnotation(ConfigProperty.class).value();
            Class<?> type = field.getType();
            FieldAccessor<?> accessor = Reflections.getField(clazz, field.getName());

            ConfigField configField = new ConfigField(key, type, accessor);
            fields.add(configField);
        }
    }

    public T newInstance(JsonObject config) {
        try {
            T instance = (T) Reflections.getConstructor(clazz).newInstance();

            fields.forEach(field -> {
                String key = field.getKey();
                if (!config.has(key)) return;
                Class<?> type = field.getType();
                FieldAccessor<?> accessor = field.getAccessor();

                if (Config.class.isAssignableFrom(type)) {
                    Class<? extends Config> configClass = (Class<? extends Config>) type;
                    ConfigFactory<?> configFactory = HyCraft.get().getConfigManager().getFactory(configClass);

                    JsonObject nested = config.getAsJsonObject(key);
                    Object value = configFactory.newInstance(nested);
                    accessor.set(instance, value);
                } else {
                    ConfigType<?> configType = HyCraft.get().getConfigManager().getTypeRegistry().get(type);
                    if (configType == null) {
                        throw new IllegalArgumentException("Invalid config type: " + type.getName());
                    }

                    Object value = configType.read(config.get(key));
                    accessor.set(instance, value);
                }
            });

            return instance;
        } catch (Exception e) {
            System.out.println("Error creating new instance of config " + clazz.getName());
            System.out.println(e.getMessage());
        }

        return null;
    }
}
