package me.elyndor.fakebar;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.*;

public final class ElyndorFakeBar extends JavaPlugin implements Listener, CommandExecutor {
    private final Map<String,String> worlds = new HashMap<>();
    private String format, powerPlaceholder;
    private int updateTicks;
    private BarManager bar;

    @Override public void onEnable() {
        saveDefaultConfig();
        loadSettings();
        bar = new BarManager(this);
        Bukkit.getPluginManager().registerEvents(this, this);
        Objects.requireNonNull(getCommand("fakebar")).setExecutor(this);
        Bukkit.getScheduler().runTaskTimer(this, () -> Bukkit.getOnlinePlayers().forEach(bar::update), 1L, updateTicks);
        getLogger().info("ElyndorFakeBar enabled. Text-only ACTION_BAR HUD active.");
    }
    private void loadSettings() {
        worlds.clear();
        if (getConfig().isConfigurationSection("worlds"))
            worlds.putAll(Objects.requireNonNull(getConfig().getConfigurationSection("worlds")).getValues(false).entrySet().stream().collect(HashMap::new, (m,e)->m.put(e.getKey(), String.valueOf(e.getValue())), HashMap::putAll));
        format = getConfig().getString("format", "<[region]> • <[direction]> • <[power]>");
        powerPlaceholder = getConfig().getString("power-placeholder", "0");
        updateTicks = Math.max(1, getConfig().getInt("update-ticks", 5));
    }
    public Map<String,String> getWorlds(){ return worlds; }
    public String getFormat(){ return format; }
    public String getPowerPlaceholder(){ return powerPlaceholder; }
    public boolean hasPapi(){ return Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI"); }

    @EventHandler public void join(PlayerJoinEvent e){ Bukkit.getScheduler().runTask(this, () -> bar.update(e.getPlayer())); }
    @EventHandler public void world(PlayerChangedWorldEvent e){ bar.update(e.getPlayer()); }
    @Override public boolean onCommand(CommandSender s, Command c, String l, String[] a){
        if (!s.hasPermission("elyndorfakebar.admin")) { s.sendMessage("§cNo permission."); return true; }
        if (a.length == 1 && a[0].equalsIgnoreCase("reload")) { reloadConfig(); loadSettings(); s.sendMessage("§aElyndorFakeBar reloaded."); return true; }
        s.sendMessage("§e/fakebar reload"); return true;
    }
}
