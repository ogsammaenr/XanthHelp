package xanth.ogsammaenr.xanthHelpDevelop;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import xanth.ogsammaenr.xanthHelpDevelop.command.HelpCommand;
import xanth.ogsammaenr.xanthHelpDevelop.listener.ChatInputListener;
import xanth.ogsammaenr.xanthHelpDevelop.listener.InventoryClickListener;
import xanth.ogsammaenr.xanthHelpDevelop.manager.*;
import xanth.ogsammaenr.xanthHelpDevelop.storage.*;

import java.io.File;


public final class XanthHelp extends JavaPlugin {
    private static XanthHelp instance;

    private SQLiteConnector databaseConnector;
    private TicketDAO ticketDAO;
    private TicketManager ticketManager;
    private TicketCategoryManager ticketCategoryManager;
    private CategoryLoader categoryLoader;
    private GuiConfigManager guiConfigManager;
    private GuiConfigLoader guiConfigLoader;
    private TicketInputManager ticketInputManager;
    private PingStaffManager pingStaffManager;

    private InventoryClickListener inventoryClickListener;
    private ChatInputListener chatInputListener;

    @Override
    public void onEnable() {
        instance = this;

        saveResource("categories.yml", false);

        File dataFolder = getDataFolder();
        if (!dataFolder.exists()) dataFolder.mkdirs();
        File dbFile = new File(dataFolder, "tickets.db");

        File categoryFile = new File(dataFolder, "categories.yml");
        FileConfiguration categoriesFile = new YamlConfiguration().loadConfiguration(categoryFile);

        this.ticketCategoryManager = new TicketCategoryManager(this);
        this.categoryLoader = new CategoryLoader(categoriesFile, this);

        this.databaseConnector = new SQLiteConnector(dbFile.getAbsolutePath());
        this.ticketDAO = new SQLiteTicketDAO(databaseConnector);
        this.ticketManager = new TicketManager(ticketDAO);

        categoryLoader.loadAll();
        ticketManager.loadTickets();

        this.guiConfigManager = new GuiConfigManager(this);
        this.guiConfigLoader = new GuiConfigLoader(this);

        guiConfigLoader.loadAllConfigs();

        this.ticketInputManager = new TicketInputManager();
        this.pingStaffManager = new PingStaffManager();

        getCommand("xanthhelp").setExecutor(new HelpCommand());

        this.inventoryClickListener = new InventoryClickListener(this);
        this.chatInputListener = new ChatInputListener();
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(inventoryClickListener, this);
        pm.registerEvents(chatInputListener, this);

        getLogger().info("******* XanthHelp Enabled *******");
    }

    @Override
    public void onDisable() {
        getLogger().info("******* XanthHelp Disabled *******");
    }

    /* ===== GETTERS ===== */

    public TicketManager getTicketManager() {
        return ticketManager;
    }

    public TicketCategoryManager getTicketCategoryManager() {
        return ticketCategoryManager;
    }

    public CategoryLoader getCategoryLoader() {
        return categoryLoader;
    }

    public GuiConfigManager getGuiConfigManager() {
        return guiConfigManager;
    }

    public TicketInputManager getTicketInputManager() {
        return ticketInputManager;
    }

    public PingStaffManager getPingStaffManager() {
        return pingStaffManager;
    }

    public static XanthHelp getInstance() {
        return instance;
    }

}
