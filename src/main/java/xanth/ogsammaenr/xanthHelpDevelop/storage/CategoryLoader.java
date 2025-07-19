package xanth.ogsammaenr.xanthHelpDevelop.storage;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.PluginManager;
import xanth.ogsammaenr.xanthHelpDevelop.XanthHelp;
import xanth.ogsammaenr.xanthHelpDevelop.manager.TicketCategoryManager;
import xanth.ogsammaenr.xanthHelpDevelop.model.TicketCategory;
import xanth.ogsammaenr.xanthHelpDevelop.model.TicketCategoryType;

import java.util.List;

public class CategoryLoader {
    private final FileConfiguration config;
    private final TicketCategoryManager categoryManager;
    private final XanthHelp plugin;

    public CategoryLoader(FileConfiguration config, XanthHelp plugin) {
        this.config = config;
        this.plugin = plugin;
        this.categoryManager = plugin.getTicketCategoryManager();
    }

    public void loadAll() {
        categoryManager.clear();

        PluginManager pm = Bukkit.getPluginManager();
        for (Permission perm : pm.getPermissions()) {
            if (perm.getName().startsWith("xanthhelp.category")) {
                pm.removePermission(perm);
            }
        }

        loadCategoryTypes();
        loadCategories();
    }


    public void loadCategoryTypes() {
        ConfigurationSection section = config.getConfigurationSection("category-types");
        if (section == null) {
            plugin.getLogger().warning("Category Types section not found!");
            return;
        }

        for (String id : section.getKeys(false)) {
            ConfigurationSection typeSection = section.getConfigurationSection(id);
            if (typeSection == null) continue;

            String displayName = typeSection.getString("display-name");
            if (displayName == null) {
                plugin.getLogger().warning("Category '" + id + "' is missing display-name.");
                continue;
            }
            String iconStr = typeSection.getString("icon");
            if (iconStr == null) {
                plugin.getLogger().warning("Category '" + id + "' is missing icon.");
                continue;
            }
            Material icon = Material.getMaterial(iconStr);
            if (icon == null) {
                plugin.getLogger().warning("Invalid material '" + iconStr + "' for category '" + id + "'. Using STONE instead.");
                icon = Material.STONE;
            }
            String permission = typeSection.getString("permission");
            if (permission == null) {
                plugin.getLogger().warning("Category '" + id + "' is missing permission.");
                continue;
            }
            String fullPermission = "xanthhelp.category." + permission;
            PluginManager pm = Bukkit.getPluginManager();
            if (pm.getPermission(permission) == null) {
                String description = "Allows access to category type: " + id;
                Permission perm = new Permission(fullPermission, description, PermissionDefault.OP);
                pm.addPermission(perm);
            }

            List<String> lore = typeSection.getStringList("lore");
            if (lore.isEmpty()) {
                plugin.getLogger().warning("Category '" + id + "' is missing lore.");
                continue;
            }

            TicketCategoryType type = new TicketCategoryType(id, displayName, fullPermission, lore, icon);

            categoryManager.addCategoryType(type);
        }

    }

    public void loadCategories() {
        ConfigurationSection section = config.getConfigurationSection("categories");
        if (section == null) {
            plugin.getLogger().warning("Categories section not found!");
            return;
        }

        for (String id : section.getKeys(false)) {
            ConfigurationSection categorySection = section.getConfigurationSection(id);
            if (categorySection == null) continue;

            // DisplayName
            String displayName = categorySection.getString("display-name");
            if (displayName == null) {
                plugin.getLogger().warning("Category '" + id + "' is missing display-name.");
                continue;
            }
            // IconStr
            String iconStr = categorySection.getString("icon");
            if (iconStr == null) {
                plugin.getLogger().warning("Category '" + id + "' is missing icon.");
                continue;
            }
            // ICON
            Material icon = Material.getMaterial(iconStr);
            if (icon == null) {
                plugin.getLogger().warning("Invalid material '" + iconStr + "' for category '" + id);
                continue;
            }
            // CategoryTypeStr
            String type = categorySection.getString("type");
            if (type == null) {
                plugin.getLogger().warning("Category '" + id + "' is missing type");
                continue;
            }
            // CategoryType
            TicketCategoryType categoryType = categoryManager.getCategoryTypeById(type);
            if (categoryType == null) {
                plugin.getLogger().warning("Category type '" + type + "' for category '" + id + "' not found.");
                continue;
            }
            // Lore
            List<String> lore = categorySection.getStringList("lore");
            if (lore.isEmpty()) {
                plugin.getLogger().warning("Category '" + id + "' is missing lore.");
                continue;
            }

            TicketCategory category = new TicketCategory(id, displayName, lore, categoryType, icon);

            categoryManager.addCategory(category);
        }
    }
}
