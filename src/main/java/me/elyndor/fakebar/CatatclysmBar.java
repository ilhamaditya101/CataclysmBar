package me.catatclysm.bar;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

public final class CatatclysmBar extends JavaPlugin {

    private BarManager barManager;

    private BukkitTask updateTask;

    @Override
    public void onEnable() {

        saveDefaultConfig();

        barManager = new BarManager(this);

        startUpdater();

        getLogger().info("=================================");
        getLogger().info(" CatatclysmBar 1.0.1");
        getLogger().info(" Status: ENABLED");
        getLogger().info("=================================");
    }

    private void startUpdater() {

        if (updateTask != null) {
            updateTask.cancel();
        }

        long ticks = Math.max(
                1L,
                getConfig().getLong("update-ticks", 5L)
        );

        updateTask = Bukkit.getScheduler().runTaskTimer(
                this,
                () -> {

                    if (!barManager.isEnabled()) {
                        return;
                    }

                    for (Player player : Bukkit.getOnlinePlayers()) {

                        barManager.update(player);

                    }

                },
                1L,
                ticks
        );
    }

    @Override
    public void onDisable() {

        if (updateTask != null) {
            updateTask.cancel();
        }

        if (barManager != null) {
            barManager.clearAll();
        }
    }

    @Override
    public boolean onCommand(
            CommandSender sender,
            Command command,
            String label,
            String[] args
    ) {

        if (!command.getName().equalsIgnoreCase("ccb")) {
            return false;
        }

        if (!sender.hasPermission("catatclysmbar.admin")) {

            sender.sendMessage(
                    "§cYou don't have permission."
            );

            return true;
        }

        if (args.length == 0) {

            sender.sendMessage("§e/ccb reload");
            sender.sendMessage("§e/ccb on");
            sender.sendMessage("§e/ccb off");

            return true;
        }

        switch (args[0].toLowerCase()) {

            case "reload":

                reloadConfig();

                barManager.reload();

                startUpdater();

                sender.sendMessage(
                        "§aCatatclysmBar configuration reloaded."
                );

                break;

            case "on":

                getConfig().set("enabled", true);

                saveConfig();

                barManager.reload();

                sender.sendMessage(
                        "§aCatatclysmBar enabled in all worlds."
                );

                break;

            case "off":

                getConfig().set("enabled", false);

                saveConfig();

                barManager.reload();

                barManager.clearAll();

                sender.sendMessage(
                        "§cCatatclysmBar disabled in all worlds."
                );

                break;

            default:

                sender.sendMessage(
                        "§e/ccb reload"
                );

                sender.sendMessage(
                        "§e/ccb on"
                );

                sender.sendMessage(
                        "§e/ccb off"
                );

                break;
        }

        return true;
    }
}
