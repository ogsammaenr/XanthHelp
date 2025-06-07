package xanth.ogsammaenr.xanthHelp.manager;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xanth.ogsammaenr.xanthHelp.XanthHelp;
import xanth.ogsammaenr.xanthHelp.model.GuiCategoryItem;
import xanth.ogsammaenr.xanthHelp.model.GuiCategoryTypeItem;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GuiConfigManager {
    private final Map<String, GuiCategoryTypeItem> categoryTypeItems = new HashMap<>();
    private final Map<String, GuiCategoryItem> categoryItems = new HashMap<>();

    private final Map<String, String> categoryMenuTitles = new HashMap<>();
    private final Map<String, Integer> categoryMenuRows = new HashMap<>();

    private int mainMenuRows;
    private String mainMenuTitle;
    private ItemStack categoryTypeFillerItem;

    private ItemStack categoryFillerItem;

    private final XanthHelp plugin;

    public GuiConfigManager(XanthHelp plugin) {
        this.plugin = plugin;
    }

    public void load() {
        plugin.saveResource("gui.yml", false);
        File file = new File(plugin.getDataFolder(), "gui.yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        categoryTypeItems.clear();

        // Kategori Tipi Öğelerini Yükle
        ConfigurationSection itemsSection = config.getConfigurationSection("category-types.items");
        if (itemsSection != null) {
            for (String key : itemsSection.getKeys(false)) {
                ConfigurationSection iconSection = itemsSection.getConfigurationSection(key + ".icon");
                if (iconSection == null) continue;

                Material material = Material.getMaterial(iconSection.getString("material", "STONE"));
                if (material == null) {
                    plugin.getLogger().warning("Invalid material: " + iconSection.getString("material") + " for " + key);
                    continue;
                }

                String name = iconSection.getString("name", "&7Bilinmeyen");
                List<String> lore = iconSection.getStringList("lore");
                int slot = itemsSection.getInt(key + ".slot", -1);

                GuiCategoryTypeItem item = new GuiCategoryTypeItem(key, material, name, lore, slot);
                categoryTypeItems.put(key, item);

                // Kategori türünün menüsü ile ilgili kısımlar

                String title = itemsSection.getString(key + ".title", "&cUndefined Title");
                int rows = itemsSection.getInt(key + ".rows", 2);
                categoryMenuTitles.put(key, title.replace("&", "§"));
                categoryMenuRows.put(key, rows);
            }
        }
        categoryItems.clear();

        //  Kategori Öğelerini Yükle
        ConfigurationSection categorySection = config.getConfigurationSection("categories.items");
        if (categorySection != null) {
            for (String key : categorySection.getKeys(false)) {
                String materialName = categorySection.getString(key + ".icon.material");
                String name = categorySection.getString(key + ".icon.name");
                List<String> lore = categorySection.getStringList(key + ".icon.lore");
                int slot = categorySection.getInt(key + ".slot");

                Material material = Material.getMaterial(materialName);
                if (material == null) {
                    plugin.getLogger().warning("Invalid material: " + materialName + " for category: " + key);
                    continue;
                }

                GuiCategoryItem item = new GuiCategoryItem(key, material, name, lore, slot);
                categoryItems.put(key, item);
            }
        }

        // Filler Item Yükle
        ConfigurationSection fillerSection = config.getConfigurationSection("category-types.general.filler");
        if (fillerSection != null) {
            this.categoryTypeFillerItem = createItemStackFromSection(fillerSection);
        }
        ConfigurationSection categoryFillerSection = config.getConfigurationSection("categories.general.filler");
        if (categoryFillerSection != null) {
            this.categoryFillerItem = createItemStackFromSection(categoryFillerSection);
        }

        // Menü Satır Sayısı ve Başlık
        mainMenuRows = config.getInt("category-types.general.rows", 3);
        mainMenuTitle = ChatColor.translateAlternateColorCodes('&', config.getString("category-types.general.title", "&aYardım Menüsü"));


    }

    // Ortak ItemStack Oluşturucu
    private ItemStack createItemStackFromSection(ConfigurationSection section) {
        String materialName = section.getString("material", "STONE");
        String name = section.getString("name", " ");
        List<String> lore = section.getStringList("lore");

        Material material = Material.getMaterial(materialName.toUpperCase());
        if (material == null) {
            plugin.getLogger().warning("Invalid material: " + materialName);
            return null;
        }

        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
            if (lore != null) {
                meta.setLore(lore.stream().map(s -> ChatColor.translateAlternateColorCodes('&', s)).toList());
            }
            item.setItemMeta(meta);
        }
        return item;
    }

    public Map<String, GuiCategoryTypeItem> getCategoryTypeItems() {
        return categoryTypeItems;
    }

    public Map<String, GuiCategoryItem> getCategoryItems() {
        return categoryItems;
    }

    public int getMainMenuRows() {
        return mainMenuRows;
    }

    public String getMainMenuTitle() {
        return mainMenuTitle;
    }

    public ItemStack getCategoryFillerItem() {
        return categoryFillerItem;
    }

    public String getCategoryMenuTitle(String categoryTypeId) {
        return categoryMenuTitles.getOrDefault(categoryTypeId, "Yardım Menüsü");
    }

    public int getCategoryMenuRows(String categoryTypeId) {
        return categoryMenuRows.getOrDefault(categoryTypeId, 3);
    }

    public ItemStack getCategoryTypeFillerItem() {
        return categoryTypeFillerItem;
    }

    public String getCategoryTypeIdByMenuTitle(String menuTitle) {
        for (Map.Entry<String, String> entry : categoryMenuTitles.entrySet()) {
            if (entry.getValue().equals(menuTitle)) {
                return entry.getKey(); // categoryTypeId döner
            }
        }
        return null; // bulunamazsa
    }
}
