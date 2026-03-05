package es.edwardbelt.hycraft.config.type;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import es.edwardbelt.hycraft.HyCraft;
import es.edwardbelt.hycraft.config.Config;
import es.edwardbelt.hycraft.config.factory.ConfigFactory;

import java.util.ArrayList;
import java.util.List;

public class ListType {
    public static List<?> read(JsonElement json, Class<?> elementType) {
        JsonArray array = json.getAsJsonArray();
        List<Object> list = new ArrayList<>();

        for (JsonElement element : array) {
            if (Config.class.isAssignableFrom(elementType)) {
                Class<? extends Config> configClass = (Class<? extends Config>) elementType;
                ConfigFactory<?> factory = HyCraft.get().getConfigManager().getFactory(configClass);
                list.add(factory.newInstance(element.getAsJsonObject()));
            } else {
                ConfigType<?> configType = HyCraft.get().getConfigManager().getTypeRegistry().get(elementType);
                if (configType == null) throw new IllegalArgumentException("Unsupported list element type: " + elementType.getName());
                list.add(configType.read(element));
            }
        }

        return list;
    }
}
