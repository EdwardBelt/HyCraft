package es.edwardbelt.hycraft.util;

public class TickRateDetector {
    private long lastTickNanos = 0;
    private int sampleCount = 0;
    private double avgTickTimeMs = 25.0;
    private static final int MAX_SAMPLES = 100;

    public void onTick() {
        long now = System.nanoTime();
        if (lastTickNanos > 0) {
            double tickTimeMs = (now - lastTickNanos) / 1_000_000.0;

            if (tickTimeMs < 100) {
                avgTickTimeMs = (avgTickTimeMs * sampleCount + tickTimeMs) / (sampleCount + 1);
                sampleCount = Math.min(sampleCount + 1, MAX_SAMPLES);
            }
        }
        lastTickNanos = now;
    }

    public int getDetectedTPS() {
        return Math.round(1000f / (float) avgTickTimeMs);
    }

    public double getAvgTickTimeMs() {
        return avgTickTimeMs;
    }

    public boolean isCalibrated() {
        return sampleCount >= 20;
    }
}
