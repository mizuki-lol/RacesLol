package com.races.plugin;

import com.races.plugin.commands.RaceChangeCommand;
import com.races.plugin.items.ItemManager;
import com.races.plugin.listeners.AbilityListener;
import com.races.plugin.listeners.JoinListener;
import com.races.plugin.managers.CooldownManager;
import com.races.plugin.managers.RaceManager;
import org.bukkit.plugin.java.JavaPlugin;

public class RacesPlugin extends JavaPlugin {

    private RaceManager    raceManager;
    private CooldownManager cooldowns;
    private ItemManager    itemManager;
    private AbilityListener abilityListener;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        raceManager     = new RaceManager(this);
        cooldowns       = new CooldownManager();
        itemManager     = new ItemManager(this);
        itemManager.registerRecipes();

        abilityListener = new AbilityListener(this);
        getServer().getPluginManager().registerEvents(abilityListener, this);
        getServer().getPluginManager().registerEvents(new JoinListener(this), this);

        RaceChangeCommand cmd = new RaceChangeCommand(this);
        getCommand("racechange").setExecutor(cmd);
        getCommand("racechange").setTabCompleter(cmd);

        getLogger().info("╔══════════════════════════════╗");
        getLogger().info("║   RacesPlugin v2.0  loaded!  ║");
        getLogger().info("║  6 расы · V1 + V2 · 1.21.1  ║");
        getLogger().info("╚══════════════════════════════╝");
    }

    @Override
    public void onDisable() {
        if (raceManager != null) raceManager.save();
        getLogger().info("RacesPlugin выгружен. Данные сохранены.");
    }

    public RaceManager     getRaceManager()  { return raceManager; }
    public CooldownManager getCooldowns()    { return cooldowns; }
    public ItemManager     getItemManager()  { return itemManager; }
    public AbilityListener getAbilities()    { return abilityListener; }

    public String prefix() { return "§8[§6Расы§8] §r"; }
}
