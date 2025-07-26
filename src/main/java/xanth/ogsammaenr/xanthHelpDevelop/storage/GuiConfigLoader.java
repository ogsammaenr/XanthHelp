package xanth.ogsammaenr.xanthHelpDevelop.storage;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import xanth.ogsammaenr.xanthHelpDevelop.XanthHelp;
import xanth.ogsammaenr.xanthHelpDevelop.manager.GuiConfigManager;
import xanth.ogsammaenr.xanthHelpDevelop.model.GuiConfig;
import xanth.ogsammaenr.xanthHelpDevelop.model.TicketCategory;
import xanth.ogsammaenr.xanthHelpDevelop.model.TicketCategoryType;
import xanth.ogsammaenr.xanthHelpDevelop.util.ItemBuilder;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The type Gui config loader.
 */
public class GuiConfigLoader {
    private final FileConfiguration confirmationMenuConfig;
    private final FileConfiguration mainMenuConfig;
    private final FileConfiguration ticketCategoriesMenuConfig;
    private final FileConfiguration ticketDetailsMenuConfig;
    private final FileConfiguration ticketsMenuConfig;

    private final XanthHelp plugin;
    private final GuiConfigManager manager;

    /**
     * Instantiates a new Gui config loader.
     *
     * @param plugin the plugin
     */
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

    public void loadAllConfigs() {
        manager.setConfirmationMenuConfig(loadConfirmationMenu());
        manager.setMainMenuConfig(loadMainMenu());
        manager.setTicketCategoriesMenuConfig(loadTicketCategoriesMenu());
        manager.setTicketDetailMenuConfig(loadTicketDetailMenu());
        manager.setTicketsMenuConfig(loadTicketsMenu());
    }

    private GuiConfig.ConfirmationMenuConfig loadConfirmationMenu() {
        String title = ChatColor.translateAlternateColorCodes('&', confirmationMenuConfig.getString("title"));
        int rows = confirmationMenuConfig.getInt("rows");
        if (rows < 1 || rows > 6) {
            throw new IllegalArgumentException("Invalid row count in 'ConfirmationMenu.yml': " + rows + ". Must be between 1 and 6.");
        }
        ConfigurationSection fillersSection = confirmationMenuConfig.getConfigurationSection("fillers");
        GuiConfig.FillerSettings fillers = getFillerSettingsFromSection(fillersSection);

        ConfigurationSection configSection = confirmationMenuConfig.getConfigurationSection("confirmations-menu");

        ItemStack acceptItem = getItemStackFromSection(configSection.getConfigurationSection("accept"));
        int acceptSlot = configSection.getInt("accept.slot");

        ItemStack denyItem = getItemStackFromSection(configSection.getConfigurationSection("deny"));
        int denySlot = configSection.getInt("deny.slot");

        plugin.getLogger().info("Confirmation Menu Loaded");
        return new GuiConfig.ConfirmationMenuConfig(title, rows, fillers, denyItem, denySlot, acceptItem, acceptSlot);

    }

    private GuiConfig.MainMenuConfig loadMainMenu() {
        String title = ChatColor.translateAlternateColorCodes('&', mainMenuConfig.getString("title"));
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

        plugin.getLogger().info("Main Menu Loaded");
        return new GuiConfig.MainMenuConfig(title, rows, fillers, categoryTypeButtons);

    }

    private GuiConfig.TicketCategoriesMenuConfig loadTicketCategoriesMenu() {
        String title = ChatColor.translateAlternateColorCodes('&', ticketCategoriesMenuConfig.getString("title"));
        int rows = ticketCategoriesMenuConfig.getInt("rows");
        if (rows <= 0 || rows > 6) {
            throw new IllegalArgumentException("Invalid row count in 'TicketCategoriesMenu.yml': " + rows + ". Must be between 1 and 6.");
        }
        ConfigurationSection fillersSection = ticketCategoriesMenuConfig.getConfigurationSection("fillers");
        GuiConfig.FillerSettings fillers = getFillerSettingsFromSection(fillersSection);

        Map<TicketCategoryType, Map<TicketCategory, Integer>> categoryButtons = new HashMap<>();

        ConfigurationSection configSection = ticketCategoriesMenuConfig.getConfigurationSection("categories");
        for (String key : configSection.getKeys(false)) {
            TicketCategoryType type = plugin.getTicketCategoryManager().getCategoryTypeById(key);
            if (type == null) {
                throw new IllegalArgumentException("Unknown TicketCategoryType id: '" + key + "' in TicketCategoriesMenu.yml");
            }

            Map<TicketCategory, Integer> cat = new HashMap<>();
            ConfigurationSection subSection = configSection.getConfigurationSection(key);
            if (subSection == null) continue;

            for (String categoryId : subSection.getKeys(false)) {
                TicketCategory category = plugin.getTicketCategoryManager().getCategoryById(categoryId);
                if (category == null) {
                    throw new IllegalArgumentException("Unknown TicketCategory id: '" + categoryId + "' under type '" + key + "' in TicketCategoriesMenu.yml");
                }
                String path = key + "." + categoryId;
                if (!configSection.isInt(path)) {
                    throw new IllegalArgumentException("Missing or invalid slot number for '" + categoryId + "' in '" + path + "' in TicketCategoriesMenu.yml");
                }
                int slot = configSection.getInt(path);

                cat.put(category, slot);
            }

            categoryButtons.put(type, cat);
        }


        plugin.getLogger().info("TicketCategoriesMenu Loaded");
        return new GuiConfig.TicketCategoriesMenuConfig(title, rows, fillers, categoryButtons);

    }

    private GuiConfig.TicketDetailMenuConfig loadTicketDetailMenu() {
        String title = ChatColor.translateAlternateColorCodes('&', ticketDetailsMenuConfig.getString("title"));
        int rows = ticketDetailsMenuConfig.getInt("rows");
        if (rows <= 0 || rows > 6) {
            throw new IllegalArgumentException("Invalid row count in 'TicketDetailsMenu.yml': " + rows + ". Must be between 1 and 6.");
        }
        ConfigurationSection fillersSection = ticketDetailsMenuConfig.getConfigurationSection("fillers");
        GuiConfig.FillerSettings fillers = getFillerSettingsFromSection(fillersSection);

        ConfigurationSection detailsSec = ticketDetailsMenuConfig.getConfigurationSection("details");
        ItemStack id =
                getItemStackFromSection(detailsSec.getConfigurationSection("id"));
        ItemStack ownerName =
                getItemStackFromSection(detailsSec.getConfigurationSection("owner-name"));
        ItemStack status =
                getItemStackFromSection(detailsSec.getConfigurationSection("status"));
        ItemStack category =
                getItemStackFromSection(detailsSec.getConfigurationSection("category"));
        ItemStack description =
                getItemStackFromSection(detailsSec.getConfigurationSection("description"));
        ItemStack creationDate =
                getItemStackFromSection(detailsSec.getConfigurationSection("creation-date"));
        ItemStack staffName =
                getItemStackFromSection(detailsSec.getConfigurationSection("staff-name"));
        ItemStack assignationDate =
                getItemStackFromSection(detailsSec.getConfigurationSection("assignation-date"));
        ItemStack resolveDate =
                getItemStackFromSection(detailsSec.getConfigurationSection("resolve-date"));
        ItemStack participants =
                getItemStackFromSection(detailsSec.getConfigurationSection("participants"));

        ConfigurationSection buttonsSec = ticketDetailsMenuConfig.getConfigurationSection("buttons");

        ItemStack claim =
                getItemStackFromSection(buttonsSec.getConfigurationSection("claim"));
        ItemStack markAsResolved =
                getItemStackFromSection(buttonsSec.getConfigurationSection("mark-as-resolved"));
        ItemStack joinParticipants =
                getItemStackFromSection(buttonsSec.getConfigurationSection("join-participants"));
        ItemStack leaveParticipants =
                getItemStackFromSection(buttonsSec.getConfigurationSection("leave-participants"));
        ItemStack unClaim =
                getItemStackFromSection(buttonsSec.getConfigurationSection("unclaim"));
        ItemStack cancelTicket =
                getItemStackFromSection(buttonsSec.getConfigurationSection("cancel-ticket"));
        ItemStack pingStaffEnabled =
                getItemStackFromSection(buttonsSec.getConfigurationSection("ping-staff-enabled"));
        ItemStack pingStaffCooldown =
                getItemStackFromSection(buttonsSec.getConfigurationSection("ping-staff-cooldown"));

        plugin.getLogger().info("TicketDetailsMenu Loaded");
        return new GuiConfig.TicketDetailMenuConfig(title, rows, fillers, id, ownerName, status, category, description
                , creationDate, staffName, assignationDate, resolveDate, participants, claim, markAsResolved
                , joinParticipants, leaveParticipants, unClaim, cancelTicket, pingStaffEnabled, pingStaffCooldown);

    }

    private GuiConfig.TicketsMenuConfig loadTicketsMenu() {
        String title = ChatColor.translateAlternateColorCodes('&', ticketsMenuConfig.getString("title"));
        int rows = ticketsMenuConfig.getInt("rows");
        if (rows <= 0 || rows > 6) {
            throw new IllegalArgumentException("Invalid row count in 'TicketsMenu.yml': " + rows + ". Must be between 1 and 6.");
        }
        ConfigurationSection fillersSection = ticketsMenuConfig.getConfigurationSection("fillers");
        GuiConfig.FillerSettings fillers = getFillerSettingsFromSection(fillersSection);

        int[] ticketButtons = ticketsMenuConfig.getIntegerList("ticket-buttons")
                .stream()
                .mapToInt(Integer::intValue)
                .toArray();

        ItemStack ticketFormat =
                getItemStackFromSection(ticketsMenuConfig.getConfigurationSection("ticket-format"));

        ConfigurationSection buttonsSec = ticketsMenuConfig.getConfigurationSection("buttons");
        ItemStack next = getItemStackFromSection(buttonsSec.getConfigurationSection("next"));
        int nextSlot = buttonsSec.getInt("next.slot");

        ItemStack previous =
                getItemStackFromSection(buttonsSec.getConfigurationSection("previous"));
        int previousSlot = buttonsSec.getInt("previous.slot");

        ItemStack playerFilterButton =
                getItemStackFromSection(buttonsSec.getConfigurationSection("player-filter-button"));
        int playerFilterSlot = buttonsSec.getInt("player-filter-button.slot");

        ItemStack open =
                getItemStackFromSection(buttonsSec.getConfigurationSection("open"));
        int openSlot = buttonsSec.getInt("open.slot");

        ItemStack inProgress =
                getItemStackFromSection(buttonsSec.getConfigurationSection("in_progress"));
        int inProgressSlot = buttonsSec.getInt("in_progress.slot");

        ItemStack canceled =
                getItemStackFromSection(buttonsSec.getConfigurationSection("canceled"));
        int canceledSlot = buttonsSec.getInt("canceled.slot");

        ItemStack resolved =
                getItemStackFromSection(buttonsSec.getConfigurationSection("resolved"));
        int resolvedSlot = buttonsSec.getInt("resolved.slot");

        ItemStack all =
                getItemStackFromSection(buttonsSec.getConfigurationSection("all"));
        int allSlot = buttonsSec.getInt("all.slot");

        plugin.getLogger().info("TicketsMenu Loaded");
        return new GuiConfig.TicketsMenuConfig(title, rows, fillers, ticketButtons, next, previous
                , playerFilterButton, open, inProgress, canceled, resolved, all, nextSlot, previousSlot
                , playerFilterSlot, openSlot, inProgressSlot, canceledSlot, resolvedSlot, allSlot, ticketFormat);
    }

    private GuiConfig.FillerSettings getFillerSettingsFromSection(ConfigurationSection section) {
        Material generalMat = Material.getMaterial(section.getString("general.material"));
        String generalStr =
                ChatColor.translateAlternateColorCodes('&', section.getString("general.name"));
        ItemStack general = new ItemBuilder(generalMat).setName(generalStr).build();

        Material topRowMat = Material.getMaterial(section.getString("top-row.material"));
        String topRowStr =
                ChatColor.translateAlternateColorCodes('&', section.getString("top-row.name"));
        ItemStack topRow = new ItemBuilder(topRowMat).setName(topRowStr).build();

        Material bottomRowMat = Material.getMaterial(section.getString("bottom-row.material"));
        String bottomRowStr =
                ChatColor.translateAlternateColorCodes('&', section.getString("bottom-row.name"));
        ItemStack bottomRow = new ItemBuilder(bottomRowMat).setName(bottomRowStr).build();

        return new GuiConfig.FillerSettings(topRow, bottomRow, general);
    }

    private ItemStack getItemStackFromSection(ConfigurationSection section) {
        Material material = Material.getMaterial(section.getString("material"));
        String name = ChatColor.translateAlternateColorCodes('&', section.getString("name"));
        List<String> rawLore = section.getStringList("lore");

        List<String> lore = new ArrayList<>();
        for (String line : rawLore) {
            lore.add(ChatColor.translateAlternateColorCodes('&', line));
        }

        return new ItemBuilder(material).setName(name).setLore(lore).build();
    }
}
