package org.soak.map;

import org.bukkit.util.Vector;
import org.spongepowered.math.vector.Vector3d;
import org.spongepowered.math.vector.Vector3i;

public class SoakVectorMap {

    public static Vector3d to3d(Vector vector) {
        return new Vector3d(vector.getX(), vector.getY(), vector.getZ());
    }

    public static Vector3i to3i(Vector vector) {
        return new Vector3i(vector.getBlockX(), vector.getBlockY(), vector.getBlockZ());
    }

    public static Vector toBukkit(Vector3d vector) {
        return new Vector(vector.x(), vector.y(), vector.z());
    }

    public static Vector toBukkit(Vector3i vector) {
        return new Vector(vector.x(), vector.y(), vector.z());
    }
}
