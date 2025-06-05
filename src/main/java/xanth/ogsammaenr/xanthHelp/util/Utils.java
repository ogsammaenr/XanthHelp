package xanth.ogsammaenr.xanthHelp.util;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.List;

public class Utils {
    public static FileConfiguration loadConfig(JavaPlugin plugin, String name) {
        File file = new File(plugin.getDataFolder(), name);
        if (!file.exists()) {
            plugin.saveResource(name, false);
        }
        return YamlConfiguration.loadConfiguration(file);
    }

    public static ItemStack createItem(Material material, String name, List<String> lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return item;

        if (name != null) {
            meta.setDisplayName(color(name));
        }

        if (lore != null && !lore.isEmpty()) {
            meta.setLore(color(lore));
        }

        item.setItemMeta(meta);
        return item;
    }

    private static String color(String text) {
        return text.replace("&", "§");
    }

    private static List<String> color(List<String> textList) {
        return textList.stream().map(Utils::color).toList();
    }
}
