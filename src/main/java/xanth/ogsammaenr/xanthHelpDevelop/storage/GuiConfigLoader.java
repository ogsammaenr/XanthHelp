package xanth.ogsammaenr.xanthHelpDevelop.storage;

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
        String title = confirmationMenuConfig.getString("title");
        int rows = confirmationMenuConfig.getInt("rows");
        if (rows < 1 || rows > 6) {
            throw new IllegalArgumentException("Invalid row count in 'ConfirmationMenu.yml': " + rows + ". Must be between 1 and 6.");
        }
        ConfigurationSection fillersSection = confirmationMenuConfig.getConfigurationSection("fillers");
        GuiConfig.FillerSettings fillers = getFillerSettingsFromSection(fillersSection);

        ConfigurationSection configSection = confirmationMenuConfig.getConfigurationSection("confirmations-menu");
        Material acceptMat = Material.getMaterial(configSection.getString("accept.material"));
        if (acceptMat == null) {
            throw new IllegalArgumentException("Invalid material for accept button in 'ConfirmationMenu.yml'");
        }
        String acceptName = configSection.getString("accept.name");
        ItemStack acceptItem = new ItemBuilder(acceptMat).setName(acceptName).build();

        Material denyMat = Material.getMaterial(configSection.getString("deny.material"));
        if (acceptMat == null) {
            throw new IllegalArgumentException("Invalid material for deny button in 'ConfirmationMenu.yml'");
        }
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

    private GuiConfig.TicketCategoriesMenuConfig loadTicketCategoriesMenu() {
        String title = ticketCategoriesMenuConfig.getString("title");
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

        return new GuiConfig.TicketCategoriesMenuConfig(title, rows, fillers, categoryButtons);

    }

    private GuiConfig.TicketDetailMenuConfig loadTicketDetailMenu() {
        String title = ticketDetailsMenuConfig.getString("title");
        int rows = ticketDetailsMenuConfig.getInt("rows");
        if (rows <= 0 || rows > 6) {
            throw new IllegalArgumentException("Invalid row count in 'TicketDetailsMenu.yml': " + rows + ". Must be between 1 and 6.");
        }
        ConfigurationSection fillersSection = ticketDetailsMenuConfig.getConfigurationSection("fillers");
        GuiConfig.FillerSettings fillers = getFillerSettingsFromSection(fillersSection);

        ConfigurationSection detailsSec = ticketDetailsMenuConfig.getConfigurationSection("details");
        String id = detailsSec.getString("id");
        String ownerName = detailsSec.getString("owner-name");
        String status = detailsSec.getString("status");
        String category = detailsSec.getString("category");
        String description = detailsSec.getString("description");
        String creationDate = detailsSec.getString("creation-date");
        String staffName = detailsSec.getString("staff-name");
        String assignationDate = detailsSec.getString("assignation-date");
        String resolveDate = detailsSec.getString("resolve-date");
        String participants = detailsSec.getString("participants");

        ConfigurationSection buttonsSec = ticketDetailsMenuConfig.getConfigurationSection("buttons");

        ItemStack claim = getItemStackFromSection(buttonsSec.getConfigurationSection("claim"));
        ItemStack markAsResolved = getItemStackFromSection(buttonsSec.getConfigurationSection("mark-as-resolved"));
        ItemStack joinParticipants = getItemStackFromSection(buttonsSec.getConfigurationSection("join-participants"));
        ItemStack leaveParticipants = getItemStackFromSection(buttonsSec.getConfigurationSection("leave-participants"));
        ItemStack unClaim = getItemStackFromSection(buttonsSec.getConfigurationSection("unclaim"));
        ItemStack cancelTicket = getItemStackFromSection(buttonsSec.getConfigurationSection("cancel-ticket"));
        ItemStack pingStaffEnabled = getItemStackFromSection(buttonsSec.getConfigurationSection("ping-staff-enabled"));
        ItemStack pingStaffCooldown =
                getItemStackFromSection(buttonsSec.getConfigurationSection("ping-staff-cooldown"));

        return new GuiConfig.TicketDetailMenuConfig(title, rows, fillers, id, ownerName, status, category, description
                , creationDate, staffName, assignationDate, resolveDate, participants, claim, markAsResolved
                , joinParticipants, leaveParticipants, unClaim, cancelTicket, pingStaffEnabled, pingStaffCooldown);

    }

    private GuiConfig.TicketsMenuConfig loadTicketsMenu() {
        String title = ticketsMenuConfig.getString("title");
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

        ConfigurationSection buttonsSec = ticketsMenuConfig.getConfigurationSection("buttons");
        ItemStack next = getItemStackFromSection(buttonsSec.getConfigurationSection("next"));
        int nextSlot = buttonsSec.getInt("next.slot");

        ItemStack previous = getItemStackFromSection(buttonsSec.getConfigurationSection("previous"));
        int previousSlot = buttonsSec.getInt("previous.slot");

        ItemStack playerFilterButton =
                getItemStackFromSection(buttonsSec.getConfigurationSection("player-filter-button"));
        int playerFilterSlot = buttonsSec.getInt("player-filter-button.slot");

        ItemStack open = getItemStackFromSection(buttonsSec.getConfigurationSection("OPEN"));
        int openSlot = buttonsSec.getInt("open.slot");

        ItemStack inProgress = getItemStackFromSection(buttonsSec.getConfigurationSection("IN-PROGRESS"));
        int inProgressSlot = buttonsSec.getInt("in-progress.slot");

        ItemStack canceled = getItemStackFromSection(buttonsSec.getConfigurationSection("CANCELED"));
        int canceledSlot = buttonsSec.getInt("canceled.slot");

        ItemStack resolved = getItemStackFromSection(buttonsSec.getConfigurationSection("RESOLVED"));
        int resolvedSlot = buttonsSec.getInt("resolved.slot");

        ItemStack all = getItemStackFromSection(buttonsSec.getConfigurationSection("ALL"));
        int allSlot = buttonsSec.getInt("all.slot");

        return new GuiConfig.TicketsMenuConfig(title, rows, fillers, ticketButtons, next, previous
                , playerFilterButton, open, inProgress, canceled, resolved, all, nextSlot, previousSlot
                , playerFilterSlot, openSlot, inProgressSlot, canceledSlot, resolvedSlot, allSlot);
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

    private ItemStack getItemStackFromSection(ConfigurationSection section) {
        Material material = Material.getMaterial(section.getString("material"));
        String name = section.getString("name");
        List<String> lore = section.getStringList("lore");

        return new ItemBuilder(material).setName(name).setLore(lore).build();
    }
}
