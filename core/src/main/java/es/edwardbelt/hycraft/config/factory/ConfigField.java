package es.edwardbelt.hycraft.config.factory;

import es.edwardbelt.hycraft.util.reflection.FieldAccessor;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ConfigField {
    private String key;
    private Class<?> type;
    private FieldAccessor<?> accessor;
}
