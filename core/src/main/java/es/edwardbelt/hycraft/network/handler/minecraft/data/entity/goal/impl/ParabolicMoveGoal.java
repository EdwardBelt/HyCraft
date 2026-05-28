package es.edwardbelt.hycraft.network.handler.minecraft.data.entity.goal.impl;

import es.edwardbelt.hycraft.network.handler.minecraft.data.entity.goal.Goal;
import org.joml.Vector3d;

public class ParabolicMoveGoal extends Goal {
    private final double startX;
    private final double startY;
    private final double startZ;
    private final double targetX;
    private final double targetY;
    private final double targetZ;
    private final double height;
    private final long timeToComplete;

    private int currentTick;
    private int totalTicks;

    private boolean shouldExecute;

    public ParabolicMoveGoal(Vector3d start, Vector3d end, double height, long timeToComplete) {
        this.startX = start.x();
        this.startY = start.y();
        this.startZ = start.z();
        this.targetX = end.x();
        this.targetY = end.y();
        this.targetZ = end.z();
        this.height = height;
        this.timeToComplete = timeToComplete;
        this.shouldExecute = true;
    }

    @Override
    public boolean shouldExecute() {
        return shouldExecute;
    }

    @Override
    public void start() {
        this.currentTick = 0;
        this.totalTicks = (int) Math.max(1, timeToComplete / TICK_TIME);
    }

    @Override
    public void tick() {
        currentTick++;

        if (currentTick >= totalTicks) {
            getEntity().move(getConnection(), targetX, targetY, targetZ);
            shouldExecute = false;
            return;
        }

        double t = (double) currentTick / totalTicks;

        double interpolatedX = startX + (targetX - startX) * t;
        double interpolatedZ = startZ + (targetZ - startZ) * t;
        double interpolatedY = startY + (targetY - startY) * t + height * 4 * t * (1 - t);

        getEntity().move(getConnection(), interpolatedX, interpolatedY, interpolatedZ);
    }
}