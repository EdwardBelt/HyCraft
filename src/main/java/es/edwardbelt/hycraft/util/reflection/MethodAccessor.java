package es.edwardbelt.hycraft.util.reflection;

public interface MethodAccessor {
    Object invoke(Object target, Object... args);
    boolean hasMethod(Object target);
}

