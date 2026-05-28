package es.edwardbelt.hycraft.util;

import org.joml.Vector3d;

public class VectorUtil {

    public static double distanceSquaredTo(Vector3d from, Vector3d to) {
        double dx = from.x - to.x;
        double dy = from.y - to.y;
        double dz = from.z - to.z;
        return dx * dx + dy * dy + dz * dz;
    }
}
