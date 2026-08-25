package me.elyndor.fakebar;

import org.bukkit.entity.Player;

public final class DirectionManager {
    private DirectionManager() {}
    public static String get(Player p) {
        float yaw = p.getLocation().getYaw() % 360;
        if (yaw < 0) yaw += 360;
        if (yaw >= 315 || yaw < 45) return "South";
        if (yaw < 135) return "West";
        if (yaw < 225) return "North";
        return "East";
    }
}
