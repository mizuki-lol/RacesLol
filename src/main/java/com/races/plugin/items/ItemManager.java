package com.races.plugin.items;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import java.util.List;

public class ItemManager {

    public static final String TAG_UPGRADER = "race_upgrader";
    public static final String TAG_SWITCHER  = "race_switcher";

    private final Plugin plugin;
    private final NamespacedKey keyUpgrader;
    private final NamespacedKey keySwitcher;

    public ItemManager(Plugin plugin) {
        this.plugin = plugin;
        keyUpgrader = new NamespacedKey(plugin, TAG_UPGRADER);
        keySwitcher  = new NamespacedKey(plugin, TAG_SWITCHER);
    }

    // ── Апгрейдер ── текстура золотой моркови
    // Крафт:  O D O
    //         D N D
    //         O D O
    // O = обсидиан, D = блок алмаза, N = блок незерита
    public ItemStack upgrader() {
        ItemStack item = new ItemStack(Material.GOLDEN_CARROT);
        ItemMeta m = item.getItemMeta();
        m.displayName(Component.text("✦ Апгрейдер расы", NamedTextColor.GOLD)
            .decoration(TextDecoration.ITALIC, false));
        m.lore(List.of(
            comp("ПКМ — улучшить расу до версии 2", NamedTextColor.GRAY),
            comp("Предмет исчезнет после применения", NamedTextColor.DARK_GRAY)
        ));
        m.getPersistentDataContainer().set(keyUpgrader, PersistentDataType.STRING, TAG_UPGRADER);
        item.setItemMeta(m);
        return item;
    }

    // ── Сменник ── текстура звезды незера
    // Крафт:  O O O
    //         O G O
    //         O O O
    // O = обсидиан, G = золотой блок
    public ItemStack switcher() {
        ItemStack item = new ItemStack(Material.NETHER_STAR);
        ItemMeta m = item.getItemMeta();
        m.displayName(Component.text("✦ Сменник расы", NamedTextColor.LIGHT_PURPLE)
            .decoration(TextDecoration.ITALIC, false));
        m.lore(List.of(
            comp("ПКМ — случайно изменить расу", NamedTextColor.GRAY),
            comp("Предмет исчезнет после применения", NamedTextColor.DARK_GRAY)
        ));
        m.getPersistentDataContainer().set(keySwitcher, PersistentDataType.STRING, TAG_SWITCHER);
        item.setItemMeta(m);
        return item;
    }

    public boolean isUpgrader(ItemStack i) {
        if (i == null || !i.hasItemMeta()) return false;
        return TAG_UPGRADER.equals(i.getItemMeta().getPersistentDataContainer()
            .get(keyUpgrader, PersistentDataType.STRING));
    }

    public boolean isSwitcher(ItemStack i) {
        if (i == null || !i.hasItemMeta()) return false;
        return TAG_SWITCHER.equals(i.getItemMeta().getPersistentDataContainer()
            .get(keySwitcher, PersistentDataType.STRING));
    }

    public void registerRecipes() {
        ShapedRecipe up = new ShapedRecipe(new NamespacedKey(plugin, "race_upgrader"), upgrader());
        up.shape("ODO", "DNB", "ODO");
        up.setIngredient('O', Material.OBSIDIAN);
        up.setIngredient('D', Material.DIAMOND_BLOCK);
        up.setIngredient('N', Material.NETHERITE_BLOCK);
        up.setIngredient('B', Material.NETHERITE_BLOCK);
        plugin.getServer().addRecipe(up);

        ShapedRecipe sw = new ShapedRecipe(new NamespacedKey(plugin, "race_switcher"), switcher());
        sw.shape("OOO", "OGO", "OOO");
        sw.setIngredient('O', Material.OBSIDIAN);
        sw.setIngredient('G', Material.GOLD_BLOCK);
        plugin.getServer().addRecipe(sw);
    }

    private static Component comp(String text, NamedTextColor color) {
        return Component.text(text, color).decoration(TextDecoration.ITALIC, false);
    }
}
