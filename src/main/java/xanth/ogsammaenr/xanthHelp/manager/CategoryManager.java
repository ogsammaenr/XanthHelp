package xanth.ogsammaenr.xanthHelp.manager;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.PluginManager;
import xanth.ogsammaenr.xanthHelp.XanthHelp;
import xanth.ogsammaenr.xanthHelp.model.Category;
import xanth.ogsammaenr.xanthHelp.model.CategoryType;

import java.io.File;
import java.util.*;

public class CategoryManager {
    private final Map<String, CategoryType> types;
    private final Map<String, Category> categories;

    private final XanthHelp plugin;

    public CategoryManager(XanthHelp plugin) {
        this.types = new HashMap<>();
        this.categories = new HashMap<>();

        this.plugin = plugin;
    }

    public void loadCategories() {
        types.clear();
        categories.clear();

        plugin.saveResource("categories.yml", false);
        File categoriesFile = new File(plugin.getDataFolder(), "categories.yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(categoriesFile);

        // Tipleri yükle
        ConfigurationSection typeSection = config.getConfigurationSection("types");
        if (typeSection != null) {
            for (String key : typeSection.getKeys(false)) {
                String permission = typeSection.getString(key + ".permission", null);
                CategoryType type = new CategoryType(key, permission);
                types.put(key, type);
                plugin.getLogger().info(type.toString() + " Loaded ");
            }
        }

        // Kategorileri yükle
        ConfigurationSection categorySection = config.getConfigurationSection("categories");
        if (categorySection != null) {
            for (String key : categorySection.getKeys(false)) {
                String typeId = config.getString("categories." + key + ".type");

                CategoryType type = types.get(typeId);
                if (type != null) {
                    Category category = new Category(key, type);
                    categories.put(key, category);
                    plugin.getLogger().info(category.toString() + "  has been loaded");
                } else {
                    plugin.getLogger().warning(key + " Category has not been loaded due to missing type");
                }
            }
        }

        plugin.getLogger().info("Loaded " + categories.size() + " categories");
        plugin.getLogger().info("Loaded " + types.size() + " category types");
    }

    public void registerPermissions() {
        PluginManager pluginManager = plugin.getServer().getPluginManager();

        for (CategoryType type : types.values()) {
            String permName = type.getPermission();

            if (permName == null || pluginManager.getPermission(permName) != null) continue;

            Permission perm = new Permission(permName, "Permission for category type: " + type.getId(), PermissionDefault.OP);
            pluginManager.addPermission(perm);
        }
    }

    public Category getCategory(String id) {
        return categories.get(id);
    }

    public CategoryType getCategoryType(String id) {
        return types.get(id);
    }

    public Collection<Category> getAllCategories() {
        return categories.values();
    }

    public Collection<CategoryType> getAllCategoryTypes() {
        return types.values();
    }

    public List<Category> getCategoriesByType(String typeId) {
        List<Category> list = new ArrayList<>();
        for (Category category : categories.values()) {
            if (category.getType().getId().equalsIgnoreCase(typeId)) {
                list.add(category);
            }
        }
        return list;
    }

}
