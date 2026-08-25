package me.elyndor.fakebar;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;
import java.util.Map;

public final class BarManager {
    private final ElyndorFakeBar plugin;
    private final MiniMessage mm = MiniMessage.miniMessage();
    private final LegacyComponentSerializer legacy = LegacyComponentSerializer.legacyAmpersand();
    public BarManager(ElyndorFakeBar plugin) { this.plugin = plugin; }
    public void update(Player p) {
        Map<String,String> worlds = plugin.getWorlds();
        String region = worlds.get(p.getWorld().getName());
        if (region == null) {
            p.sendActionBar(Component.empty());
            return;
        }
        String s = plugin.getFormat()
                .replace("<[region]>", region)
                .replace("<[direction]>", DirectionManager.get(p));
        String power = plugin.getPowerPlaceholder();
        if (plugin.hasPapi() && power != null && !power.isEmpty()) {
            power = me.clip.placeholderapi.PlaceholderAPI.setPlaceholders(p, power);
        }
        s = s.replace("<[power]>", power == null ? "0" : power);
        Component c;
        try {
            // Convert the user's <&color[...]> syntax into MiniMessage tags.
            s = s.replace("<&color[dark_purple]>", "<dark_purple>")
                 .replace("<&color[white]>", "<white>")
                 .replace("<&color[gray]>", "<gray>");
            c = mm.deserialize(s);
        } catch (Exception ex) {
            c = legacy.deserialize(s.replace("<&color[dark_purple]>", "&5"));
        }
        p.sendActionBar(c);
    }
}
