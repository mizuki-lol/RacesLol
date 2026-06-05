package com.races.plugin.managers;

import com.races.plugin.RacesPlugin;
import com.races.plugin.races.Race;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RaceManager {

    private final RacesPlugin plugin;
    private final Map<UUID, Race> races    = new HashMap<>();
    private final Map<UUID, Integer> vers  = new HashMap<>();
    private File file;
    private FileConfiguration cfg;

    public RaceManager(RacesPlugin plugin) {
        this.plugin = plugin;
        load();
    }

    public void setRace(Player p, Race race) {
        races.put(p.getUniqueId(), race);
        vers.put(p.getUniqueId(), 1);
        save();
    }

    public Race  getRace(Player p)   { return races.get(p.getUniqueId()); }
    public boolean hasRace(Player p) { return races.containsKey(p.getUniqueId()); }
    public int   getVersion(Player p){ return vers.getOrDefault(p.getUniqueId(), 1); }
    public boolean isV2(Player p)    { return getVersion(p) == 2; }

    public void upgrade(Player p) {
        vers.put(p.getUniqueId(), 2);
        save();
    }

    private void load() {
        file = new File(plugin.getDataFolder(), "playerdata.yml");
        if (!file.exists()) {
            plugin.getDataFolder().mkdirs();
            try { file.createNewFile(); } catch (IOException e) { e.printStackTrace(); }
        }
        cfg = YamlConfiguration.loadConfiguration(file);
        var sec = cfg.getConfigurationSection("players");
        if (sec == null) return;
        for (String key : sec.getKeys(false)) {
            try {
                UUID uuid = UUID.fromString(key);
                Race r = Race.fromId(cfg.getString("players." + key + ".race", ""));
                int v  = cfg.getInt("players." + key + ".version", 1);
                if (r != null) { races.put(uuid, r); vers.put(uuid, v); }
            } catch (Exception ignored) {}
        }
    }

    public void save() {
        races.forEach((uuid, race) -> {
            String path = "players." + uuid;
            cfg.set(path + ".race",    race.getId());
            cfg.set(path + ".version", vers.getOrDefault(uuid, 1));
        });
        try { cfg.save(file); } catch (IOException e) { e.printStackTrace(); }
    }
}
