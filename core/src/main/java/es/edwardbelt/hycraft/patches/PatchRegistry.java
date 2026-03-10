package es.edwardbelt.hycraft.patches;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

public class PatchRegistry {
    private static final int IDX_INSTANCE = 0;
    private static final int IDX_ARGS = 1;
    private static final int IDX_CANCEL = 2;
    private static final int IDX_RETURN = 3;

    private static final Map<String, List<Consumer<Object[]>>> handlers = new ConcurrentHashMap<>();

    public static void register(String key, Consumer<Object[]> handler) {
        handlers.computeIfAbsent(key, k -> new CopyOnWriteArrayList<>()).add(handler);
    }

    public static Object[] invoke(String key, Object instance, Object[] args, Object currentReturn) {
        Object[] state = new Object[]{instance, args, Boolean.FALSE, currentReturn};
        List<Consumer<Object[]>> list = handlers.get(key);
        if (list != null) {
            for (Consumer<Object[]> h : list) {
                h.accept(state);
                if ((boolean) state[IDX_CANCEL]) break;
            }
        }
        return state;
    }

    public static boolean hasHandler(String key) {
        List<Consumer<Object[]>> list = handlers.get(key);
        return list != null && !list.isEmpty();
    }

    public static void unregister(String key) {
        handlers.remove(key);
    }

    public static void clear() {
        handlers.clear();
    }
}
