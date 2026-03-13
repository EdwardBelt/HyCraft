package es.edwardbelt.hycraft.util;

import com.hypixel.hytale.math.vector.Vector3d;

// Interpolates entity positions between tick updates for smoother movement.

public class PositionInterpolator {
    private Vector3d startPos;
    private Vector3d targetPos;
    private long startTime;
    private long duration;

    public PositionInterpolator() {
        this.startPos = new Vector3d(0, 0, 0);
        this.targetPos = new Vector3d(0, 0, 0);
    }

    public void updateTarget(Vector3d newPos, long durationMs) {
        this.startPos = getCurrentPos();
        this.targetPos = newPos;
        this.startTime = System.currentTimeMillis();
        this.duration = durationMs;
    }

    public Vector3d getCurrentPos() {
        if (duration == 0) return targetPos;

        long elapsed = System.currentTimeMillis() - startTime;
        float progress = Math.min(1.0f, elapsed / (float) duration);

        return lerp(startPos, targetPos, progress);
    }

    private Vector3d lerp(Vector3d a, Vector3d b, float t) {
        return new Vector3d(
            a.x + (b.x - a.x) * t,
            a.y + (b.y - a.y) * t,
            a.z + (b.z - a.z) * t
        );
    }
}
