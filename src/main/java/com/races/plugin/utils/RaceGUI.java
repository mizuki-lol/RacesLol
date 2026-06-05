package com.races.plugin.utils;

import com.races.plugin.races.Race;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class RaceGUI {

    public static final String TITLE = "⚔ Выбор расы ⚔";

    // 6 slots in a 54-slot chest — two rows, centered
    private static final int[] SLOTS = { 19, 21, 23, 25, 30, 32 };

    private static final Material[] ICONS = {
        Material.FEATHER,           // ANGEL
        Material.HEART_OF_THE_SEA,  // SHARK
        Material.ROTTEN_FLESH,      // GHOUL
        Material.LIGHTNING_ROD,     // ELECTRO
        Material.DRAGON_EGG,        // DRAGON
        Material.PACKED_ICE         // TUNDRA
    };

    private static final NamedTextColor[] COLORS = {
        NamedTextColor.WHITE,       // ANGEL
        NamedTextColor.AQUA,        // SHARK
        NamedTextColor.DARK_GREEN,  // GHOUL
        NamedTextColor.YELLOW,      // ELECTRO
        NamedTextColor.DARK_PURPLE, // DRAGON
        NamedTextColor.DARK_AQUA    // TUNDRA
    };

    private static final List<List<Component>> LORE = List.of(
        // ANGEL
        List.of(
            lore("✦ Иммунитет к урону от падения"),
            lore("✦ 2×Shift — рывок 5 блоков (КД 10с)"),
            lore("✦ 5% шанс: замедл. + слабость врагу 7с"),
            lore("✦ Пострадавший подсвечивается для тебя"),
            lore(""),
            lore("§eV2: §72 рывка подряд → КД 10с")
        ),
        // SHARK
        List.of(
            lore("✦ Дыхание под водой"),
            lore("✦ Скорость + Dolphin's Grace в воде"),
            lore("✦ 10% шанс: игнорировать 50% брони"),
            lore(""),
            lore("§eV2: §7Сила 2 + Скорость 2 + Сопр. 1 в воде"),
            lore("§eV2: §72×Shift — режим воды на 10с")
        ),
        // GHOUL
        List.of(
            lore("✦ 10% шанс: украсть 1 сердце"),
            lore("✦ 2×Shift — гарпун (КД 8с)"),
            lore("✦ Жертва кражи подсвечивается для тебя"),
            lore(""),
            lore("§eV2: §7+10% стан при ударе")
        ),
        // ELECTRO
        List.of(
            lore("✦ Пассивная Скорость 1"),
            lore("✦ 10% → цепь: 2x→+25%→+50%→+100%"),
            lore("✦ После 100% — КД 90с"),
            lore("✦ Частицы дыма + подсветка при ударе"),
            lore(""),
            lore("§eV2: §72×Shift — Форма Бога Молний 10с")
        ),
        // DRAGON
        List.of(
            lore("✦ Постоянная огнеупорность"),
            lore("✦ 2×Shift — фаербол (4 серд., игнор брони, стан)"),
            lore("✦ 10% шанс: удар ×1.5"),
            lore(""),
            lore("§eV2: §72×Shift — неуязвимость 5с (КД 45с)")
        ),
        // TUNDRA
        List.of(
            lore("✦ 2×Shift — луч заморозки 15 блоков (5с)"),
            lore("✦ 10% шанс: стан 1с + звук наковальни"),
            lore("✦ Застунённый подсвечивается для тебя"),
            lore(""),
            lore("§eV2: §73 удара подряд — гарантированный стан")
        )
    );

    public static void open(Player p) {
        Inventory inv = Bukkit.createInventory(null, 54,
            Component.text(TITLE, NamedTextColor.DARK_RED)
                .decoration(TextDecoration.BOLD, true));

        // Glass border fill
        ItemStack glass = glass();
        for (int i = 0; i < 54; i++) inv.setItem(i, glass);

        // Info item
        ItemStack book = new ItemStack(Material.ENCHANTED_BOOK);
        ItemMeta bm = book.getItemMeta();
        bm.displayName(Component.text("Выбор расы", NamedTextColor.GOLD)
            .decoration(TextDecoration.ITALIC, false));
        bm.lore(List.of(
            lore("Нажмите на расу чтобы выбрать её."),
            Component.text("Оператор может сменить: ", NamedTextColor.DARK_GRAY)
                .decoration(TextDecoration.ITALIC, false),
            Component.text("/racechange <игрок> <раса>", NamedTextColor.YELLOW)
                .decoration(TextDecoration.ITALIC, false)
        ));
        book.setItemMeta(bm);
        inv.setItem(4, book);

        Race[] races = Race.values();
        for (int i = 0; i < races.length; i++)
            inv.setItem(SLOTS[i], buildItem(races[i], i));

        p.openInventory(inv);
    }

    private static ItemStack buildItem(Race race, int idx) {
        ItemStack item = new ItemStack(ICONS[idx]);
        ItemMeta m = item.getItemMeta();
        m.displayName(Component.text("✦ " + race.getDisplay(), COLORS[idx])
            .decoration(TextDecoration.BOLD, true)
            .decoration(TextDecoration.ITALIC, false));
        m.lore(LORE.get(idx));
        item.setItemMeta(m);
        return item;
    }

    public static Race getBySlot(int slot) {
        Race[] races = Race.values();
        for (int i = 0; i < SLOTS.length; i++)
            if (SLOTS[i] == slot) return races[i];
        return null;
    }

    private static ItemStack glass() {
        ItemStack g = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta m  = g.getItemMeta();
        m.displayName(Component.text(" "));
        g.setItemMeta(m);
        return g;
    }

    private static Component lore(String text) {
        return Component.text(text, NamedTextColor.GRAY)
            .decoration(TextDecoration.ITALIC, false);
    }
}
