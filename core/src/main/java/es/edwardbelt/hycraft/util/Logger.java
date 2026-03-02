package es.edwardbelt.hycraft.util;

import es.edwardbelt.hycraft.HyCraft;
import es.edwardbelt.hycraft.api.HyCraftApi;

import java.util.logging.Level;

public enum Logger {

    INFO,
    WARN,
    ERROR,
    DEBUG;

    private static DebugLevel debugLevel = new DebugLevel();

    public void log(String msg) {
        HyCraft plugin = (HyCraft) HyCraftApi.get();

        switch (this) {
            case Logger.INFO -> plugin.getLogger().at(Level.INFO).log(msg);
            case Logger.WARN -> plugin.getLogger().at(Level.WARNING).log(msg);
            case Logger.ERROR -> plugin.getLogger().at(Level.WARNING).log(msg);
            case Logger.DEBUG -> {
                if (!plugin.getConfigManager().getMain().isLogDebug()) return;

                Class<?> callerClass = StackWalker
                        .getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE)
                        .getCallerClass();

                plugin.getLogger().at(debugLevel).log("[" + callerClass + "] " + msg);
            }
        }

    }

    private static class DebugLevel extends Level {

        DebugLevel() {
            super("DEBUG", Level.FINE.intValue() - 100);
        }

    }

}

