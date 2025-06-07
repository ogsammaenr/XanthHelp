package xanth.ogsammaenr.xanthHelp.util;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import xanth.ogsammaenr.xanthHelp.XanthHelp;

import java.io.File;
import java.util.List;
import java.util.Map;

public class Utils {
    public static FileConfiguration loadConfig(JavaPlugin plugin, String name) {
        File file = new File(plugin.getDataFolder(), name);
        if (!file.exists()) {
            plugin.saveResource(name, false);
        }
        return YamlConfiguration.loadConfiguration(file);
    }

    public static ItemStack createItem(Material material, String name, List<String> lore, Map<String, String> nbtData) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return item;

        if (name != null) {
            meta.setDisplayName(color(name));
        }

        if (lore != null && !lore.isEmpty()) {
            meta.setLore(color(lore));
        }

        XanthHelp plugin = XanthHelp.getInstance();
        // PersistentDataContainer ile görünmez veri ekleme
        if (nbtData != null) {
            for (Map.Entry<String, String> entry : nbtData.entrySet()) {
                NamespacedKey key = new NamespacedKey(plugin, entry.getKey());
                meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, entry.getValue());
            }
        }

        item.setItemMeta(meta);
        return item;
    }

    public static String color(String text) {
        return text.replace("&", "§");
    }

    public static List<String> color(List<String> textList) {
        return textList.stream().map(Utils::color).toList();
    }

    public static void setStringTag(ItemStack item, String key, String value) {
        XanthHelp plugin = XanthHelp.getInstance();
        if (item == null || item.getItemMeta() == null) return;

        var meta = item.getItemMeta();
        var container = meta.getPersistentDataContainer();
        container.set(new NamespacedKey(plugin, key), PersistentDataType.STRING, value);
        item.setItemMeta(meta);
    }

    public static String getStringTag(ItemStack item, String key) {
        XanthHelp plugin = XanthHelp.getInstance();
        if (item == null || item.getItemMeta() == null) return null;

        var meta = item.getItemMeta();
        var container = meta.getPersistentDataContainer();
        NamespacedKey namespacedKey = new NamespacedKey(plugin, key);

        if (container.has(namespacedKey, PersistentDataType.STRING)) {
            return container.get(namespacedKey, PersistentDataType.STRING);
        }
        return null;
    }
}
