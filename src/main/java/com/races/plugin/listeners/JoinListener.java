package com.races.plugin.listeners;

import com.races.plugin.RacesPlugin;
import com.races.plugin.races.Race;
import com.races.plugin.utils.RaceGUI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {

    private final RacesPlugin plugin;

    public JoinListener(RacesPlugin plugin) { this.plugin = plugin; }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if (!plugin.getRaceManager().hasRace(p))
            Bukkit.getScheduler().runTaskLater(plugin, () -> RaceGUI.open(p), 20L);
    }

    @EventHandler
    public void onGuiClick(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player p)) return;
        if (e.getView() == null) return;

        String title = PlainTextComponentSerializer.plainText().serialize(e.getView().title());
        if (!title.contains("Выбор расы")) return;

        e.setCancelled(true);

        Race race = RaceGUI.getBySlot(e.getRawSlot());
        if (race == null) return;

        plugin.getRaceManager().setRace(p, race);
        p.closeInventory();

        p.sendMessage(Component.text(plugin.prefix() + "Вы выбрали расу: ", NamedTextColor.GREEN)
            .append(Component.text(race.getDisplay(), NamedTextColor.YELLOW)));

        plugin.getAbilities().applyPassives(p);
    }
}
