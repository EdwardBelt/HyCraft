package es.edwardbelt.hycraft.util;

import es.edwardbelt.hycraft.HyCraft;
import es.edwardbelt.hycraft.api.HyCraftApi;

import java.util.logging.Level;

public enum Logger {
    INFO,
    WARN,
    ERROR,
    DEBUG;

    private static final Level DEBUG_LEVEL = new DebugLevel();

    public void log(String msg) {
        HyCraft plugin = (HyCraft) HyCraftApi.get();

        switch (this) {
            case INFO -> plugin.getLogger().at(Level.INFO).log(msg);
            case WARN -> plugin.getLogger().at(Level.WARNING).log(msg);
            case ERROR -> plugin.getLogger().at(Level.WARNING).log(msg);
            case DEBUG -> {
                if (!plugin.getConfigManager().getMain().isLogDebug()) return;

                Class<?> callerClass = StackWalker
                        .getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE)
                        .getCallerClass();

                plugin.getLogger().at(DEBUG_LEVEL).log("[" + callerClass + "] " + msg);
            }
        }
    }

    private static class DebugLevel extends Level {
        DebugLevel() {
            super("DEBUG", 400);
        }
    }
}

