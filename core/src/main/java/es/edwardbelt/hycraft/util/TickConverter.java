package es.edwardbelt.hycraft.util;

public class TickConverter {
    private static final int MINECRAFT_TPS = 20;
    private static final int DEFAULT_HYTALE_TPS = 30;

    private static volatile int hytaleTPS = DEFAULT_HYTALE_TPS;

    public static int secondsToMinecraftTicks(float seconds) {
        return Math.round(seconds * MINECRAFT_TPS);
    }

    public static int hytaleTicksToMinecraft(int hytaleTicks) {
        return Math.round(hytaleTicks * (MINECRAFT_TPS / (float) hytaleTPS));
    }

    public static long hytaleTicksToMillis(int hytaleTicks) {
        return Math.round(hytaleTicks * (1000.0 / hytaleTPS));
    }

    public static int millisToMinecraftTicks(long millis) {
        return Math.round(millis * MINECRAFT_TPS / 1000f);
    }

    public static int getHytaleTPS() {
        return hytaleTPS;
    }

    public static void setHytaleTPS(int tps) {
        if (tps > 0 && tps <= 120) {
            hytaleTPS = tps;
        }
    }

    public static int getMinecraftTPS() {
        return MINECRAFT_TPS;
    }
}
