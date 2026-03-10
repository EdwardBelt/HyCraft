package es.edwardbelt.hycraft.patches.api;

import es.edwardbelt.hycraft.util.reflection.FieldAccessor;
import es.edwardbelt.hycraft.util.reflection.Reflections;

import java.util.concurrent.ConcurrentHashMap;

public class CallbackInfo {
    private static final ConcurrentHashMap<String, FieldAccessor<Object>> fieldCache = new ConcurrentHashMap<>();
    private static final int IDX_INSTANCE = 0;
    private static final int IDX_ARGS = 1;
    private static final int IDX_CANCEL = 2;
    private static final int IDX_RETURN = 3;
    public static final int STATE_LENGTH = 4;

    private final Object[] state;

    public CallbackInfo(Object[] state) {
        this.state = state;
    }

    @SuppressWarnings("unchecked")
    public <T> T getInstance() {
        return (T) state[IDX_INSTANCE];
    }

    public Object[] getArgs() {
        return (Object[]) state[IDX_ARGS];
    }

    @SuppressWarnings("unchecked")
    public <T> T getArg(int index) {
        return (T) getArgs()[index];
    }

    public boolean isCancelled() {
        return (boolean) state[IDX_CANCEL];
    }

    public void cancel() {
        state[IDX_CANCEL] = true;
    }

    @SuppressWarnings("unchecked")
    public <T> T getReturnValue() {
        return (T) state[IDX_RETURN];
    }

    public void setReturnValue(Object value) {
        state[IDX_RETURN] = value;
        state[IDX_CANCEL] = true;
    }

    @SuppressWarnings("unchecked")
    public <T> T getField(String fieldName) {
        Object instance = state[IDX_INSTANCE];
        if (instance == null) throw new IllegalStateException("Cannot access fields on a static method target");
        return (T) resolveAccessor(instance.getClass(), fieldName).get(instance);
    }

    public void setField(String fieldName, Object value) {
        Object instance = state[IDX_INSTANCE];
        if (instance == null) throw new IllegalStateException("Cannot access fields on a static method target");
        resolveAccessor(instance.getClass(), fieldName).set(instance, value);
    }

    private static FieldAccessor<Object> resolveAccessor(Class<?> clazz, String fieldName) {
        String cacheKey = clazz.getName() + "#" + fieldName;
        return fieldCache.computeIfAbsent(cacheKey, k -> Reflections.getField(clazz, fieldName));
    }
}
