package xanth.ogsammaenr.xanthHelpDevelop.storage;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import xanth.ogsammaenr.xanthHelpDevelop.XanthHelp;
import xanth.ogsammaenr.xanthHelpDevelop.manager.GuiConfigManager;
import xanth.ogsammaenr.xanthHelpDevelop.model.GuiConfig;
import xanth.ogsammaenr.xanthHelpDevelop.model.TicketCategoryType;
import xanth.ogsammaenr.xanthHelpDevelop.util.ItemBuilder;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class GuiConfigLoader {
    private final FileConfiguration confirmationMenuConfig;
    private final FileConfiguration mainMenuConfig;
    private final FileConfiguration ticketCategoriesMenuConfig;
    private final FileConfiguration ticketDetailsMenuConfig;
    private final FileConfiguration ticketsMenuConfig;

    private final XanthHelp plugin;
    private final GuiConfigManager manager;

    public GuiConfigLoader(XanthHelp plugin) {
        this.plugin = plugin;
        this.manager = plugin.getGuiConfigManager();


        plugin.saveResource("gui/ConfirmationMenu.yml", false);
        this.confirmationMenuConfig = YamlConfiguration
                .loadConfiguration(new File(plugin.getDataFolder(), "gui/ConfirmationMenu.yml"));

        plugin.saveResource("gui/MainMenu.yml", false);
        this.mainMenuConfig = YamlConfiguration
                .loadConfiguration(new File(plugin.getDataFolder(), "gui/MainMenu.yml"));

        plugin.saveResource("gui/TicketCategoriesMenu.yml", false);
        this.ticketCategoriesMenuConfig = YamlConfiguration
                .loadConfiguration(new File(plugin.getDataFolder(), "gui/TicketCategoriesMenu.yml"));

        plugin.saveResource("gui/TicketDetailsMenu.yml", false);
        this.ticketDetailsMenuConfig = YamlConfiguration
                .loadConfiguration(new File(plugin.getDataFolder(), "gui/TicketDetailsMenu.yml"));

        plugin.saveResource("gui/TicketsMenu.yml", false);
        this.ticketsMenuConfig = YamlConfiguration
                .loadConfiguration(new File(plugin.getDataFolder(), "gui/TicketsMenu.yml"));


    }

    private GuiConfig.ConfirmationMenuConfig loadConfirmationMenu() {
        String title = confirmationMenuConfig.getString("title");
        int rows = confirmationMenuConfig.getInt("rows");
        ConfigurationSection fillersSection = confirmationMenuConfig.getConfigurationSection("fillers");
        GuiConfig.FillerSettings fillers = getFillerSettingsFromSection(fillersSection);

        ConfigurationSection configSection = mainMenuConfig.getConfigurationSection("confirmations-menu");
        Material acceptMat = Material.getMaterial(configSection.getString("accept.material"));
        String acceptName = configSection.getString("accept.name");
        ItemStack acceptItem = new ItemBuilder(acceptMat).setName(acceptName).build();

        Material denyMat = Material.getMaterial(configSection.getString("deny.material"));
        String denyName = configSection.getString("deny.name");
        ItemStack denyItem = new ItemBuilder(denyMat).setName(denyName).build();

        return new GuiConfig.ConfirmationMenuConfig(title, rows, fillers, denyItem, acceptItem);

    }

    private GuiConfig.MainMenuConfig loadMainMenu() {
        String title = mainMenuConfig.getString("title");
        int rows = mainMenuConfig.getInt("rows");
        if (rows <= 0 || rows > 6) {
            throw new IllegalArgumentException("Invalid row count in 'MainMenu.yml': " + rows + ". Must be between 1 and 6.");
        }
        ConfigurationSection fillersSection = mainMenuConfig.getConfigurationSection("fillers");
        GuiConfig.FillerSettings fillers = getFillerSettingsFromSection(fillersSection);

        Map<TicketCategoryType, Integer> categoryTypeButtons = new HashMap<>();
        for (String key : mainMenuConfig.getConfigurationSection("category-types").getKeys(false)) {
            TicketCategoryType type = plugin.getTicketCategoryManager().getCategoryTypeById(key);
            int slot = mainMenuConfig.getInt("category-types." + key);
            if (type != null) {
                categoryTypeButtons.put(type, slot);
            } else {
                plugin.getLogger().warning("Unknown category type " + key);
            }
        }
        return new GuiConfig.MainMenuConfig(title, rows, fillers, categoryTypeButtons);

    }

    private GuiConfig.FillerSettings getFillerSettingsFromSection(ConfigurationSection section) {
        Material generalMat = Material.getMaterial(section.getString("general.material"));
        String generalStr = section.getString("general.name");
        ItemStack general = new ItemBuilder(generalMat).setName(generalStr).build();

        Material topRowMat = Material.getMaterial(section.getString("top-row.material"));
        String topRowStr = section.getString("top-row.name");
        ItemStack topRow = new ItemBuilder(topRowMat).setName(topRowStr).build();

        Material bottomRowMat = Material.getMaterial(section.getString("bottom-row.material"));
        String bottomRowStr = section.getString("bottom-row.name");
        ItemStack bottomRow = new ItemBuilder(bottomRowMat).setName(bottomRowStr).build();

        return new GuiConfig.FillerSettings(topRow, bottomRow, general);
    }
}
