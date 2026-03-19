package es.edwardbelt.hycraft.mixin;

import es.edwardbelt.hycraft.mixins.MixinConstants;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HookRegistry {
    private static final Object LOCK = new Object();

    public static void load() {
        register(MixinConstants.INTERACTION_HOOK, new InteractionHook());
        register(MixinConstants.PROFILE_HOOK, new ProfileHook());
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Object> getBridge() {
        Object bridge = System.getProperties().get(MixinConstants.BRIDGE_KEY);
        if (bridge instanceof Map) {
            return (Map<String, Object>) bridge;
        }
        synchronized (LOCK) {
            bridge = System.getProperties().get(MixinConstants.BRIDGE_KEY);
            if (bridge instanceof Map) {
                return (Map<String, Object>) bridge;
            }
            Map<String, Object> newBridge = new ConcurrentHashMap<>();
            System.getProperties().put(MixinConstants.BRIDGE_KEY, newBridge);
            return newBridge;
        }
    }

    public static void register(String key, Object hook) {
        getBridge().put(key, hook);
    }

    public static Object get(String key) {
        return getBridge().get(key);
    }

    public static void unregister(String key) {
        getBridge().remove(key);
    }
}
