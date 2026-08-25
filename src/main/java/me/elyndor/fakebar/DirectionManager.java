package me.catatclysm.bar;

import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public final class DirectionManager {

    private DirectionManager() {
    }

    public static String getDirection(Player player) {

        Vector direction =
                player.getLocation().getDirection();

        double x = direction.getX();
        double z = direction.getZ();

        if (Math.abs(x) > Math.abs(z)) {

            if (x > 0) {
                return "East";
            }

            return "West";
        }

        if (z > 0) {
            return "South";
        }

        return "North";
    }
}
