package me.catatclysm.bar;

import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public final class BarManager {

    private final JavaPlugin plugin;

    private final MiniMessage miniMessage =
            MiniMessage.miniMessage();

    private Map<String, String> worlds =
            new HashMap<>();

    private String format;

    private String manaPlaceholder;

    private String maxManaPlaceholder;

    private String powerPlaceholder;

    private boolean enabled;

    private boolean showInUnlistedWorlds;

    public BarManager(JavaPlugin plugin) {

        this.plugin = plugin;

        reload();
    }

    public void reload() {

        enabled =
                plugin.getConfig().getBoolean(
                        "enabled",
                        true
                );

        showInUnlistedWorlds =
                plugin.getConfig().getBoolean(
                        "show-in-unlisted-worlds",
                        false
                );

        format =
                plugin.getConfig().getString(
                        "format",
                        "<[region]> • <[direction]>"
                );

        manaPlaceholder =
                plugin.getConfig().getString(
                        "auraskills.mana-placeholder",
                        "%auraskills_mana%"
                );

        maxManaPlaceholder =
                plugin.getConfig().getString(
                        "auraskills.max-mana-placeholder",
                        "%auraskills_max_mana%"
                );

        powerPlaceholder =
                plugin.getConfig().getString(
                        "auraskills.power-placeholder",
                        "%auraskills_power%"
                );

        worlds.clear();

        if (plugin.getConfig()
                .getConfigurationSection("worlds") != null) {

            for (
                    String key :
                    plugin.getConfig()
                            .getConfigurationSection(
                                    "worlds"
                            )
                            .getKeys(false)
            ) {

                worlds.put(
                        key,
                        plugin.getConfig().getString(
                                "worlds." + key
                        )
                );
            }
        }
    }

    public boolean isEnabled() {

        return enabled;
    }

    public void update(Player player) {

        if (!enabled) {

            player.sendActionBar(
                    Component.empty()
            );

            return;
        }

        String worldName =
                player.getWorld().getName();

        String region =
                worlds.get(worldName);

        if (region == null) {

            if (!showInUnlistedWorlds) {

                player.sendActionBar(
                        Component.empty()
                );

                return;
            }

            region = worldName;
        }

        String direction =
                DirectionManager.getDirection(
                        player
                );

        String hp =
                getHealth(player);

        String maxHp =
                getMaxHealth(player);

        String mana =
                getPlaceholder(
                        player,
                        manaPlaceholder,
                        "0"
                );

        String maxMana =
                getPlaceholder(
                        player,
                        maxManaPlaceholder,
                        "0"
                );

        String power =
                getPlaceholder(
                        player,
                        powerPlaceholder,
                        "0"
                );

        String text =
                format

                        .replace(
                                "<[hp]>",
                                hp
                        )

                        .replace(
                                "<[max_hp]>",
                                maxHp
                        )

                        .replace(
                                "<[mana]>",
                                mana
                        )

                        .replace(
                                "<[max_mana]>",
                                maxMana
                        )

                        .replace(
                                "<[power]>",
                                power
                        )

                        .replace(
                                "<[region]>",
                                region
                        )

                        .replace(
                                "<[direction]>",
                                direction
                        );

        text =
                convertColors(text);

        try {

            Component component =
                    miniMessage.deserialize(text);

            player.sendActionBar(
                    component
            );

        } catch (Exception exception) {

            player.sendActionBar(
                    Component.text(text)
            );
        }
    }

    private String getHealth(Player player) {

        double health =
                player.getHealth();

        int decimals =
                plugin.getConfig().getInt(
                        "hp.decimals",
                        0
                );

        if (decimals <= 0) {

            return String.valueOf(
                    (int) Math.round(health)
            );
        }

        return String.format(
                "%." + decimals + "f",
                health
        );
    }

    private String getMaxHealth(Player player) {

        if (player.getAttribute(
                Attribute.GENERIC_MAX_HEALTH
        ) == null) {

            return "20";
        }

        double max =
                player.getAttribute(
                        Attribute.GENERIC_MAX_HEALTH
                )
                .getValue();

        int decimals =
                plugin.getConfig().getInt(
                        "hp.decimals",
                        0
                );

        if (decimals <= 0) {

            return String.valueOf(
                    (int) Math.round(max)
            );
        }

        return String.format(
                "%." + decimals + "f",
                max
        );
    }

    private String getPlaceholder(
            Player player,
            String placeholder,
            String fallback
    ) {

        if (
                placeholder == null ||
                placeholder.isEmpty()
        ) {

            return fallback;
        }

        if (
                Bukkit.getPluginManager()
                        .getPlugin(
                                "PlaceholderAPI"
                        ) == null
        ) {

            return fallback;
        }

        try {

            String result =
                    PlaceholderAPI.setPlaceholders(
                            player,
                            placeholder
                    );

            if (
                    result == null ||
                    result.equals(
                            placeholder
                    )
            ) {

                return fallback;
            }

            return result;

        } catch (Exception exception) {

            return fallback;
        }
    }

    private String convertColors(
            String text
    ) {

        return text

                .replace(
                        "<&color[dark_red]>",
                        "<dark_red>"
                )

                .replace(
                        "<&color[red]>",
                        "<red>"
                )

                .replace(
                        "<&color[dark_purple]>",
                        "<dark_purple>"
                )

                .replace(
                        "<&color[purple]>",
                        "<light_purple>"
                )

                .replace(
                        "<&color[dark_blue]>",
                        "<dark_blue>"
                )

                .replace(
                        "<&color[blue]>",
                        "<blue>"
                )

                .replace(
                        "<&color[white]>",
                        "<white>"
                )

                .replace(
                        "<&color[gray]>",
                        "<gray>"
                )

                .replace(
                        "<&color[dark_gray]>",
                        "<dark_gray>"
                )

                .replace(
                        "<&color[green]>",
                        "<green>"
                )

                .replace(
                        "<&color[dark_green]>",
                        "<dark_green>"
                )

                .replace(
                        "<&color[aqua]>",
                        "<aqua>"
                )

                .replace(
                        "<&color[dark_aqua]>",
                        "<dark_aqua>"
                )

                .replace(
                        "<&color[yellow]>",
                        "<yellow>"
                )

                .replace(
                        "<&color[gold]>",
                        "<gold>"
                );
    }

    public void clearAll() {

        for (
                Player player :
                Bukkit.getOnlinePlayers()
        ) {

            player.sendActionBar(
                    Component.empty()
            );
        }
    }
}
