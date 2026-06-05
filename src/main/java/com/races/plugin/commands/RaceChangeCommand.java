package com.races.plugin.commands;

import com.races.plugin.RacesPlugin;
import com.races.plugin.races.Race;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class RaceChangeCommand implements CommandExecutor, TabCompleter {

    private final RacesPlugin plugin;

    public RaceChangeCommand(RacesPlugin plugin) { this.plugin = plugin; }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("races.admin")) {
            sender.sendMessage(Component.text(plugin.prefix() + "Нет прав!", NamedTextColor.RED));
            return true;
        }
        if (args.length < 2) {
            sender.sendMessage(Component.text(plugin.prefix() + "Использование: /racechange <игрок> <раса>", NamedTextColor.YELLOW));
            sender.sendMessage(Component.text("Расы: angel, shark, ghoul, electro, dragon, tundra", NamedTextColor.GRAY));
            return true;
        }
        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage(Component.text(plugin.prefix() + "Игрок не найден: " + args[0], NamedTextColor.RED));
            return true;
        }
        Race race = Race.fromId(args[1]);
        if (race == null) {
            sender.sendMessage(Component.text(plugin.prefix() + "Неизвестная раса: " + args[1], NamedTextColor.RED));
            sender.sendMessage(Component.text("Расы: angel, shark, ghoul, electro, dragon, tundra", NamedTextColor.GRAY));
            return true;
        }
        plugin.getRaceManager().setRace(target, race);
        target.sendMessage(Component.text(plugin.prefix() + "Ваша раса изменена на ", NamedTextColor.GOLD)
            .append(Component.text(race.getDisplay(), NamedTextColor.YELLOW)));
        sender.sendMessage(Component.text(plugin.prefix())
            .append(Component.text(target.getName(), NamedTextColor.YELLOW))
            .append(Component.text(" → ", NamedTextColor.GRAY))
            .append(Component.text(race.getDisplay(), NamedTextColor.YELLOW)));
        plugin.getAbilities().applyPassives(target);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
        if (!sender.hasPermission("races.admin")) return List.of();
        if (args.length == 1)
            return Bukkit.getOnlinePlayers().stream()
                .map(Player::getName)
                .filter(n -> n.toLowerCase().startsWith(args[0].toLowerCase()))
                .collect(Collectors.toList());
        if (args.length == 2)
            return Arrays.stream(Race.values())
                .map(Race::getId)
                .filter(id -> id.startsWith(args[1].toLowerCase()))
                .collect(Collectors.toList());
        return List.of();
    }
}
